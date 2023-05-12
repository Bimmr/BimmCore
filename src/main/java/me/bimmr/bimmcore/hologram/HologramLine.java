package me.bimmr.bimmcore.hologram;

import me.bimmr.bimmcore.BimmCore;
import me.bimmr.bimmcore.utils.timed.Timed;
import me.bimmr.bimmcore.utils.timed.TimedObject;
import me.bimmr.bimmcore.utils.timed.TimedEvent;
import me.bimmr.bimmcore.reflection.Packets;
import me.bimmr.bimmcore.reflection.Reflection;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;

/**
 * The type Hologram line.
 */
public class HologramLine extends TimedObject {
    private int id;
    private Hologram hologram;

    private Object hologramObject;
    private String text;
    private Location location;


    /**
     * Instantiates a new Hologram line.
     *
     * @param hologram the hologram
     * @param location the location
     * @param text     the text
     */
    public HologramLine(Hologram hologram, Location location, String text) {
        this(hologram,  location, text, null, false);
    }

    /**
     * Instantiates a new Hologram line.
     *
     * @param hologram the hologram
     * @param location the location
     * @param text     the text
     * @param timed    the timed
     * @param time     the time
     */
    public HologramLine(Hologram hologram,  Location location, String text, Timed timed, int time) {
        this(hologram, location, text, new TimedEvent(time) {
            @Override
            public void run() {
                timed.onRun(this);
            }
        }, false);
    }

    /**
     * Instantiates a new Hologram line.
     *
     * @param hologram   the hologram
     * @param location   the location
     * @param text       the text
     * @param timedEvent the timed event
     */
    public HologramLine(Hologram hologram,  Location location, String text, TimedEvent timedEvent) {
        this(hologram, location, text, timedEvent, false);
    }

    /**
     * Instantiates a new Hologram line.
     *
     * @param hologram        the hologram
     * @param location        the location
     * @param text            the text
     * @param timed           the timed
     * @param time            the time
     * @param startTimedEvent the start timed event
     */
    public HologramLine(Hologram hologram, Location location, String text, Timed timed, int time, boolean startTimedEvent) {
        this(hologram, location, text, new TimedEvent(time) {
            @Override
            public void run() {
                timed.onRun(this);
            }
        }, startTimedEvent);
    }

    /**
     * Instantiates a new Hologram line.
     *
     * @param hologram        the hologram
     * @param location        the location
     * @param text            the text
     * @param timedEvent      the timed event
     * @param startTimedEvent the start timed event
     */
    public HologramLine(Hologram hologram, Location location, String text, TimedEvent timedEvent, boolean startTimedEvent) {
        this.hologram = hologram;
        this.text = text;
        this.location = location;
        Object[] holoAPI = HologramAPI.createHologram(location);
        this.id = (Integer) holoAPI[0];
        this.hologramObject = holoAPI[1];
        HologramAPI.setText(hologramObject, text);
        setTimedEvent(timedEvent, startTimedEvent);
    }

    /**
     * Gets id.
     *
     * @return the id
     */
    public int getId() {
        return this.id;
    }

    /**
     * Gets hologram object.
     *
     * @return the hologram object
     */
    public Object getHologramObject() {
        return this.hologramObject;
    }

    /**
     * Gets location.
     *
     * @return the location
     */
    public Location getLocation() {
        return this.location;
    }

    /**
     * Gets text.
     *
     * @return the text
     */
    public String getText() {
        return this.text;
    }

    /**
     * Sets text.
     *
     * @param text the text
     */
    public void setText(String text) {
        this.text = text;
        HologramAPI.setText(this.hologramObject, this.text);
        this.hologram.getViewer().update();
    }

    /**
     * Show player.
     *
     * @param player the player
     */
    public void showPlayer(String player) {
        showPlayer(Bukkit.getPlayer(player));
    }

    /**
     * Show player.
     *
     * @param player the player
     */
    public void showPlayer(Player player) {
        HologramAPI.deleteHologram(this.id, player);
        HologramAPI.showToPlayer(this.hologramObject, player);
    }

    /**
     * Remove.
     */
    public void remove() {
        HologramAPI.deleteHologram(this.id);
    }

    /**
     * Remove player.
     *
     * @param player the player
     */
    public void removePlayer(String player) {
        removePlayer(Bukkit.getPlayer(player));
    }

    /**
     * Remove player.
     *
     * @param player the player
     */
    public void removePlayer(Player player) {
        HologramAPI.deleteHologram(this.id, player);
    }


    private static class HologramAPI {
        private static Class<?> chatComponentText = Reflection.getNMSClass("ChatComponentText");
        private static Class<?> chatBaseComponent = Reflection.getNMSClass("IChatBaseComponent");
        private static Class<?> craftWorldClass = Reflection.getCraftClass("CraftWorld");
        private static Class<?> nmsEntityClass = Reflection.getNMSClass("Entity");
        private static Class<?> nmsEntityLivingClass = Reflection.getNMSClass("EntityLiving");
        private static Class<?> nmsWorldClass = Reflection.getNMSClass("World");
        private static Class<?> nmsArmorStandClass = Reflection.getNMSClass("EntityArmorStand");
        private static Class<?> nmsDataWatcherClass = Reflection.getNMSClass("DataWatcher");

        private static Class<?> packetPlayOutSpawnEntityLivingClass = Reflection.getNMSClass("PacketPlayOutSpawnEntityLiving");
        private static Class<?> packetPlayOutEntityDestroyClass = Reflection.getNMSClass("PacketPlayOutEntityDestroy");
        private static Class<?> packetPlayOutEntityMetadataClass;

