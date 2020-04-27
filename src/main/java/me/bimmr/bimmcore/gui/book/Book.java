package me.bimmr.bimmcore.gui.book;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import me.bimmr.bimmcore.BimmCore;
import me.bimmr.bimmcore.messages.fancymessage.FancyMessage;
import me.bimmr.bimmcore.reflection.Packets;
import me.bimmr.bimmcore.reflection.Reflection;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BookMeta;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Randy on 02/06/17.
 */

class BookExample {
    public BookExample() {
        Book book = new Book()
                .addLine(new FancyMessage("Hello"))
                .setLine(5, new FancyMessage("Line 5"))
                .addBlankLine()
                .addLine(new FancyMessage("Test"));

    }
}

public class Book {

    private final int MAXLINESPERPAGE = 14;

    private String title;
    private String author;
    private List<FancyMessage> lines;

    public Book() {
        this.title = "BimmCore";
        this.author = "Bimmr";
        this.lines = new ArrayList<>();
    }

    /**
     * Set the text at a line
     * Calls {@link #setLine(int, FancyMessage)}
     *
     * @param line The Line
     * @param text The text
     * @return The Book
     */
    public Book setLine(int line, String text) {
        return setLine(line, new FancyMessage(text));
    }

    /**
     * Add a line
     * Calls {@link #addLine(FancyMessage)}
     *
     * @param line The Line
     * @param message The Fancy Message
     * @return The Book
     */
    public Book setLine(int line, FancyMessage message) {
        if (lines.size() < line) {
            for (int i = lines.size(); i < line; i++)
                addBlankLine();
        }
        if (message == null || message.toPlainText().equals(" "))
            addBlankLine();
        else
            addLine(message);
        return this;
    }

    /**
     * Remove all lines
     */
    public void clear() {
        lines.clear();
    }

    /**
     * Add a line
     * Calls {@link #addLine(FancyMessage)}
     *
     * @param line Line to add
     * @return The Book
     */
    public Book addLine(String line) {
        return addLine(new FancyMessage(line));
    }

    /**
     * Add a Line
     *
     * @param line The Line to add
     * @return The Book
     */
    public Book addLine(FancyMessage line) {
        if (line == null || line.toPlainText().equals(" "))
            lines.add(new FancyMessage(" "));
        else
            lines.add(line);
        return this;
    }

    /**
     * Add a blank line
     *
     * @return The Book
     */
    public Book addBlankLine() {
        return addLine(" ");
    }

    /**
     * Move index to the start of the next page
     *
     * @return The Book
     */
    public Book nextPage() {
        int line = getLines().size() / 13;
        return setLine((((line + 1) * 13)), " ");
    }

    /**
     * Go to the bottom of the page, leaving x amount of empty lines
     *
     * @param lines The lines to leave at the bottom
     * @return The Book
     */
    public Book goToFooter(int lines) {
        int line = getLines().size() / 13;
        return setLine((((line + 1) * 13)) - lines, " ");
    }

    /**
     * @return Get the lines in the book
     */
    public List<FancyMessage> getLines() {
        return this.lines;
    }

    /**
     * Open the book for a player
     *
     * @param player The player
     */
    public void openFor(Player player) {
        BookAPI.openBook(this, player);
    }


    /**
     * @return Get the book as an ItemStack
     */
    public ItemStack getAsItem() {
        return BookAPI.getAsItemStack(this);
    }

    public static class BookAPI {
        private static Class<?> chatSerializer = Reflection.getNMSClass("IChatBaseComponent$ChatSerializer");
        private static Class<?> packetDataSerializer = Reflection.getNMSClass("PacketDataSerializer");
        private static Class<?> craftMetaBook = Reflection.getCraftClass("inventory.CraftMetaBook");
        private static Class<?> packetPlayOutCustomPayLoad;
        private static Class<?> craftKeyClass;
        private static Class<?> enumHandClass;
        private static Class<?> packetPlayOutOpenBookClass;

        private static Constructor<?> packetDataSerializerConstructor = Reflection.getConstructor(packetDataSerializer, ByteBuf.class);
        private static Constructor<?> packetPlayOutCustomPayLoadConstructor;
        private static Constructor<?> packetPlayOutOpenBookConstructor;
        private static Constructor<?> craftKeyConstructor;
        private static Method serializer = Reflection.getMethod(chatSerializer, "a", String.class);

        private static Object mainHandEnum;

        private static Field fieldPages = Reflection.getField(craftMetaBook, "pages");

