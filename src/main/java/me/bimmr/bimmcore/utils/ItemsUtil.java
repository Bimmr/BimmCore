package me.bimmr.bimmcore.utils;

import me.bimmr.bimmcore.items.Items;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

/**
 * The type Items util.
 */
public class ItemsUtil {

    /**
     * Gets item stacks to string.
     *
     * @param list the list
     * @return the item stacks to string
     */
    public static ArrayList<String> getItemStacksToString(List<ItemStack> list) {
        ArrayList<String> items = new ArrayList<>();
        if (list != null && !list.isEmpty())
            for (ItemStack item : list)
                items.add((new Items(item)).toString());
        return items;
    }

    /**
     * Gets items to string.
     *
     * @param list the list
     * @return the items to string
     */
    public static ArrayList<String> getItemsToString(List<Items> list) {
        ArrayList<String> items = new ArrayList<>();
        if (list != null && !list.isEmpty())
            for (Items item : list)
                items.add(items.toString());
        return items;
    }

    /**
     * Gets item stacks.
     *
     * @param list the list
     * @return the item stacks
     */
    public static ArrayList<ItemStack> getItemStacks(List<String> list) {
        ArrayList<ItemStack> items = new ArrayList<>();
        if (list != null && !list.isEmpty())
            for (String string : list)
                items.add((new Items(string)).getItem());
        return items;
    }

    /**
     * Gets items.
     *
     * @param list the list
     * @return the items
     */
    public static ArrayList<Items> getItems(List<String> list) {
        ArrayList<Items> items = new ArrayList<>();
        if (list != null && !list.isEmpty())
            for (String string : list)
                items.add((new Items(string)));
        return items;
    }
}
