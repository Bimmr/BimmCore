package me.bimmr.bimmcore.hologram;

import me.bimmr.bimmcore.BimmCore;
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
 * HologramLine Class - Should only be used by Holograms
 */
public class HologramLine extends TimedObject {
    private int id;
    private Hologram hologram;

    private Object hologramObject;
    private String text;
    private Location location;


    /**
     * Create a HologramLine - Should only be used by Holograms
     * TimedEvent is null
     *
     * @param hologram  The Hologram
     * @param location  The location for the hologram line
     * @param text      The text for the hologram line
     */
    public HologramLine(Hologram hologram, Location location, String text) {
        this(hologram,  location, text, null, false);
    }

    /**
     * Create a HologramLine - Should only be used by Holograms
     * Doesn't start TimedEvent right away
     *
     * @param hologram  The Hologram
     * @param location   The location for the hologram line
     * @param text       The text for the hologram line
     * @param timedEvent The timed event to run
     */
    public HologramLine(Hologram hologram,  Location location, String text, TimedEvent timedEvent) {
        this(hologram, location, text, timedEvent, false);
    }

    /**
     * Create a HologramLine - Should only be used by Holograms
     *
     * @param hologram  The Hologram
     * @param location        The location for the hologram line
     * @param text            The text for the hologram line
     * @param timedEvent      The timed event to run
     * @param startTimedEvent If timed even is starting right away
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
     * @return Get Bukiit Entity's ID
     */
    public int getId() {
        return this.id;
    }

    /**
     * @return Get the HologramAPI Object
     */
    public Object getHologramObject() {
        return this.hologramObject;
    }

    /**
     * @return Get the HologramLine's Location
     */
    public Location getLocation() {
        return this.location;
    }

    /**
     * @return Get the HologramLine's Text
     */
    public String getText() {
        return this.text;
    }

    /**
     * Set the HologramLine's text
     *
     * @param text The Text to set
     */
    public void setText(String text) {
        this.text = text;
        HologramAPI.setText(this.hologramObject, this.text);
        this.hologram.getViewer().update();
    }

    /**
     * Show the HologramLine to the player
     * Calls {@link #showPlayer(Player)}
     *
     * @param player Player's Name
     */
    public void showPlayer(String player) {
        showPlayer(Bukkit.getPlayer(player));
    }

    /**
     * Show the HologramLine to the player
     *
     * @param player The player
     */
    public void showPlayer(Player player) {
        HologramAPI.deleteHologram(this.id, player);
        HologramAPI.showToPlayer(this.hologramObject, player);
    }

    /**
     * Remove the HologramLine from every player
     */
    public void remove() {
        HologramAPI.deleteHologram(this.id);
    }

    /**
     * Remove the HologramLine from a specific player
     * Calls {@link #removePlayer(Player)}
     *
     * @param player The player's name
     */
    public void removePlayer(String player) {
        removePlayer(Bukkit.getPlayer(player));
    }

    /**
     * Remove the HologramLine from a specific player
     *
     * @param player The player
     */
    public void removePlayer(Player player) {
        HologramAPI.deleteHologram(this.id, player);
    }