        static {

            if (BimmCore.supports(14)) {
                packetPlayOutOpenBookClass = Reflection.getNMSClass("PacketPlayOutOpenBook");
                enumHandClass = Reflection.getNMSClass("EnumHand");
                packetPlayOutOpenBookConstructor = Reflection.getConstructor(packetPlayOutOpenBookClass, enumHandClass);
                Method value = Reflection.getMethod(enumHandClass, "valueOf", String.class);
                mainHandEnum = Reflection.invokeMethod(value, null, "MAIN_HAND");
            } else if (BimmCore.supports(13)) {
                packetPlayOutCustomPayLoad = Reflection.getNMSClass("PacketPlayOutCustomPayload");
                craftKeyClass = Reflection.getNMSClass("MinecraftKey");
                craftKeyConstructor = Reflection.getConstructor(craftKeyClass, String.class);
            } else {
                packetPlayOutCustomPayLoadConstructor = Reflection.getConstructor(packetPlayOutCustomPayLoad, craftKeyClass, packetDataSerializer);
                packetPlayOutCustomPayLoad = Reflection.getNMSClass("PacketPlayOutCustomPayload");
                packetPlayOutCustomPayLoadConstructor = Reflection.getConstructor(packetPlayOutCustomPayLoad, String.class, packetDataSerializer);
            }
        }

        /**
         * @param book The Book
         * @return Get the Book as an ItemStack
         */
        private static ItemStack getAsItemStack(Book book) {
            ItemStack iBook = new ItemStack(Material.WRITTEN_BOOK);
            ArrayList<FancyMessage> lines = new ArrayList(book.lines);

            //create the book
            BookMeta bookMeta = (BookMeta) iBook.getItemMeta();
            List<Object> pages;

            //get the pages
            pages = (List<Object>) Reflection.get(fieldPages, bookMeta);


            Object page = null;
            String prefix = "{\"extra\":[";
            String suffix = "],\"text\":\"\"}";

            StringBuilder sb = new StringBuilder();
            sb.append(prefix);

            //Add all the lines
            for (int i = 0; i < lines.size(); i++) {

                //If line is multiple of 13, it means a new page is needed
                if (i >= 13 && i % 13 == 0) {

                    //Finish current page
                    String temp = sb.toString();
                    temp = temp.substring(0, temp.length() - 1);
                    temp += suffix;
                    String line = temp;
                    page = Reflection.invokeMethod(serializer, null, line);
                    pages.add(page);

                    //Start new page
                    sb = new StringBuilder();
                    sb.append(prefix);
                }
                FancyMessage fm = lines.get(i);
                String part = fm.toJSON();

                part = "[\"\", " + part + ",{\"text\":\"\\n\"}]";
                if (i != lines.size() - 1)
                    part += ",";
                sb.append(part);
            }

            sb.append(suffix);
            String line = sb.toString();
            page = Reflection.invokeMethod(serializer, null, line);

            pages.add(page);

            //set the title and author of this book
            bookMeta.setTitle(book.title);
            bookMeta.setAuthor(book.author);

            //update the ItemStack with this new meta
            iBook.setItemMeta(bookMeta);

            return iBook;
        }

        /**
         * Open the book for the player
         *
         * @param book The Book
         * @param p    The Player
         */
        private static void openBook(ItemStack book, Player p) {
            int slot = p.getInventory().getHeldItemSlot();
            ItemStack iBook = book;
            ItemStack old = p.getInventory().getItem(slot);

            p.getInventory().setItem(slot, iBook);

            if (BimmCore.supports(14)) {
                Object packetPlayOutOpenBookInstance = Reflection.newInstance(packetPlayOutOpenBookConstructor, mainHandEnum);
                Packets.sendPacket(p, packetPlayOutOpenBookInstance);
            } else {
                ByteBuf byteBuf = Unpooled.buffer(256);
                byteBuf.setByte(0, (byte) 0);
                byteBuf.writerIndex(1);

                Object packetDataSerializerInstance = Reflection.newInstance(packetDataSerializerConstructor, byteBuf);

                Object packetPlayOutCustomPayLoadInstance;

                if (BimmCore.supports(13)) {
                    Object craftKeyInstance = Reflection.newInstance(craftKeyConstructor, "minecraft:book_open");
                    packetPlayOutCustomPayLoadInstance = Reflection.newInstance(packetPlayOutCustomPayLoadConstructor, craftKeyInstance, packetDataSerializerInstance);
                } else
                    packetPlayOutCustomPayLoadInstance = Reflection.newInstance(packetPlayOutCustomPayLoadConstructor, "MC|BOpen", packetDataSerializerInstance);

                Packets.sendPacket(p, packetPlayOutCustomPayLoadInstance);
            }

            p.getInventory().setItem(slot, old);
        }

        /**
         * Open the Book for the player
         *
         * @param book The Book
         * @param p    The player
         */
        public static void openBook(Book book, Player p) {
            openBook(getAsItemStack(book), p);
        }

    }
}
