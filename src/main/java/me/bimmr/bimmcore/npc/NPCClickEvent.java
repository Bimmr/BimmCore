package me.bimmr.bimmcore.npc;

import org.bukkit.entity.Player;

public abstract class NPCClickEvent {

    public NPCBase getNPC() {
        return npcBase;
    }

    public Player getPlayer() {
        return player;
    }

    private NPCBase npcBase;
    private Player player;

    public NPCClickEvent(){
    }

    public void setup(NPCBase npcBase) {
        this.npcBase = npcBase;
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
