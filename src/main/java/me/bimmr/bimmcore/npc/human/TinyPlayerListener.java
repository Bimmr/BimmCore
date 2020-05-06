package me.bimmr.bimmcore.npc.human;

import io.netty.channel.Channel;
import me.bimmr.bimmcore.BimmCore;
import me.bimmr.bimmcore.npc.NPC;
import me.bimmr.bimmcore.npc.NPCManager;
import me.bimmr.bimmcore.reflection.Reflection;
import me.bimmr.bimmcore.reflection.tinyprotocol.TinyProtocol;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.lang.reflect.Field;

public class TinyPlayerListener implements NPCPlayerListener {
    // Classes:
    private final Class<?> packetPlayInUseEntityClazz = Reflection.getNMSClass("PacketPlayInUseEntity");
    // Fields:
    private final Field entityIdField = Reflection.getField(packetPlayInUseEntityClazz, "a");
    private final Field actionField = Reflection.getField(packetPlayInUseEntityClazz, "action");
    private TinyProtocol tinyProtocol;

    @Override
    public void start() {
        this.tinyProtocol = new TinyProtocol(BimmCore.getInstance()) {

            @Override
            public Object onPacketInAsync(Player player, Channel channel, Object packet) {
                if (!packetPlayInUseEntityClazz.isInstance(packet))
                    return super.onPacketInAsync(player, channel, packet);
                else {
                    if (cooldown.isCooledDown(player.getUniqueId())) {
                        NPCPlayer npcPlayer = null;
                        int entityID = (int) Reflection.get(entityIdField, packet);
                        boolean isLeftClick = Reflection.get(actionField, packet).toString().equals("ATTACK");

                        for (NPC npc : NPCManager.getAllNPCs()) {
                            if (npc.getNPCType() == NPC.NPCType.PLAYER && ((NPCPlayer) npc).isShown(player) && npc.getId() == entityID) {
                                npcPlayer = (NPCPlayer) npc;
                                break;
                            }
                        }

                        if (npcPlayer != null) {
                            cooldown.addToCooldown(player.getUniqueId());
                            Bukkit.getScheduler().scheduleSyncDelayedTask(BimmCore.getInstance(), new TaskCallNpcClickEvent(player, isLeftClick, npcPlayer.getNPCClickEvent()), 1L);
                            return null;
                        }

                    }
                    return super.onPacketInAsync(player, channel, packet);
                }
            }
        };

    }

}
