package me.bimmr.bimmcore.npc;


import me.bimmr.bimmcore.npc.mob.NPCMob;
import me.bimmr.bimmcore.npc.player.NPCPlayer;
import me.bimmr.bimmcore.npc.player.NPCPlayerListener;
import me.bimmr.bimmcore.npc.player.ProtocolLibPlayerListener;
import me.bimmr.bimmcore.npc.player.TinyPlayerListener;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityCombustEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;

import java.util.ArrayList;

public class NPCManager implements Listener {

    private static ArrayList<NPC> npcs = new ArrayList<>();
    private static NPCPlayerListener npcPlayerListener;

    public static void unregister(NPC npc) {
        npcs.remove(npc);
    }

    public static void register(NPC npc) {
        if (getNPC(npc.getId()) == null)
            npcs.add(npc);
    }

    public static ArrayList<NPC> getAllNPCs() {
        return npcs;
    }

    public static NPC getNPC(int id) {
        for (NPC npcPlayer : getAllNPCs())
            if (npcPlayer.getId() == id)
                return npcPlayer;
        return null;
    }

    public static NPC createNPC(NPC.NPCType npcType, String name, Location location) {
        return npcType == NPC.NPCType.PLAYER ? new NPCPlayer(name, location) : new NPCMob(name, location);
    }

    public static NPC createNPC(String name, Location location, EntityType type) {
        return new NPCMob(name, location, type);
    }

    public static NPC createNPC(String name, Location location, String skin) {
        return new NPCPlayer(name, location, skin);
    }

    public NPCPlayerListener getNPCPlayerListener() {
        if (npcPlayerListener == null)
            if (Bukkit.getPluginManager().getPlugin("ProtocolLib") != null)
                npcPlayerListener = new ProtocolLibPlayerListener();
            else npcPlayerListener = new TinyPlayerListener();

        return npcPlayerListener;
    }

    @EventHandler
    public void playerDamageNPC(EntityDamageByEntityEvent e) {
        int id = e.getEntity().getEntityId();
        NPC npc = getNPC(id);
        if (npc != null) {
            if (e.getDamager() instanceof Player)
                npc.getNPCClickEvent().playerLeftClick((Player) e.getDamager());
            e.setCancelled(true);
        }
    }

    @EventHandler
    public void damageNPC(EntityDamageEvent e) {
        int id = e.getEntity().getEntityId();
        NPC npc = getNPC(id);
        if (npc != null)
            e.setCancelled(true);
    }

    @EventHandler
    public void npcOnFire(EntityCombustEvent e) {
        int id = e.getEntity().getEntityId();
        NPC npc = getNPC(id);
        if (npc != null)
            e.setCancelled(true);
    }

    @EventHandler
    public void playerInteract(PlayerInteractEntityEvent e) {
        int id = e.getRightClicked().getEntityId();
        NPC npc = getNPC(id);
        if (npc != null)
            npc.getNPCClickEvent().playerRightClick(e.getPlayer());
    }
}
