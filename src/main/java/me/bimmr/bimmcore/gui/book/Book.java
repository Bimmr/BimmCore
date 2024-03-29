package me.bimmr.bimmcore.gui.book;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import me.bimmr.bimmcore.BimmCore;
import me.bimmr.bimmcore.messages.fancymessage.FancyMessage;
import me.bimmr.bimmcore.reflection.Packets;
import me.bimmr.bimmcore.reflection.Reflection;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.TextComponent;
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
 * The type Book.
 */
public class Book {

    private static final int MAX_LINE_HEIGHT = 14;

    private String title;
    private String author;
    private ArrayList<ArrayList<FancyMessage>> lines = new ArrayList<>();

    /**
     * Instantiates a new Book.
     */
    public Book() {
        this.title = "BimmCore";
        this.author = "Bimmr";
        nextPage();
    }

    /**
     * Gets lines.
     *
     * @return the lines
     */
    public ArrayList<ArrayList<FancyMessage>> getLines() {
        return lines;
    }

    /**
     * Gets lines.
     *
     * @param page the page
     * @return the lines
     */
    public ArrayList<FancyMessage> getLines(int page) {
        return getLines().get(page);
    }

    /**
     * Add line book.
     *
     * @param message the message
     * @return the book
     */
    public Book addLine(String message) {
        return addLine(getCurrentPage(), new FancyMessage(message));
    }

    /**
     * Add line book.
     *
     * @param message the message
     * @return the book
     */
    public Book addLine(FancyMessage message) {
        return addLine(getCurrentPage(), message);
    }

    private int getCurrentPage() {
        return getLines().size() - 1;
    }

    /**
     * Add line book.
     *
     * @param page    the page
     * @param message the message
     * @return the book
     */
    public Book addLine(int page, String message) {
        return addLine(page, new FancyMessage(message));
    }

    /**
     * Add line book.
     *
     * @param page    the page
     * @param message the message
     * @return the book
     */
    public Book addLine(int page, FancyMessage message) {
        if (lines.size() <= page)
            nextPage();
        if (getLines(page).size() >= MAX_LINE_HEIGHT)
            return addLine(++page, message);

        lines.get(page).add(message);

        return this;
    }

    /**
     * Sets line.
     *
     * @param line    the line
     * @param message the message
     * @return the line
     */
    public Book setLine(int line, String message) {
        return setLine(getCurrentPage(), line, new FancyMessage(message));
    }

    /**
     * Sets line.
     *
     * @param page    the page
     * @param line    the line
     * @param message the message
     * @return the line
     */
    public Book setLine(int page, int line, String message) {
        return setLine(page, line, new FancyMessage(message));
    }

    /**
     * Sets line.
     *
     * @param line    the line
     * @param message the message
     * @return the line
     */
    public Book setLine(int line, FancyMessage message) {
        return setLine(getCurrentPage(), line, message);
    }

    /**
     * Sets line.
     *
     * @param page    the page
     * @param line    the line
     * @param message the message
     * @return the line
     */
    public Book setLine(int page, int line, FancyMessage message) {
        ArrayList<FancyMessage> list = lines.get(page);
        while (line > list.size())
            list.add(new FancyMessage());
        list.add(message);
        lines.add(page, list);
        lines.remove(page + 1);
        return this;
    }

    /**
     * Next page book.
     *
     * @return the book
     */
    public Book nextPage() {
        lines.add(new ArrayList());

        return this;
    }

    /**
     * Add blank line book.
     *
     * @return the book
     */
    public Book addBlankLine() {
        return addBlankLine(getCurrentPage());
    }

    /**
     * Add blank line book.
     *
     * @param page the page
     * @return the book
     */
    public Book addBlankLine(int page) {
        return addLine(page, "");
    }

    /**
     * To bottom book.
     *
     * @param space the space
     * @return the book
     */
    public Book toBottom(int space) {

        int line = MAX_LINE_HEIGHT - space - 1;

        return setLine(getCurrentPage(), line, "");
    }

    /**
     * Show.
     *
     * @param player the player
     */
    public void show(Player player) {
            player.openBook(getAsItem());

    }


