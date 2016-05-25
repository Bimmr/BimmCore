package me.bimmr.bimmcore.messages;

import me.bimmr.bimmcore.BimmCore;
import me.bimmr.bimmcore.events.timing.TimedEvent;
import me.bimmr.bimmcore.reflection.Packets;
import me.bimmr.bimmcore.reflection.Reflection;
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
public class ActionBar extends MessageDisplay {

    private static HashMap<String, BukkitTask> tasks = new HashMap<>();
    private static HashMap<String, ActionBar>  bars  = new HashMap<>();

    /**
     * Create an actionbar
     *
     * @param text
     */
    public ActionBar(String text) {
        this(text, 2, null);
    }

    /**
     * Create an actionbar
     *
     * @param text
     * @param time
     */
    public ActionBar(String text, int time) {
        this(text, time, null);
    }

    /**
     * Create an actionbar
     *
     * @param text
     * @param timedEvent
     */
    public ActionBar(String text, TimedEvent timedEvent) {
        this(text, 2, timedEvent);
    }

    /**
     * Create an actionbar
     *
     * @param text
     * @param time
     * @param timedEvent
     */
    public ActionBar(String text, int time, TimedEvent timedEvent) {
        this.text = text;
        this.time = time;
        setTimedEvent(timedEvent);
    }

    /**
     * Check if a title is being sent to the player
     *
     * @param player
     * @return
     */
    private static boolean isRunning(Player player) {
        return tasks.containsKey(player.getName());
    }

    /**
     * Clear the player's title
     *
     * @param player
     */
    public static void clear(Player player) {
        if (isRunning(player)) {
            ActionBarAPI.sendActionBar(player, " ");
            tasks.get(player.getName()).cancel();
            tasks.remove(player.getName());
            bars.remove(player.getName());
        }
    }

    /**
     * Get the actionbar that is being played for the player
     *
     * @param player
     * @return
     */
    public static ActionBar getPlayingActionBar(Player player) {
        if (isRunning(player))
            return bars.get(player.getName());
        else
            return null;
    }

    /**
     * Get the text
     *
     * @return
     */
    @Override
    public String getText() {
        return text;
    }

    /**
     * Set the text
     *
     * @param text
     */
    @Override
    public void setText(String text) {
        this.text = text;
    }

    /**
     * Get the time
     *
     * @return
     */
    @Override
    public int getTime() {
        return time;
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
     * A function that gets called every second that the BossBar is active for
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
     * Stop showing the actionbar
     *
     * @param player
     */
    @Override
    public void stop(Player player) {
        clear(player);
    }

    /**
     * Send the player an title
     *
     * @param player
     */
    @Override
    public void send(final Player player) {
        clear(player);
        bars.put(player.getName(), this);
        tasks.put(player.getName(), new BukkitRunnable() {
            int timeLeft = time * 20;

            @Override
            public void run() {
                if (timedEvent != null && timeLeft % timedEvent.getTicks() == 0)
                    timedEvent.run();

                if (timeLeft <= 0)
                    clear(player);

                else if (timeLeft % 20 == 0 || (timedEvent != null && timeLeft % timedEvent.getTicks() == 0))
                    ActionBarAPI.sendActionBar(player, text);

                timeLeft--;
            }
        }.runTaskTimer(BimmCore.getInstance(), 0L, 1L));
    }

    public static class ActionBarAPI {

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
