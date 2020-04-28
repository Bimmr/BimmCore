package me.bimmr.bimmcore.gui.inventory.helpers;

import me.bimmr.bimmcore.BimmCore;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.tags.CustomItemTagContainer;
import org.bukkit.persistence.PersistentDataType;

import java.util.UUID;

public class SingleClickEventUtil {

    private static NamespacedKey key = new NamespacedKey(BimmCore.getInstance(), "bimmcore");

    /**
     * Add UUIDTag to itemstack
     *
     * @param id   The UUID
     * @param item The ItemStack
     * @return The adjusted ItemStack
     */
    @SuppressWarnings("deprecation")
    public static ItemStack addUUIDTag(UUID id, ItemStack item) {
        UUIDItemTagType uUIDItemTagType = new UUIDItemTagType();
        ItemMeta im = item.getItemMeta();
        if (BimmCore.supports(14))
            im.getPersistentDataContainer().set(key, PersistentDataType.STRING, id.toString());
        else
            im.getCustomTagContainer().setCustomTag(key, uUIDItemTagType, id);
        item.setItemMeta(im);
        return item;
    }

    /**
     * @param itemStack The ItemStack
     * @return Get the UUID From the ItemStack
     */
    @SuppressWarnings("deprecation")
    public static UUID getUUIDFromTag(ItemStack itemStack) {
        UUIDItemTagType uUIDItemTagType = new UUIDItemTagType();
        ItemMeta itemMeta = itemStack.getItemMeta();
        if (BimmCore.supports(14)) {
            if (itemMeta.getPersistentDataContainer().has(key, PersistentDataType.STRING))
                return UUID.fromString(itemMeta.getPersistentDataContainer().get(key, PersistentDataType.STRING));

        } else {
            CustomItemTagContainer tagContainer = itemMeta.getCustomTagContainer();
            if (tagContainer.hasCustomTag(key, uUIDItemTagType)) {
                UUID id = tagContainer.getCustomTag(key, uUIDItemTagType);
                return id;
            }
        }
        return null;
    }
}
