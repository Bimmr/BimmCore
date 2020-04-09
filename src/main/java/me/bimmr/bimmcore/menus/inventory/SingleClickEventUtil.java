package me.bimmr.bimmcore.menus.inventory;

import java.util.UUID;
import me.bimmr.bimmcore.BimmCore;
import me.bimmr.bimmcore.UUIDItemTagType;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.tags.CustomItemTagContainer;
import org.bukkit.inventory.meta.tags.ItemTagType;
import org.bukkit.plugin.Plugin;

public class SingleClickEventUtil {
    private static NamespacedKey key = new NamespacedKey((Plugin)BimmCore.getInstance(), "bimmcore");

    public static ItemStack addUUIDTag(UUID id, ItemStack item) {
        UUIDItemTagType uUIDItemTagType = new UUIDItemTagType();
        ItemMeta im = item.getItemMeta();
        im.getCustomTagContainer().setCustomTag(key, (ItemTagType)uUIDItemTagType, id);
        item.setItemMeta(im);
        return item;
    }

    public static UUID getUUIDFromTag(ItemStack itemStack) {
        UUIDItemTagType uUIDItemTagType = new UUIDItemTagType();
        ItemMeta itemMeta = itemStack.getItemMeta();
        CustomItemTagContainer tagContainer = itemMeta.getCustomTagContainer();
        if (tagContainer.hasCustomTag(key, (ItemTagType)uUIDItemTagType)) {
            UUID id = (UUID)tagContainer.getCustomTag(key, (ItemTagType)uUIDItemTagType);
            return id;
        }
        return null;
    }
}
