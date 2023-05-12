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
 * The type Anvil.
 */
@Deprecated
public class Anvil {

    private String title, defaultText;
    private AnvilFinishEvent anvilFinishEvent;

    /**
     * Instantiates a new Anvil.
     *
     * @param title       the title
     * @param defaultText the default text
     * @param finishEvent the finish event
     */
    public Anvil(String title, String defaultText, AnvilFinishEvent finishEvent) {
        this.title = title;
        this.anvilFinishEvent = finishEvent;
    }

    /**
     * Open.
     *
     * @param player the player
     */
    public void open(Player player) {
        Inventory inv = AnvilAPI.getInventory(player);
        ItemStack paper = new ItemStack(Material.PAPER);
        ItemMeta paperMeta = paper.getItemMeta();
        paperMeta.setDisplayName(this.defaultText);
        paper.setItemMeta(paperMeta);
        inv.setItem(0, paper);
        player.openInventory(inv);
    }

    /**
     * Gets title.
     *
     * @return the title
     */
    public String getTitle() {
        return this.title;
    }


    /**
     * The type Anvil api.
     */
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
                nmsContainerAccess = Reflection.getNMSClass("ContainerAccess");
                nmsContainers = Reflection.getNMSClass("Containers");

                conContainerAnvil = Reflection.getConstructor(nmsContainerAnvil, int.class, nmsPlayerInventory, nmsContainerAccess);

        }

        /**
         * Gets inventory.
         *
         * @param player the player
         * @return the inventory
         */
        public static Inventory getInventory(Player player) {
            Object playerHandler = Reflection.getHandle(player);

                Method metContainerAt = Reflection.getMethod(nmsContainerAccess, "at", nmsWorld, nmsBlockPosition);

                Object objInventory = Reflection.get("inventory", playerHandler);

                Object objWorld = Reflection.get("world", playerHandler);
                Object objContainerAccess = Reflection.invokeMethod(metContainerAt, nmsContainerAccess, objWorld);

                Object objBlockPosition = Reflection.newInstance(conBlockPosition, 0, 0, 0);

                Object objContainerAnvil = Reflection.newInstance(conContainerAnvil, 9, objInventory, objContainerAccess, objBlockPosition);
                Reflection.setField(nmsContainer, "checkReachable", objContainerAnvil, false);

                Object objBukkitView = Reflection.invokeMethod("getBukkitView", objContainerAnvil);
                return (Inventory) Reflection.invokeMethod("getTopInventory", objBukkitView);

        }
    }
}
