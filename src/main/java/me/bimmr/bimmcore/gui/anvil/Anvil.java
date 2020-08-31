package me.bimmr.bimmcore.gui.anvil;

import me.bimmr.bimmcore.BimmCore;
import me.bimmr.bimmcore.reflection.Reflection;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;

/**
 * The start of an AnvilGUI
 */
@Deprecated
public class Anvil {

    private String title, defaultText;
    private AnvilFinishEvent anvilFinishEvent;

    public Anvil(String title, String defaultText, AnvilFinishEvent finishEvent) {
        this.title = title;
        this.anvilFinishEvent = finishEvent;
    }

    public void open(Player player) {
        Inventory inv = AnvilAPI.getInventory(player);
        ItemStack paper = new ItemStack(Material.PAPER);
        ItemMeta paperMeta = paper.getItemMeta();
        paperMeta.setDisplayName(this.defaultText);
        paper.setItemMeta(paperMeta);
        inv.setItem(0, paper);
        player.openInventory(inv);
    }

    public String getTitle() {
        return this.title;
    }


    public static class AnvilAPI {

        private static Class<?> nmsBlockPosition = Reflection.getNMSClass("BlockPosition");
        private static Class<?> nmsPacketPlayOutOpenWindow = Reflection.getNMSClass("PacketPlayOutOpenWindow");
        private static Class<?> nmsContainerAnvil = Reflection.getNMSClass("ContainerAnvil");
        private static Class<?> nmsChatMessage = Reflection.getNMSClass("ChatMessage");
        private static Class<?> nmsEntityHuman = Reflection.getNMSClass("EntityHuman");
        private static Class<?> nmsWorld = Reflection.getNMSClass("World");
        private static Class<?> nmsPlayerInventory = Reflection.getNMSClass("PlayerInventory");
        private static Class<?> nmsContainer = Reflection.getNMSClass("Container");

        private static Constructor<?> conChatMessage = Reflection.getConstructor(nmsChatMessage, String.class, Object[].class);
        private static Constructor<?> conBlockPosition = Reflection.getConstructor(nmsBlockPosition, int.class, int.class, int.class);

        // Support 1.14
        private static Class<?> nmsContainerAccess;
        private static Class<?> nmsContainers;
        private static Constructor<?> conContainerAnvil;

        static {
            if (BimmCore.supports(14)) {
                nmsContainerAccess = Reflection.getNMSClass("ContainerAccess");
                nmsContainers = Reflection.getNMSClass("Containers");

                conContainerAnvil = Reflection.getConstructor(nmsContainerAnvil, int.class, nmsPlayerInventory, nmsContainerAccess);
            }
        }

        public static Inventory getInventory(Player player) {
            Object playerHandler = Reflection.getHandle(player);

            if (BimmCore.supports(14)) {
                Method metContainerAt = Reflection.getMethod(nmsContainerAccess, "at", nmsWorld, nmsBlockPosition);

                Object objInventory = Reflection.get("inventory", playerHandler);

                Object objWorld = Reflection.get("world", playerHandler);
                Object objContainerAccess = Reflection.invokeMethod(metContainerAt, nmsContainerAccess, objWorld);

                Object objBlockPosition = Reflection.newInstance(conBlockPosition, 0, 0, 0);

                Object objContainerAnvil = Reflection.newInstance(conContainerAnvil, 9, objInventory, objContainerAccess, objBlockPosition);
                Reflection.setField(nmsContainer, "checkReachable", objContainerAnvil, false);

                Object objBukkitView = Reflection.invokeMethod("getBukkitView", objContainerAnvil);
                return (Inventory) Reflection.invokeMethod("getTopInventory", objBukkitView);
            } else return null;
//
//
//                for(AnvilSlot AS : items.keySet()) inventory.setItem(AS.getSlot(), items.get(AS));
//
//                int ID = (Integer) NMSManager.invokeMethod("nextContainerCounter", P);
//
//                Object PC = NMSManager.getPlayerField(player, "playerConnection");
//                Object PPOOW = PacketPlayOutOpenWindow.getConstructor(int.class, Containers, NMSManager.getNMSClass("IChatBaseComponent")).newInstance(ID, NMSManager.getField(Containers, "ANVIL").get(Containers), CM.newInstance(ChatColor.translateAlternateColorCodes(colorchar, Title), new Object[]{}));
//
//                Method SP = NMSManager.getMethod("sendPacket", PC.getClass(), PacketPlayOutOpenWindow);
//                SP.invoke(PC, PPOOW);
//
//                Field AC = NMSManager.getField(EntityHuman, "activeContainer");
//                if(AC != null) {
//                    AC.set(P, CA);
//                    NMSManager.getField(NMSManager.getNMSClass("Container"), "windowId").set(AC.get(P), ID);
//                    NMSManager.getMethod("addSlotListener", AC.get(P).getClass(), P.getClass()).invoke(AC.get(P), P);
//                }
        }
    }
}
