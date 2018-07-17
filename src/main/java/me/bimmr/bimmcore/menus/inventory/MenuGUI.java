package me.bimmr.bimmcore.menus.inventory;

import me.bimmr.bimmcore.StringUtil;
import me.bimmr.bimmcore.items.Items;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

public class MenuGUI {

    public static final int MAXITEMSPERPAGE = 54;
    public static final int MAXITEMSPERPAGEBORDERED = 28;
    public static ItemStack PREVIOUSPAGEITEM, NEXTPAGEITEM;

    private MenuGUIManager menuGUIManager;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ItemStack getBorderCorners() {
        return borderCorners;
    }

    public MenuGUIManager getMenuGUIManager() {
        return menuGUIManager;
    }

    public HashMap<Player, Integer> getPlayerPage() {
        return playerPage;
    }

    public ArrayList<ArrayList<ItemStack>> getPages() {
        return pages;
    }

    public ArrayList<Inventory> getInventories() {
        return inventories;
    }

    public void setBorderCorners(ItemStack borderCorners) {
        this.borderCorners = borderCorners;
    }

    public ItemStack getBorderSides() {
        return borderSides;
    }

    public void setBorderSides(ItemStack borderSides) {
        this.borderSides = borderSides;
    }

    private String name;
    private ClickEvent clickEvent;
    private boolean centered;
    private boolean bordered;
    private ItemStack borderCorners, borderSides;
    private HashMap<Player, Integer> playerPage = new HashMap<>();

    private ArrayList<ArrayList<ItemStack>> pages = new ArrayList<>();
    private ArrayList<Inventory> inventories = new ArrayList<>();

    public MenuGUI(MenuGUIManager menuGUIManager, String name, ClickEvent clickEvent) {
        this.menuGUIManager = menuGUIManager;
        this.name = name;
        this.clickEvent = clickEvent;
        this.pages.add(new ArrayList<ItemStack>());

        if (PREVIOUSPAGEITEM == null)
            PREVIOUSPAGEITEM = new Items(new ItemStack(Material.SKULL_ITEM)).setDisplayName("" + ChatColor.YELLOW + ChatColor.BOLD + "<-").setSkullOwner("MHF_ArrowLeft").getItem();
        if (NEXTPAGEITEM == null)
            NEXTPAGEITEM = new Items(new ItemStack(Material.SKULL_ITEM)).setDisplayName("" + ChatColor.YELLOW + ChatColor.BOLD + "->").setSkullOwner("MHF_ArrowRight").getItem();

        menuGUIManager.menus.add(this);
    }

    /**
     * Set border items, and set bordered to true
     *
     * @param borderCorners the items at the corner of the GUI
     * @param borderSides   the items at the sides of the GUI
     * @return the MenuGUI
     */
    public MenuGUI border(ItemStack borderCorners, ItemStack borderSides) {
        this.borderCorners = format(borderCorners, ChatColor.GRAY + "");
        this.borderSides = format(borderSides, ChatColor.GRAY + "");
        this.bordered = true;

        return this;
    }

    /**
     * Format an itemstack
     *
     * @param itemstack the itemstack to format
     * @param name      the display name
     * @param lore      the lore
     * @return the modified itemstack
     */
    public ItemStack format(ItemStack itemstack, String name, String... lore) {
        ItemMeta im = itemstack.getItemMeta();
        if (im != null) {
            if (name != null)
                im.setDisplayName(StringUtil.addColor(name));
            if (lore != null) {
                ArrayList<String> lores = new ArrayList<String>();

                //If a lore has a ';' in it, split it into a new line
                for (String line : lore)
                    if (line.contains(";"))
                        for (String part : line.split(";"))
                            lores.add(StringUtil.addColor(part));
                    else
                        lores.add(StringUtil.addColor(line));

                im.setLore(Arrays.asList(lore));
            }
            itemstack.setItemMeta(im);
        }
        return itemstack;
    }

    /**
     * Format an itemstack
     *
     * @param itemstack the itemstack to format
     * @param name      the display name
     * @return the modified itemstack
     */
    public ItemStack format(ItemStack itemstack, String name) {
        return format(itemstack, name, null);
    }

    /**
     * Set the bordered boolean
     *
     * @param bordered if bordered or not
     * @return the MenuGUI
     */
    public MenuGUI setBordered(boolean bordered) {
        this.bordered = false;
        return this;
    }

    /**
     * Set centered to true
     *
     * @return the MenuGUI
     */
    public MenuGUI center() {
        this.centered = true;
        return this;
    }

    /**
     * Set the centered boolean
     *
     * @param centered if centered or not
     * @return the MenuGUI
     */
    public MenuGUI setCentered(boolean centered) {
        this.centered = centered;
        return this;
    }

    /**
     * Add an item to a specific page
     *
     * @param page      the page to modify
     * @param itemStack the itemstack to add
     * @return the MenuGUI
     */
    public MenuGUI addItem(int page, ItemStack itemStack) {
        if (pages.get(page) == null)
            pages.add(page, new ArrayList<ItemStack>());
        pages.get(page).add(itemStack);
        return this;
    }

    /**
     * Add an item to a specific page
     *
     * @param page      the page to modify
     * @param itemStack the itemstack to add
     * @param name      the custom name of the itemstack
     * @param lore      the custom lore of the itemstack
     * @return the MenuGUI
     */
    public MenuGUI addItem(int page, ItemStack itemStack, String name, String... lore) {
        return addItem(page, format(itemStack, name, lore));
    }

