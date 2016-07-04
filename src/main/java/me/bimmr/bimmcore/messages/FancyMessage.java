package me.bimmr.bimmcore.messages;

import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
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

    private TextComponent text;
    private TextComponent all = new TextComponent("");

    public FancyMessage() {
        text = new TextComponent("");
    }

    public FancyMessage(String string) {
        text = new TextComponent(string);
    }

    /**
     * Adds a new string section to the messages, this allows new tooltips, and
     * events.
     *
     * @param string
     * @return
     */
    public FancyMessage then(String string) {
        all.addExtra(text);
        text = new TextComponent(string);
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
        text.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, component.create()));
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
        text.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, string));
        return this;
    }

    /**
     * Autotypes a messages into their chatbar when they click the messages
     *
     * @param string
     * @return
     */
    public FancyMessage suggest(String string) {
        text.setClickEvent(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, string));
        return this;
    }

    /**
     * Opens the link for the player when they click the messages
     *
     * @param string
     * @return
     */
    public FancyMessage link(String string) {
        text.setClickEvent(new ClickEvent(ClickEvent.Action.OPEN_URL, string));
        return this;
    }

    /**
     * Sends the messages to a player
     *
     * @param player
     */
    public void send(Player player) {
        if (text != null) all.addExtra(text);
        text = null;

        player.spigot().sendMessage(all);
    }

    /**
     * Sends the messages to an array of players
     *
     * @param players
     */
    public void send(Player[] players) {
        if (text != null) all.addExtra(text);

        for (Player player : players)
            player.spigot().sendMessage(all);

    }

    /**
     * Append a FancyMessage to the end of a FancyMessage
     *
     * @param fancyMessage
     */
    public void append(FancyMessage fancyMessage) {
        this.all.addExtra(fancyMessage.getTextComponent());
    }

    /**
     * Get the TextComponent that the FancyMessage uses
     *
     * @return
     */
    public TextComponent getTextComponent() {
        return this.all;
    }

    /**
     * Sends the fancy messages in the form of plain text to the console
     *
     * @param sender
     */
    public void sendToConsole(CommandSender sender) {
        Bukkit.getConsoleSender().sendMessage(all.toPlainText());
    }
}
