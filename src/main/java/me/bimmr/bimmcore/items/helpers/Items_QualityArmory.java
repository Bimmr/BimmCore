package me.bimmr.bimmcore.items.helpers;

import me.bimmr.bimmcore.items.Items;
import me.zombie_striker.qg.api.QualityArmory;
import me.zombie_striker.qg.guns.Gun;
import org.bukkit.inventory.ItemStack;

/**
 * QualityArmory support for {@link Items}
 */
public class Items_QualityArmory {

    public static ItemStack getGunItemStack(String code) {
        return QualityArmory.getGunByName(code.replaceAll("_", " ")).getItemStack();
    }

    public static String getGunName(ItemStack itemStack) {
        Gun gun;
        if ((gun = QualityArmory.getGun(itemStack)) != null)
            return "gun:" + gun.getName().replaceAll("_", " ");
        return null;
    }
}
