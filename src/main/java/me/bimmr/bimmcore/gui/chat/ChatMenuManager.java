package me.bimmr.bimmcore.gui.chat;

import me.bimmr.bimmcore.messages.fancymessage.FancyClickEvent;
import org.bukkit.entity.Player;

import java.util.ArrayList;

class ChatMenuExample {
    public ChatMenuExample() {

        Player player = null;
        final String[] arenaNames = new String[]{"Arena1", "Arena2", "Arena3"};

        ChatMenu main = new ChatMenu().addLine("================= Edit Arenas ===============")
                .addLine("Edit Spawns", new FancyClickEvent() {
                            @Override
                            public void onClick() {
                                player.sendMessage("Editing Spawns");
                            }
                        }
                ).addLine("Edit Icon", new FancyClickEvent() {
                            @Override
                            public void onClick() {
                                new ChatMenu().addLine(" Edit Icon ==================")
                                        .addLine("Show Icon", new FancyClickEvent() {
                                            @Override
                                            public void onClick() {
                                                player.sendMessage("Icon");
                                            }
                                        })
                                        .addLine("Set Icon", new FancyClickEvent() {
                                            @Override
                                            public void  onClick() {
                                                new ChatMenu().addLine("======== Set Icon =====")
                                                        .addLine("Something", new FancyClickEvent() {
                                                            @Override
                                                            public void onClick() {
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


public class ChatMenuManager {

    private static ArrayList<ChatMenu> chatMenus = new ArrayList<ChatMenu>();

    public static void unregister(ChatMenu chatMenu) {
        chatMenus.remove(chatMenu);
    }
    public static void register(ChatMenu chatMenu) {
        chatMenus.add(chatMenu);
    }

}
