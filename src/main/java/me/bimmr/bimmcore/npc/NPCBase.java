package me.bimmr.bimmcore.npc;

import me.bimmr.bimmcore.npc.mob.NPCMob;
import me.bimmr.bimmcore.npc.player.NPCPlayer;
import org.bukkit.Location;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;

/**
 * The type Npc base.
 */
public abstract class NPCBase {

    private String name;
    private Location location;
    private HashMap<ItemSlots, ItemStack> equipment = new HashMap<>();
    private NPCClickEvent npcClickEvent;
    private NPCType npcType;

    /**
     * Instantiates a new Npc base.
     *
     * @param npcType  the npc type
     * @param name     the name
     * @param location the location
     */
    public NPCBase(NPCType npcType, String name, Location location) {
        this.npcType = npcType;
        this.name = name;
        this.location = location;
    }

    /**
     * Gets npc type.
     *
     * @return the npc type
     */
    public NPCType getNPCType() {
        return npcType;
    }

    /**
     * Gets equipment.
     *
     * @return the equipment
     */
    public HashMap<ItemSlots, ItemStack> getEquipment() {
        return equipment;
    }

    /**
     * Gets npc click event.
     *
     * @return the npc click event
     */
    public NPCClickEvent getNPCClickEvent() {
        return npcClickEvent;
    }

    /**
     * Sets npc click event.
     *
     * @param npcClickEvent the npc click event
     */
    public void setNPCClickEvent(NPCClickEvent npcClickEvent) {
        this.npcClickEvent = npcClickEvent;
        this.npcClickEvent.setup(this);
    }

    /**
     * Gets name.
     *
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * Sets name.
     *
     * @param name the name
     */
    public void setName(String name) {
        this.name = name;
        renamed(name);
    }

    /**
     * Gets location.
     *
     * @return the location
     */
    public Location getLocation() {
        return location;
    }

    /**
     * Sets location.
     *
     * @param location the location
     */
    public void setLocation(Location location) {
        this.location = location;
        teleported(location);
    }

    /**
     * Equip.
     *
     * @param itemSlot the item slot
     * @param item     the item
     */
    public void equip(ItemSlots itemSlot, ItemStack item) {
        this.equipment.put(itemSlot, item);
        equipped(itemSlot, item);
    }

    /**
     * Renamed.
     *
     * @param name the name
     */
    protected abstract void renamed(String name);

    /**
     * Teleported.
     *
     * @param location the location
     */
    protected abstract void teleported(Location location);

    /**
     * Equipped.
     *
     * @param itemSlot  the item slot
     * @param itemStack the item stack
     */
    protected abstract void equipped(ItemSlots itemSlot, ItemStack itemStack);

    /**
     * Destroy.
     */
    public void destroy() {
        destroyed();
        NPCManager.unregister(this);
    }

    /**
     * Create.
     */
    public void create() {
        created();
        NPCManager.register(this);
    }

    /**
     * Is mob boolean.
     *
     * @return the boolean
     */
    public boolean isMob() {
        return npcType == NPCType.MOB;
    }

    /**
     * Is player boolean.
     *
     * @return the boolean
     */
    public boolean isPlayer() {
        return npcType == NPCType.PLAYER;
    }

    /**
     * Destroyed.
     */
    protected abstract void destroyed();

    /**
     * Created.
     */
    protected abstract void created();

    /**
     * Gets id.
     *
     * @return the id
     */
    public abstract int getId();

    /**
     * As player npc player.
     *
     * @return the npc player
     */
    public NPCPlayer asPlayer() {
        return (NPCPlayer) this;
    }

    /**
     * As mob npc mob.
     *
     * @return the npc mob
     */
    public NPCMob asMob() {
        return (NPCMob) this;
    }

    /**
     * The enum Item slots.
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

    /**
     * The enum Npc type.
     */
    public enum NPCType {
        /**
         * Player npc type.
         */
        PLAYER,
        /**
         * Mob npc type.
         */
        MOB}
}
