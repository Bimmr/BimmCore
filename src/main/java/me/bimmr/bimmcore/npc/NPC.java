package me.bimmr.bimmcore.npc;

import com.mojang.authlib.GameProfile;
import me.bimmr.bimmcore.reflection.Packets;
import me.bimmr.bimmcore.reflection.Reflection;
import net.minecraft.server.v1_12_R1.PacketPlayOutEntityEquipment;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.ItemStack;

import java.lang.reflect.Array;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.UUID;

//TODO: Finish equipping NPC code
public class NPC {

    private int id;
    private GameProfile gameProfile;
    private int entityID;
    private Location location;
    private String name;
    private Object entityPlayer;

    public NPC(String name, Location location) {
        this.name = name;
        this.location = location;

        this.gameProfile = new GameProfile(UUID.randomUUID(), name);

        entityPlayer = NPCAPI.create(this);
        NPCAPI.getEntityID(entityPlayer);
        teleport(location);
        showAll();
    }

    public void show(Player player) {
        NPCAPI.show(entityPlayer, player);
    }

    public void showAll() {
        for (Player p : Bukkit.getOnlinePlayers())
            show(p);
    }
    public void hideAll() {
        for (Player p : Bukkit.getOnlinePlayers())
            hide(p);
    }

    public void hide(Player player) {
        NPCAPI.hide(entityPlayer, player);
    }

    public void teleport(Location location) {
        NPCAPI.teleport(entityPlayer, location);
    }


    public GameProfile getGameProfile() {
        return gameProfile;
    }

    public void setGameProfile(GameProfile gameProfile) {
        this.gameProfile = gameProfile;
    }

    public int getEntityID() {
        return entityID;
    }

