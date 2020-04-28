package me.bimmr.bimmcore.npc;

import me.bimmr.bimmcore.BimmCore;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;

/**
 * The type Npc click event.
 */
@Deprecated
public abstract class NPCClickEvent implements Listener {
    private NPC npc;

    /**
     * Abstract RightClickEvent
     *
     * @param npcEvent the npc event
     */
    public abstract void onRightClick(NPCEvent npcEvent);

    /**
     * On left click.
     *
     * @param npcEvent the npc event
     */
    public abstract void onLeftClick(NPCEvent npcEvent);

    /**
     * Sets .
     *
     * @param npc the npc
     */
    public void setup(NPC npc) {
        this.npc = npc;
        Bukkit.getPluginManager().registerEvents(this, BimmCore.getInstance());
        System.out.println("Setting up");
    }

    /**
     * On click event.
     *
     * @param event the event
     */
    @EventHandler
    public void onClickEvent(PlayerInteractEntityEvent event) {
        System.out.println(event.getRightClicked().getEntityId());
        System.out.println(this.npc.getId());
        if (event.getRightClicked().getEntityId() == this.npc.getId())
            onRightClick(new NPCEvent(this.npc, event.getPlayer(), event));
    }

    /**
     * On hit event.
     *
     * @param event the event
     */
    @EventHandler
    public void onHitEvent(EntityDamageByEntityEvent event) {
        if (event.getEntity().getEntityId() == this.npc.getId() && event.getDamager() instanceof Player)
            onLeftClick(new NPCEvent(this.npc, (Player) event.getDamager(), event));
    }
}
