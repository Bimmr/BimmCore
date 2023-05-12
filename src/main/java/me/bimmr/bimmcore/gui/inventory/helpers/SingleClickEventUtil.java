package me.bimmr.bimmcore.gui.inventory.helpers;

import me.bimmr.bimmcore.BimmCore;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;

import java.util.UUID;

/**
 * The type Single click event util.
 */
public class SingleClickEventUtil {

    private static final NamespacedKey key = new NamespacedKey(BimmCore.getInstance(), "bimmcore");

    /**
     * Add uuid tag item stack.
     *
     * @param id   the id
     * @param item the item
     * @return the item stack
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
     * Gets uuid from tag.
     *
     * @param itemStack the item stack
     * @return the uuid from tag
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
