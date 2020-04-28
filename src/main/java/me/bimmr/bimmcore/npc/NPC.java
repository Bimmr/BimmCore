package me.bimmr.bimmcore.npc;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import me.bimmr.bimmcore.BimmCore;
import me.bimmr.bimmcore.reflection.Viewer;
import me.bimmr.bimmcore.reflection.Packets;
import me.bimmr.bimmcore.reflection.Reflection;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Array;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * The type Npc.
 */
public class NPC {

    private static HashMap<Object, String[]> skins = new HashMap<>();

    private GameProfile gameProfile;
    private String name;
    private Location location;
    private HashMap<ItemSlots, ItemStack> equipment = new HashMap<>();

    private Object entityPlayer;
    private int id;
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
        setSkin(name);
        this.viewer = new Viewer() {

            public void update(Player p) {
                NPC.NPCAPI.destroy(NPC.this, p);
                NPC.NPCAPI.show(NPC.this, p);
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
        this.npcClickEvent = npcClickEvent;
        if (this.npcClickEvent != null)
            this.npcClickEvent.setup(this);
    }

    /**
     * Gets skin.
     *
     * @return The Property from the GameProfile
     */
    public Property getSkin() {
        if (this.gameProfile.getProperties().isEmpty())
            return null;
        return (Property) this.gameProfile.getProperties().get("textures").toArray()[0];
    }

    /**
     * Gets equipment.
     *
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

    /**
     * Sets name.
     *
     * @param name the name
     */
    public void setName(String name) {
        this.name = name;

        Property skin = this.getSkin();
        this.gameProfile = new GameProfile(this.gameProfile.getId(), this.name);
        if (skin != null)
            this.gameProfile.getProperties().put("textures", new Property("textures", skin.getValue(), skin.getSignature()));

    }

    public void refresh() {
        destroy();
        create();
    }

    /**
     * Sets skin.
     *
     * @param nameOrUUID The player's name or
     */
    public void setSkin(String nameOrUUID) {
        if (skins.containsKey(nameOrUUID)) {
            this.gameProfile.getProperties().removeAll("textures");
            this.gameProfile.getProperties().put("textures", new Property("textures", skins.get(nameOrUUID)[0], skins.get(nameOrUUID)[1]));
        } else {
            try {
                String value = nameOrUUID;
                if (!value.matches("[0-9a-fA-F]{8}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{12}")) {
                    URL url = new URL("https://api.mojang.com/users/profiles/minecraft/" + value);
                    InputStreamReader reader = new InputStreamReader(url.openStream());
                    value = new JsonParser().parse(reader).getAsJsonObject().get("id").getAsString();
                    reader.close();
                }

                URL url2 = new URL("https://sessionserver.mojang.com/session/minecraft/profile/" + value
                        + "?unsigned=false");
                InputStreamReader reader2 = new InputStreamReader(url2.openStream());
                JsonObject prop = new JsonParser().parse(reader2).getAsJsonObject().get("properties").getAsJsonArray().get(0).getAsJsonObject();
                reader2.close();
                String tex = prop.get("value").getAsString();
                String sig = prop.get("signature").getAsString();
                this.gameProfile.getProperties().removeAll("textures");
                this.gameProfile.getProperties().put("textures", new Property("textures", tex, sig));
                skins.put(nameOrUUID, new String[]{tex, sig});
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void setSkin(String texture, String signature) {
        this.gameProfile.getProperties().removeAll("textures");
        this.gameProfile.getProperties().put("textures", new Property("textures", texture, signature));
    }


    public NPCClickEvent getNpcClickEvent() {
        return npcClickEvent;
    }

    private void create() {
        this.entityPlayer = NPCAPI.create(this);
        this.id = NPCAPI.getEntityID(this.entityPlayer);
        NPCAPI.setLocation(this.entityPlayer, this.location);

        if (this.viewer != null)
            this.viewer.update();

        if (this.npcClickEvent != null)
            this.npcClickEvent.setup(this);

        NPCManager.register(this);
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
        if (this.entityPlayer != null && this.viewer != null)
            for (String p : this.viewer.getPlayers())
                NPCAPI.destroy(this, Bukkit.getPlayer(p));

        this.entityPlayer = null;

        NPCManager.unregister(this);
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
     * @return game profile
     */
    public GameProfile getGameProfile() {
        return this.gameProfile;
    }


    /**
     * Gets id.
     *
     * @return Get The NPC's Id
     */
    public int getId() {
        return this.id;
    }

    /**
     * Gets location.
     *
     * @return Get the NPC's Location
     */
    public Location getLocation() {
        return this.location;
    }


    /**
     * Gets name.
     *
     * @return Get the NPC's Name
     */
    public String getName() {
        return this.name;
    }

    public boolean isShown(Player player) {
        return this.entityPlayer != null && this.viewer.isViewing(player.getName());
    }


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
            if (entity instanceof NPC)
                entity = ((NPC) entity).entityPlayer;

            Object[] entities = (Object[]) Array.newInstance(entityPlayerClass, 1);
            entities[0] = entity;
            Constructor<?> packetPlayOutPlayerInfoConstructor = Reflection.getConstructor(packetPlayOutPlayerInfoClass, enumPlayerInfoActionClass, entities.getClass());
            Object packetPlayOutPlayerInfoAdd = Reflection.newInstance(packetPlayOutPlayerInfoConstructor, playerInfoActionEnumAdd, entities);
            Object packetPlayOutNamedEntitySpawn = Reflection.newInstance(packetPlayOutNamedEntitySpawnConstructor, entity);
            Object packetPlayOutPlayerInfoRemove = Reflection.newInstance(packetPlayOutPlayerInfoConstructor, playerInfoActionEnumRemove, entities);
//            PacketPlayOutEntityMetadata p = new PacketPlayOutEntityMetadata(getEntityID(entity), ((EntityPlayer)entity).getDataWatcher(), true);
            Packets.sendPacket(player, packetPlayOutPlayerInfoAdd, packetPlayOutNamedEntitySpawn);

            Bukkit.getScheduler().runTaskLater(BimmCore.getInstance(), () ->
                    Packets.sendPacket(player, packetPlayOutPlayerInfoRemove), 20);
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
            int id = getEntityID(entity);
            Object packetPlayOutEntityEquipment = Reflection.newInstance(packetPlayOutEntityEquipmentConstructor, id, oSlot, craftItem);
            Packets.sendPacket(player, packetPlayOutEntityEquipment);
        }

        /**
         * Create object.
         *
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

        public static int getEntityID(Object entityPlayer) {
            int id = (int) Reflection.invokeMethod(entityPlayerClass, "getId", entityPlayer);
            return id;
        }
    }
}
