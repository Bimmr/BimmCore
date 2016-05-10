package me.bimmr.bimmcore.menus;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;

import java.util.ArrayList;

/**
 * Created by Randy on 3/5/2016.
 */


class Example extends MenuGUIManager {

    public Example(Plugin plugin) {
        super(plugin);

        //Create a new menu
        MenuGUI menu = new MenuGUI(this, "Menu Name", 3, new ClickEvent() {
            @Override
            public void click() {
                //Method gets called whenever an item gets clicked
                setClose(true);
                setDestroy(false);
            }
        },      //Corrner Item
                new ItemStack(Material.GOLD_BLOCK),
                //Side Item
                new ItemStack(Material.DIAMOND_BLOCK));


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
     * Gets the MenuGUI that has the same name
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

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onInventoryClick(InventoryClickEvent event) {
        MenuGUI menu = getMenuGUI(event.getInventory().getTitle());
        if (menu != null) {
            if (menu.getPlayer() == null || menu.getPlayer() == event.getWhoClicked()) {
                event.setCancelled(true);
                int position = event.getRawSlot();
                if (menu.getItems().length > position && position >= 0) {
                    if (menu.getItems()[position] != null) {
                        menu.getClickEvent().setup((Player) event.getWhoClicked(), position, menu.getItems()[position]);
                        final Player player = (Player) event.getWhoClicked();
                        menu.getClickEvent().click();
                        player.updateInventory();
                        if (menu.getClickEvent().willClose()) {
                            player.closeInventory();
                            //Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {

                            //  public void run() {
                            //}
                            //});
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
    public int getRows(int numberOfItems) {
        return (int) Math.ceil((numberOfItems / 7.0)) + 4;

    }
}