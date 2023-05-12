package me.bimmr.bimmcore.npc.player;

import me.bimmr.bimmcore.npc.NPCClickEvent;
import org.bukkit.Location;
import org.bukkit.entity.Player;

/**
 * The type Task call npc click event.
 */
public class TaskCallNpcClickEvent implements Runnable {
    private static Location playerLocation = new Location(null, 0, 0, 0);
    private Player player;
    private boolean leftClick;
    private NPCClickEvent eventToCall;

    /**
     * Instantiates a new Task call npc click event.
     *
     * @param player      the player
     * @param leftClick   the left click
     * @param eventToCall the event to call
     */
    TaskCallNpcClickEvent(Player player, boolean leftClick, NPCClickEvent eventToCall) {
        this.player = player;
        this.leftClick = leftClick;
        this.eventToCall = eventToCall;
    }

    @Override
    public void run() {
        if (!player.getWorld().equals(eventToCall.getNPC().getLocation().getWorld()))
            return;

        double distance = player.getLocation(playerLocation).distanceSquared(eventToCall.getNPC().getLocation());
        if (distance <= 64)
            if (leftClick)
                eventToCall.playerLeftClick(player);
            else
                eventToCall.playerRightClick(player);
    }
}