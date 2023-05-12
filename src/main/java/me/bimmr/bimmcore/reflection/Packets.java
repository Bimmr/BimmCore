package me.bimmr.bimmcore.reflection;

import me.bimmr.bimmcore.BimmCore;
import net.minecraft.network.protocol.Packet;
import org.bukkit.entity.Player;

import java.lang.reflect.Method;

/**
 * The type Packets.
 */
public class Packets {
    private static Class<?> classPacket;
    static {

            classPacket = Reflection.getNMClass("network.protocol.Packet");
}

    /**
     * Send packet.
     *
     * @param player the player
     * @param packet the packet
     */
    public static void sendPacket(Player player, Object packet) {
        Object handle = Reflection.getHandle(player);
        Object playerConnection;
            playerConnection = Reflection.get(handle.getClass(), "b", handle);
        Method methodSend = Reflection.getMethod(playerConnection.getClass(), "sendPacket", classPacket);
        Reflection.invokeMethod(methodSend, playerConnection, new Object[]{packet});
    }

    /**
     * Send packet.
     *
     * @param player  the player
     * @param packets the packets
     */
    public static void sendPacket(Player player, Object... packets) {
        for(Object packet : packets)
            sendPacket(player, packet);
    }
}
