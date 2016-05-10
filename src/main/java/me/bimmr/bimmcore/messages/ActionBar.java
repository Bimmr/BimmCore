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

    private static HashMap<String, BukkitTask> running = new HashMap<>();
    private String text;
    private int    time;

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
     * Stop showing an title
     *
     * @param player
     */
    private static void stop(Player player) {
        if (isRunning(player)) {
            running.get(player.getName()).cancel();
        }
    }

    /**
     * Check if a title is being sent to the player
     *
     * @param player
     * @return
     */
    private static boolean isRunning(Player player) {
        return running.containsKey(player.getName());
    }

    /**
     * Clear the player's title
     *
     * @param player
     */
    public static void clear(Player player) {
        ActionBarAPI.sendActionBar(player, " ");
        stop(player);
    }

    /**
     * Send the player an title
     *
     * @param player
     */
    public void send(final Player player) {
        if (isRunning(player)) stop(player);

        running.put(player.getName(), new BukkitRunnable() {
            int timeLeft = time;

            @Override
            public void run() {

                if (timeLeft <= 0)
                    stop(player);
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
