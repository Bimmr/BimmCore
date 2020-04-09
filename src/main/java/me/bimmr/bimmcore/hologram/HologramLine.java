package me.bimmr.bimmcore.hologram;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;

import me.bimmr.bimmcore.BimmCore;
import me.bimmr.bimmcore.TimedObject;
import me.bimmr.bimmcore.events.timing.TimedEvent;
import me.bimmr.bimmcore.reflection.Packets;
import me.bimmr.bimmcore.reflection.Reflection;
import net.minecraft.server.v1_15_R1.Entity;
import net.minecraft.server.v1_15_R1.EntityArmorStand;
import net.minecraft.server.v1_15_R1.PacketPlayOutEntityMetadata;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;

public class HologramLine extends TimedObject {
    private int id;

    private Object hologramObject;

    private String text;

    private Location location;

    public HologramLine(boolean showToAll, Location location, String text) {
        this(showToAll, location, text, (TimedEvent) null);
    }

    public HologramLine(Location location, String text) {
        this(true, location, text, (TimedEvent) null);
    }

    public HologramLine(Location location, String text, TimedEvent timedEvent) {
        this(true, location, text, timedEvent);
    }

    public HologramLine(Location location, String text, TimedEvent timedEvent, boolean startTimedEvent) {
        this(true, location, text, timedEvent, startTimedEvent);
    }

    public HologramLine(boolean showToAll, Location location, String text, TimedEvent timedEvent) {
        this(showToAll, location, text, timedEvent, false);
    }

    public HologramLine(boolean showToAll, Location location, String text, TimedEvent timedEvent, boolean startTimedEvent) {
        this.text = text;
        this.location = location;
        Object[] holoAPI = HologramAPI.createHologram(location, text);
        this.id = ((Integer) holoAPI[0]).intValue();
        this.hologramObject = holoAPI[1];
        setTimedEvent(timedEvent);
        if (startTimedEvent)
            startTask();
    }

    public int getId() {
        return this.id;
    }

    public Object getHologramObject() {
        return this.hologramObject;
    }

    public Location getLocation() {
        return this.location;
    }

    public String getText() {
        return this.text;
    }

    public void setText(String text) {
        this.text = text;
        HologramAPI.setText(this.hologramObject, text);
    }

    public void showPlayer(String player) {
        HologramAPI.deleteHologram(this.id, Bukkit.getPlayer(player));
        HologramAPI.showToPlayer(this.hologramObject, Bukkit.getPlayer(player));
    }

    public void remove() {
        HologramAPI.deleteHologram(this.id);
    }

    public void removePlayer(String player) {
        HologramAPI.deleteHologram(this.id, Bukkit.getPlayer(player));
    }

    private static class HologramAPI {
        private static Class<?> chatComponentText = Reflection.getNMSClass("ChatComponentText");
        private static Class<?> chatBaseComponent = Reflection.getNMSClass("IChatBaseComponent");
        private static Constructor chatComponentTextConstructor;
        private static Class<?> craftWorldClass = Reflection.getCraftClass("CraftWorld");
        private static Class<?> nmsEntityClass = Reflection.getNMSClass("Entity");
        private static Class<?> nmsWorldClass = Reflection.getNMSClass("World");
        private static Class<?> nmsArmorStandClass = Reflection.getNMSClass("EntityArmorStand");
        private static Class<?> nmsEntityLivingClass = Reflection.getNMSClass("EntityLiving");
        private static Class<?> nmsDataWatcherClass = Reflection.getNMSClass("DataWatcher");

        private static Class<?> packetPlayOutSpawnEntityLivingClass = Reflection.getNMSClass("PacketPlayOutSpawnEntityLiving");
        private static Class<?> packetPlayOutEntityDestroyClass = Reflection.getNMSClass("PacketPlayOutEntityDestroy");
        private static Class<?> packetPlayOutEntityMetadataClass = Reflection.getNMSClass("PacketPlayOutEntityMetadata");
        private static Constructor armorStandConstructor;
        private static Constructor packetPlayOutSpawnEntityLivingConstructor;
        private static Constructor packetPlayOutEntityDestroyConstructor;
        private static Constructor packetPlayOutEntityMetadataConstructor;
        private static Method entityGetIdMethod;
        private static Method setCustomNameMethod;
        private static Method setCustomNameVisibleMethod;
        private static Method setNoGravityMethod;
        private static Method setLocationMethod;
        private static Method setInvisibleMethod;
        private static Method getDataWatcherMethod;

