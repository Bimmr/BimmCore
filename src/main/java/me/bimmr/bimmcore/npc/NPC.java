package me.bimmr.bimmcore.npc;

import com.mojang.authlib.GameProfile;
import java.lang.reflect.Array;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import me.bimmr.bimmcore.Viewer;
import me.bimmr.bimmcore.reflection.Packets;
import me.bimmr.bimmcore.reflection.Reflection;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.ItemStack;

public class NPC {
    private static List<NPC> npcs = new ArrayList<>();

    private int id;

    private GameProfile gameProfile;

    private int entityID;

    private String name;

    private Object entityPlayer;

    private Viewer viewer;

    public static void addNPC(NPC npc) {
        npcs.add(npc);
    }

    public static void removeNPC(NPC npc) {
        npcs.remove(npc);
    }

    public static NPC getNPC(int id) {
        for (NPC npc : npcs) {
            if (npc.id == id)
                return npc;
        }
        return null;
    }

    private HashMap<ItemSlots, ItemStack> equipment = new HashMap<>();

    private NPCClickEvent npcClickEvent;

    private Location location;

    public NPC(String name, Location location) {
        this.name = name;
        this.location = location;
        this.gameProfile = new GameProfile(UUID.randomUUID(), name);
        this.entityPlayer = NPCAPI.create(this);
        this.id = NPCAPI.getEntityID(this.entityPlayer);
        this.viewer = new Viewer() {
            public void update(Player p) {
                NPC.NPCAPI.show(NPC.this.entityPlayer, p);
                for (Map.Entry<NPC.ItemSlots, ItemStack> e : NPC.this.getEquipment().entrySet())
                    NPC.NPCAPI.equip(NPC.this.entityPlayer, p, e.getKey(), e.getValue());
            }

            public void onAddToView(Player p) {}

            public void onRemoveFromView(Player p) {}
        };
        teleport(location);
        addNPC(this);
    }

    public void setNPCClickEvent(NPCClickEvent npcClickEvent) {
        this.npcClickEvent = npcClickEvent;
        this.npcClickEvent.setup(this);
    }

    public void removeNPCClickEvent() {
        if (this.npcClickEvent != null)
            HandlerList.unregisterAll(this.npcClickEvent);
        this.npcClickEvent = null;
    }

    public HashMap<ItemSlots, ItemStack> getEquipment() {
        return this.equipment;
    }

    public void show(Player player) {
        this.viewer.addPlayer(player.getName());
        this.viewer.update(player);
    }

    public void hide(Player player) {
        this.viewer.removePlayer(player.getName());
    }

    public void teleport(Location location) {
        this.location = location;
        NPCAPI.teleport(this.entityPlayer, location);
        this.viewer.update();
    }

    public void equip(ItemSlots slot, ItemStack itemStack) {
        this.equipment.put(slot, itemStack);
        this.viewer.update();
    }

    public GameProfile getGameProfile() {
        return this.gameProfile;
    }

    public void setGameProfile(GameProfile gameProfile) {
        this.gameProfile = gameProfile;
    }

    public int getEntityID() {
        return this.entityID;
    }

    public void setEntityID(int entityID) {
        this.entityID = entityID;
    }

