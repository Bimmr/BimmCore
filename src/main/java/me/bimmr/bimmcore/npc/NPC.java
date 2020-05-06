package me.bimmr.bimmcore.npc;

import org.bukkit.Location;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;

public abstract class NPC {

    private String name;
    private Location location;
    private HashMap<ItemSlots, ItemStack> equipment = new HashMap<>();
    private NPCClickEvent npcClickEvent;
    private NPCType npcType;

    public NPC(NPCType npcType, String name, Location location) {

        this.npcType = npcType;
        this.name = name;
        this.location = location;

        NPCManager.register(this);
    }

    public NPCType getNPCType() {
        return npcType;
    }

    public HashMap<ItemSlots, ItemStack> getEquipment() {
        return equipment;
    }

    public NPCClickEvent getNPCClickEvent() {
        return npcClickEvent;
    }

    public void setNPCClickEvent(NPCClickEvent npcClickEvent) {
        this.npcClickEvent = npcClickEvent;
        this.npcClickEvent.setup(this);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
        renamed(name);
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
        teleported(location);
    }

    public void setEquipment(ItemSlots itemSlot, ItemStack item) {
        this.equipment.put(itemSlot, item);
    }

    public abstract void renamed(String name);

    public abstract void teleported(Location location);

    public abstract void equipped(ItemSlots itemSlot, ItemStack itemStack);

    public void destroy() {
        NPCManager.register(this);
        destroyed();
    }

    public abstract void destroyed();

    public abstract int getId();

    /**
     * ItemSlots NPC ENUM
     */
    public enum ItemSlots {
        /**
         * Mainhand item slots.
         */
        MAINHAND(0),
        /**
         * Offhand item slots.
         */
        OFFHAND(1),
        /**
         * Feet item slots.
         */
        FEET(2),
        /**
         * Legs item slots.
         */
        LEGS(3),
        /**
         * Chest item slots.
         */
        CHEST(4),
        /**
         * Head item slots.
         */
        HEAD(5);

        private int id;

        ItemSlots(int id) {
            this.id = id;
        }
    }

    public enum NPCType {PLAYER, MOB}
}
