package me.bimmr.bimmcore.gui.inventory.helpers;

import me.bimmr.bimmcore.BimmCore;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;

import java.util.UUID;

/**
 * A Utilities class for adding a persistent uuid to the itemstack
 */
public class SingleClickEventUtil {

    private static final NamespacedKey key = new NamespacedKey(BimmCore.getInstance(), "bimmcore");

    /**
     * Add UUIDTag to itemstack
     *
     * @param id   The UUID
     * @param item The ItemStack
     * @return The adjusted ItemStack
     */
    public static ItemStack addUUIDTag(UUID id, ItemStack item) {
        if (item.hasItemMeta()) {
            ItemMeta im = item.getItemMeta();
            im.getPersistentDataContainer().set(key, PersistentDataType.STRING, id.toString());
            item.setItemMeta(im);
        }
        return item;
    }

    /**
     * @param itemStack The ItemStack
     * @return Get the UUID From the ItemStack
     */
    public static UUID getUUIDFromTag(ItemStack itemStack) {
        if (itemStack.hasItemMeta()) {
            ItemMeta itemMeta = itemStack.getItemMeta();
                if (itemMeta.getPersistentDataContainer().has(key, PersistentDataType.STRING))
                    return UUID.fromString(itemMeta.getPersistentDataContainer().get(key, PersistentDataType.STRING));


        }
        return null;
    }
}