    public Location getLocation() {
        return this.location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public class NPCListener implements Listener {
        @EventHandler
        public void join(PlayerJoinEvent e) {}
    }

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

        private static Class<?> worldClass = Reflection.getNMSClass("World");

        private static Class<?> playerInteractManagerClass = Reflection.getNMSClass("PlayerInteractManager");

        private static Class<?> worldServerClass = Reflection.getNMSClass("WorldServer");

        private static Class<?> minecraftServerClass = Reflection.getNMSClass("MinecraftServer");

        private static Class<?> entityPlayerClass = Reflection.getNMSClass("EntityPlayer");

        private static Class<?> entityClass = Reflection.getNMSClass("Entity");

        private static Class<?> craftItemStackClass;

        private static Class<?> itemStackClass;

        private static Constructor<?> playerInteractManagerConstructor;

        private static Constructor<?> entityConstructor;

        private static Method asNMSCopy;

        private static Method setLocation = Reflection.getMethod(entityPlayerClass, "setLocation", new Class[] { double.class, double.class, double.class, float.class, float.class });

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

        private static Class<?> packetPlayOutEntityEquipmentClass;

        private static Class<?> packetPlayOutEntityHeadRotationClass;

        private static Object playerInfoActionEnumAdd;

        private static Object playerInfoActionEnumRemove;

        static {
            craftItemStackClass = Reflection.getCraftClass("inventory.CraftItemStack");
            itemStackClass = Reflection.getNMSClass("ItemStack");
            asNMSCopy = Reflection.getMethod(craftItemStackClass, "asNMSCopy", new Class[] { ItemStack.class });
            enumItemSlotClass = Reflection.getNMSClass("EnumItemSlot");
            itemSlotEnumMainHand = enumItemSlotClass.getEnumConstants()[0];
            itemSlotEnumOffHand = enumItemSlotClass.getEnumConstants()[1];
            itemSlotEnumFeet = enumItemSlotClass.getEnumConstants()[2];
            itemSlotEnumLegs = enumItemSlotClass.getEnumConstants()[3];
            itemSlotEnumChest = enumItemSlotClass.getEnumConstants()[4];
            itemSlotEnumHead = enumItemSlotClass.getEnumConstants()[5];
            setEquipment = Reflection.getMethod(entityPlayerClass, "setEquipment", new Class[] { enumItemSlotClass, itemStackClass });
            packetPlayOutPlayerInfoClass = Reflection.getNMSClass("PacketPlayOutPlayerInfo");
            enumPlayerInfoActionClass = Reflection.getNMSClass("PacketPlayOutPlayerInfo$EnumPlayerInfoAction");
            packetPlayOutEntityHeadRotationClass = Reflection.getNMSClass("PacketPlayOutEntityHeadRotation");
            playerInfoActionEnumAdd = enumPlayerInfoActionClass.getEnumConstants()[0];
            playerInfoActionEnumRemove = enumPlayerInfoActionClass.getEnumConstants()[4];
            entityHumanClass = Reflection.getNMSClass("EntityHuman");
            packetPlayOutNamedEntitySpawnClass = Reflection.getNMSClass("PacketPlayOutNamedEntitySpawn");
            packetPlayOutEntityEquipmentClass = Reflection.getNMSClass("PacketPlayOutEntityEquipment");
        }

        public static void setRotation(Object entity, Player player, float yaw) {
            try {
                Constructor<?> packetPlayOutEntityHeadRotationConstructor = packetPlayOutEntityHeadRotationClass.getConstructor(new Class[] { entityClass, byte.class });
                Object packetPlayOutEntityHeadRotation = packetPlayOutEntityHeadRotationConstructor.newInstance(new Object[] { entity, Byte.valueOf(getFixRotation(yaw)) });
                Packets.sendPacket(player, packetPlayOutEntityHeadRotation);
            } catch (NoSuchMethodException|InstantiationException|IllegalAccessException|java.lang.reflect.InvocationTargetException e) {
                e.printStackTrace();
            }
        }

        public static void teleport(Object entity, Location location) {
            try {
                setLocation.invoke(entity, new Object[] { Double.valueOf(location.getX()), Double.valueOf(location.getY()), Double.valueOf(location.getZ()), Float.valueOf(location.getYaw()), Float.valueOf(location.getPitch()) });
            } catch (IllegalAccessException|java.lang.reflect.InvocationTargetException e) {
                e.printStackTrace();
            }
        }

        private static byte getFixRotation(float yawpitch) {
            return (byte)(int)(yawpitch * 256.0F / 360.0F);
        }

        public static int getEntityID(Object entity) {
            return ((Integer)Reflection.invokeMethod(entityPlayerClass, "getId", entity)).intValue();
        }

        public static void show(Object entity, Player player) {
            try {
                Object[] entities = (Object[])Array.newInstance(entityPlayerClass, 1);
                entities[0] = entity;
                Constructor<?> packetPlayOutPlayerInfoConstructor = packetPlayOutPlayerInfoClass.getConstructor(new Class[] { enumPlayerInfoActionClass, entities.getClass() });
                Object packetPlayOutPlayerInfo = packetPlayOutPlayerInfoConstructor.newInstance(new Object[] { playerInfoActionEnumAdd, entities });
                Packets.sendPacket(player, packetPlayOutPlayerInfo);
                Constructor<?> packetPlayOutNamedEntitySpawnConstructor = packetPlayOutNamedEntitySpawnClass.getConstructor(new Class[] { entityHumanClass });
                Object packetPlayOutNamedEntitySpawn = packetPlayOutNamedEntitySpawnConstructor.newInstance(new Object[] { entity });
                Packets.sendPacket(player, packetPlayOutNamedEntitySpawn);
            } catch (InstantiationException|java.lang.reflect.InvocationTargetException|IllegalAccessException|NoSuchMethodException e) {
                e.printStackTrace();
            }
        }

        public static void hide(Object entity, Player player) {
            try {
                Object[] entities = (Object[])Array.newInstance(entityPlayerClass, 1);
                entities[0] = entity;
                Constructor<?> packetPlayOutPlayerInfoConstructor = packetPlayOutPlayerInfoClass.getConstructor(new Class[] { enumPlayerInfoActionClass, entities.getClass() });
                Object packetPlayOutPlayerInfo = packetPlayOutPlayerInfoConstructor.newInstance(new Object[] { playerInfoActionEnumRemove, entities });
                Packets.sendPacket(player, packetPlayOutPlayerInfo);
            } catch (InstantiationException|java.lang.reflect.InvocationTargetException|IllegalAccessException|NoSuchMethodException e) {
                e.printStackTrace();
            }
        }

        private static Object getCraftItemStack(ItemStack itemStack) {
            try {
                return asNMSCopy.invoke(craftItemStackClass, new Object[] { itemStack });
            } catch (IllegalAccessException|java.lang.reflect.InvocationTargetException e) {
                e.printStackTrace();
                return null;
            }
        }

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
            try {
                Object craftItem = getCraftItemStack(item);
                Constructor<?> packetPlayOutEntityEquipmentConstructor = packetPlayOutEntityEquipmentClass.getConstructor(new Class[] { int.class, enumItemSlotClass, itemStackClass });
                Object packetPlayOutEntityEquipment = packetPlayOutEntityEquipmentConstructor.newInstance(new Object[] { Integer.valueOf(getEntityID(entity)), oSlot, craftItem });
                Packets.sendPacket(player, packetPlayOutEntityEquipment);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        public static Object create(NPC npc) {
            try {
                Object craftWorld = craftWorldClass.cast(npc.getLocation().getWorld());
                Object worldHandle = Reflection.getHandle(craftWorld);
                playerInteractManagerConstructor = playerInteractManagerClass.getConstructor(new Class[] { worldServerClass });
                Object craftServer = craftServerClass.cast(Bukkit.getServer());
                Object minecraftServer = Reflection.invokeMethod(craftServerClass, "getServer", craftServer);
                Object playerInteractManager = playerInteractManagerConstructor.newInstance(new Object[] { worldHandle });
                entityConstructor = entityPlayerClass.getConstructor(new Class[] { minecraftServerClass, worldServerClass, GameProfile.class, playerInteractManagerClass });
                Object entityPlayer = entityConstructor.newInstance(new Object[] { minecraftServer, worldHandle, npc.getGameProfile(), playerInteractManager });
                return entityPlayer;
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }
    }
}
