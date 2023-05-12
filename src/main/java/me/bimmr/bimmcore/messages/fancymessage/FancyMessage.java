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
 * The type Fancy message.
 */
public class FancyMessage {

    private BaseComponent[] built;
    private ComponentBuilder builder;

    /**
     * Instantiates a new Fancy message.
     */
    public FancyMessage() {
        this("");
    }

    /**
     * Instantiates a new Fancy message.
     *
     * @param string the string
     */
    public FancyMessage(String string) {
        builder = new ComponentBuilder(ChatColor.RESET + string);
    }

    /**
     * Instantiates a new Fancy message.
     *
     * @param fancyMessage the fancy message
     */
    public FancyMessage(FancyMessage fancyMessage) {
        this("");
        then(fancyMessage);
    }


    /**
     * Then fancy message.
     *
     * @param string the string
     * @return the fancy message
     */
    public FancyMessage then(String string) {
        builder.append(ChatColor.RESET + string, ComponentBuilder.FormatRetention.NONE);
        return this;
    }


    /**
     * Then fancy message.
     *
     * @param fancyMessage the fancy message
     * @return the fancy message
     */
    public FancyMessage then(FancyMessage fancyMessage) {
        if (fancyMessage != null) {
                for (BaseComponent component : fancyMessage.getBaseComponents())
                    builder.append(component, ComponentBuilder.FormatRetention.NONE);

        }
        return this;
    }

    /**
     * Tooltip fancy message.
     *
     * @param strings the strings
     * @return the fancy message
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

    /**
     * Show item fancy message.
     *
     * @param item the item
     * @return the fancy message
     */
    public FancyMessage showItem(ItemStack item) {
        if (item != null && item.getType() != Material.AIR) {
            //TODO: Find a way to do this with NMS
        }
        return this;
    }

    /**
     * Command fancy message.
     *
     * @param string the string
     * @return the fancy message
     */
    public FancyMessage command(String string) {
        if (!string.startsWith("/"))
            string = "/" + string;
        builder.event(new ClickEvent(ClickEvent.Action.RUN_COMMAND, string));
        return this;
    }

    /**
     * Say fancy message.
     *
     * @param string the string
     * @return the fancy message
     */
    public FancyMessage say(String string) {
        builder.event(new ClickEvent(ClickEvent.Action.RUN_COMMAND, string));
        return this;
    }

    /**
     * Change page fancy message.
     *
     * @param page the page
     * @return the fancy message
     */
    public FancyMessage changePage(String page) {
        builder.event(new ClickEvent(ClickEvent.Action.CHANGE_PAGE, page));

        return this;
    }

    /**
     * On click fancy message.
     *
     * @param fce the fce
     * @return the fancy message
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

    /**
     * On click fancy message.
     *
     * @param fce the fce
     * @return the fancy message
     */
    public FancyMessage onClick(FancyClickEvent fce) {
        FancyMessageListener.chats.add(fce);
        return command("/BimmCore " + fce.getUUID());
    }

    /**
     * Suggest fancy message.
     *
     * @param string the string
     * @return the fancy message
     */
    public FancyMessage suggest(String string) {
        builder.event(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, string));
        return this;
    }

    /**
     * Link fancy message.
     *
     * @param string the string
     * @return the fancy message
     */
    public FancyMessage link(String string) {
        builder.event(new ClickEvent(ClickEvent.Action.OPEN_URL, string));
        return this;
    }

    /**
     * Send.
     *
     * @param player the player
     */
    public void send(Player player) {
        if (built == null)
            built = builder.create();

        player.spigot().sendMessage(built);
    }

    /**
     * Send.
     *
     * @param players the players
     */
    public void send(Player[] players) {
        if (built == null)
            built = builder.create();

        for (Player player : players)
            player.spigot().sendMessage(built);
    }

    /**
     * Get base components base component [ ].
     *
     * @return the base component [ ]
     */
    public BaseComponent[] getBaseComponents() {
        if (built == null)
            built = builder.create();

        return this.built;
    }

    /**
     * To json string.
     *
     * @return the string
     */
    public String toJSON() {
        return ComponentSerializer.toString(getBaseComponents());
    }


    /**
     * Send to console.
     *
     * @param sender the sender
     */
    public void sendToConsole(CommandSender sender) {
        Bukkit.getConsoleSender().sendMessage(toPlainText());
    }

    /**
     * To plain text string.
     *
     * @return the string
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
