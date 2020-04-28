package me.bimmr.bimmcore.npc;

import org.bukkit.entity.Player;
import org.bukkit.event.Event;

public abstract class NPCClickEvent {

    public NPC getNPC() {
        return npc;
    }

    public Player getPlayer() {
        return player;
    }

    private NPC npc;
    private Player player;

    public NPCClickEvent(){
    }

    public void setup(NPC npc) {
        this.npc = npc;
    }

    public void playerRightClick(Player player){
        this.player = player;
        onRightClick();
    }
    public void playerLeftClick(Player player){
        this.player = player;
        onLeftClick();
    }
    /**
     * Abstract RightClickEvent
     *
     */
    public abstract void onRightClick();

    /**
     * On left click.
     *
     */
    public abstract void onLeftClick();

}
