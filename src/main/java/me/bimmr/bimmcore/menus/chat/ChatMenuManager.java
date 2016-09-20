package me.bimmr.bimmcore.menus.chat;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;

import java.util.ArrayList;
import java.util.UUID;

class ChatMenuExample {
    public ChatMenuExample() {

        Player player = null;
        final String[] arenaNames = new String[]{"Arena1", "Arena2", "Arena3"};

        ChatMenu main = new ChatMenu().addLine("================= Edit Arenas ===============")
                .addLine("Edit Spawns", new ChatOptionClick() {
                            @Override
                            public void run(Player player) {
                                player.sendMessage("Editing Spawns");
                            }
                        }
                ).addLine("Edit Icon", new ChatOptionClick() {
                            @Override
                            public void run(Player player) {
                                new ChatMenu().addLine(" Edit Icon ==================")
                                        .addLine("Show Icon", new ChatOptionClick() {
                                            @Override
                                            public void run(Player player) {
                                                player.sendMessage("Icon");
                                            }
                                        })
                                        .addLine("Set Icon", new ChatOptionClick() {
                                            @Override
                                            public void run(Player player) {
                                                new ChatMenu().addLine("======== Set Icon =====")
                                                        .addLine("Something", new ChatOptionClick() {
                                                            @Override
                                                            public void run(Player player) {
                                                                player.sendMessage("Something Else");
                                                            }
                                                        }).show(player);
                                            }
                                        })
                                        .show(player);
                            }
                        }
                )
                .show(player);

        main.destroy();
    }
}

/**
 * Created by Randy on 07/06/16.
 */
public class ChatMenuManager implements Listener {

    private static ArrayList<ChatMenu> chatMenus = new ArrayList<ChatMenu>();

    public static void unregister(ChatMenu chatMenu) {
        chatMenus.remove(chatMenu);
    }

    public static void register(ChatMenu chatMenu) {
        chatMenus.add(chatMenu);
    }

    public ChatMenu getChatMenuFromUUID(UUID uuid) {
        for (ChatMenu chatMenu : chatMenus)
            for (ChatOption chatOption : chatMenu.getChatOptions())
                if (chatOption.getUUID() == uuid)
                    return chatMenu;
        return null;
    }

    public ChatOption getChatOptionFromUUID(UUID uuid) {
        for (ChatMenu chatMenu : chatMenus)
            for (ChatOption chatOption : chatMenu.getChatOptions())
                if (chatOption.getUUID() == uuid)
                    return chatOption;
        return null;
    }

    @EventHandler
    public void onCommand(PlayerCommandPreprocessEvent event) {
        if (event.getMessage().startsWith("bcore")) {
            ChatOption chatOption;
            if ((chatOption = getChatOptionFromUUID(UUID.fromString(event.getMessage().split(" ")[0]))) != null) {
                chatOption.getChatOptionClick().run(event.getPlayer());
            }
        }
    }
}
