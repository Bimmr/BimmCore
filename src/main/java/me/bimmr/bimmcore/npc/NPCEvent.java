package me.bimmr.bimmcore.npc;

import org.bukkit.entity.Player;
import org.bukkit.event.Event;

@Deprecated
public class NPCEvent {
    private Event event;

    public Event getEvent() {
        return event;
    }

    public NPC getNpc() {
        return npc;
    }

    public Player getPlayer() {
        return player;
    }

    private NPC npc;
    private Player player;

    public NPCEvent(NPC npc, Player player, Event event) {
        this.npc = npc;
        this.player = player;
        this.event = event;
    }
}
