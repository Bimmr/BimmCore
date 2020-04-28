package me.bimmr.bimmcore.messages;

import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.entity.Player;

/**
 * Created by Randy on 05/09/16.
 */
public class Message {

    /**
     * Send a Message using the messagetype
     *
     * @param messageType the message type
     * @param player      the player
     * @param message     the message
     * @param extra       the extra
     */
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

    /**
     * Send a Title and a subTitle
     *
     * @param player   the player
     * @param title    the title
     * @param subTitle the sub title
     */
    public static void sendTitleAndSubTitle(Player player, String title, String subTitle) {
        new Title(title, subTitle, 1, 2, 1).send(player);
    }

    /**
     * Send a title
     *
     * @param player  the player
     * @param message the message
     */
    public static void sendTitle(Player player, String message) {
        new Title(message, " ", 1, 2, 1).send(player);
    }

    /**
     * Send a subtitle
     *
     * @param player  the player
     * @param message the message
     */
    public static void sendSubTitle(Player player, String message) {
        new Title(" ", message, 1, 2, 1).send(player);
    }

    /**
     * Send an actionbar
     *
     * @param player  the player
     * @param message the message
     */
    public static void sendActionBar(Player player, String message) {
        new ActionBar(message, 2).send(player);
    }

    /**
     * Send a regular chat message
     *
     * @param player  the player
     * @param message the message
     */
    public static void sendChatMessage(Player player, String message) {
        player.sendMessage(message);
    }

    /**
     * Send a BossBar
     *
     * @param player   the player
     * @param text     the text
     * @param barColor the bar color
     * @param barStyle the bar style
     * @param progress the progress
     */
    public static void sendBossBar(Player player, String text, BarColor barColor, BarStyle barStyle, Double progress) {
        new BossBar(text, 2, barColor, barStyle, progress).send(player);
    }

    /**
     * The enum Message type.
     */
    public enum MessageType {
        /**
         * Title message type.
         */
        TITLE,
        /**
         * Subtitle message type.
         */
        SUBTITLE,
        /**
         * Actionbar message type.
         */
        ACTIONBAR,
        /**
         * Chat message type.
         */
        CHAT,
        /**
         * Title and subtitle message type.
         */
        TITLE_AND_SUBTITLE,
        /**
         * Bossbar message type.
         */
        BOSSBAR}
}