        private static Constructor chatComponentTextConstructor = Reflection.getConstructor(chatComponentText, String.class);
        private static Constructor packetPlayOutSpawnEntityLivingConstructor = Reflection.getConstructor(packetPlayOutSpawnEntityLivingClass, nmsEntityLivingClass);
        private static Constructor packetPlayOutEntityDestroyConstructor = Reflection.getConstructor(packetPlayOutEntityDestroyClass, int[].class);
        private static Constructor armorStandConstructor;
        private static Constructor packetPlayOutEntityMetadataConstructor;

        private static Method setCustomNameVisibleMethod = Reflection.getMethod(nmsEntityClass, "setCustomNameVisible", boolean.class);
        private static Method setInvisibleMethod = Reflection.getMethod(nmsEntityClass, "setInvisible", boolean.class);

        private static Method setCustomNameMethod;
        private static Method setNoGravityMethod;
        private static Method setLocationMethod = Reflection.getMethod(nmsEntityClass, "setLocation", double.class, double.class, double.class, float.class, float.class);
        private static Method getDataWatcherMethod;

        static {
                armorStandConstructor = Reflection.getConstructor(nmsArmorStandClass, nmsWorldClass, double.class, double.class, double.class);
                setCustomNameMethod = Reflection.getMethod(nmsEntityClass, "setCustomName", chatBaseComponent);
                setNoGravityMethod = Reflection.getMethod(nmsEntityClass, "setNoGravity", boolean.class);

                packetPlayOutEntityMetadataClass = Reflection.getNMSClass("PacketPlayOutEntityMetadata");
                packetPlayOutEntityMetadataConstructor = Reflection.getConstructor(packetPlayOutEntityMetadataClass, int.class, nmsDataWatcherClass, boolean.class);
                getDataWatcherMethod = Reflection.getMethod(nmsEntityClass, "getDataWatcher");

        }

        private static void setText(Object entityArmorStand, String text) {
            Object iText;
                iText = Reflection.newInstance(chatComponentTextConstructor, text);

            Reflection.invokeMethod(setCustomNameMethod, entityArmorStand, iText);
        }

        private static Object[] createHologram(Location location) {

            Object entityArmorStand;
            Object craftWorld = craftWorldClass.cast(location.getWorld());
            Object worldHandle = Reflection.getHandle(craftWorld);

                entityArmorStand = Reflection.newInstance(armorStandConstructor, worldHandle, location.getX(), location.getY(), location.getZ());

                Reflection.invokeMethod(setNoGravityMethod, entityArmorStand, true);


            Reflection.invokeMethod(setCustomNameVisibleMethod, entityArmorStand, true);
            Reflection.invokeMethod(setInvisibleMethod, entityArmorStand, true);

            Object id = getEntityID(entityArmorStand);
            return new Object[]{id, entityArmorStand};
        }

        /**
         * Gets entity id.
         *
         * @param entityPlayer the entity player
         * @return the entity id
         */
        public static int getEntityID(Object entityPlayer) {
            return (int) Reflection.invokeMethod(nmsArmorStandClass, "getId", entityPlayer);
        }

        /**
         * Show to player.
         *
         * @param entityArmorStand the entity armor stand
         * @param p                the p
         */
        public static void showToPlayer(Object entityArmorStand, Player p) {
            Object packetPlayOutSpawnEntityLiving = Reflection.newInstance(packetPlayOutSpawnEntityLivingConstructor, entityArmorStand);
            Packets.sendPacket(p, packetPlayOutSpawnEntityLiving);

            showMetaDataToPlayer(entityArmorStand, p);
        }

        /**
         * Show meta data to player.
         *
         * @param entityArmorStand the entity armor stand
         * @param p                the p
         */
        public static void showMetaDataToPlayer(Object entityArmorStand, Player p) {
                Object id = getEntityID(entityArmorStand);
                Object dataWatcher = Reflection.invokeMethod(getDataWatcherMethod, entityArmorStand);
                Object packetPlayOutEntityMetaData = Reflection.newInstance(packetPlayOutEntityMetadataConstructor, id, dataWatcher, true);
                Packets.sendPacket(p, packetPlayOutEntityMetaData);

        }

        /**
         * Delete hologram.
         *
         * @param id the id
         */
        public static void deleteHologram(int id) {
            for (Player p : Bukkit.getOnlinePlayers())
                deleteHologram(id, p);
        }

        /**
         * Delete hologram.
         *
         * @param entityArmorStand the entity armor stand
         */
        public static void deleteHologram(Object entityArmorStand) {
            int id = getEntityID(entityArmorStand);
            for (Player p : Bukkit.getOnlinePlayers())
                deleteHologram(id, p);
        }

        /**
         * Delete hologram.
         *
         * @param entityArmorStand the entity armor stand
         * @param p                the p
         */
        public static void deleteHologram(Object entityArmorStand, Player p) {
            deleteHologram(getEntityID(entityArmorStand), p);
        }

        /**
         * Delete hologram.
         *
         * @param id the id
         * @param p  the p
         */
        public static void deleteHologram(int id, Player p) {
            Object packetPlayOutEntityDestroy = Reflection.newInstance(packetPlayOutEntityDestroyConstructor, new int[]{id});
            Packets.sendPacket(p, packetPlayOutEntityDestroy);
        }
    }
}
