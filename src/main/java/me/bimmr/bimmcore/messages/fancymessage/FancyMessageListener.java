package me.bimmr.bimmcore.messages.fancymessage;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;

import java.util.ArrayList;
import java.util.List;

/**
 * The type Fancy message listener.
 */
public class FancyMessageListener implements Listener {
    /**
     * The constant chats.
     */
    public static List<FancyClickEvent> chats = new ArrayList<>();

    /**
     * Chat click.
     *
     * @param event the event
     */
    @EventHandler
    public void chatClick(PlayerCommandPreprocessEvent event) {
        if (event.getMessage().startsWith("/BimmCore ")) {
            String uuid = event.getMessage().split(" ")[1];

            for (FancyClickEvent chatClickEvent : chats) {
                if (chatClickEvent.getUUID().toString().equals(uuid)) {
                    event.setCancelled(true);
                    chatClickEvent.setPlayer(event.getPlayer());
                    chatClickEvent.onClick();
                    chatClickEvent.startRemoval();
                    break;
                }
            }
        }
    }

}
