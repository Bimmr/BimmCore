package me.bimmr.bimmcore.npc;

import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import me.bimmr.bimmcore.BimmCore;
import me.bimmr.bimmcore.hologram.Viewer;
import me.bimmr.bimmcore.reflection.Packets;
import me.bimmr.bimmcore.reflection.Reflection;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.inventory.ItemStack;
import org.yaml.snakeyaml.external.biz.base64Coder.Base64Coder;

import java.lang.reflect.Array;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.*;

@Deprecated
public class NPC {

    private GameProfile gameProfile;
    private String name;
    private Location location;
    private HashMap<ItemSlots, ItemStack> equipment = new HashMap<>();

    private Object entityPlayer;
    private Viewer viewer;

    private NPCClickEvent npcClickEvent;

    /**
     * Create a NPC
     *
     * @param name     The Name
     * @param location The Location
     */
    public NPC(String name, Location location) {
        this.name = name;
        this.location = location;
        this.gameProfile = new GameProfile(UUID.randomUUID(), name);

        create();
        this.viewer = new Viewer() {

            public void update(Player p) {
                NPC.NPCAPI.destroy(NPC.this, p);
                NPC.NPCAPI.show(NPC.this.entityPlayer, p);
                NPC.NPCAPI.setRotation(NPC.this.entityPlayer, p, NPC.this.location.getYaw());
                for (Map.Entry<NPC.ItemSlots, ItemStack> e : NPC.this.getEquipment().entrySet())
                    NPC.NPCAPI.equip(NPC.this.entityPlayer, p, e.getKey(), e.getValue());
            }

            public void onAddToView(Player p) {
            }

            public void onRemoveFromView(Player p) {
                destroy();
            }
        };
    }

    /**
     * Set the NPCClickEvent
     *
     * @param npcClickEvent The ClickEvent
     */
    public void setNPCClickEvent(NPCClickEvent npcClickEvent) {
        if (this.npcClickEvent != null && npcClickEvent == null)
            HandlerList.unregisterAll(this.npcClickEvent);

        this.npcClickEvent = npcClickEvent;
        if (this.npcClickEvent != null)
            this.npcClickEvent.setup(this);
    }

    /**
     * Remove The NPCClickEvent
     * Calls {@link #setNPCClickEvent(NPCClickEvent)}
     */
    public void removeNPCClickEvent() {
        setNPCClickEvent(null);
    }

    /**
     * @return The Property from the GameProfile
     */
    public Property getSkin() {
        if (this.gameProfile.getProperties().isEmpty())
            return null;
        return (Property) this.gameProfile.getProperties().get("textures").toArray()[0];
    }

    /**
     * @return Get a HashMap of the NPC's Equipment
     */
    public HashMap<ItemSlots, ItemStack> getEquipment() {
        return this.equipment;
    }

    /**
     * Show the NPC to the player
     *
     * @param player The Player
     */
    public void show(Player player) {
        this.viewer.addPlayer(player.getName());
        this.viewer.update(player);
    }

    public void setName(String name) {
        destroy();
        this.name = name;

        Property skin = this.getSkin();
        this.gameProfile = new GameProfile(this.gameProfile.getId(), this.name);
        if (skin != null)
            this.gameProfile.getProperties().put("textures", new Property("textures", skin.getValue(), skin.getSignature()));

        create();
    }

    //TODO: Find out why this doesn't work
    public void setSkin(String value) {
        destroy();
        System.out.println(value);
        if (value.startsWith("http://") || value.startsWith("https://")) {

            System.out.println("Creating URL Skin: " + value);
            this.gameProfile.getProperties().put("textures", new Property("textures", Base64Coder.encodeString("{textures:{SKIN:{url:\"" + value + "\"}}}")));
        } else {
            System.out.println("Creating Temp Player for skin: " + value);
            GameProfile temp = new GameProfile(UUID.randomUUID(), value);
            Property skin = (Property) temp.getProperties().get("textures").toArray()[0];
            if (skin != null)
                this.gameProfile.getProperties().put("textures", new Property("textures", skin.getValue(), skin.getSignature()));
        }

        create();
    }