    /**
     * HologramAPI Class
     */
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
            if (BimmCore.supports(13)) {
                armorStandConstructor = Reflection.getConstructor(nmsArmorStandClass, nmsWorldClass, double.class, double.class, double.class);
                setCustomNameMethod = Reflection.getMethod(nmsEntityClass, "setCustomName", chatBaseComponent);
                setNoGravityMethod = Reflection.getMethod(nmsEntityClass, "setNoGravity", boolean.class);
            } else {
                armorStandConstructor = Reflection.getConstructor(nmsArmorStandClass, nmsWorldClass);
                setCustomNameMethod = Reflection.getMethod(nmsEntityClass, "setCustomName", String.class);
            }
            if (BimmCore.supports(15)) {
                packetPlayOutEntityMetadataClass = Reflection.getNMSClass("PacketPlayOutEntityMetadata");
                packetPlayOutEntityMetadataConstructor = Reflection.getConstructor(packetPlayOutEntityMetadataClass, int.class, nmsDataWatcherClass, boolean.class);
                getDataWatcherMethod = Reflection.getMethod(nmsEntityClass, "getDataWatcher");
            }
        }

        /**
         * Set the ArmorStand's Custom Name
         *
         * @param entityArmorStand The ArmorStand
         * @param text             The name
         */
        private static void setText(Object entityArmorStand, String text) {
            Object iText;
            if (BimmCore.supports(13))
                iText = Reflection.newInstance(chatComponentTextConstructor, text);
            else
                iText = text;

            Reflection.invokeMethod(setCustomNameMethod, entityArmorStand, iText);
        }

        /**
         * Create the ArmorStand Object
         *
         * @param location The location
         * @return Array with [EntityID, NMS ArmorStandObject]
         */
        private static Object[] createHologram(Location location) {

            Object entityArmorStand;
            Object craftWorld = craftWorldClass.cast(location.getWorld());
            Object worldHandle = Reflection.getHandle(craftWorld);

            if (BimmCore.supports(13)) {
                entityArmorStand = Reflection.newInstance(armorStandConstructor, worldHandle, location.getX(), location.getY(), location.getZ());
            } else {
                entityArmorStand = Reflection.newInstance(armorStandConstructor, worldHandle);
                Reflection.invokeMethod(setLocationMethod, entityArmorStand, location.getX(), location.getY(), location.getZ(), 0.0F, 0.0F);
            }

            if (BimmCore.supports(13))
                Reflection.invokeMethod(setNoGravityMethod, entityArmorStand, true);


            Reflection.invokeMethod(setCustomNameVisibleMethod, entityArmorStand, true);
            Reflection.invokeMethod(setInvisibleMethod, entityArmorStand, true);

            Object id = getEntityID(entityArmorStand);
            return new Object[]{id, entityArmorStand};
        }
        public static int getEntityID(Object entityPlayer) {
            return (int) Reflection.invokeMethod(nmsArmorStandClass, "getId", entityPlayer);
        }
        /**
         * Show the ArmorStand to the player
         *
         * @param entityArmorStand The ArmorStand object
         * @param p                The player
         */
        public static void showToPlayer(Object entityArmorStand, Player p) {
            Object packetPlayOutSpawnEntityLiving = Reflection.newInstance(packetPlayOutSpawnEntityLivingConstructor, entityArmorStand);
            Packets.sendPacket(p, packetPlayOutSpawnEntityLiving);

            showMetaDataToPlayer(entityArmorStand, p);
        }

        /**
         * Show the ArmorStand's MetaData to the player
         *
         * @param entityArmorStand The ArmorStand object
         * @param p                The player
         */
        public static void showMetaDataToPlayer(Object entityArmorStand, Player p) {
            if (BimmCore.supports(15)) {
                Object id = getEntityID(entityArmorStand);
                Object dataWatcher = Reflection.invokeMethod(getDataWatcherMethod, entityArmorStand);
                Object packetPlayOutEntityMetaData = Reflection.newInstance(packetPlayOutEntityMetadataConstructor, id, dataWatcher, true);
                Packets.sendPacket(p, packetPlayOutEntityMetaData);

            }
        }

        /**
         * Send DestroyPacket's for the ArmorStandObject
         *
         * @param id The ID of the ArmorStand
         */
        public static void deleteHologram(int id) {
            for (Player p : Bukkit.getOnlinePlayers())
                deleteHologram(id, p);
        }

        /**
         * Send DestroyPacket's for the ArmorStandObject
         *
         * @param entityArmorStand The ArmorStand
         */
        public static void deleteHologram(Object entityArmorStand) {
            int id = getEntityID(entityArmorStand);
            for (Player p : Bukkit.getOnlinePlayers())
                deleteHologram(id, p);
        }

        /**
         * Send DestroyPacket's for the ArmorStandObject to a player
         * Calls {@link #deleteHologram(int, Player)}
         *
         * @param entityArmorStand The ArmorStand object
         * @param p                The player
         */
        public static void deleteHologram(Object entityArmorStand, Player p) {
            deleteHologram(getEntityID(entityArmorStand), p);
        }

        /**
         * Send DestroyPacket's for the ArmorStandObject to a player
         *
         * @param id The ArmorStand's ID
         * @param p  The player
         */
        public static void deleteHologram(int id, Player p) {
            Object packetPlayOutEntityDestroy = Reflection.newInstance(packetPlayOutEntityDestroyConstructor, new int[]{id});
            Packets.sendPacket(p, packetPlayOutEntityDestroy);
        }
    }
}
