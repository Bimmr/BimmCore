package me.bimmr.bimmcore.messages;

import me.bimmr.bimmcore.BimmCore;
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
public class ActionBar {

    private static HashMap<String, BukkitTask> tasks = new HashMap<>();
    private static HashMap<String, ActionBar>  bars  = new HashMap<>();
    private String      text;
    private int         time;
    private SecondEvent secondEvent;

    /**
     * Create an title defaulting to show for 2 seconds
     *
     * @param text
     */
    public ActionBar(String text) {
        this.text = text;
        this.time = 2;
    }

    /**
     * Create an title
     *
     * @param text
     * @param time
     */
    public ActionBar(String text, int time) {
        this.text = text;
        this.time = time;
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
     * A function that gets called every second that the BossBar is active for
     *
     * @param secondEvent
     */
    public void setSecondEvent(SecondEvent secondEvent) {
        this.secondEvent = secondEvent;
    }

    /**
     * Send the player an title
     *
     * @param player
     */
    public void send(final Player player) {
        clear(player);

        bars.put(player.getName(), this);
        tasks.put(player.getName(), new BukkitRunnable() {
            int timeLeft = time;

            @Override
            public void run() {
                if (secondEvent != null)
                    secondEvent.run();

                if (timeLeft <= 0)
                    clear(player);
                else
                    ActionBarAPI.sendActionBar(player, text);

                timeLeft--;
            }
        }.runTaskTimer(BimmCore.getInstance(), 0L, 20L));
    }

    public static class ActionBarAPI {

        private static Class<?>       chatSerializer;
        private static Class<?>       chatBaseComponent;
        private static Method         serializer;
        private static Constructor<?> chatConstructor;

        /**
         * Load everything needed
         */
        public static void setup() {
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
