package me.bimmr.bimmcore.menus.inventory;

import me.bimmr.bimmcore.StringUtil;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;

/**
 * Created by Randy on 3/5/2016.
 */
public class MenuGUI {

    private MenuGUIManager menuGUIManager;

    private String      name;
    private int         size;
    private Player      player;
    private ItemStack[] items;
    private Inventory   inventory;
    private boolean centered = false;
    private int     slot     = 18;
    private int        numOfItems;
    private ClickEvent clickEvent;
    

    public MenuGUI(MenuGUIManager menuGUIManager, String name, int numOfItems, ClickEvent clickEvent, ItemStack cornerItem, ItemStack sideItem) {
        this.menuGUIManager = menuGUIManager;
        this.name = name;
        this.numOfItems = numOfItems;
        this.size = menuGUIManager.getRows(numOfItems) * 9;
        this.clickEvent = clickEvent;

        items = new ItemStack[size];

        inventory = Bukkit.createInventory(player, size, name);

        outline(this, cornerItem, sideItem);
        menuGUIManager.menus.add(this);
    }

    public Inventory getInventory() {
        return inventory;
    }

    /**
     * Get the name of the menu
     *
     * @return
     */
    public String getName() {
        return this.name;
    }

    /**
     * Get the player the menu is meant for
     *
     * @return
     */
    public Player getPlayer() {
        return this.player;
    }

    /**
     * Set the menu's player
     *
     * @param player
     * @return
     */
    public MenuGUI setPlayer(Player player) {
        this.player = player;
        return this;
    }

    /**
     * Get the click event
     *
     * @return
     */
    public ClickEvent getClickEvent() {
        return this.clickEvent;
    }

    /**
     * Get the items
     *
     * @return
     */
    public ItemStack[] getItems() {
        return this.items;
    }

    /**
     * Set an item to a specific position
     *
     * @param position
     * @param itemStack
     * @param name
     * @param lore
     */
    public MenuGUI setItem(int position, ItemStack itemStack, String name, String... lore) {
        setItem(position, setItemNameAndLore(itemStack, name, lore));
        return this;
    }

    /**
     * Set an item into a specific spots
     *
     * @param position
     * @param item
     */
    public MenuGUI setItem(int position, ItemStack item) {
        items[position] = item;
        return this;
    }


    public MenuGUI addItem(ItemStack itemStack, String name, String... lore) {
        addItem(setItemNameAndLore(itemStack, name, lore));
        return this;
    }

    /**
     * Add an item
     *
     * @param itemStack
     */
    public MenuGUI addItem(ItemStack itemStack) {
        numOfItems--;

        int rows = size / 9;

        while (getItems()[slot] != null)
            slot++;

        int currentrow = ((slot + 1) / 9);

        if (currentrow >= (rows - 3) && !centered) {
            slot = (slot + 3) - (numOfItems / 2);
            centered = true;
        }
        items[slot] = itemStack;
        slot++;
        return this;
    }

    /**
     * Builds the inventory
     *
     * @return
     */
    public MenuGUI build() {
        for (int position = 0; position < items.length; position++)
            if (items[position] != null)
                inventory.setItem(position, items[position]);
        return this;
    }

    /**
     * Opens the inventory, don't forget to build
     */
    public void open() {
        this.player.openInventory(inventory);
    }

    /**
     * Opens the inventory, don't forget to build
     */
    public void open(Player player) {
        player.openInventory(inventory);
    }

    /**
     * Creates an itemstack with the given values
     *
     * @param item
     * @param name
     * @param lore
     * @return
     */
    private ItemStack setItemNameAndLore(ItemStack item, String name, String[] lore) {
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

    public void destroy() {
        menuGUIManager.menus.remove(this);

        player = null;
        name = null;
        size = 0;
        items = null;
    }

    /**
     * Will outline the GUI with itemstacks
     *
     * @param menu
     * @param cornerItem
     * @param sideItem
     */
    private void outline(MenuGUI menu, ItemStack cornerItem, ItemStack sideItem) {
        int rows = size / 9;
        for (int r = 0; r != rows; r++)
            for (int c = 0; c != 9; c++)
                if ((r == 0 || r + 1 == rows) && (c == 0 || c + 1 == 9))
                    menu.setItem((9 * r) + c, cornerItem, ChatColor.GRAY + "");
                else if (c == 0 || c + 1 == 9 || r == 0 || r + 1 == rows)
                    menu.setItem((9 * r) + c, sideItem, ChatColor.GRAY + "");
    }

}