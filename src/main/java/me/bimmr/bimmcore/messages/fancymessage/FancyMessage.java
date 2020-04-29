package me.bimmr.bimmcore.messages.fancymessage;

import me.bimmr.bimmcore.BimmCore;
import me.bimmr.bimmcore.reflection.Reflection;
import net.md_5.bungee.api.chat.*;
import net.md_5.bungee.chat.ComponentSerializer;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.List;

/**
 * Created by Randy on 9/23/2015.
 */
//class FancyMessageExample {
//    public FancyMessageExample() {
//        Player player = null;
//
//        //Create the fancy message
//        FancyMessage fancy = new FancyMessage("!").then("Click Here").suggest("Clicked");
//        fancy.then(" Hover Here").tooltip("Hovered");
//
//        //Send the fancy message
//        fancy.send(player);
//    }
//}

public class FancyMessage {

    //Show Item
    private static Class<?> classCraftItemStack = Reflection.getCraftClass("inventory.CraftItemStack");
    private static Method methodAsNMSCopy = Reflection.getMethod(classCraftItemStack, "asNMSCopy", ItemStack.class);
    private static Class<?> classNBTTagCompound = Reflection.getNMSClass("NBTTagCompound");
    private static Constructor<?> consNBTTagCompount = Reflection.getConstructor(classNBTTagCompound);
    private static Class<?> classNmsItemStack = Reflection.getNMSClass("ItemStack");
    private static Method nmsItemStackSave = Reflection.getMethod(classNmsItemStack, "save", classNBTTagCompound);

    //Append FancyMessage together
    private static Class classComponentBuilder = Reflection.getClass("net.md_5.bungee.api.chat.ComponentBuilder");
    private static Field fieldParts = Reflection.getField(classComponentBuilder, "parts");


    private BaseComponent[] built;
    private ComponentBuilder builder;

    /**
     * Default Constructor
     */
    public FancyMessage() {
        this("");
    }

    /**
     * Constructor with first text
     *
     * @param string
     */
    public FancyMessage(String string) {
        builder = new ComponentBuilder(ChatColor.RESET + string);
    }

    /**
     * Adds a new string section to the messages, this allows new tooltips, and
     * events.
     *
     * @param string The String to add
     * @return The FancyMessage
     */
    public FancyMessage then(String string) {
        builder.append(ChatColor.RESET + string);
        builder.event((HoverEvent) null);
        builder.event((ClickEvent) null);
        return this;
    }

    public FancyMessage reset() {
        builder.append(ChatColor.RESET + "");
        builder.event((HoverEvent) null);
        builder.event((ClickEvent) null);
        return this;
    }

    public FancyMessage then(FancyMessage fancyMessage) {
        if(BimmCore.supports(12)) {
            builder.append(fancyMessage.getBaseComponents());
            builder.event((HoverEvent) null);
            builder.event((ClickEvent) null);
        }else{
            builder.append("");
            Object parts = Reflection.get(fieldParts, builder);
            for(BaseComponent component : fancyMessage.getBaseComponents())
                ((List<BaseComponent>)parts).add(component);
        }
        return this;
    }

    /**
     * Tooltips are what appear when you hover over the messages
     *
     * @param strings The tooltips to add
     * @return The FancyMessage
     */
    public FancyMessage tooltip(String... strings) {
        ComponentBuilder component = new ComponentBuilder(strings[0]);
        for (int i = 1; i != strings.length; i++) {
            component.append("\n");
            component.append(strings[i]);
        }
        builder.event(new HoverEvent(HoverEvent.Action.SHOW_TEXT, component.create()));
        return this;
    }

    public FancyMessage showItem(ItemStack item) {

//        net.minecraft.server.v1_15_R1.ItemStack nmsItemStack = CraftItemStack.asNMSCopy(item);
        Object asNMSCopy = Reflection.invokeMethod(methodAsNMSCopy, null, item);

//        NBTTagCompound compound = new NBTTagCompound();
        Object compound = Reflection.newInstance(consNBTTagCompount);

//        nmsItemStack.save(compound);
        Reflection.invokeMethod(nmsItemStackSave, asNMSCopy, compound);

        String json = compound.toString();
        BaseComponent[] hoverEventComponents = new BaseComponent[]{
                new TextComponent(json)
        };
        builder.event(new HoverEvent(HoverEvent.Action.SHOW_ITEM, hoverEventComponents));
        return this;
    }

    /**
     * The command/Message the player will use/say when they message the messages
     * To make it a command, don't forget to have a "/"
     *
     * @param string The command to run
     * @return The FancyMessage
     */
    public FancyMessage command(String string) {
        builder.event(new ClickEvent(ClickEvent.Action.RUN_COMMAND, string));
        return this;
    }

    /**
     * Change the page in a book
     *
     * @param page The page to change to
     * @return The FancyMessage
     */
    public FancyMessage changePage(String page) {
        builder.event(new ClickEvent(ClickEvent.Action.CHANGE_PAGE, page));
        return this;
    }

    /**
     * CAN NOT BE USED WITH COMMAND
     * Add a callback for an onClick
     * CallBack self deletes after 5 minutes from first message, unless told otherwise
     *
     * @param fce The FancyClickEvent
     * @return The FancyMessage
     */
    public FancyMessage onClick(FancyClickEvent fce) {
        FancyMessageListener.chats.add(fce);
        return command("/BimmCore " + fce.getUUID());
    }

    /**
     * Autotypes a messages into their chatbar when they message the messages
     *
     * @param string The String to type
     * @return The FancyMessage
     */
    public FancyMessage suggest(String string) {
        builder.event(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, string));
        return this;
    }

    /**
     * Opens the link for the player when they message the messages
     *
     * @param string The link
     * @return The FancyMessage
     */
    public FancyMessage link(String string) {
        builder.event(new ClickEvent(ClickEvent.Action.OPEN_URL, string));
        return this;
    }

    /**
     * Sends the messages to a player
     *
     * @param player The player
     */
    public void send(Player player) {

        if (built == null)
            built = builder.create();
        player.spigot().sendMessage(built);
    }

    /**
     * Sends the messages to an array of players
     *
     * @param players The players
     */
    public void send(Player[] players) {
        if (built == null)
            built = builder.create();
        for (Player player : players)
            player.spigot().sendMessage(built);
    }

    /**
     * @return Get all the BaseComponents
     */
    public BaseComponent[] getBaseComponents() {
        if (built == null)
            built = builder.create();
        return this.built;
    }

    /**
     * @return Get the FancyMessage as JSON
     */
    public String toJSON() {
        return ComponentSerializer.toString(getBaseComponents());
    }


    /**
     * Sends the fancy messages in the form of plain text to the console
     *
     * @param sender The console sender
     */
    public void sendToConsole(CommandSender sender) {
        Bukkit.getConsoleSender().sendMessage(toPlainText());
    }

    /**
     * @return Get the Fancy Message as just plain text
     */
    public String toPlainText() {
        if (built == null)
            built = builder.create();
        StringBuilder plainTxt = new StringBuilder();
        for (BaseComponent component : built)
            plainTxt.append(component.toPlainText());

        return plainTxt.toString();
    }


}
