package me.bimmr.bimmcore.items;

import com.shampaggon.crackshot.CSUtility;
import org.bukkit.inventory.ItemStack;

/**
 * Created by Randy on 4/11/2016.
 * Class created so Crackshot is not needed to use the Items class
 */
public class Items_Crackshot {

    public static ItemStack getGunItemStack(String code) {
        CSUtility cs = new CSUtility();
        return cs.generateWeapon(code);
    }

    public static String getGunName(ItemStack itemStack) {
        CSUtility cs = new CSUtility();
        if (cs.getWeaponTitle(itemStack) != null)
            return "gun:" + cs.getWeaponTitle(itemStack);
        return null;
    }
}
