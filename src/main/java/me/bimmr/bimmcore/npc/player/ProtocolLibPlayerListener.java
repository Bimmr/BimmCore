package me.bimmr.bimmcore.npc.player;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.events.ListenerPriority;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.events.PacketEvent;
import com.comphenix.protocol.wrappers.EnumWrappers;
import me.bimmr.bimmcore.BimmCore;
import me.bimmr.bimmcore.npc.NPCBase;
import me.bimmr.bimmcore.npc.NPCManager;
import org.bukkit.Bukkit;

/**
 * The type Protocol lib player listener.
 */
public class ProtocolLibPlayerListener implements NPCPlayerListener {

    @Override
    public void start() {
        ProtocolLibrary.getProtocolManager().addPacketListener(
                new PacketAdapter(BimmCore.getInstance(), ListenerPriority.NORMAL, PacketType.Play.Client.USE_ENTITY) {
                    @Override
                    public void onPacketReceiving(PacketEvent event) {
                        if (cooldown.isCooledDown(event.getPlayer().getUniqueId())) {
                            NPCPlayer npcPlayer = null;
                            int entityID = -1;
                            boolean isLeftClick = false;

                            PacketContainer packetContainer = (PacketContainer) event.getPacket();
                            entityID = packetContainer.getIntegers().read(0);
                            isLeftClick = packetContainer.getEntityUseActions().getValues().get(0) == EnumWrappers.EntityUseAction.ATTACK;

                            for (NPCBase npcBase : NPCManager.getAllNPCs()) {
                                if (npcBase.getNPCType() == NPCBase.NPCType.PLAYER && ((NPCPlayer) npcBase).isShown(event.getPlayer()) && npcBase.getId() == entityID) {
                                    npcPlayer = (NPCPlayer) npcBase;
                                    break;
                                }
                            }

                            if (npcPlayer != null) {
                                cooldown.addToCooldown(event.getPlayer().getUniqueId());
                                Bukkit.getScheduler().scheduleSyncDelayedTask(BimmCore.getInstance(), new TaskCallNpcClickEvent(event.getPlayer(), isLeftClick, npcPlayer.getNPCClickEvent()), 1L);
                            }
                        }
                    }
                });
    }
}
