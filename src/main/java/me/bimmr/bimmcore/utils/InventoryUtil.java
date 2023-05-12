package me.bimmr.bimmcore.utils;

import org.bukkit.Material;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;


/**
 * The type Inventory util.
 */
public class InventoryUtil {


    /**
     * Contains item boolean.
     *
     * @param inventory the inventory
     * @param itemStack the item stack
     * @param quantity  the quantity
     * @return the boolean
     */
    public static boolean containsItem(Inventory inventory, ItemStack itemStack, int quantity) {
        int found = 0;

        //Loop through the inventory, unless we found all the needed items already
        for (int i = 0; i < inventory.getContents().length && found < quantity; i++) {
            ItemStack stack = inventory.getItem(i);

            //Make sure that the item in the inventory slot is a match
            if (stack != null && itemStack.isSimilar(stack)) {
                int amount = stack.getAmount();
                found += amount;
            }
        }
        return found >= quantity;
    }

    /**
     * Remove item boolean.
     *
     * @param inventory the inventory
     * @param itemStack the item stack
     * @param quantity  the quantity
     * @return the boolean
     */
    public static boolean removeItem(Inventory inventory, ItemStack itemStack, int quantity) {

        //Loop through the inventory, unless we removed all of the item already
        for (int i = inventory.getContents().length - 1; i >= 0 && quantity > 0; i--) {
            ItemStack stack = inventory.getItem(i);

            //Make sure that the item in the inventory slot is a match
            if (stack != null && itemStack.isSimilar(stack)) {
                int amount = stack.getAmount();

                //If the amount in the stack is less than or equal to the amount to remove, remove what is there, and update values to what was removed
                if (amount <= quantity) {
                    inventory.setItem(i, new ItemStack(Material.AIR));
                    quantity -= amount;
                }
                //If the amount in the stack is more than the amount to remove, remove only what else is needed
                else {
                    stack.setAmount(stack.getAmount() - quantity);
                    quantity -= quantity;
                }
            }
        }
        //Return a true or false depending on if we removed all 10
        return quantity <= 0;
    }
}
