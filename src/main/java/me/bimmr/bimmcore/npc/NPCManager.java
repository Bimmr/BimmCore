package me.bimmr.bimmcore.npc;


import me.bimmr.bimmcore.BimmCore;

import java.util.ArrayList;

public class NPCManager {

    private static ArrayList<NPC> npcs = new ArrayList<>();
    private static NPCPacketListener npcPacketListener;

    public static void unregister(NPC npc) {
        npcs.remove(npc);
    }

    public static void register(NPC npc) {
        npcs.add(npc);
    }

    public static ArrayList<NPC> getAllNPCs() {
        return npcs;
    }

    public static NPC getNPC(int id) {
        for (NPC npc : getAllNPCs())
            if (npc.getId() == id)
                return npc;
        return null;
    }

    public static NPCPacketListener getNPCPacketListener() {
        if (npcPacketListener == null)
            npcPacketListener = new NPCPacketListener();

        return npcPacketListener;
    }
}