    private void create() {
        System.out.println("Creating New NPC");
        this.entityPlayer = NPCAPI.create(this);
        System.out.println("New Entity: " + this.entityPlayer);
        NPCAPI.setLocation(this.entityPlayer, this.location);

        if (this.viewer != null)
            this.viewer.update();

        if (this.npcClickEvent != null) {
            HandlerList.unregisterAll(this.npcClickEvent);
            this.npcClickEvent.setup(this);
        }
    }

    /**
     * Hide the NPC from the player
     *
     * @param player The Player
     */
    public void hide(Player player) {
        this.viewer.removePlayer(player.getName());
    }

    /**
     * Destroy the NPC for all players
     */
    private void destroy() {
        System.out.println("Destroying");
        for (String p : this.viewer.getPlayers())
            NPCAPI.destroy(this, Bukkit.getPlayer(p));
    }

    /**
     * Teleport the NPC
     *
     * @param location The Location
     */
    public void setLocation(Location location) {
        this.location = location;
        NPCAPI.setLocation(this.entityPlayer, location);
        if (this.viewer != null)
            this.viewer.update();
    }

    /**
     * Set NPC's Equipment
     *
     * @param equipment The Equipment HashMap
     */
    public void setEquipment(HashMap<ItemSlots, ItemStack> equipment) {
        this.equipment = equipment;
    }

    /**
     * Equip the NPC
     *
     * @param slot      The Slot
     * @param itemStack The ItemStack
     */
    public void equip(ItemSlots slot, ItemStack itemStack) {
        this.equipment.put(slot, itemStack);
        this.viewer.update();
    }

    /**
     * Get the NPC's GameProfile
     *
     * @return
     */
    public GameProfile getGameProfile() {
        return this.gameProfile;
    }

    /**
     * Set the NPC's GameProfile
     *
     * @param gameProfile The GameProfile
     */
    public void setGameProfile(GameProfile gameProfile) {
        this.gameProfile = gameProfile;
    }

    /**
     * @return Get The NPC's Id
     */
    public int getId() {
        return Reflection.getEntityID(entityPlayer.getClass(), this.entityPlayer);
    }

    /**
     * @return Get the NPC's Location
     */
    public Location getLocation() {
        return this.location;
    }


    /**
     * @return Get the NPC's Name
     */
    public String getName() {
        return this.name;
    }


    /**
     * ItemSlots NPC ENUM
     */
    public enum ItemSlots {
        MAINHAND(0),
        OFFHAND(1),
        FEET(2),
        LEGS(3),
        CHEST(4),
        HEAD(5);

        private int id;

        ItemSlots(int id) {
            this.id = id;
        }
    }

    private static class NPCAPI {

        private static Class<?> craftServerClass = Reflection.getCraftClass("CraftServer");
        private static Class<?> craftWorldClass = Reflection.getCraftClass("CraftWorld");
        private static Class<?> playerInteractManagerClass = Reflection.getNMSClass("PlayerInteractManager");
        private static Class<?> worldServerClass = Reflection.getNMSClass("WorldServer");
        private static Class<?> minecraftServerClass = Reflection.getNMSClass("MinecraftServer");
        private static Class<?> entityPlayerClass = Reflection.getNMSClass("EntityPlayer");
        private static Class<?> entityClass = Reflection.getNMSClass("Entity");
        private static Class<?> enumPlayerInfoActionClass = Reflection.getNMSClass("PacketPlayOutPlayerInfo$EnumPlayerInfoAction");
        private static Class<?> entityHumanClass = Reflection.getNMSClass("EntityHuman");
        private static Class<?> packetPlayOutNamedEntitySpawnClass = Reflection.getNMSClass("PacketPlayOutNamedEntitySpawn");
        private static Class<?> packetPlayOutEntityEquipmentClass = Reflection.getNMSClass("PacketPlayOutEntityEquipment");
        private static Class<?> packetPlayOutEntityDestroyClass = Reflection.getNMSClass("PacketPlayOutEntityDestroy");
        private static Class<?> packetPlayOutEntityHeadRotationClass = Reflection.getNMSClass("PacketPlayOutEntityHeadRotation");
        private static Class<?> craftItemStackClass = Reflection.getCraftClass("inventory.CraftItemStack");
        private static Class<?> itemStackClass = Reflection.getNMSClass("ItemStack");
        private static Class<?> enumItemSlotClass = Reflection.getNMSClass("EnumItemSlot");
        private static Class<?> packetPlayOutPlayerInfoClass = Reflection.getNMSClass("PacketPlayOutPlayerInfo");

