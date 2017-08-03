package me.bimmr.bimmcore.menus;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import me.bimmr.bimmcore.messages.FancyMessage;
import me.bimmr.bimmcore.reflection.Packets;
import me.bimmr.bimmcore.reflection.Reflection;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BookMeta;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
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

    private final int MaxLinesPerPage = 14;

    private String             title;
    private String             author;
    private List<FancyMessage> lines;

    public Book() {
        this.title = "BimmCore";
        this.author = "Bimmr";
        this.lines = new ArrayList<>();
    }

    /**
     * Set a specific value at a specific line
     *
     * @param line
     * @param message
     * @return
     */
    public Book setLine(int line, String message) {
        if (message != null)
            setLine(line, new FancyMessage(message));
        else
            throw new NullPointerException("null cannot be added to a book");
        return this;
    }

    /**
     * Set a specific value at a specific line
     *
     * @param line
     * @param message
     * @return
     */
    public Book setLine(int line, FancyMessage message) {
        if (lines.size() < line) {
            for (int i = lines.size(); i < line; i++)
                addBlankLine();
        }
        lines.add(message);
        return this;
    }

    /**
     * Remove all lines
     */
    public void clear() {
        lines.clear();
    }

    /**
     * Add a new line
     *
     * @param message
     * @return
     */
    public Book addLine(String message) {
        if (message != null)
            addLine(new FancyMessage(message));
        else
            throw new NullPointerException("null cannot be added to a book");
        return this;
    }

    /**
     * Add a new line
     *
     * @param message
     * @return
     */
    public Book addLine(FancyMessage message) {
        lines.add(message);
        return this;
    }

    /**
     * Add a blank line
     *
     * @return
     */
    public Book addBlankLine() {
        addLine("");
        return this;
    }

    /**
     * Go to the next page
     *
     * @return
     */
    public Book nextPage() {
        int line = getLines().size() / 13;
        setLine((((line + 1) * 13)), "");
        return this;
    }

    /**
     * Go to the bottom of the page, leaving space for a footer
     *
     * @param lines
     * @return
     */
    public Book goToFooter(int lines) {
        int line = getLines().size() / 13;
        setLine((((line + 1) * 13)) - lines, "");
        return this;
    }

    /**
     * Get the lines in the book
     *
     * @return
     */
    public List<FancyMessage> getLines() {
        return this.lines;
    }

    /**
     * Open the book for a player
     *
     * @param player
     */
    public void openFor(Player player) {
        BookAPI.openBook(this, player);
    }


    /**
     * Get the book as an ItemStack
     *
     * @return
     */
    public ItemStack getAsItem() {
        return BookAPI.getAsItemStack(this);
    }

    public static class BookAPI {
        private static Class<?> chatSerializer;
        private static Method   serializer;

        private static Class<?>       packetDataSerializer;
        private static Constructor<?> packetDataSerializerConstructor;

        private static Class<?>       packetPlayOutCustomPayLoad;
        private static Constructor<?> packetPlayOutCustomPayLoadConstructor;

        private static Class<?> craftMetaBook;

        static {
            chatSerializer = Reflection.getNMSClass("IChatBaseComponent$ChatSerializer");
            craftMetaBook = Reflection.getCraftClass("inventory.CraftMetaBook");
            packetPlayOutCustomPayLoad = Reflection.getNMSClass("PacketPlayOutCustomPayload");
            packetDataSerializer = Reflection.getNMSClass("PacketDataSerializer");

            try {
                serializer = chatSerializer.getMethod("a", String.class);
                packetDataSerializerConstructor = packetDataSerializer.getConstructor(ByteBuf.class);
                packetPlayOutCustomPayLoadConstructor = packetPlayOutCustomPayLoad.getConstructor(String.class, packetDataSerializer);

            } catch (NoSuchMethodException e) {
                e.printStackTrace();
            }
        }

        public static ItemStack getAsItemStack(Book book) {
            ItemStack iBook = new ItemStack(Material.WRITTEN_BOOK);
            ArrayList<FancyMessage> lines = new ArrayList(book.lines);

            try {
                //create the book
                BookMeta bookMeta = (BookMeta) iBook.getItemMeta();
                List<Object> pages;

                //get the pages
                pages = (List<Object>) craftMetaBook.getDeclaredField("pages").get(bookMeta);


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
                        page = serializer.invoke(null, line);
                        pages.add(page);

                        //Start new page
                        sb = new StringBuilder();
                        sb.append(prefix);
                    }
                    FancyMessage fm = lines.get(i);
                    String part = fm.toJSON();
                    part = part.substring(prefix.length(), part.length() - suffix.length());
                    part += ",{\"text\":\"\\n\"}";
                    if (i != lines.size() - 1)
                        part += ",";
                    sb.append(part);
                }

                sb.append(suffix);
                String line = sb.toString();
                page = serializer.invoke(null, line);

                pages.add(page);

                //set the title and author of this book
                bookMeta.setTitle(book.title);
                bookMeta.setAuthor(book.author);

                //update the ItemStack with this new meta
                iBook.setItemMeta(bookMeta);

            } catch (ReflectiveOperationException ex) {
                ex.printStackTrace();
            }
            return iBook;
        }

        public static void openBook(ItemStack book, Player p) {
            int slot = p.getInventory().getHeldItemSlot();
            ItemStack iBook = book;
            ItemStack old = p.getInventory().getItem(slot);
            try {
                p.getInventory().setItem(slot, iBook);

                ByteBuf byteBuf = Unpooled.buffer(256);
                byteBuf.setByte(0, (byte) 0);
                byteBuf.writerIndex(1);

                Object packetDataSerializerInstance = packetDataSerializerConstructor.newInstance(byteBuf);
                Object packetPlayOutCustomPayLoadInstance = packetPlayOutCustomPayLoadConstructor.newInstance("MC|BOpen", packetDataSerializerInstance);

                Packets.sendPacket(p, packetPlayOutCustomPayLoadInstance);
            } catch (IllegalAccessException | InvocationTargetException | InstantiationException e) {
                e.printStackTrace();
            }
            p.getInventory().setItem(slot, old);
        }

        public static void openBook(Book book, Player p) {
            openBook(getAsItemStack(book), p);
        }

    }
}
