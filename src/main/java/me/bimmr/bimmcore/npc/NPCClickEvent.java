package me.bimmr.bimmcore.npc;

import me.bimmr.bimmcore.BimmCore;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityInteractEvent;
import org.bukkit.plugin.Plugin;

public abstract class NPCClickEvent implements Listener {
    private NPC npc;

    public abstract void onClick(EntityInteractEvent paramEntityInteractEvent);

    public void setup(NPC npc) {
        this.npc = npc;
        Bukkit.getPluginManager().registerEvents(this, (Plugin)BimmCore.getInstance());
    }

    @EventHandler
    public void onClickEvent(EntityInteractEvent eie) {
        if (eie.getEntity().getEntityId() == this.npc.getEntityID())
            onClick(eie);
    }

    @EventHandler
    public void onHitEvent(EntityDamageByEntityEvent edee) {
        if (edee.getEntity().getEntityId() == this.npc.getEntityID());
    }
}
