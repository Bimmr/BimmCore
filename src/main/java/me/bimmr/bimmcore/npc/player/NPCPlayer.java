package me.bimmr.bimmcore.npc.player;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import com.mojang.datafixers.util.Pair;
import me.bimmr.bimmcore.BimmCore;
import me.bimmr.bimmcore.npc.NPCBase;
import me.bimmr.bimmcore.reflection.Packets;
import me.bimmr.bimmcore.reflection.Reflection;
import me.bimmr.bimmcore.reflection.Viewer;
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
import java.util.*;
import java.util.logging.Level;

/**
 * The type Npc player.
 */
public class NPCPlayer extends NPCBase {

    private static HashMap<Object, String[]> skins = new HashMap<>();

    private GameProfile gameProfile;

    private Object entityPlayer;
    private Viewer viewer;


    /**
     * Instantiates a new Npc player.
     *
     * @param name     the name
     * @param location the location
     */
    public NPCPlayer(String name, Location location) {
        this(name, location, name);
    }

    /**
     * Instantiates a new Npc player.
     *
     * @param name     the name
     * @param location the location
     * @param skin     the skin
     */
    public NPCPlayer(String name, Location location, String skin) {
        super(NPCType.PLAYER, name, location);

        this.gameProfile = new GameProfile(UUID.randomUUID(), name);

        create();
        setSkin(skin, false);
        this.viewer = new Viewer() {

            public void update(Player p) {
                NPCAPI.destroy(NPCPlayer.this, p);
                NPCPlayer.NPCAPI.show(NPCPlayer.this, p);
                NPCPlayer.NPCAPI.setRotation(NPCPlayer.this.entityPlayer, p, getLocation().getYaw());
                for (Map.Entry<ItemSlots, ItemStack> e : getEquipment().entrySet())
                    NPCPlayer.NPCAPI.equip(NPCPlayer.this.entityPlayer, p, e.getKey(), e.getValue());
            }

            public void onAddToView(Player p) {
            }

            public void onRemoveFromView(Player p) {
                NPCAPI.destroy(NPCPlayer.this, p);
            }
        };
    }


    /**
     * Gets skin.
     *
     * @return the skin
     */
    public Property getSkin() {
        if (this.gameProfile.getProperties().isEmpty())
            return null;
        return (Property) this.gameProfile.getProperties().get("textures").toArray()[0];
    }

    /**
     * Sets skin.
     *
     * @param nameOrUUID the name or uuid
     */
    public void setSkin(String nameOrUUID) {
        setSkin(nameOrUUID, true);
    }

    /**
     * Gets skin texture.
     *
     * @return the skin texture
     */
    public String getSkinTexture() {
        return getSkin().getValue();
    }

    /**
     * Gets skin signature.
     *
     * @return the skin signature
     */
    public String getSkinSignature() {
        return getSkin().getSignature();
    }

    /**
     * Sets skin.
     *
     * @param nameOrUUID the name or uuid
     * @param refresh    the refresh
     */
    public void setSkin(String nameOrUUID, boolean refresh) {
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
        if (refresh)
            refresh();
    }

    /**
     * Show.
     *
     * @param player the player
     */
    public void show(Player player) {
        this.viewer.addPlayer(player.getName());
        this.viewer.update(player);
    }

    /**
     * Refresh.
     */
    public void refresh() {
        destroy();
        create();
    }

    /**
     * Sets skin.
     *
     * @param texture   the texture
     * @param signature the signature
     */
    public void setSkin(String texture, String signature) {
        setSkin(texture, signature, true);
    }

    /**
     * Sets skin.
     *
     * @param texture   the texture
     * @param signature the signature
     * @param refresh   the refresh
     */
    public void setSkin(String texture, String signature, boolean refresh) {
        this.gameProfile.getProperties().removeAll("textures");
        this.gameProfile.getProperties().put("textures", new Property("textures", texture, signature));
        if (refresh)
            refresh();
    }

