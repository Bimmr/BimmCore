package me.bimmr.bimmcore.messages;

import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.entity.Player;

/**
 * Created by Randy on 05/09/16.
 */
public class Message {
    public static void sendMessage(MessageType messageType, Player player, String message, Object... extra) {
        switch (messageType) {
            case TITLE:
                sendTitle(player, message);
                break;
            case SUBTITLE:
                sendSubTitle(player, message);
                break;
            case ACTIONBAR:
                sendActionBar(player, message);
                break;
            case CHAT:
                sendChatMessage(player, message);
                break;
            case TITLE_AND_SUBTITLE:
                sendTitleAndSubTitle(player, message, extra[0] != null ? (String) extra[0] : "");
                break;
            case BOSSBAR:
                sendBossBar(player, message, extra[0] != null ? (BarColor) extra[0] : BarColor.WHITE, extra[1] != null ? (BarStyle) extra[1] : BarStyle.SOLID, extra[2] != null ? (Double) extra[0] : 1.0);
                break;
        }
    }

    public static void sendTitleAndSubTitle(Player player, String title, String subTitle) {
        new Title(title, subTitle, 1, 2, 1).send(player);
    }

    public static void sendTitle(Player player, String message) {
        new Title(message, " ", 1, 2, 1).send(player);
    }

    public static void sendSubTitle(Player player, String message) {
        new Title(" ", message, 1, 2, 1).send(player);
    }

    public static void sendActionBar(Player player, String message) {
        new ActionBar(message, 2).send(player);
    }

    public static void sendChatMessage(Player player, String message) {
        player.sendMessage(message);
    }

    public static void sendBossBar(Player player, String text, BarColor barColor, BarStyle barStyle, Double progress) {
        new BossBar(text, 2).send(player);
    }

    public enum MessageType {TITLE, SUBTITLE, ACTIONBAR, CHAT, TITLE_AND_SUBTITLE, BOSSBAR}
}
