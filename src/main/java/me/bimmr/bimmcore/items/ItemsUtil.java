package me.bimmr.bimmcore.items;

import java.util.ArrayList;
import java.util.List;
import org.bukkit.inventory.ItemStack;

public class ItemsUtil {
    public static ArrayList<String> getItemStacksToString(List<ItemStack> list) {
        ArrayList<String> items = new ArrayList<>();
        if (list != null && !list.isEmpty())
            for (ItemStack item : list)
                items.add((new Items(item)).toString());
        return items;
    }

    public static ArrayList<String> getItemsToString(List<Items> list) {
        ArrayList<String> items = new ArrayList<>();
        if (list != null && !list.isEmpty())
            for (Items item : list)
                items.add(items.toString());
        return items;
    }

    public static ArrayList<ItemStack> getItemStacks(List<String> list) {
        ArrayList<ItemStack> items = new ArrayList<>();
        if (list != null && !list.isEmpty())
            for (String string : list)
                items.add((new Items(string)).getItem());
        return items;
    }
}
