package me.bimmr.bimmcore.utils;

import me.bimmr.bimmcore.items.Items;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

/**
 * Utilities class for {@link Items}
 */
public class ItemsUtil {

    /**
     * Get ArrayList of String from a List of ItemStack
     *
     * @param list List of ItemStacks
     * @return ArrayList of Strings
     */
    public static ArrayList<String> getItemStacksToString(List<ItemStack> list) {
        ArrayList<String> items = new ArrayList<>();
        if (list != null && !list.isEmpty())
            for (ItemStack item : list)
                items.add((new Items(item)).toString());
        return items;
    }

    /**
     * Get ArrayList of String from a List of {@link Items}
     *
     * @param list List of Items
     * @return ArrayList of Strings
     */
    public static ArrayList<String> getItemsToString(List<Items> list) {
        ArrayList<String> items = new ArrayList<>();
        if (list != null && !list.isEmpty())
            for (Items item : list)
                items.add(items.toString());
        return items;
    }

    /**
     * Get ArrayList of ItemStack from a List of Strings
     *
     * @param list List of Strings
     * @return ArrayList of ItemStacks
     */
    public static ArrayList<ItemStack> getItemStacks(List<String> list) {
        ArrayList<ItemStack> items = new ArrayList<>();
        if (list != null && !list.isEmpty())
            for (String string : list)
                items.add((new Items(string)).getItem());
        return items;
    }

    /**
     * Get ArrayList of {@link Items} from a List of Strings
     *
     * @param list List of Strings
     * @return ArrayList of Items
     */
    public static ArrayList<Items> getItems(List<String> list) {
        ArrayList<Items> items = new ArrayList<>();
        if (list != null && !list.isEmpty())
            for (String string : list)
                items.add((new Items(string)));
        return items;
    }
}
