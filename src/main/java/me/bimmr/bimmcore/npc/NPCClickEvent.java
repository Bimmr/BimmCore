package me.bimmr.bimmcore.npc;

import org.bukkit.entity.Player;

/**
 * The type Npc click event.
 */
public abstract class NPCClickEvent {

    /**
     * Gets npc.
     *
     * @return the npc
     */
    public NPCBase getNPC() {
        return npcBase;
    }

    /**
     * Gets player.
     *
     * @return the player
     */
    public Player getPlayer() {
        return player;
    }

    private NPCBase npcBase;
    private Player player;

    /**
     * Instantiates a new Npc click event.
     */
    public NPCClickEvent(){
    }

    /**
     * Sets .
     *
     * @param npcBase the npc base
     */
    public void setup(NPCBase npcBase) {
        this.npcBase = npcBase;
    }

    /**
     * Player right click.
     *
     * @param player the player
     */
    public void playerRightClick(Player player){
        this.player = player;
        onRightClick();
    }

    /**
     * Player left click.
     *
     * @param player the player
     */
    public void playerLeftClick(Player player){
        this.player = player;
        onLeftClick();
    }

    /**
     * On right click.
     */
    public abstract void onRightClick();

    /**
     * On left click.
     */
    public abstract void onLeftClick();

}
