package me.bimmr.bimmcore.items.helpers;

import me.bimmr.bimmcore.items.Items;
import me.zombie_striker.qg.api.QualityArmory;
import me.zombie_striker.qg.guns.Gun;
import org.bukkit.inventory.ItemStack;

/**
 * The type Items quality armory.
 */
public class Items_QualityArmory {

    /**
     * Gets gun item stack.
     *
     * @param code the code
     * @return the gun item stack
     */
    public static ItemStack getGunItemStack(String code) {
        return QualityArmory.getGunByName(code.replaceAll("_", " ")).getItemStack();
    }

    /**
     * Gets gun name.
     *
     * @param itemStack the item stack
     * @return the gun name
     */
    public static String getGunName(ItemStack itemStack) {
        Gun gun;
        if ((gun = QualityArmory.getGun(itemStack)) != null)
            return "gun:" + gun.getName().replaceAll("_", " ");
        return null;
    }
}
