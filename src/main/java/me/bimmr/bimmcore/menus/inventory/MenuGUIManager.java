package me.bimmr.bimmcore.menus.inventory;

import me.bimmr.bimmcore.StringUtil;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.Plugin;

import java.util.ArrayList;

/**
 * Created by Randy on 3/5/2016.
 */


class MenuGUIManagerExample extends MenuGUIManager {

    public MenuGUIManagerExample(Plugin plugin) {
        super(plugin);

        //Create a new menu
        MenuGUI menu = new MenuGUI(this, "MenuGUI Name", new ClickEvent() {
            @Override
            public void click() {
                //Method gets called whenever an item gets clicked
                setClose(true);
                setDestroy(false);
            }
        })
                .center()
                .border(new ItemStack(Material.GOLD_BLOCK), new ItemStack(Material.DIAMOND_BLOCK));


        menu.addItem(new ItemStack(Material.DIAMOND), "Fancy Diamond", "Lore Can Go Here");
        menu.addItem(new ItemStack(Material.GOLD_INGOT), "Fancy Gold", "Lore Can Go Here");
        menu.addItem(new ItemStack(Material.IRON_INGOT), "Fancy Iron", "Lore Can Go Here");

        //Don't forget to build the GUI
        menu.build();
        //Player
        menu.open(null);
    }
}

public abstract class MenuGUIManager implements Listener {
    public ArrayList<MenuGUI> menus = new ArrayList<MenuGUI>();
    private Plugin plugin;

    public MenuGUIManager(Plugin plugin) {
        this.plugin = plugin;
        Bukkit.getPluginManager().registerEvents(this, plugin);
    }

    /**
     * Gets the MenuGUI that has that name
     *
     * @param name
     * @return
     */
    public MenuGUI getMenuGUI(String name) {
        for (MenuGUI menuGUI : menus)
            if (menuGUI.getName().equals(name))
                return menuGUI;
        return null;
    }

    public MenuGUI getMenuGUI(Inventory inventory) {
        for (MenuGUI menuGUI : menus)
            for (Inventory inv : menuGUI.getInventories())
                if (inv.equals(inventory))
                    return menuGUI;
        return null;
    }


    @EventHandler(priority = EventPriority.HIGHEST)
    public void onInventoryClick(InventoryClickEvent event) {
        MenuGUI menu = getMenuGUI(event.getInventory());
        if (menu != null) {
            event.setCancelled(true);
            Player player = (Player) event.getWhoClicked();
            int position = event.getRawSlot();

            int page = menu.getCurrentPage(player);
            Inventory inv = menu.getInventories().get(page);

            if (position < inv.getContents().length && position >= 0) {
                if (inv.getContents()[position] != null) {
                    if (inv.getContents()[position] == MenuGUI.PREVIOUSPAGEITEM)
                        menu.openPreviousPage(player);
                    else if (inv.getContents()[position] == MenuGUI.NEXTPAGEITEM)
                        menu.openNextPage(player);

                    else {
                        menu.getClickEvent().setup((Player) event.getWhoClicked(), menu.getCurrentPage(player), position, inv.getContents()[position], event);
                        menu.getClickEvent().click();
                        player.updateInventory();
                        if (menu.getClickEvent().willClose()) {
                            player.closeInventory();
                        }
                        if (menu.getClickEvent().willDestroy())
                            menu.destroy();
                    }
                }
            }
        }
    }

    /**
     * Gets how many rows are needed depending on the amount of items
     *
     * @param numberOfItems
     * @return
     */
    public static int getRows(int numberOfItems) {
        return (int) Math.ceil((numberOfItems / 7.0)) + 2;
    }

    /**
     * Creates an itemstack with the given values
     *
     * @param item
     * @param name
     * @param lore
     * @return
     */
    public static ItemStack setItemNameAndLore(ItemStack item, String name, String[] lore) {
        ItemMeta im = item.getItemMeta();

        //Set the meta if it has been given
        if (im != null) {
            if (name != null) im.setDisplayName(name);
            if (lore != null) {
                ArrayList<String> lores = new ArrayList<String>();

                //If a lore has a ';' in it, split it into a new line
                for (String line : lore)
                    if (line.contains(";"))
                        for (String part : line.split(";"))
                            lores.add(StringUtil.addColor(part));
                    else
                        lores.add(StringUtil.addColor(line));

                im.setLore(lores);
            }
            item.setItemMeta(im);
        }
        return item;
    }
}
