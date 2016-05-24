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

    /**
     * Create a title
     *
     * @param title
     */
    public Title(String title) {
        this(title, "", 0, 2, 0, null);
    }

    /**
     * Create a title
     *
     * @param title
     * @param show
     */
    public Title(String title, int show) {
        this(title, "", 0, show, 0, null);
    }

    /**
     * Create a title
     *
     * @param title
     * @param subTitle
     */
    public Title(String title, String subTitle) {
        this(title, subTitle, 0, 2, 0, null);
    }

    /**
     * Create a title
     *
     * @param title
     * @param subTitle
     * @param show
     */
    public Title(String title, String subTitle, int show) {
        this(title, subTitle, 0, show, 0, null);
    }

    /**
     * Create a title
     *
     * @param title
     * @param subTitle
     * @param animationTime
     * @param show
     */
    public Title(String title, String subTitle, int animationTime, int show) {
        this(title, subTitle, animationTime, show, animationTime, null);
    }

    /**
     * Create a title
     *
     * @param title
     * @param subTitle
     * @param fadeIn
     * @param show
     * @param fadeOut
     */
    public Title(String title, String subTitle, int fadeIn, int show, int fadeOut) {
        this(title, subTitle, fadeIn, show, fadeOut, null);
    }

    /**
     * Create a title
     *
     * @param title
     * @param timedEvent
     */
    public Title(String title, TimedEvent timedEvent) {
        this(title, "", 0, 2, 0, timedEvent);
    }

    /**
     * Create a title
     *
     * @param title
     * @param show
     * @param timedEvent
     */
    public Title(String title, int show, TimedEvent timedEvent) {
        this(title, "", 0, show, 0, timedEvent);
    }

    /**
     * Create a title
     *
     * @param title
     * @param subTitle
     * @param timedEvent
     */
    public Title(String title, String subTitle, TimedEvent timedEvent) {
        this(title, subTitle, 0, 2, 0, timedEvent);
    }

    /**
     * Create a title
     *
     * @param title
     * @param subTitle
     * @param show
     * @param timedEvent
     */
    public Title(String title, String subTitle, int show, TimedEvent timedEvent) {
        this(title, subTitle, 0, show, 0, timedEvent);
    }

    /**
     * Create a title
     *
     * @param title
     * @param subTitle
     * @param animationTime
     * @param show
     * @param timedEvent
     */
    public Title(String title, String subTitle, int animationTime, int show, TimedEvent timedEvent) {
        this(title, subTitle, animationTime, show, animationTime, timedEvent);
    }

    /**
     * Create a title
     *
     * @param title
     * @param subTitle
     * @param fadeIn
     * @param show
     * @param fadeOut
     * @param timedEvent
     */
    public Title(String title, String subTitle, int fadeIn, int show, int fadeOut, TimedEvent timedEvent) {
        this.text = title;
        this.subTitle = subTitle;
        this.fadeIn = fadeIn;
        this.time = show;
        this.fadeOut = fadeOut;
        setTimedEvent(timedEvent);
    }

    /**
     * Check if a Title is being sent to the player
     *
     * @param player
     * @return
     */
    private static boolean isRunning(Player player) {
        return titles.containsKey(player.getName());
    }

    /**
     * Get the SubTitle text
     *
     * @return
     */
    public String getSubTitle() {
        return this.subTitle;
    }

    /**
     * Set the SubTitle text
     *
     * @param text
     */
    public void setSubTitle(String text) {
        this.subTitle = text;
    }

    /**
     * Get the Title text
     *
     * @return
     */
    @Override
    public String getText() {
        return this.text;
    }

    /**
     * Set the Title text
     *
     * @param text
     */
    @Override
    public void setText(String text) {
        this.text = text;
    }

    /**
     * Get the FadeIn time
     *
     * @return
     */
    public int getFadeIn() {
        return this.fadeIn;
    }

    /**
     * Set the FadeIn time
     *
     * @param fadeIn
     */
    public void setFadeIn(int fadeIn) {
        this.fadeIn = fadeIn;
    }

    /**
     * Get the FadeOut time
     *
     * @return
     */
    public int getFadeOut() {
        return this.fadeOut;
    }

    /**
     * Set the FadeOut time
     *
     * @param fadeOut
     */
    public void setFadeOut(int fadeOut) {
        this.fadeOut = fadeOut;
    }

    /**
     * Get the Show time
     *
     * @return
     */
    @Override
    public int getTime() {
        return this.time;
    }

    /**
     * Send the Title to the player
     *
     * @param player
     */
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

    /**
     * Stop sending the title to the player
     *
     * @param player
     */
    @Override
    public void stop(Player player) {
        clear(player);
    }

    /**
     * Get the TimedEvent
     *
     * @return
     */
    @Override
    public TimedEvent getTimedEvent() {
        return timedEvent;
    }

    /**
     * Set the TimedEvent
     *
     * @param timedEvent
     */
    @Override
    public void setTimedEvent(TimedEvent timedEvent) {
        if (timedEvent != null) {
            this.timedEvent = timedEvent;
            this.timedEvent.setAttachedObject(this);
        }
    }

    /**
     * Clear the title off the player's screen
     *
     * @param player
     */
    public void clear(Player player) {

        if (isRunning(player)) {
            TitleAPI.sendTitle(player, " ", " ", 0, 0, 0);
            tasks.get(player.getName()).cancel();
            tasks.remove(player.getName());
            titles.remove(player.getName());
        }
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
