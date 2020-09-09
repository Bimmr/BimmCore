package me.bimmr.bimmcore.items.attributes;

import org.bukkit.Material;

/**
 * Attribute Default values &amp; Slots
 * Written by: StarTux
 */
public class AttributeDefaults {

    static double getDefaultArmor(Material material) {
        switch (material) {
            case LEATHER_HELMET:
                return 1;
            case LEATHER_CHESTPLATE:
                return 3;
            case LEATHER_LEGGINGS:
                return 2;
            case LEATHER_BOOTS:
                return 1;
            case GOLDEN_HELMET:
                return 2;
            case GOLDEN_CHESTPLATE:
                return 5;
            case GOLDEN_LEGGINGS:
                return 3;
            case GOLDEN_BOOTS:
                return 1;
            case CHAINMAIL_HELMET:
                return 2;
            case CHAINMAIL_CHESTPLATE:
                return 5;
            case CHAINMAIL_LEGGINGS:
                return 4;
            case CHAINMAIL_BOOTS:
                return 1;
            case IRON_HELMET:
                return 2;
            case IRON_CHESTPLATE:
                return 6;
            case IRON_LEGGINGS:
                return 5;
            case IRON_BOOTS:
                return 2;
            case DIAMOND_HELMET:
                return 3;
            case DIAMOND_CHESTPLATE:
                return 8;
            case DIAMOND_LEGGINGS:
                return 6;
            case DIAMOND_BOOTS:
                return 3;
            default:
                return 0;
        }
    }

    static double getDefaultArmorToughness(Material material) {
        switch (material) {
            case DIAMOND_HELMET:
                return 3;
            case DIAMOND_CHESTPLATE:
                return 8;
            case DIAMOND_LEGGINGS:
                return 6;
            case DIAMOND_BOOTS:
                return 3;
            default:
                return 0;
        }
    }

    static double getDefaultAttackDamage(Material material) {
        switch (material) {
            case WOODEN_SWORD:
                return 4;
            case GOLDEN_SWORD:
                return 4;
            case STONE_SWORD:
                return 5;
            case IRON_SWORD:
                return 6;
            case DIAMOND_SWORD:
                return 7;
            // Axes
            case WOODEN_AXE:
                return 7;
            case GOLDEN_AXE:
                return 7;
            case STONE_AXE:
                return 9;
            case IRON_AXE:
                return 9;
            case DIAMOND_AXE:
                return 9;
            // Pickaxes
            case WOODEN_PICKAXE:
                return 2;
            case GOLDEN_PICKAXE:
                return 2;
            case STONE_PICKAXE:
                return 3;
            case IRON_PICKAXE:
                return 4;
            case DIAMOND_PICKAXE:
                return 5;
            // Shovel
            case WOODEN_SHOVEL:
                return 2.5;
            case GOLDEN_SHOVEL:
                return 2.5;
            case STONE_SHOVEL:
                return 3.5;
            case IRON_SHOVEL:
                return 4.5;
            case DIAMOND_SHOVEL:
                return 5.5;
            // Hoe
            case WOODEN_HOE:
                return 1;
            case GOLDEN_HOE:
                return 1;
            case STONE_HOE:
                return 1;
            case IRON_HOE:
                return 1;
            case DIAMOND_HOE:
                return 1;
            default:
                return 0;
        }
    }

    static double getDefaultAttackSpeed(Material material) {
        switch (material) {
            case WOODEN_SWORD:
            case GOLDEN_SWORD:
            case STONE_SWORD:
            case IRON_SWORD:
            case DIAMOND_SWORD:
                return 1.6;
            // Axes
            case WOODEN_AXE:
                return 0.8;
            case GOLDEN_AXE:
                return 1.0;
            case STONE_AXE:
                return 0.8;
            case IRON_AXE:
                return 0.9;
            case DIAMOND_AXE:
                return 1.0;
            // Pickaxes
            case WOODEN_PICKAXE:
                return 1.2;
            case STONE_PICKAXE:
                return 1.2;
            case GOLDEN_PICKAXE:
                return 1.2;
            case IRON_PICKAXE:
                return 1.2;
            case DIAMOND_PICKAXE:
                return 1.2;
            // Shovels
            case WOODEN_SHOVEL:
                return 1.0;
            case GOLDEN_SHOVEL:
                return 1.0;
            case STONE_SHOVEL:
                return 1.0;
            case IRON_SHOVEL:
                return 1.0;
            case DIAMOND_SHOVEL:
                return 1.0;
            // Hoes
            case WOODEN_HOE:
                return 1.0;
            case GOLDEN_HOE:
                return 1.0;
            case STONE_HOE:
                return 2.0;
            case IRON_HOE:
                return 3.0;
            case DIAMOND_HOE:
                return 4.0;
            default:
                return 0;
        }
    }

    public static Slot getEquipmentSlot(Material material)  {
        String itemName = material.name();
        if (itemName.contains("_HELMET")) {
            return Slot.HEAD;
        } else if (itemName.contains("_CHESTPLATE")) {
            return Slot.CHEST;
        } else if (itemName.contains("_LEGGINGS")) {
            return Slot.LEGS;
        } else if (itemName.contains("_BOOTS")) {
            return Slot.FEET;
        } else {
            return Slot.HAND;
        }
    }
}