        static {
            try {
                chatComponentTextConstructor = chatComponentText.getConstructor(String.class);
                if (!BimmCore.oldAPI) {
                    armorStandConstructor = nmsArmorStandClass.getConstructor(nmsWorldClass, double.class, double.class, double.class);
                } else {
                    armorStandConstructor = nmsArmorStandClass.getConstructor(nmsWorldClass);
                }
                packetPlayOutSpawnEntityLivingConstructor = packetPlayOutSpawnEntityLivingClass.getConstructor(nmsEntityLivingClass);
                packetPlayOutEntityDestroyConstructor = packetPlayOutEntityDestroyClass.getConstructor(int[].class);
                packetPlayOutEntityMetadataConstructor = packetPlayOutEntityMetadataClass.getConstructor(int.class, nmsDataWatcherClass, boolean.class);

                entityGetIdMethod = nmsArmorStandClass.getMethod("getId");
                setCustomNameVisibleMethod = nmsEntityClass.getMethod("setCustomNameVisible", boolean.class);
                setInvisibleMethod = nmsEntityClass.getMethod("setInvisible", boolean.class);

                if (!BimmCore.oldAPI) {
                    getDataWatcherMethod = nmsEntityClass.getMethod("getDataWatcher");
                    setCustomNameMethod = nmsEntityClass.getMethod("setCustomName", chatBaseComponent);
                    setNoGravityMethod = nmsEntityClass.getMethod("setNoGravity", boolean.class);
                } else {
                    setLocationMethod = nmsEntityClass.getMethod("setLocation", double.class, double.class, double.class, float.class, float.class);
                    setCustomNameMethod = nmsEntityClass.getMethod("setCustomName", String.class);
                }

            } catch (NoSuchMethodException e) {
                e.printStackTrace();
            }
        }

        private static void setText(Object entityArmorStand, String text) {
            try {
                Object itext;
                if (BimmCore.oldAPI) {
                    itext = text;
                } else {
                    itext = chatComponentTextConstructor.newInstance(text);
                }
                setCustomNameMethod.invoke(entityArmorStand, itext);
            } catch (IllegalAccessException | java.lang.reflect.InvocationTargetException | InstantiationException e) {
                e.printStackTrace();
            }
        }

        private static Object[] createHologram(Location location, String text) {
            try {
                Object entityArmorStand, craftWorld = craftWorldClass.cast(location.getWorld());
                Object worldHandle = Reflection.getHandle(craftWorld);
                if (!BimmCore.oldAPI) {
                    entityArmorStand = armorStandConstructor.newInstance(worldHandle, location.getX(), location.getY(), location.getZ());
                } else {
                    entityArmorStand = armorStandConstructor.newInstance(worldHandle);
                    setLocationMethod.invoke(entityArmorStand, location.getX(), location.getY(), location.getZ(), 0.0F, 0.0F);
                }
                Object id = entityGetIdMethod.invoke(entityArmorStand);

                if (!BimmCore.oldAPI) {
                    setNoGravityMethod.invoke(entityArmorStand, true);
                }

                setCustomNameVisibleMethod.invoke(entityArmorStand, true);
                setText(entityArmorStand, text);
                setInvisibleMethod.invoke(entityArmorStand, true);
                return new Object[]{id, entityArmorStand};
            } catch (IllegalAccessException | java.lang.reflect.InvocationTargetException | InstantiationException e) {
                e.printStackTrace();
                return null;
            }
        }

        public static void showToPlayer(Object entityArmorStand, Player p) {
            try {
                Object packetPlayOutSpawnEntityLiving = packetPlayOutSpawnEntityLivingConstructor.newInstance(entityArmorStand);
                Packets.sendPacket(p, packetPlayOutSpawnEntityLiving);

                if(!BimmCore.oldAPI && !Reflection.getVersion().startsWith("v1_13") && !Reflection.getVersion().startsWith("v1_14")) {
                    Object packetPlayOutEntityMetaData = packetPlayOutEntityMetadataConstructor.newInstance(entityGetIdMethod.invoke(entityArmorStand), getDataWatcherMethod.invoke(entityArmorStand), true);
                    Packets.sendPacket(p, packetPlayOutEntityMetaData);
                }

            } catch (InstantiationException | IllegalAccessException | java.lang.reflect.InvocationTargetException e) {
                e.printStackTrace();
            }
        }

        public static void deleteHologram(int id) {
            for (Player p : Bukkit.getOnlinePlayers())
                deleteHologram(id, p);
        }

        public static void deleteHologram(int id, Player p) {
            try {
                Object packetPlayOutEntityDestroy = packetPlayOutEntityDestroyConstructor.newInstance(new int[]{id});
                Packets.sendPacket(p, packetPlayOutEntityDestroy);
            } catch (InstantiationException | IllegalAccessException | java.lang.reflect.InvocationTargetException e) {
                e.printStackTrace();
            }
        }
    }
}