        private static Constructor<?> playerInteractManagerConstructor = Reflection.getConstructor(playerInteractManagerClass, worldServerClass);
        private static Constructor<?> entityConstructor = Reflection.getConstructor(entityPlayerClass, minecraftServerClass, worldServerClass, GameProfile.class, playerInteractManagerClass);
        private static Constructor<?> packetPlayOutEntityHeadRotationConstructor = Reflection.getConstructor(packetPlayOutEntityHeadRotationClass, entityClass, byte.class);
        private static Constructor<?> packetPlayOutNamedEntitySpawnConstructor = Reflection.getConstructor(packetPlayOutNamedEntitySpawnClass, entityHumanClass);
        private static Constructor<?> packetPlayOutEntityEquipmentConstructor = Reflection.getConstructor(packetPlayOutEntityEquipmentClass, int.class, enumItemSlotClass, itemStackClass);
        private static Constructor<?> playOutEntityDestroyConstructor = Reflection.getConstructor(packetPlayOutEntityDestroyClass, int[].class);

        private static Method asNMSCopy = Reflection.getMethod(craftItemStackClass, "asNMSCopy", ItemStack.class);
        private static Method setLocation = Reflection.getMethod(entityPlayerClass, "setLocation", double.class, double.class, double.class, float.class, float.class);

        private static Object itemSlotEnumMainHand;
        private static Object itemSlotEnumOffHand;
        private static Object itemSlotEnumFeet;
        private static Object itemSlotEnumLegs;
        private static Object itemSlotEnumChest;
        private static Object itemSlotEnumHead;

        private static Object playerInfoActionEnumAdd;
        private static Object playerInfoActionEnumRemove;

        static {
            itemSlotEnumMainHand = enumItemSlotClass.getEnumConstants()[0];
            itemSlotEnumOffHand = enumItemSlotClass.getEnumConstants()[1];
            itemSlotEnumFeet = enumItemSlotClass.getEnumConstants()[2];
            itemSlotEnumLegs = enumItemSlotClass.getEnumConstants()[3];
            itemSlotEnumChest = enumItemSlotClass.getEnumConstants()[4];
            itemSlotEnumHead = enumItemSlotClass.getEnumConstants()[5];
            playerInfoActionEnumAdd = enumPlayerInfoActionClass.getEnumConstants()[0];
            playerInfoActionEnumRemove = enumPlayerInfoActionClass.getEnumConstants()[4];
        }

        /**
         * Set the Entity's Yaw Rotation then send to player
         *
         * @param entity The Entity
         * @param player The Player
         * @param yaw    The Yaw
         */
        public static void setRotation(Object entity, Player player, float yaw) {
            Object packetPlayOutEntityHeadRotation = Reflection.newInstance(packetPlayOutEntityHeadRotationConstructor, entity, (byte) ((yaw * 256.0F) / 360.0F));
            Packets.sendPacket(player, packetPlayOutEntityHeadRotation);
        }

        /**
         * Set Entity's Location
         *
         * @param entity   The Entity
         * @param location The Location
         */
        public static void setLocation(Object entity, Location location) {
            Reflection.invokeMethod(setLocation, entity, location.getX(), location.getY(), location.getZ(), location.getYaw(), location.getPitch());
        }

