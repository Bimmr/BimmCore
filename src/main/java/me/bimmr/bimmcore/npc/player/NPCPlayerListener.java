package me.bimmr.bimmcore.npc.player;

import me.bimmr.bimmcore.misc.Cooldown;

import java.util.UUID;

/**
 * The interface Npc player listener.
 */
public interface NPCPlayerListener {
    /**
     * The constant cooldown.
     */
    Cooldown<UUID> cooldown = new Cooldown<>(250);

    /**
     * Start.
     */
    void start();

}
