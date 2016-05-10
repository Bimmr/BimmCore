package me.bimmr.bimmcore.messages;

import org.bukkit.entity.Player;

/**
 * Created by Randy on 05/09/16.
 */
public class Message {
    public static void sendMessage(MessageType messageType, Player player, String message, String... extra) {
        switch(messageType){
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
                sendChatMessage(player,message);
                break;
            case TITLE_AND_SUBTITLE:
                setTitleAndSubTitle(player, message, extra[0]);
                break;
        }
    }
    public static void setTitleAndSubTitle(Player player, String title, String subTitle){new Title(title, subTitle, 1,2,1).send(player);}
    public static void sendTitle(Player player, String message){new Title(message, " ",1,2,1).send(player);}
    public static void sendSubTitle(Player player, String message){new Title(" ", message,1,2,1).send(player);}
    public static void sendActionBar(Player player, String message){new ActionBar(message).send(player);}
    public static void sendChatMessage(Player player, String message){player.sendMessage(message);}

    public enum MessageType {TITLE, SUBTITLE, ACTIONBAR, CHAT, TITLE_AND_SUBTITLE}
}
