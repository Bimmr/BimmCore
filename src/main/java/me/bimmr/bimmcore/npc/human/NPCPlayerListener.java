package me.bimmr.bimmcore.npc.human;

import me.bimmr.bimmcore.misc.Cooldown;

import java.util.UUID;

public interface NPCPlayerListener {
    Cooldown<UUID> cooldown = new Cooldown<>(100);

    void start();

}
