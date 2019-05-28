package me.bimmr.bimmcore.items;

import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Randy on 3/4/2016.
 */
public class ItemsUtil {

    /**
     * Go through a list of <u>ItemStacks</u> and return a list of the <u>ItemStacks = Items#ToString</u>
     */
    public static ArrayList<String> getItemStacksToString(List<ItemStack> list) {
        ArrayList<String> items = new ArrayList<String>();
        if (list != null && !list.isEmpty())
            for (ItemStack item : list)
                items.add(new Items(item).toString());
        return items;
    }

    /**
     * Go through a list of <u>Items</u> and return a list of the <u>Items#ToString</u>
     *
     * @param list
     * @return
     */
    public static ArrayList<String> getItemsToString(List<Items> list) {
        ArrayList<String> items = new ArrayList<String>();
        if (list != null && !list.isEmpty())
            for (Items item : list)
                items.add(items.toString());
        return items;
    }

    /**
     * Go through a list of <u>Items#ToString</u> and return a list of <u>Items#getItem</u>
     */
    public static ArrayList<ItemStack> getItemStacks(List<String> list) {
        ArrayList<ItemStack> items = new ArrayList<ItemStack>();
        if ((list != null) && !list.isEmpty())
            for (String string : list)
                items.add(new Items(string).getItem());

        return items;
    }

}
