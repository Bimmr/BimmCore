package me.bimmr.bimmcore.messages;

import me.bimmr.bimmcore.BimmCore;
import me.bimmr.bimmcore.utils.timed.Timed;
import me.bimmr.bimmcore.utils.timed.TimedEvent;
import me.bimmr.bimmcore.reflection.Packets;
import me.bimmr.bimmcore.reflection.Reflection;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;

/**
 * A Utilities class to create an actionbar
 */
public class ActionBar extends MessageDisplay {

    private static HashMap<String, BukkitTask> tasks = new HashMap<>();
    private static HashMap<String, ActionBar>  bars  = new HashMap<>();

    /**
     * Create an actionbar
     *
     * @param text The Text
     */
    public ActionBar(String text) {
        this(text, 2, null);
    }

    /**
     * Create an actionbar
     *
     * @param text The Text
     * @param time The Time
     */
    public ActionBar(String text, int time) {
        this(text, time, null);
    }

    /**
     * Create an actionbar
     *
     * @param text The Text
     * @param timed The TimedEvent
     * @param timer the timer
     */
    public ActionBar(String text, Timed timed, int timer ) {
        this(text, 2, new TimedEvent(timer) {
            @Override
            public void run() {
                timed.onRun(this);
            }
        }, false);
    }

    /**
     * Create an actionbar
     *
     * @param text The Text
     * @param timedEvent The TimedEvent
     */
    public ActionBar(String text, TimedEvent timedEvent) {
        this(text, 2, timedEvent, false);
    }

    /**
     * Create an actionbar
     *
     * @param text The Text
     * @param timed The TimedEvent
     * @param timer the timer
     * @param autoStartTimedEvent If the TimedEvent autostarts
     */
    public ActionBar(String text, Timed timed, int timer, boolean autoStartTimedEvent) {
        this(text, 2, new TimedEvent(timer) {
            @Override
            public void run() {
                timed.onRun(this);
            }
        }, autoStartTimedEvent);
    }

    /**
     * Create an actionbar
     *
     * @param text The Text
     * @param timedEvent The TimedEvent
     * @param autoStartTimedEvent If the TimedEvent autostarts
     */
    public ActionBar(String text, TimedEvent timedEvent, boolean autoStartTimedEvent) {
        this(text, 2, timedEvent, autoStartTimedEvent);
    }

    /**
     * Create an actionbar
     *
     * @param text The text
     * @param time The time
     * @param timed If the TimedEvent Autostarts
     * @param timer the timer
     */
    public ActionBar(String text, int time, Timed timed, int timer) {
        this(text, time,new TimedEvent(timer) {
            @Override
            public void run() {
                timed.onRun(this);
            }
        }, false);
    }
    /**
     * Create an actionbar
     *
     * @param text The text
     * @param time The time
     * @param timedEvent If the TimedEvent Autostarts
     */
    public ActionBar(String text, int time, TimedEvent timedEvent) {
        this(text,time,timedEvent, false);
    }
    /**
     * Create an actionbar
     *
     * @param text The Text
     * @param time The time
     * @param timed The TimedEvent
     * @param time time;
     * @param autoStartTimedEvent If the TimedEvent autostarts
     */
    public ActionBar(String text, int time, Timed timed, int timer, boolean autoStartTimedEvent) {
        this.text = text;
        this.time = time;

        this.setTimedEvent(timed, timer, autoStartTimedEvent);
    }/**
     * Create an actionbar
     *
     * @param text The Text
     * @param time The time
     * @param timedEvent The TimedEvent
     * @param autoStartTimedEvent If the TimedEvent autostarts
     */
    public ActionBar(String text, int time, TimedEvent timedEvent, boolean autoStartTimedEvent) {
        this.text = text;
        this.time = time;

        setTimedEvent(timedEvent, autoStartTimedEvent);
    }

    /**
     *
     *
     * @param player The player
     * @return Check if a title is being sent to the player
     */
    private static boolean isRunning(Player player) {
        return tasks.containsKey(player.getName());
    }

    /**
     * Clear the player's title
     *
     * @param player The Player
     */
    public static void clear(Player player) {
        if (isRunning(player)) {
            if (BimmCore.supports(17))
                player.spigot().sendMessage(ChatMessageType.ACTION_BAR, new TextComponent(""));
            else if (BimmCore.supports(11))
                ActionBarAPI.sendActionBar(player, "");
            else
                ActionBarAPIOld.sendActionBar(player, "");

            tasks.get(player.getName()).cancel();
            tasks.remove(player.getName());
            bars.remove(player.getName());
        }
    }

    /**
     *
     *
     * @param player The player
     * @return Get the actionbar that is being played for the player
     */
    public static ActionBar getPlayingActionBar(Player player) {
        if (isRunning(player))
            return bars.get(player.getName());
        else
            return null;
    }

    /**
     *
     *
     * @return Get the text
     */
    @Override
    public String getText() {
        return text;
    }