    /**
     * Gets as item.
     *
     * @return the as item
     */
    public ItemStack getAsItem() {
            ItemStack iBook = new ItemStack(Material.WRITTEN_BOOK);

            //create the book
            BookMeta bookMeta = (BookMeta) iBook.getItemMeta();

            for (int page = 0; page < this.getLines().size(); page++) {

                ComponentBuilder builder = new ComponentBuilder();

                ArrayList<FancyMessage> lines = this.getLines(page);
                for (int line = 0; line < lines.size() && line < MAX_LINE_HEIGHT; line++)
                    builder.append(lines.get(line).getBaseComponents()).append("\n");

                bookMeta.spigot().addPage(builder.create());
            }

            //set the title and author of this book
            bookMeta.setTitle(this.title);
            bookMeta.setAuthor(this.author);

            //update the ItemStack with this new meta
            iBook.setItemMeta(bookMeta);


            return iBook;

    }

    /**
     * The type Book api.
     */
    public static class BookAPI {
        private static Class<?> chatSerializer = Reflection.getNMSClass("IChatBaseComponent$ChatSerializer");
        private static Class<?> packetDataSerializer = Reflection.getNMSClass("PacketDataSerializer");
        private static Class<?> craftMetaBook = Reflection.getCraftClass("inventory.CraftMetaBook");
        private static Class<?> packetPlayOutCustomPayLoad = Reflection.getNMSClass("PacketPlayOutCustomPayload");
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

                packetPlayOutOpenBookClass = Reflection.getNMSClass("PacketPlayOutOpenBook");
                enumHandClass = Reflection.getNMSClass("EnumHand");
                packetPlayOutOpenBookConstructor = Reflection.getConstructor(packetPlayOutOpenBookClass, enumHandClass);
                Method value = Reflection.getMethod(enumHandClass, "valueOf", String.class);
                mainHandEnum = Reflection.invokeMethod(value, null, "MAIN_HAND");

        }

        private static ItemStack getAsItemStack(Book book) {
            ItemStack iBook = new ItemStack(Material.WRITTEN_BOOK);

            //create the book
            BookMeta bookMeta = (BookMeta) iBook.getItemMeta();
            List<Object> pages;

            //get the pages
            pages = (List<Object>) Reflection.get(fieldPages, bookMeta);

            String prefix = "{\"extra\":[";
            String suffix = "],\"text\":\"\"}";

            for (int page = 0; page < book.getLines().size(); page++) {
                StringBuilder pageBuilder = new StringBuilder();

                ArrayList<FancyMessage> lines = book.getLines(page);
                pageBuilder.append(prefix);
                for (int line = 0; line < lines.size() && line < MAX_LINE_HEIGHT; line++) {
                    pageBuilder.append("[\"\", ").append(lines.get(line).toJSON()).append(",{\"text\":\"\\n\"}]");
                    if (line < lines.size() - 1)
                        pageBuilder.append(",");
                }
                pageBuilder.append(suffix);

                Object pageData = Reflection.invokeMethod(serializer, null, pageBuilder.toString());
                pages.add(pageData);
            }

            //set the title and author of this book
            bookMeta.setTitle(book.title);
            bookMeta.setAuthor(book.author);

            //update the ItemStack with this new meta
            iBook.setItemMeta(bookMeta);

            return iBook;
        }

        private static void openBook(ItemStack book, Player p) {
            int slot = p.getInventory().getHeldItemSlot();
            ItemStack iBook = book;
            ItemStack old = p.getInventory().getItem(slot);

            p.getInventory().setItem(slot, iBook);
                Object packetPlayOutOpenBookInstance = Reflection.newInstance(packetPlayOutOpenBookConstructor, mainHandEnum);
                Packets.sendPacket(p, packetPlayOutOpenBookInstance);



            p.getInventory().setItem(slot, old);
        }

        /**
         * Open book.
         *
         * @param book the book
         * @param p    the p
         */
        public static void openBook(Book book, Player p) {
            openBook(getAsItemStack(book), p);
        }

    }
}
