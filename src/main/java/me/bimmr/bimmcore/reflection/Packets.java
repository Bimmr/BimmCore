package me.bimmr.bimmcore.reflection;

import org.bukkit.entity.Player;

import java.lang.reflect.InvocationTargetException;

/**
 * A Utilities class to handle packets
 */
public class Packets {

    /**
     * Send a packet to a player
     *
     * @param player
     * @param packet
     */
    public static void sendPacket(Player player, Object packet) {
        try {
            Object handle = Reflection.getHandle(player);
            Object playerConnection = handle.getClass().getField("playerConnection").get(handle);
            playerConnection.getClass().getMethod("sendPacket", new Class[]{Reflection.getNMSClass("Packet")}).invoke(playerConnection, new Object[]{packet});
        } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException | NoSuchFieldException e) {
            e.printStackTrace();
        }
    }
}
