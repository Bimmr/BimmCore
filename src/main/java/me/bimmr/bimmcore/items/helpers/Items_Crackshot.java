package me.bimmr.bimmcore.items.helpers;

import com.shampaggon.crackshot.CSUtility;
import me.bimmr.bimmcore.items.Items;
import org.bukkit.inventory.ItemStack;

/**
 * The type Items crackshot.
 */
public class Items_Crackshot {

    /**
     * Gets gun item stack.
     *
     * @param code the code
     * @return the gun item stack
     */
    public static ItemStack getGunItemStack(String code) {
        CSUtility cs = new CSUtility();
        return cs.generateWeapon(code.replaceAll("_", " "));
    }

    /**
     * Gets gun name.
     *
     * @param itemStack the item stack
     * @return the gun name
     */
    public static String getGunName(ItemStack itemStack) {
        CSUtility cs = new CSUtility();
        if (cs.getWeaponTitle(itemStack) != null)
            return "gun:" + cs.getWeaponTitle(itemStack).replaceAll(" ", "_");
        return null;
    }
}