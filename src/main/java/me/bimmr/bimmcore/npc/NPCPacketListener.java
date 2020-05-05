package me.bimmr.bimmcore.npc;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.events.ListenerPriority;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.events.PacketEvent;
import com.comphenix.protocol.wrappers.EnumWrappers;
import io.netty.channel.Channel;
import me.bimmr.bimmcore.misc.Cooldown;
import me.bimmr.bimmcore.reflection.Reflection;
import me.bimmr.bimmcore.reflection.tinyprotocol.TinyProtocol;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import java.lang.reflect.Field;
import java.util.UUID;
import java.util.logging.Level;

/**
 * Original Authour: Jitse Boonstra
 * Modified for BimmCore NPC
 */
public class NPCPacketListener {

    // Classes:
    private final Class<?> packetPlayInUseEntityClazz = Reflection.getNMSClass("PacketPlayInUseEntity");

    // Fields:
    private final Field entityIdField = Reflection.getField(packetPlayInUseEntityClazz, "a");
    private final Field actionField = Reflection.getField(packetPlayInUseEntityClazz, "action");

    private Plugin plugin;
    private Cooldown<UUID> cooldown;
    private boolean usingProtocolLib;
    private boolean started = false;
    private TinyProtocol tinyProtocol;

    public void start(Plugin plugin) {
        if (!started) {
            started = true;
            this.plugin = plugin;
            this.cooldown = new Cooldown(1);

            if (Bukkit.getPluginManager().getPlugin("ProtocolLib") != null)
                usingProtocolLib = true;

            if (usingProtocolLib) {
                plugin.getLogger().log(Level.INFO, "Using ProtocolLib for NPCPacketListener");
                ProtocolLibrary.getProtocolManager().addPacketListener(
                        new PacketAdapter(plugin, ListenerPriority.NORMAL, PacketType.Play.Client.USE_ENTITY) {
                            @Override
                            public void onPacketReceiving(PacketEvent event) {
                                handleInteractPacket(event.getPlayer(), event.getPacket());
                                event.setCancelled(true);
                            }
                        });
            } else {
                plugin.getLogger().log(Level.INFO, "Using TinyProtocol for NPCPacketListener");
                this.tinyProtocol = new TinyProtocol(plugin) {

                    @Override
                    public Object onPacketInAsync(Player sender, Channel channel, Object packet) {
                        if (!packetPlayInUseEntityClazz.isInstance(packet))
                            return super.onPacketInAsync(sender, channel, packet);
                        return handleInteractPacket(sender, packet) ? super.onPacketInAsync(sender, channel, packet) : null;
                    }
                };
            }
        }
    }

    public void stop() {
        if (!usingProtocolLib)
            tinyProtocol.close();
    }

    private boolean handleInteractPacket(Player player, Object packet) {
        if (!cooldown.isCooledDown(player.getUniqueId()))
            return false;

        NPC npc = null;
        int entityID = -1;
        boolean isLeftClick = false;

        if (usingProtocolLib) {
            PacketContainer packetContainer = (PacketContainer)packet;
            entityID = packetContainer.getIntegers().read(0);
            isLeftClick= packetContainer.getEntityUseActions().getValues().get(0) == EnumWrappers.EntityUseAction.ATTACK;
        } else {
            entityID = (int) Reflection.get(entityIdField, packet);
            isLeftClick = Reflection.get(actionField, packet).toString().equals("ATTACK");
        }


        // Not using streams here is an intentional choice.
        // Packet listeners is one of the few places where it is important to write optimized code.
        // Lambdas (And the stream api) create a massive amount of objects, especially if it isn't a static lambda.
        // So, we're avoiding them here.
        // ~ Kneesnap, 9 / 20 / 2019.

        for (NPC testNPC : NPCManager.getAllNPCs()) {
            if (testNPC.isShown(player) && testNPC.getId() == entityID) {
                npc = testNPC;
                break;
            }
        }

        if (npc == null) {
            // Default player, not doing magic with the packet.
            return true;
        }


        cooldown.addToCooldown(player.getUniqueId());
        Bukkit.getScheduler().runTask(plugin, new TaskCallNpcClickEvent(player, isLeftClick, npc.getNpcClickEvent()));
        return false;
    }

    // This would be a non-static lambda, and its usage matters, so we'll make it a full class.
    private static final class TaskCallNpcClickEvent implements Runnable {
        private static Location playerLocation = new Location(null, 0, 0, 0);
        private Player player;
        private boolean leftClick;
        private NPCClickEvent eventToCall;

        TaskCallNpcClickEvent(Player player, boolean leftClick, NPCClickEvent eventToCall) {
            this.player = player;
            this.leftClick = leftClick;
            this.eventToCall = eventToCall;
        }

        @Override
        public void run() {
            if (!player.getWorld().equals(eventToCall.getNPC().getLocation().getWorld()))
                return; // If the NPC and player are not in the same world, abort!

            double distance = player.getLocation(playerLocation).distanceSquared(eventToCall.getNPC().getLocation());
            if (distance <= 64) // Only handle the interaction if the player is within interaction range. This way, hacked clients can't interact with NPCs that they shouldn't be able to interact with.
                if (leftClick)
                    eventToCall.playerLeftClick(player);
                else
                    eventToCall.playerRightClick(player);
        }
    }
}