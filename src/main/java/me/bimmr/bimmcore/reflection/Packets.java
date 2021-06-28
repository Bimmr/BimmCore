package me.bimmr.bimmcore.reflection;

import me.bimmr.bimmcore.BimmCore;
import net.minecraft.network.protocol.Packet;
import org.bukkit.entity.Player;

import java.lang.reflect.Method;

/**
 * A Utilities class to handle packets
 */
public class Packets {
    private static Class<?> classPacket;
    static {

        if(BimmCore.supports(17))
            classPacket = Reflection.getNMClass("network.protocol.Packet");
        else
            classPacket = Reflection.getNMSClass("Packet");
}
    /**
     * Send a packet to a player
     *
     * @param player The Player
     * @param packet The Packet
     */
    public static void sendPacket(Player player, Object packet) {
        Object handle = Reflection.getHandle(player);
        Object playerConnection;
        if(BimmCore.supports(17))
            playerConnection = Reflection.get(handle.getClass(), "b", handle);
        else
            playerConnection = Reflection.get(handle.getClass(), "playerConnection", handle);
        Method methodSend = Reflection.getMethod(playerConnection.getClass(), "sendPacket", classPacket);
        Reflection.invokeMethod(methodSend, playerConnection, new Object[]{packet});
    }

    /**
     * Send packets to player
     *
     * Calls {@link #sendPacket(Player, Object)}
     * @param player The Player
     * @param packets The packets
     */
    public static void sendPacket(Player player, Object... packets) {
        for(Object packet : packets)
            sendPacket(player, packet);
    }
}
