package me.bimmr.bimmcore.messages.fancymessage;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;

import java.util.ArrayList;
import java.util.List;

/**
 * Listener for the FancyMessage Click Callbacks
 */
public class FancyMessageListener implements Listener {
    public static List<FancyClickEvent> chats = new ArrayList<>();

    /**
     * The FancyMessage OnClick Callback Listener
     *
     * @param event The PlayercommandPreprocessEvent
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
