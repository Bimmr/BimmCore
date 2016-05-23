package me.bimmr.bimmcore.messages;

import me.bimmr.bimmcore.BimmCore;
import me.bimmr.bimmcore.events.timing.TimedEvent;
import me.bimmr.bimmcore.reflection.Packets;
import me.bimmr.bimmcore.reflection.Reflection;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;

/**
 * Created by Randy on 05/09/16.
 */
public class Title extends MessageDisplay {

    private static HashMap<String, BukkitTask> tasks    = new HashMap<>();
    private static HashMap<String, Title>      titles   = new HashMap<>();
    private        String                      subTitle = "";
    private        int                         fadeIn   = 0;
    private        int                         fadeOut  = 0;

    public Title(String title) {
        this.text = title;
    }

    public Title(String title, TimedEvent timedEvent) {
        this.text = title;
        setTimedEvent(timedEvent);
    }

    public Title(String title, String subTitle) {
        this.text = title;
        this.subTitle = title;
    }

    public Title(String title, String subTitle, TimedEvent timedEvent) {
        this.text = title;
        this.subTitle = title;
        setTimedEvent(timedEvent);
    }

    public Title(String title, String subTitle, int show) {
        this.text = title;
        this.subTitle = subTitle;
        this.fadeIn = 1;
        this.fadeOut = 1;
        this.time = show;
    }

    public Title(String title, String subTitle, int show, TimedEvent timedEvent) {
        this.text = title;
        this.subTitle = subTitle;
        this.fadeIn = 0;
        this.fadeOut = 0;
        this.time = show;
        setTimedEvent(timedEvent);
    }

    public Title(String title, int show) {
        this.text = title;
        this.subTitle = "";
        this.fadeIn = 1;
        this.fadeOut = 1;
        this.time = show;
    }

    public Title(String title, int show, TimedEvent timedEvent) {
        this.text = title;
        this.subTitle = "";
        this.fadeIn = 0;
        this.fadeOut = 0;
        this.time = show;
        setTimedEvent(timedEvent);
    }

    public Title(String title, String subTitle, int animationTime, int show) {
        this.text = title;
        this.subTitle = subTitle;
        this.fadeIn = animationTime;
        this.fadeOut = animationTime;
        this.time = show;
    }

    public Title(String title, String subTitle, int animationTime, int show, TimedEvent timedEvent) {
        this.text = title;
        this.subTitle = subTitle;
        this.fadeIn = animationTime;
        this.fadeOut = animationTime;
        this.time = show;
        setTimedEvent(timedEvent);
    }

    public Title(String title, String subTitle, int fadeIn, int show, int fadeOut) {
        this.text = title;
        this.subTitle = subTitle;
        this.fadeIn = fadeIn;
        this.time = show;
        this.fadeOut = fadeOut;
    }

    public Title(String title, String subTitle, int fadeIn, int show, int fadeOut, TimedEvent timedEvent) {
        this.text = title;
        this.subTitle = subTitle;
        this.fadeIn = fadeIn;
        this.time = show;
        this.fadeOut = fadeOut;
        setTimedEvent(timedEvent);
    }

    public String getSubTitle() {
        return this.subTitle;
    }

    public void setSubTitle(String text) {
        this.subTitle = text;
    }

    @Override
    public String getText() {
        return this.text;
    }

    @Override
    public void setText(String text) {
        this.text = text;
    }

    public int getFadeIn() {
        return this.fadeIn;
    }

    public int getFadeOut() {
        return this.fadeOut;
    }

    @Override
    public int getTime() {
        return this.time;
    }

    @Override
    public void send(final Player player) {
        clear(player);
        titles.put(player.getName(), this);
        tasks.put(player.getName(), new BukkitRunnable() {
            int timeLeft = time * 20;

            @Override
            public void run() {
                if (timedEvent != null && timeLeft % timedEvent.getTicks() == 0)
                    timedEvent.run();

                if (timeLeft <= 0)
                    clear(player);

                else if (timeLeft % 20 == 0 || (timedEvent != null && timeLeft % timedEvent.getTicks() == 0))
                    TitleAPI.sendTitle(player, text, subTitle, fadeIn * 20, 20, fadeOut * 20);

                timeLeft--;
            }
        }.runTaskTimer(BimmCore.getInstance(), 0L, 1L));
    }

    @Override
    public void stop(Player player) {
        TitleAPI.sendTitle(player, "", "", 0, 0, 0);
    }

    @Override
    public TimedEvent getTimedEvent() {
        return timedEvent;
    }

    @Override
    public void setTimedEvent(TimedEvent timedEvent) {
        this.timedEvent = timedEvent;
        this.timedEvent.setAttachedObject(this);
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

        static {
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
                Object lengthPacket = timeConstructor.newInstance(timeEnum, null, fadeIn, show, fadeOut);
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