    /**
     * Set the text
     *
     * @param text The text to set
     */
    @Override
    public void setText(String text) {
        this.text = text;
    }

    /**
     *
     *
     * @return Get the time
     */
    @Override
    public int getTime() {
        return time;
    }

    /**
     *
     *
     * @return Get the TimedEvent
     */
    @Override
    public TimedEvent getTimedEvent() {
        return timedEvent;
    }

    /**
     * Set the TimedEvent
     *
     * @param timed The TimedEvent
     */
    public void setTimedEvent(Timed timed, int time) {
        this.setTimedEvent(new TimedEvent(time) {
            @Override
            public void run() {
                timed.onRun(this);
            }
        });
    }
    /**
     * Set the TimedEvent
     *
     * @param timedEvent The TimedEvent
     */
    @Override
    public void setTimedEvent(TimedEvent timedEvent) {
        if (timedEvent != null) {
            this.timedEvent = timedEvent;
            this.timedEvent.setAttachedObject(this);
        }
    }

    /**
     * Stop showing the actionbar
     *
     * @param player The player
     */
    @Override
    public void stop(Player player) {
        clear(player);
    }

    /**
     * Send the player an title
     *
     * @param player The Player
     */
    @Override
    public void send(final Player player) {
        clear(player);
        bars.put(player.getName(), this);
        tasks.put(player.getName(), new BukkitRunnable() {
            int timeLeft = time * (time == Integer.MAX_VALUE ? 1 : 20);

            @Override
            public void run() {
                if (timedEvent != null && timeLeft % timedEvent.getTicks() == 0)
                    timedEvent.run();

                if (timeLeft <= 0)
                    clear(player);

                else if (timeLeft % 20 == 0 || (timedEvent != null && timeLeft % timedEvent.getTicks() == 0))
                    if (BimmCore.supports(17))
                        player.spigot().sendMessage(ChatMessageType.ACTION_BAR, new TextComponent(text));
                    else if (BimmCore.supports(11))
                        ActionBarAPI.sendActionBar(player, text);
                    else
                        ActionBarAPIOld.sendActionBar(player, text);

                timeLeft--;
            }
        }.runTaskTimer(BimmCore.getInstance(), 0L, 1L));
    }

    /**
     * Actionbar for 1.11 and newer
     */
    public static class ActionBarAPI {

        private static Class<?>       chatSerializer;
        private static Method         serializer;
        private static Class<?>       chatBaseComponent;
        private static Constructor<?> chatConstructor;
        private static Class<?>       titleAction;
        private static Object         actionEnum;

        static {
            chatBaseComponent = Reflection.getNMSClass("IChatBaseComponent");
            chatSerializer = Reflection.getNMSClass("IChatBaseComponent$ChatSerializer");
            titleAction = Reflection.getNMSClass("PacketPlayOutTitle$EnumTitleAction");

            try {
                serializer = chatSerializer.getMethod("a", String.class);

                Class<?> packetType = Reflection.getNMSClass("PacketPlayOutTitle");
                chatConstructor = packetType.getConstructor(titleAction, chatBaseComponent);

            } catch (NoSuchMethodException e) {
                e.printStackTrace();
            }

            actionEnum = titleAction.getEnumConstants()[2];
        }

        public static void sendActionBar(Player player, String text) {
            try {
                Object actionSerialized = serializer.invoke(null, "{\"text\":\"" + text + "\"}");
                Object actionPack = chatConstructor.newInstance(actionEnum, actionSerialized);
                Packets.sendPacket(player, actionPack);

            } catch (InvocationTargetException | IllegalAccessException | InstantiationException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Actionbar for 1.7, 1.8, 1.9, 1.10 (Minecraft versions are odd)
     */
    public static class ActionBarAPIOld {

        private static Class<?>       chatSerializer;
        private static Class<?>       chatBaseComponent;
        private static Method         serializer;
        private static Constructor<?> chatConstructor;

        static {
            chatBaseComponent = Reflection.getNMSClass("IChatBaseComponent");
            chatSerializer = Reflection.getNMSClass("IChatBaseComponent$ChatSerializer");

            try {
                serializer = chatSerializer.getMethod("a", String.class);

                Class<?> packetType = Reflection.getNMSClass("PacketPlayOutChat");
                chatConstructor = packetType.getConstructor(chatBaseComponent, byte.class);
            } catch (NoSuchMethodException e) {
                e.printStackTrace();
            }
        }

        /**
         * Send the title
         *
         * @param player
         * @param msg
         */
        private static void sendActionBar(Player player, String msg) {
            try {
                Object serialized = serializer.invoke(null, "{\"text\":\"" + msg + "\"}");

                Object packet = chatConstructor.newInstance(serialized, (byte) 2);
                Packets.sendPacket(player, packet);

            } catch (InstantiationException | IllegalAccessException | InvocationTargetException e) {
                e.printStackTrace();
            }

        }

    }
}
