package me.bimmr.bimmcore.items.helpers;

import me.bimmr.bimmcore.BimmCore;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.enchantments.EnchantmentTarget;
import org.bukkit.enchantments.EnchantmentWrapper;
import org.bukkit.inventory.ItemStack;

import java.lang.reflect.Field;

public class GlowEnchant {

    public static Enchantment getGlowEnchantment() {
        return EnchantGlow.getGlow();
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

    /*
    private static class EnchantGlowOld {

        public static ItemStack addGlow(ItemStack itemStack) {

            // net.minecraft.server.v1_15_R1.ItemStack nmsStack = CraftItemStack.asNMSCopy(itemStack);
            Class<?> craftItemStack = Reflection.getCraftClass("inventory.CraftItemStack");
            Method asNMSCopy = Reflection.getMethod(craftItemStack, "asNMSCopy", ItemStack.class);
            Object nmsStack = Reflection.invokeMethod(asNMSCopy, craftItemStack, itemStack);

//            NBTTagCompound tag = new NBTTagCompound();
            Class<?> nbtCompoundClass = Reflection.getNMSClass("NBTTagCompound");
            Constructor<?> nbtCompoundConstructor = Reflection.getConstructor(nbtCompoundClass);
            Object tag = Reflection.newInstance(nbtCompoundConstructor);


//            NBTTagList ench = new NBTTagList();
            Class<?> nbtTagListClass = Reflection.getNMSClass("NBTTagList");
            Constructor<?> nbtTagListConstructor = Reflection.getConstructor(nbtTagListClass);
            Object newNBTTagList = Reflection.newInstance(nbtTagListConstructor);

//            tag.set("Enchantments", ench);
            Class<?> nbtBaseClass = Reflection.getNMSClass("NBTBase");
            Method set = Reflection.getMethod(nbtCompoundClass, "set", String.class, nbtBaseClass);
//            Reflection.invokeMethod(set, tag, "Enchantments", newNBTTagList);
            Reflection.invokeMethod(set, tag, "ench", newNBTTagList);

//            nmsStack.setTag(tag);
            Method setTag = Reflection.getMethod(nmsStack.getClass(), "setTag", nbtCompoundClass);
            Reflection.invokeMethod(setTag, nmsStack, tag);

            //return CraftItemStack.asBukkitCopy(nmsStack);
            Method asBukkitCopy = Reflection.getMethod(craftItemStack, "asBukkitCopy", nmsStack.getClass());
            return (ItemStack) Reflection.invokeMethod(asBukkitCopy, null, nmsStack);
        }
    }
    */


}
