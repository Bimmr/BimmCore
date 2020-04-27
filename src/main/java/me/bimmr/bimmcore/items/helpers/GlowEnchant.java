package me.bimmr.bimmcore.items.helpers;

import me.bimmr.bimmcore.BimmCore;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.enchantments.EnchantmentTarget;
import org.bukkit.enchantments.EnchantmentWrapper;
import org.bukkit.inventory.ItemStack;

import java.lang.reflect.Field;
import java.util.logging.Level;

public class GlowEnchant {

    public static Enchantment getGlowEnchantment() {
        if (BimmCore.supports(13))
            return EnchantGlow.getGlow();
        else {
            BimmCore.getInstance().getLogger().log(Level.INFO, "Due to old API, Custom Glow has been disabled");
            return null;
        }
    }

    private static class EnchantGlow extends EnchantmentWrapper {
        private static Enchantment glow;

        public EnchantGlow(String id) {
            super(id);
        }


        public static Enchantment getGlow() {
            if (glow != null)
                return glow;
            try {
                Field f = Enchantment.class.getDeclaredField("acceptingNew");
                f.setAccessible(true);
                f.set(null, Boolean.valueOf(true));
            } catch (Exception e) {
                e.printStackTrace();
            }
            glow = (Enchantment) new EnchantGlow("bimmcore_glow");
            try {
                Enchantment.registerEnchantment(glow);
            } catch (IllegalArgumentException illegalArgumentException) {
            }
            return glow;
        }

        public static boolean isGlow(ItemStack item) {
            return item.getEnchantments().containsKey(glow);
        }

        public boolean canEnchantItem(ItemStack item) {
            return true;
        }

        public boolean conflictsWith(Enchantment other) {
            return false;
        }

        public EnchantmentTarget getItemTarget() {
            return null;
        }

        public int getMaxLevel() {
            return 10;
        }

        public String getName() {
            return "Glow";
        }

        public int getStartLevel() {
            return 1;
        }
    }
}