    /**
     * Add an item to the first available page
     *
     * @param itemStack the itemstack to add
     * @return the MenuGUI
     */
    public MenuGUI addItem(ItemStack itemStack) {
        int page = 0;

        while (pages.get(page) != null && (bordered && pages.get(page).size() >= MAXITEMSPERPAGEBORDERED) || (!bordered && pages.get(page).size() >= MAXITEMSPERPAGE))
            page++;
        addItem(page, itemStack);

        return this;
    }

    /**
     * Add an item to the first available page
     *
     * @param itemstack the itemstack to add
     * @param name      the custom name of the itemstack
     * @param lore      the custom lore of the itemstack
     * @return the MenuGUI
     */
    public MenuGUI addItem(ItemStack itemstack, String name, String... lore) {
        return addItem(format(itemstack, name, lore));
    }

    /**
     * Set an item to a specific page's slot
     *
     * @param page the page to modify
     * @param slot the slot to set the item to
     * @param item the itemstack to set
     * @return the MenuGUI
     */
    public MenuGUI setItem(int page, int slot, ItemStack item) {
        if (pages.get(page) == null)
            pages.add(page, new ArrayList<ItemStack>());
        pages.get(page).add(slot, item);
        return this;
    }

    /**
     * Set an item to a specific page's slot
     *
     * @param page the page to modify
     * @param slot the slot to set the item to
     * @param item the itemstack to set
     * @param name the name of the item
     * @param lore the lore of the item
     * @return the MenuGUI
     */
    public MenuGUI setItem(int page, int slot, ItemStack item, String name, String... lore) {
        return setItem(page, slot, format(item, name, lore));
    }

    /**
     * Set an item on the first page
     *
     * @param slot the slot to set the item
     * @param item the itemstack to set
     * @return the MenuGUI
     */
    public MenuGUI setItem(int slot, ItemStack item) {
        pages.get(0).set(slot, item);
        return this;
    }

    /**
     * Set an item on the first page
     *
     * @param slot the slot to set the item
     * @param item the itemstack to set
     * @param name the name of the item
     * @param lore the lore of the item
     * @return the MenuGUI
     */
    public MenuGUI setItem(int slot, ItemStack item, String name, String... lore) {
        return setItem(slot, format(item, name, lore));
    }

    /**
     * Build the menu(All Pages)
     */
    public MenuGUI build() {
        for (ArrayList<ItemStack> page : pages) {
            Inventory inv = Bukkit.createInventory(null, getRows(page.size()) * 9, name);

            if (bordered) {
                ArrayList<ItemStack> formatted = outline(page);
                inv.addItem((ItemStack[]) formatted.toArray());
            } else
                inv.addItem((ItemStack[]) page.toArray());

            inv.setItem(inv.getSize() - 9, PREVIOUSPAGEITEM);
            inv.setItem(inv.getSize(), NEXTPAGEITEM);
            inventories.add(inv);
        }
        return this;
    }

    public ArrayList<ItemStack> outline(ArrayList<ItemStack> items) {
        int rows = items.size() / 9;
        ArrayList<ItemStack> inventory = new ArrayList<>();
        for (int r = 0; r != rows; r++)
            for (int c = 0; c != 9; c++)
                if ((r == 0 || r + 1 == rows) && (c == 0 || c + 1 == 9))
                    inventory.add((9 * r) + c, borderCorners);
                else if (c == 0 || c + 1 == 9 || r == 0 || r + 1 == rows)
                    inventory.add((9 * r) + c, borderSides);
        return inventory;
    }

    /**
     * Open the inventory for the player
     *
     * @param player the player to open the menu for
     */
    public void open(Player player) {
        player.openInventory(inventories.get(0));
    }

    /**
     * Open the inventories specific page for the player
     *
     * @param page   the page to open
     * @param player the player to open the menu for
     */
    public void open(int page, Player player) {
        playerPage.put(player, page);
        player.openInventory(inventories.get(page));
    }

    /**
     * Open the next page in the inventory
     *
     * @param player the player to view the next page
     */
    public void openNextPage(Player player) {
        int current = playerPage.get(player);
        open(current + 1, player);
    }

    /**
     * Open the next page in the inventory
     *
     * @param player the player to view the next page
     */
    public void openPreviousPage(Player player) {
        int current = playerPage.get(player);
        open(current - 1, player);
    }

    /**
     * Get the current page of the player
     *
     * @param player the player
     * @return the page
     */
    public int getCurrentPage(Player player) {
        if (playerPage.containsKey(player))
            playerPage.put(player, 0);
        return playerPage.get(player);
    }

    /**
     * Dispose of the menu cleanly
     */
    public void destroy() {
        menuGUIManager.menus.remove(this);

        name = null;
        pages = null;
        inventories = null;
        borderSides = null;
        borderCorners = null;
        clickEvent = null;
    }

    /**
     * Get the number of rows that would be needed for the number of items specified
     *
     * @param numberOfItems
     * @return
     */
    public int getRows(int numberOfItems) {
        return (int) Math.ceil((numberOfItems / (bordered ? 7.0 : 9.0)) + 2);
    }

    public ClickEvent getClickEvent() {
        return clickEvent;
    }

    public void setClickEvent(ClickEvent clickEvent) {
        this.clickEvent = clickEvent;
    }
}
