package me.bimmr.bimmcore.items;

import me.zombie_striker.qg.api.QualityArmory;
import me.zombie_striker.qg.guns.Gun;
import org.bukkit.inventory.ItemStack;

public class Items_QualityArmory {
    public static ItemStack getGunItemStack(String code) {
        return QualityArmory.getGunByName(code).getItemStack();
    }

    public static String getGunName(ItemStack itemStack) {
        Gun gun;
        if ((gun = QualityArmory.getGun(itemStack)) != null)
            return "gun:" + gun.getName();
        return null;
    }
}