        /**
         * Show the Entity to the player
         *
         * @param entity The Entity
         * @param player The Player
         */
        public static void show(Object entity, Player player) {
            Object[] entities = (Object[]) Array.newInstance(entityPlayerClass, 1);
            entities[0] = entity;
            Constructor<?> packetPlayOutPlayerInfoConstructor = Reflection.getConstructor(packetPlayOutPlayerInfoClass, enumPlayerInfoActionClass, entities.getClass());
            Object packetPlayOutPlayerInfoAdd = Reflection.newInstance(packetPlayOutPlayerInfoConstructor, playerInfoActionEnumAdd, entities);
            Object packetPlayOutNamedEntitySpawn = Reflection.newInstance(packetPlayOutNamedEntitySpawnConstructor, entity);
            Object packetPlayOutPlayerInfoRemove = Reflection.newInstance(packetPlayOutPlayerInfoConstructor, playerInfoActionEnumRemove, entities);
            Packets.sendPacket(player, packetPlayOutPlayerInfoAdd, packetPlayOutNamedEntitySpawn);

            Bukkit.getScheduler().runTaskLater(BimmCore.getInstance(), () ->
                    Packets.sendPacket(player, packetPlayOutPlayerInfoRemove), 200);
        }

        /**
         * @param itemStack The ItemStack
         * @return Get the Itemstack as a CraftItemStack
         */
        private static Object getCraftItemStack(ItemStack itemStack) {
            return Reflection.invokeMethod(asNMSCopy, craftItemStackClass, itemStack);
        }

        /**
         * Equip the Entity for the Player
         *
         * @param entity The Entity
         * @param player The Player
         * @param slot   The Slot
         * @param item   The Item
         */
        public static void equip(Object entity, Player player, NPC.ItemSlots slot, ItemStack item) {
            Object oSlot = itemSlotEnumMainHand;
            switch (slot) {
                case OFFHAND:
                    oSlot = itemSlotEnumOffHand;
                    break;
                case FEET:
                    oSlot = itemSlotEnumFeet;
                    break;
                case LEGS:
                    oSlot = itemSlotEnumLegs;
                    break;
                case CHEST:
                    oSlot = itemSlotEnumChest;
                    break;
                case HEAD:
                    oSlot = itemSlotEnumHead;
                    break;
            }
            Object craftItem = getCraftItemStack(item);
            int id = Reflection.getEntityID(entityHumanClass, entity);
            Object packetPlayOutEntityEquipment = Reflection.newInstance(packetPlayOutEntityEquipmentConstructor, id, oSlot, craftItem);
            Packets.sendPacket(player, packetPlayOutEntityEquipment);
        }

        /**
         * @param npc The NPC
         * @return Get the Created Entity
         */
        public static Object create(NPC npc) {
            Object craftWorld = craftWorldClass.cast(npc.getLocation().getWorld());
            Object worldHandle = Reflection.getHandle(craftWorld);
            Object craftServer = craftServerClass.cast(Bukkit.getServer());
            Object minecraftServer = Reflection.invokeMethod(craftServerClass, "getServer", craftServer);
            Object playerInteractManager = Reflection.newInstance(playerInteractManagerConstructor, worldHandle);
            Object entityPlayer = Reflection.newInstance(entityConstructor, minecraftServer, worldHandle, npc.getGameProfile(), playerInteractManager);
            return entityPlayer;
        }

        /**
         * Destroy the NPC for the Player
         *
         * @param npc    The NPC
         * @param player The Player
         */
        public static void destroy(NPC npc, Player player) {
            Object[] entities = (Object[]) Array.newInstance(entityPlayerClass, 1);
            entities[0] = npc.entityPlayer;
            Constructor<?> packetPlayOutPlayerInfoConstructor = Reflection.getConstructor(packetPlayOutPlayerInfoClass, enumPlayerInfoActionClass, entities.getClass());
            Object packetPlayOutPlayerInfoRemove = Reflection.newInstance(packetPlayOutPlayerInfoConstructor, playerInfoActionEnumRemove, entities);
            Object playOutEntityDestroyPacket = Reflection.newInstance(playOutEntityDestroyConstructor, new int[]{npc.getId()});
            Packets.sendPacket(player, packetPlayOutPlayerInfoRemove, playOutEntityDestroyPacket);
        }
    }
}
