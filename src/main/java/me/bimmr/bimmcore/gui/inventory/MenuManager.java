package me.bimmr.bimmcore.gui.inventory;

import me.bimmr.bimmcore.misc.Cooldown;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.UUID;

/**
 * The Manager for {@link Menu} classes
 */
public class MenuManager implements Listener {
    public static ArrayList<Menu> menus = new ArrayList<Menu>();
    private static Cooldown<UUID> cooldown = new Cooldown<>(1);

    /**
     * Unregister the Menu
     *
     * @param menu The Menu to unRegister
     */
    public static void unregister(Menu menu) {
        menus.remove(menu);
    }

    /**
     * Register the Menu
     *
     * @param menu the Menu to register
     */
    public static void register(Menu menu) {
        menus.add(menu);
    }

    /**
     * @param name The MenuGUI's name
     * @return The MenuGUI
     */
    public static Menu getMenuGUI(String name) {
        for (Menu menu : menus)
            if (menu.getName().equals(name))
                return menu;
        return null;
    }

    /**
     * @param inventory The Inventory
     * @return The MenuGUI
     */
    public static Menu getMenuGUI(Inventory inventory) {
        for (Menu menu : menus) {
            for (Inventory inv : menu.getInventories())
                if (inv == inventory)
                    return menu;
        }
        return null;
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onInventoryClose(InventoryCloseEvent event) {
        Menu menu = getMenuGUI(event.getInventory());
        if (menu == null)
            menu = getMenuGUI(event.getView().getTitle());
        if (menu != null) {
            if (menu.willDestroy())
                menu.destroy();
        }
    }

    /**
     * Event Handler for MenuGUIs
     *
     * @param event The InventoryClickEvent
     */
    @EventHandler(priority = EventPriority.HIGHEST)
    public void onInventoryClick(InventoryClickEvent event) {

        //Get MenuGUI
        Menu menu = getMenuGUI(event.getInventory());
        if (menu == null)
            menu = getMenuGUI(event.getView().getTitle());
        if (menu != null) {
            event.setCancelled(true);

            //Setup
            Player player = (Player) event.getWhoClicked();
            int position = event.getRawSlot();
            int page = menu.getCurrentPage(player);

            //Get Inventory from page
            Inventory inv = menu.getInventories().get(page);

            //If clicked in the MenuGUI
            if (position < inv.getContents().length && position >= 0) {

                //If clicked on a valid item
                if (inv.getContents()[position] != null) {
                    if (cooldown.isCooledDown(player.getUniqueId())) {
                        cooldown.addToCooldown(player.getUniqueId());

                        //Check if clicked on page navigation
                        if (page > 0 && position == inv.getSize() - 9) {
                            menu.setClose(true);
                            menu.setDestroy(false);
                            menu.openPreviousPage(player);
                        } else if (menu.getPages().size() > 1 && page < menu.getPages().size() - 1 && position == inv.getSize() - 1) {
                            menu.setClose(true);
                            menu.setDestroy(false);
                            menu.openNextPage(player);
                        } else if (isBorderItem(menu, inv.getContents()[position]))
                            ;//Do nothing if border item

                        else {
                            //Call ClickEvent
                            if (menu.getClickEvent() != null) {
                                menu.getClickEvent().setup((Player) event.getWhoClicked(), menu.getCurrentPage(player), position, inv.getContents()[position], event);
                                menu.getClickEvent().click();
                            }
                            //Call individual item's ClickEvent
                            if (menu.getClickEvent(inv.getContents()[position]) != null) {
                                menu.getClickEvent(inv.getContents()[position]).setup((Player) event.getWhoClicked(), menu.getCurrentPage(player), position, inv.getContents()[position], event);
                                menu.getClickEvent(inv.getContents()[position]).click();
                            }

                            //Update the player's inventory
                            player.updateInventory();

                            //Close if set to close on ClickEvent
                            if (menu.willClose())
                                player.closeInventory();

                            //Destroy if set to destroy on ClickEvent
                            if (menu.willClose() && menu.willDestroy())
                                menu.destroy();
                        }
                    }
                }
            }
        }

    }

    private boolean isBorderItem(Menu menu, ItemStack itemStack) {
        boolean isBorder = false;
        if (itemStack != null)
            if (itemStack.hasItemMeta() && itemStack.getItemMeta().hasDisplayName() && ChatColor.stripColor(itemStack.getItemMeta().getDisplayName()).length() == 0)
                isBorder = true;
            else if (menu.isBordered() && (menu.getBorderCorners().isSimilar(itemStack) || menu.getBorderSides().isSimilar(itemStack)))
                isBorder = true;
        return isBorder;
    }
}