    @Override
    protected void created() {
        this.entityPlayer = NPCAPI.create(this);
        NPCAPI.setLocation(this.entityPlayer, getLocation());

        if (this.viewer != null)
            this.viewer.update();

        if (getNPCClickEvent() != null)
            getNPCClickEvent().setup(this);

    }

    /**
     * Hide.
     *
     * @param player the player
     */
    public void hide(Player player) {
        this.viewer.removePlayer(player.getName());
    }

    protected void destroyed() {
        if (this.entityPlayer != null && this.viewer != null)
            for (String p : this.viewer.getPlayers())
                NPCAPI.destroy(this, Bukkit.getPlayer(p));

        this.entityPlayer = null;
    }


    @Override
    protected void renamed(String name) {

        Property skin = this.getSkin();
        this.gameProfile = new GameProfile(this.gameProfile.getId(), name);
        if (skin != null)
            this.gameProfile.getProperties().put("textures", new Property("textures", skin.getValue(), skin.getSignature()));

        refresh();

    }

    @Override
    protected void teleported(Location location) {
        NPCAPI.setLocation(this.entityPlayer, location);
        if (this.viewer != null)
            this.viewer.update();
    }

    @Override
    protected void equipped(ItemSlots itemSlot, ItemStack itemStack) {
        this.viewer.update();
    }

    /**
     * Gets game profile.
     *
     * @return the game profile
     */
    public GameProfile getGameProfile() {
        return this.gameProfile;
    }


    public int getId() {
        return NPCAPI.getEntityID(this.entityPlayer);
    }


    /**
     * Is shown boolean.
     *
     * @param player the player
     * @return the boolean
     */
    public boolean isShown(Player player) {
        return this.entityPlayer != null && this.viewer.isViewing(player.getName());
    }

    private static class NPCAPI {

        private static Class<?> craftServerClass = Reflection.getCraftClass("CraftServer");
        private static Class<?> craftWorldClass = Reflection.getCraftClass("CraftWorld");
        private static Class<?> playerInteractManagerClass;
        private static Class<?> worldServerClass;
        private static Class<?> worldClass;
        private static Class<?> minecraftServerClass;
        private static Class<?> entityPlayerClass;
        private static Class<?> entityClass;
        private static Class<?> enumPlayerInfoActionClass;
        private static Class<?> entityHumanClass;
        private static Class<?> packetPlayOutNamedEntitySpawnClass;
        private static Class<?> packetPlayOutEntityEquipmentClass;
        private static Class<?> packetPlayOutEntityDestroyClass;
        private static Class<?> packetPlayOutEntityHeadRotationClass;
        private static Class<?> craftItemStackClass = Reflection.getCraftClass("inventory.CraftItemStack");
        private static Class<?> itemStackClass;
        private static Class<?> enumItemSlotClass;
        private static Class<?> packetPlayOutPlayerInfoClass;

        private static Constructor<?> playerInteractManagerConstructor;
        private static Constructor<?> entityConstructor;
        private static Constructor<?> packetPlayOutEntityHeadRotationConstructor;
        private static Constructor<?> packetPlayOutNamedEntitySpawnConstructor;
        private static Constructor<?> packetPlayOutEntityEquipmentConstructor;
        private static Constructor<?> playOutEntityDestroyConstructor;

        private static Method asNMSCopy;
        private static Method setLocation;

        private static Object itemSlotEnumMainHand;
        private static Object itemSlotEnumOffHand;
        private static Object itemSlotEnumFeet;
        private static Object itemSlotEnumLegs;
        private static Object itemSlotEnumChest;
        private static Object itemSlotEnumHead;

        private static Object playerInfoActionEnumAdd;
        private static Object playerInfoActionEnumRemove;

