package me.bimmr.bimmcore.items.helpers;

import com.shampaggon.crackshot.CSUtility;
import me.bimmr.bimmcore.items.Items;
import org.bukkit.inventory.ItemStack;

/**
 * CrackShot support for {@link Items}
 */
public class Items_Crackshot {

    public static ItemStack getGunItemStack(String code) {
        CSUtility cs = new CSUtility();
        return cs.generateWeapon(code.replaceAll("_", " "));
    }

    public static String getGunName(ItemStack itemStack) {
        CSUtility cs = new CSUtility();
        if (cs.getWeaponTitle(itemStack) != null)
            return "gun:" + cs.getWeaponTitle(itemStack).replaceAll(" ", "_");
        return null;
    }
}