package me.bimmr.bimmcore.messages;

import me.bimmr.bimmcore.reflection.Packets;
import me.bimmr.bimmcore.reflection.Reflection;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * Created by Randy on 05/09/16.
 */
public class Title {

    private String title = "", subTitle = "";
    private int fadeIn = 1, show = 2, fadeOut = 1;

    public Title(String title) {
        this.title = title;
    }

    public Title(String title, String subTitle) {
        this.title = title;
        this.subTitle = title;
    }

    public Title(String title, String subTitle, int animationTime, int show) {
        this.title = title;
        this.subTitle = subTitle;
        this.fadeIn = animationTime;
        this.fadeOut = animationTime;
        this.show = show;
    }

    public Title(String title, String subTitle, int fadeIn, int show, int fadeOut) {
        this.title = title;
        this.subTitle = subTitle;
        this.fadeIn = fadeIn;
        this.show = show;
        this.fadeOut = fadeOut;
    }

    public void send(Player player) {
        TitleAPI.sendTitle(player, this.title, this.subTitle, this.fadeIn, this.show, this.fadeOut);
    }

    public void clear(Player player) {
        TitleAPI.sendTitle(player, " ", " ", 0, 0, 0);
    }

    public static class TitleAPI {

        private static Class<?>       chatSerializer;
        private static Method         serializer;
        private static Class<?>       chatBaseComponent;
        private static Constructor<?> chatConstructor;
        private static Constructor<?> timeConstructor;
        private static Class<?>       titleAction;
        private static Object         timeEnum, titleEnum, subEnum;

        public static void setup() {
            chatBaseComponent = Reflection.getNMSClass("IChatBaseComponent");
            chatSerializer = Reflection.getNMSClass("IChatBaseComponent$ChatSerializer");
            titleAction = Reflection.getNMSClass("PacketPlayOutTitle$EnumTitleAction");

            try {
                serializer = chatSerializer.getMethod("a", String.class);

                Class<?> packetType = Reflection.getNMSClass("PacketPlayOutTitle");
                chatConstructor = packetType.getConstructor(titleAction, chatBaseComponent);
                timeConstructor = packetType.getConstructor(titleAction, chatBaseComponent, Integer.TYPE, Integer.TYPE, Integer.TYPE);

            } catch (NoSuchMethodException e) {
                e.printStackTrace();
            }

            titleEnum = titleAction.getEnumConstants()[0];
            subEnum = titleAction.getEnumConstants()[1];
            timeEnum = titleAction.getEnumConstants()[2];
        }

        public static void sendTitle(Player player, String title, String subTitle, int fadeIn, int show, int fadeOut) {
            try {

                Object lengthPacket = timeConstructor.newInstance(timeEnum, null, fadeIn * 20, show * 20, fadeOut * 20);
                Packets.sendPacket(player, lengthPacket);

                Object titleSerialized = serializer.invoke(null, "{\"text\":\"" + title + "\"}");
                Object titlePacket = chatConstructor.newInstance(titleEnum, titleSerialized);
                Packets.sendPacket(player, titlePacket);

                if (subTitle != "") {
                    Object subSerialized = serializer.invoke(null, "{\"text\":\"" + ChatColor.translateAlternateColorCodes('&', subTitle) + "\"}");
                    Object subPacket = chatConstructor.newInstance(subEnum, subSerialized);
                    Packets.sendPacket(player, subPacket);
                }

            } catch (InvocationTargetException | IllegalAccessException | InstantiationException e) {
                e.printStackTrace();
            }
        }

    }
}