        static {
                playerInteractManagerClass = Reflection.getNMClass("server.level.PlayerInteractManager");
                worldServerClass = Reflection.getNMClass("server.level.WorldServer");
                worldClass = Reflection.getNMWClass("level.World");
                minecraftServerClass = Reflection.getNMClass("server.MinecraftServer");
                entityPlayerClass = Reflection.getNMClass("server.level.EntityPlayer");
                entityClass = Reflection.getNMWClass("entity.Entity");
                enumPlayerInfoActionClass = Reflection.getNMClass("network.protocol.game.PacketPlayOutPlayerInfo$EnumPlayerInfoAction");
                entityHumanClass = Reflection.getNMWClass("entity.player.EntityHuman");
                packetPlayOutNamedEntitySpawnClass = Reflection.getNMClass("network.protocol.game.PacketPlayOutNamedEntitySpawn");
                packetPlayOutEntityEquipmentClass = Reflection.getNMClass("network.protocol.game.PacketPlayOutEntityEquipment");
                packetPlayOutEntityDestroyClass = Reflection.getNMClass("network.protocol.game.PacketPlayOutEntityDestroy");
                packetPlayOutEntityHeadRotationClass = Reflection.getNMClass("network.protocol.game.PacketPlayOutEntityHeadRotation");
                itemStackClass = Reflection.getNMWClass("item.ItemStack");
                enumItemSlotClass = Reflection.getNMWClass("entity.EnumItemSlot");
                packetPlayOutPlayerInfoClass = Reflection.getNMClass("network.protocol.game.PacketPlayOutPlayerInfo");
                entityConstructor = Reflection.getConstructor(entityPlayerClass, minecraftServerClass, worldServerClass, GameProfile.class);
                playOutEntityDestroyConstructor = Reflection.getConstructor(packetPlayOutEntityDestroyClass, int.class);

                packetPlayOutEntityEquipmentConstructor = Reflection.getConstructor(packetPlayOutEntityEquipmentClass, int.class, List.class);

                playerInteractManagerConstructor = Reflection.getConstructor(playerInteractManagerClass, entityPlayerClass);

            packetPlayOutEntityHeadRotationConstructor = Reflection.getConstructor(packetPlayOutEntityHeadRotationClass, entityClass, byte.class);
            packetPlayOutNamedEntitySpawnConstructor = Reflection.getConstructor(packetPlayOutNamedEntitySpawnClass, entityHumanClass);

            asNMSCopy = Reflection.getMethod(craftItemStackClass, "asNMSCopy", ItemStack.class);
            setLocation = Reflection.getMethod(entityPlayerClass, "setLocation", double.class, double.class, double.class, float.class, float.class);

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
         * Sets rotation.
         *
         * @param entity the entity
         * @param player the player
         * @param yaw    the yaw
         */
        public static void setRotation(Object entity, Player player, float yaw) {
            Object packetPlayOutEntityHeadRotation = Reflection.newInstance(packetPlayOutEntityHeadRotationConstructor, entity, (byte) ((yaw * 256.0F) / 360.0F));
            Packets.sendPacket(player, packetPlayOutEntityHeadRotation);
        }

        /**
         * Sets location.
         *
         * @param entity   the entity
         * @param location the location
         */
        public static void setLocation(Object entity, Location location) {
            Reflection.invokeMethod(setLocation, entity, location.getX(), location.getY(), location.getZ(), location.getYaw(), location.getPitch());
        }

        /**
         * Show.
         *
         * @param entity the entity
         * @param player the player
         */
        public static void show(Object entity, Player player) {
            if (entity instanceof NPCPlayer)
                entity = ((NPCPlayer) entity).entityPlayer;

            Object[] entities = (Object[]) Array.newInstance(entityPlayerClass, 1);
            entities[0] = entity;

            Constructor<?> packetPlayOutPlayerInfoConstructor = Reflection.getConstructor(packetPlayOutPlayerInfoClass, enumPlayerInfoActionClass, entities.getClass());
            Object packetPlayOutPlayerInfoAdd = Reflection.newInstance(packetPlayOutPlayerInfoConstructor, playerInfoActionEnumAdd, entities);
            Object packetPlayOutNamedEntitySpawn = Reflection.newInstance(packetPlayOutNamedEntitySpawnConstructor, entity);
            Object packetPlayOutPlayerInfoRemove = Reflection.newInstance(packetPlayOutPlayerInfoConstructor, playerInfoActionEnumRemove, entities);

//            DataWatcher watcher = ((EntityPlayer)entity).getDataWatcher();
//            watcher.set(DataWatcherRegistry.a.a(10), (byte) 127);
//            //PacketPlayOutEntityMetadata packet4 = new PacketPlayOutEntityMetadata(npc.getId(), watcher, true);
//            PacketPlayOutEntityMetadata p = new PacketPlayOutEntityMetadata(getEntityID(entity), watcher, true);
            Packets.sendPacket(player, packetPlayOutPlayerInfoAdd, packetPlayOutNamedEntitySpawn);

            Bukkit.getScheduler().runTaskLater(BimmCore.getInstance(), () ->
                    Packets.sendPacket(player, packetPlayOutPlayerInfoRemove), 20);
        }

        private static Object getCraftItemStack(ItemStack itemStack) {
            return Reflection.invokeMethod(asNMSCopy, craftItemStackClass, itemStack);
        }

        /**
         * Equip.
         *
         * @param entity the entity
         * @param player the player
         * @param slot   the slot
         * @param item   the item
         */
        public static void equip(Object entity, Player player, ItemSlots slot, ItemStack item) {
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
            Object packetPlayOutEntityEquipment;
                List<Pair<Object, Object>> list = new ArrayList<>();
                list.add(new Pair<>(oSlot, craftItem));
                packetPlayOutEntityEquipment = Reflection.newInstance(packetPlayOutEntityEquipmentConstructor, id, list);
            Packets.sendPacket(player, packetPlayOutEntityEquipment);
        }

        /**
         * Create object.
         *
         * @param npcPlayer the npc player
         * @return the object
         */
        public static Object create(NPCPlayer npcPlayer) {
//            MinecraftServer minecraftServer = ((CraftServer) Bukkit.getServer()).getServer();
//            WorldServer worldServer = ((CraftWorld) npcPlayer.getLocation().getWorld()).getHandle();
//            PlayerInteractManager playerInteractManager = new PlayerInteractManager(worldServer);
            Object craftWorld = craftWorldClass.cast(npcPlayer.getLocation().getWorld());
            Object worldServer = Reflection.getHandle(craftWorld);
            Object craftServer = craftServerClass.cast(Bukkit.getServer());
            Object minecraftServer = Reflection.invokeMethod(craftServerClass, "getServer", craftServer);
            Object playerInteractManager;
            Object entityPlayer;
                entityPlayer = Reflection.newInstance(entityConstructor, minecraftServer, worldServer, npcPlayer.getGameProfile());
                playerInteractManager = Reflection.newInstance(playerInteractManagerConstructor, entityPlayer);

            return entityPlayer;
        }

        /**
         * Destroy.
         *
         * @param npcPlayer the npc player
         * @param player    the player
         */
        public static void destroy(NPCPlayer npcPlayer, Player player) {
            Object[] entities = (Object[]) Array.newInstance(entityPlayerClass, 1);
            entities[0] = npcPlayer.entityPlayer;
            Constructor<?> packetPlayOutPlayerInfoConstructor = Reflection.getConstructor(packetPlayOutPlayerInfoClass, enumPlayerInfoActionClass, entities.getClass());
            Object packetPlayOutPlayerInfoRemove = Reflection.newInstance(packetPlayOutPlayerInfoConstructor, playerInfoActionEnumRemove, entities);
            Object playOutEntityDestroyPacket;
                playOutEntityDestroyPacket = Reflection.newInstance(playOutEntityDestroyConstructor, npcPlayer.getId());

            Packets.sendPacket(player, packetPlayOutPlayerInfoRemove, playOutEntityDestroyPacket);
        }

        /**
         * Gets entity id.
         *
         * @param entityPlayer the entity player
         * @return the entity id
         */
        public static int getEntityID(Object entityPlayer) {
            int id = (int) Reflection.invokeMethod(entityPlayerClass, "getId", entityPlayer);
            return id;
        }
    }
}
