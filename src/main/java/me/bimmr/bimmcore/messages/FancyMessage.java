package me.bimmr.bimmcore.messages;

import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 * Created by Randy on 9/23/2015.
 */
class FancyMessageExample {
    public FancyMessageExample() {
        Player player = null;

        //Create the fancy message
        FancyMessage fancy = new FancyMessage("!").then("Click Here").suggest("Clicked");
        fancy.then(" Hover Here").tooltip("Hovered");

        //Send the fancy message
        fancy.send(player);
    }
}

public class FancyMessage {

    private ComponentBuilder builder;

    public FancyMessage() {
        builder = new ComponentBuilder("");
    }

    public FancyMessage(String string) {
        builder = new ComponentBuilder(string);
    }

    /**
     * Adds a new string section to the messages, this allows new tooltips, and
     * events.
     *
     * @param string
     * @return
     */
    public FancyMessage then(String string) {
        builder.append(string);
        return this;
    }

    /**
     * Tooltips are what appear when you hover over the messages
     *
     * @param strings
     * @return
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

    /**
     * The command/Message the player will use/say when they click the messages
     * <p/>
     * To make it a command, don't forget to have a "/"
     *
     * @param string
     * @return
     */
    public FancyMessage command(String string) {
        builder.event(new ClickEvent(ClickEvent.Action.RUN_COMMAND, string));
        return this;
    }

    /**
     * Autotypes a messages into their chatbar when they click the messages
     *
     * @param string
     * @return
     */
    public FancyMessage suggest(String string) {
        builder.event(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, string));
        return this;
    }

    /**
     * Opens the link for the player when they click the messages
     *
     * @param string
     * @return
     */
    public FancyMessage link(String string) {
        builder.event(new ClickEvent(ClickEvent.Action.OPEN_URL, string));
        return this;
    }

    /**
     * Sends the messages to a player
     *
     * @param player
     */
    public void send(Player player) {
        player.spigot().sendMessage(builder.create());
    }

    /**
     * Sends the messages to an array of players
     *
     * @param players
     */
    public void send(Player[] players) {
        BaseComponent[] components = builder.create();
        for (Player player : players)
            player.spigot().sendMessage(components);

    }

    public BaseComponent[] getBaseComponents() {
        return this.builder.create();
    }


    /**
     * Sends the fancy messages in the form of plain text to the console
     *
     * @param sender
     */
    public void sendToConsole(CommandSender sender) {
        for (BaseComponent component : builder.create())
            Bukkit.getConsoleSender().sendMessage(component.toPlainText());
    }
}