    public void setEntityID(int entityID) {
        this.entityID = entityID;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public class NPCListener implements Listener {

        @EventHandler
        public void join(PlayerJoinEvent e) {

        }
    }

    private static class NPCAPI {

        public enum ItemSlots {
            MAINHAND(0), OFFHAND(1), FEET(2), LEGS(3), CHEST(4), HEAD(5);
            private int id;
            ItemSlots(int id){this.id = id;}
        }

        private static Class<?> craftServerClass;
        private static Class<?> craftWorldClass;
        private static Class<?> worldClass;
        private static Class<?> playerInteractManagerClass;
        private static Class<?> worldServerClass;
        private static Class<?> minecraftServerClass;
        private static Class<?> entityPlayerClass;
        private static Class<?> craftItemStackClass;
        private static Class<?> itemStackClass;


        private static Constructor<?> playerInteractManagerConstructor;
        private static Constructor<?> entityConstructor;

        private static Method asNMSCopy;
        private static Method setLocation;
        private static Method setEquipment;

        private static Class<?> enumItemSlotClass;

        private static Object itemSlotEnumMainHand;
        private static Object itemSlotEnumOffHand;
        private static Object itemSlotEnumFeet;
        private static Object itemSlotEnumLegs;
        private static Object itemSlotEnumChest;
        private static Object itemSlotEnumHead;

        private static Class<?> packetPlayOutPlayerInfoClass;
        private static Class<?> enumPlayerInfoActionClass;
        private static Class<?> entityHumanClass;
        private static Class<?> packetPlayOutNamedEntitySpawnClass;


        private static Object playerInfoActionEnumAdd;
        private static Object playerInfoActionEnumRemove;

        static {
            craftServerClass = Reflection.getCraftClass("CraftServer");
            craftWorldClass = Reflection.getCraftClass("CraftWorld");

            worldClass = Reflection.getNMSClass("World");
            playerInteractManagerClass = Reflection.getNMSClass("PlayerInteractManager");

            worldServerClass = Reflection.getNMSClass("WorldServer");
            minecraftServerClass = Reflection.getNMSClass("MinecraftServer");
            entityPlayerClass = Reflection.getNMSClass("EntityPlayer");

            setLocation = Reflection.getMethod(entityPlayerClass, "setLocation", double.class, double.class, double.class, float.class, float.class);

            craftItemStackClass = Reflection.getCraftClass("inventory.CraftItemStack");
            itemStackClass = Reflection.getNMSClass("ItemStack");
            asNMSCopy = Reflection.getMethod(craftItemStackClass, "asNMSCopy", org.bukkit.inventory.ItemStack.class);

            enumItemSlotClass = Reflection.getNMSClass("EnumItemSlot");
            itemSlotEnumMainHand = enumItemSlotClass.getEnumConstants()[0];
            itemSlotEnumOffHand = enumItemSlotClass.getEnumConstants()[1];
            itemSlotEnumFeet = enumItemSlotClass.getEnumConstants()[2];
            itemSlotEnumLegs = enumItemSlotClass.getEnumConstants()[3];
            itemSlotEnumChest = enumItemSlotClass.getEnumConstants()[4];
            itemSlotEnumHead = enumItemSlotClass.getEnumConstants()[5];

            setEquipment = Reflection.getMethod(entityPlayerClass, "setEquipment", enumItemSlotClass, itemStackClass);

            packetPlayOutPlayerInfoClass = Reflection.getNMSClass("PacketPlayOutPlayerInfo");
            enumPlayerInfoActionClass = Reflection.getNMSClass("PacketPlayOutPlayerInfo$EnumPlayerInfoAction");

            playerInfoActionEnumAdd = enumPlayerInfoActionClass.getEnumConstants()[0];
            playerInfoActionEnumRemove = enumPlayerInfoActionClass.getEnumConstants()[4];

            entityHumanClass = Reflection.getNMSClass("EntityHuman");
            packetPlayOutNamedEntitySpawnClass = Reflection.getNMSClass("PacketPlayOutNamedEntitySpawn");
        }

        public static void teleport(Object entity, Location location) {
            try {
                setLocation.invoke(entity, location.getX(), location.getY(), location.getZ(), location.getYaw(), location.getPitch());
            } catch (IllegalAccessException | InvocationTargetException e) {
                e.printStackTrace();
            }
        }

        public static int getEntityID(Object entity) {
            return (int) Reflection.invokeMethod(entityPlayerClass, "getId", entity);
        }

        public static void show(Object entity, Player player) {
            try {
                Object[] entities = (Object[]) Array.newInstance(entityPlayerClass, 1);
                entities[0] = entity;
                Constructor packetPlayOutPlayerInfoConstructor = packetPlayOutPlayerInfoClass.getConstructor(enumPlayerInfoActionClass, entities.getClass());
                Object packetPlayOutPlayerInfo = null;

                packetPlayOutPlayerInfo = packetPlayOutPlayerInfoConstructor.newInstance(playerInfoActionEnumAdd, entities);
                Packets.sendPacket(player, packetPlayOutPlayerInfo);

                Constructor packetPlayOutNamedEntitySpawnConstructor = packetPlayOutNamedEntitySpawnClass.getConstructor(entityHumanClass);
                Object packetPlayOutNamedEntitySpawn = packetPlayOutNamedEntitySpawnConstructor.newInstance(entity);
                Packets.sendPacket(player, packetPlayOutNamedEntitySpawn);
            } catch (InstantiationException | InvocationTargetException | IllegalAccessException | NoSuchMethodException e) {
                e.printStackTrace();
            }

        }

        public static void hide(Object entity, Player player) {

            try {
                Object[] entities = (Object[]) Array.newInstance(entityPlayerClass, 1);
                entities[0] = entity;
                Constructor packetPlayOutPlayerInfoConstructor = packetPlayOutPlayerInfoClass.getConstructor(enumPlayerInfoActionClass, entities.getClass());
                Object packetPlayOutPlayerInfo = packetPlayOutPlayerInfoConstructor.newInstance(playerInfoActionEnumRemove, entities);
                Packets.sendPacket(player, packetPlayOutPlayerInfo);
            } catch (InstantiationException | InvocationTargetException | IllegalAccessException | NoSuchMethodException e) {
                e.printStackTrace();
            }
        }

        public static Object getCraftItemStack(ItemStack itemStack) {
            try {
                return asNMSCopy.invoke(craftItemStackClass, itemStack);
            } catch (IllegalAccessException | InvocationTargetException e) {
                e.printStackTrace();
            }
            return null;
        }

        public static void equip(Object entity, Object craftItem) {
            try {
                PacketPlayOutEntityEquipment packet = new PacketPlayOutEntityEquipment();
                setEquipment.invoke(entity, itemSlotEnumChest, craftItem);
            } catch (IllegalAccessException | InvocationTargetException e) {
                e.printStackTrace();
            }
        }

        public static Object create(NPC npc) {

            try {

                Object craftWorld = craftWorldClass.cast(npc.getLocation().getWorld());
                Object worldHandle = Reflection.getHandle(craftWorld);

                playerInteractManagerConstructor = playerInteractManagerClass.getConstructor(worldClass);

                Object craftServer = craftServerClass.cast(Bukkit.getServer());
                Object minecraftServer = Reflection.invokeMethod(craftServerClass, "getServer", craftServer);
                Object playerInteractManager = playerInteractManagerConstructor.newInstance(worldHandle);

                entityConstructor = entityPlayerClass.getConstructor(minecraftServerClass, worldServerClass, GameProfile.class, playerInteractManagerClass);
                Object entityPlayer = entityConstructor.newInstance(minecraftServer, worldHandle, npc.getGameProfile(), playerInteractManager);

                return entityPlayer;

            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }
    }
}