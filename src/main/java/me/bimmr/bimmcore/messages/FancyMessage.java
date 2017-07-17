package me.bimmr.bimmcore.messages;

import me.bimmr.bimmcore.events.message.FancyClickEvent;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.chat.ComponentSerializer;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;

import java.util.ArrayList;
import java.util.List;

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

    private BaseComponent[] built;
    private ComponentBuilder builder;

    public static FancyMessage getFromJSON(String json) {
        FancyMessage fm = new FancyMessage();


        return fm;
    }

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
     * The command/Message the player will use/say when they message the messages
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
     * Change the page in a book
     *
     * @param page
     * @return
     */
    public FancyMessage changePage(String page) {
        builder.event(new ClickEvent(ClickEvent.Action.CHANGE_PAGE, page));
        return this;
    }

    /**
     * CAN NOT BE USED WITH COMMAND
     * Add a callback for an onClick
     * <p>
     * CallBack self deletes after 5 minutes from first message, unless told otherwise
     *
     * @param fce
     * @return
     */
    public FancyMessage onClick(FancyClickEvent fce) {
        FancyMessageListener.chats.add(fce);
        return command("/BimmCore " + fce.getUUID());
    }

    /**
     * Autotypes a messages into their chatbar when they message the messages
     *
     * @param string
     * @return
     */
    public FancyMessage suggest(String string) {
        builder.event(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, string));
        return this;
    }

    /**
     * Opens the link for the player when they message the messages
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

        if(built == null)
            built = builder.create();
        player.spigot().sendMessage(built);
    }

    /**
     * Sends the messages to an array of players
     *
     * @param players
     */
    public void send(Player[] players) {
        if(built == null)
            built = builder.create();
        for (Player player : players)
            player.spigot().sendMessage(built);
    }

    /**
     * Get all the BaseComponents
     *
     * @return
     */
    public BaseComponent[] getBaseComponents() {
        return this.builder.create();
    }

    /**
     * Get the FancyMessage as JSON
     *
     * @return
     */
    public String toJSON() {
        return ComponentSerializer.toString(getBaseComponents());
    }


    /**
     * Sends the fancy messages in the form of plain text to the console
     *
     * @param sender
     */
    public void sendToConsole(CommandSender sender) {
        if(built == null)
            built = builder.create();
        for (BaseComponent component : built)
            Bukkit.getConsoleSender().sendMessage(component.toPlainText());
    }


    /**
     * Listener for the Click Callbacks
     */
    public static class FancyMessageListener implements Listener {
        public static List<FancyClickEvent> chats = new ArrayList<>();

        /**
         * Event
         *
         * @param event
         */
        @EventHandler
        public void chatClick(PlayerCommandPreprocessEvent event) {
            if (event.getMessage().startsWith("/BimmCore ")) {
                String uuid = event.getMessage().split(" ")[1];

                for (FancyClickEvent chatClickEvent : chats)
                    if (chatClickEvent.getUUID().toString().equals(uuid)) {
                        event.setCancelled(true);
                        chatClickEvent.onClick();
                        chatClickEvent.startRemoval();
                        break;
                    }
            }
        }

    }

}
