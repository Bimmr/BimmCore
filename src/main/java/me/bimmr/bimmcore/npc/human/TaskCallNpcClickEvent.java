package me.bimmr.bimmcore.npc.human;

import me.bimmr.bimmcore.npc.NPCClickEvent;
import org.bukkit.Location;
import org.bukkit.entity.Player;

public class TaskCallNpcClickEvent implements Runnable {
    private static Location playerLocation = new Location(null, 0, 0, 0);
    private Player player;
    private boolean leftClick;
    private NPCClickEvent eventToCall;

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