package me.bimmr.bimmcore.messages.fancymessage;

import me.bimmr.bimmcore.reflection.Reflection;
import net.md_5.bungee.api.chat.*;
import net.md_5.bungee.api.chat.hover.content.Item;
import net.md_5.bungee.api.chat.hover.content.Text;
import net.md_5.bungee.chat.ComponentSerializer;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

/**
 * A Utilities/Builder class for the BaseComponents API
 */
public class FancyMessage {

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
     * @param string the String to start with
     */
    public FancyMessage(String string) {
        builder = new ComponentBuilder(ChatColor.RESET + string);
    }

    public FancyMessage(FancyMessage fancyMessage) {
        this("");
        then(fancyMessage);
    }


    /**
     * Adds a new string section to the messages, this allows new tooltips, and
     * events.
     *
     * @param string The String to add
     * @return The FancyMessage
     */
    public FancyMessage then(String string) {
        builder.append(ChatColor.RESET + string, ComponentBuilder.FormatRetention.NONE);
        return this;
    }


    public FancyMessage then(FancyMessage fancyMessage) {
        if (fancyMessage != null) {
                for (BaseComponent component : fancyMessage.getBaseComponents())
                    builder.append(component, ComponentBuilder.FormatRetention.NONE);

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

        if (strings != null && strings.length >= 1 && strings[0] != null) {
                List<Text> textList = new ArrayList<>();
                for (String string : strings)
                    textList.add(new Text(string));
                builder.event(new HoverEvent(HoverEvent.Action.SHOW_TEXT, textList.toArray(new Text[textList.size()])));

        }
        return this;
    }

    public FancyMessage showItem(ItemStack item) {
        if (item != null && item.getType() != Material.AIR) {
            //TODO: Find a way to do this with NMS
        }
        return this;
    }

    /**
     * The command the player will use/say when they message the messages
     *
     * @param string The command to run
     * @return The FancyMessage
     */
    public FancyMessage command(String string) {
        if (!string.startsWith("/"))
            string = "/" + string;
        builder.event(new ClickEvent(ClickEvent.Action.RUN_COMMAND, string));
        return this;
    }

    /**
     * The message the player will use/say when they message the messages
     *
     * @param string The command to run
     * @return The FancyMessage
     */
    public FancyMessage say(String string) {
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
    public FancyMessage onClick(FancyClick fce) {
        FancyClickEvent fancyClickEvent = new FancyClickEvent() {
            @Override
            public void onClick() {
                fce.onClick(this);
            }
        };
        return onClick(fancyClickEvent);
    }

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
