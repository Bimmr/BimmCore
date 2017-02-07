package me.bimmr.bimmcore.menus.book;

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
        setLine(line, new FancyMessage(message));
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
    public void clear(){
        lines.clear();
    }

    /**
     * Add a new line
     *
     * @param message
     * @return
     */
    public Book addLine(String message) {
        addLine(new FancyMessage(message));
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

                //Not getting subbed right
                for (int i = 0; i < book.lines.size(); i++) {
                    String part = book.lines.get(i).then("\n").toJSON();
                    part = part.substring(prefix.length(), part.length() - suffix.length());
                    if (i != book.lines.size() - 1)
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

        public static void openBook(ItemStack book, Player p){
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
