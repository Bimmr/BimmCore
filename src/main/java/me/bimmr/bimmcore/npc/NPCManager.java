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
import org.bukkit.event.entity.EntityTargetEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;

import java.util.ArrayList;

public class NPCManager implements Listener {

    private static ArrayList<NPCBase> npcBases = new ArrayList<>();
    private static NPCPlayerListener npcPlayerListener;

    public static void unregister(NPCBase npcBase) {
        npcBases.remove(npcBase);
    }

    public static void register(NPCBase npcBase) {
        if (getNPC(npcBase.getId()) == null)
            npcBases.add(npcBase);
    }

    public static ArrayList<NPCBase> getAllNPCs() {
        return npcBases;
    }

    public static NPCBase getNPC(int id) {
        for (NPCBase npcBasePlayer : getAllNPCs())
            if (npcBasePlayer.getId() == id)
                return npcBasePlayer;
        return null;
    }

    public static NPCBase createNPC(NPCBase.NPCType npcType, String name, Location location) {
        return npcType == NPCBase.NPCType.PLAYER ? new NPCPlayer(name, location) : new NPCMob(name, location);
    }

    public static NPCBase createNPC(String name, Location location, EntityType type) {
        return new NPCMob(name, location, type);
    }

    public static NPCBase createNPC(String name, Location location, String skin) {
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
        NPCBase npcBase = getNPC(id);
        if (npcBase != null) {
            if (e.getDamager() instanceof Player)
                npcBase.getNPCClickEvent().playerLeftClick((Player) e.getDamager());
            e.setCancelled(true);
        }
    }

    @EventHandler
    public void damageNPC(EntityDamageEvent e) {
        int id = e.getEntity().getEntityId();
        NPCBase npcBase = getNPC(id);
        if (npcBase != null)
            e.setCancelled(true);
    }

    @EventHandler
    public void npcOnFire(EntityCombustEvent e) {
        int id = e.getEntity().getEntityId();
        NPCBase npcBase = getNPC(id);
        if (npcBase != null)
            e.setCancelled(true);
    }

    @EventHandler
    public void npcTargeted(EntityTargetEvent e) {
        if (e.getTarget() != null) {
            int id = e.getTarget().getEntityId();
            NPCBase npcBase = getNPC(id);
            if (npcBase != null)
                e.setCancelled(true);
        }
    }

    @EventHandler
    public void playerInteract(PlayerInteractEntityEvent e) {
        int id = e.getRightClicked().getEntityId();
        NPCBase npcBase = getNPC(id);
        if (npcBase != null)
            npcBase.getNPCClickEvent().playerRightClick(e.getPlayer());
    }
}
