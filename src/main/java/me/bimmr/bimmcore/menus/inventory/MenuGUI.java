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
import java.util.Map;

public class MenuGUI {

    public static final int MAXITEMSPERPAGE = 45;
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

    public HashMap<String, Integer> getPlayerPage() {
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
    private int size = -1;
    private ItemStack borderCorners, borderSides;
    private HashMap<String, Integer> playerPage = new HashMap<>();
    private HashMap<Integer[], ItemStack> toSetItems = new HashMap<>();

    private ArrayList<ArrayList<ItemStack>> pages = new ArrayList<>();
    private ArrayList<Inventory> inventories = new ArrayList<>();
    private HashMap<Integer, Integer> pageSlot = new HashMap<>();

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

    public MenuGUI(MenuGUIManager menuGUIManager, String name, int size, ClickEvent clickEvent, ItemStack borderCorners, ItemStack borderSides) {
        this(menuGUIManager, name, clickEvent);
        this.size = size;
        border(borderCorners, borderSides);
        center();
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
        if (pages.size() <= page)
            pages.add(page, new ArrayList<ItemStack>());

        if (!pageSlot.containsKey(page))
            pageSlot.put(page, 0);

        int slot = pageSlot.get(page);

        pages.get(page).add(itemStack);

        pageSlot.put(page, slot + 1);
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

        while (page < pages.size() && ((bordered && pages.get(page).size() >= MAXITEMSPERPAGEBORDERED) || (!bordered && pages.get(page).size() >= MAXITEMSPERPAGE)))
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
        if (pages.size() <= page)
            pages.add(page, new ArrayList<ItemStack>());

        toSetItems.put(new Integer[]{page, slot}, item);
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
        return setItem(0, slot, item);
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
        for (int i = 0; i < pages.size(); i++) {

            ArrayList<ItemStack> items = pages.get(i);


            if (bordered && items.size() == 0)
                items.add(null);

            //Center the last row
            if (centered)
                if (pageSlot.containsKey(i))
                    items = centerLastRow(items, pageSlot.get(i));

            // Add a border if requested
            if (bordered)
                items = outline(items);


            //Set any items that need to be set
            for (Map.Entry<Integer[], ItemStack> e : toSetItems.entrySet()) {
                if (e.getKey()[0] == i) {
                    while (e.getKey()[1] > items.size() - 2)
                        items.add(null);

                    items.set(e.getKey()[1], e.getValue());
                }
            }


            Inventory inv = Bukkit.createInventory(null, size > -1 ? getRows(size, 9) * 9 : (getRows(items.size(), 9) + (bordered ? -1 : 1)) * 9, name);
            for (int position = 0; position < items.size(); position++) {
                if (items.get(position) != null)
                    inv.setItem(position, items.get(position));
            }

            if (i > 0)
                inv.setItem(inv.getSize() - 9, PREVIOUSPAGEITEM);
            if (i != pages.size() - 1)
                inv.setItem(inv.getSize() - 1, NEXTPAGEITEM);

            inventories.add(inv);
        }
        return this;
    }

    private ArrayList<ItemStack> centerLastRow(ArrayList<ItemStack> items, int lastAddPlace) {

        int count = lastAddPlace;
        int itemsPerRow = (bordered ? 7 : 9);
        int lastRowCount = count % itemsPerRow;
        int lastRowStart = count - lastRowCount;

        if (lastRowCount == 0)
            return items;


        int toShift = (itemsPerRow / 2) - (lastRowCount / 2);
        for (int i = 0; i < toShift; i++)
            items.add(lastRowStart, null);

        return items;
    }

    public ArrayList<ItemStack> outline(ArrayList<ItemStack> inventory) {
        int rows = getRows(inventory.size(), 7) + 2;
        for (int i = 0; i < 9; i++)
            inventory.add(null);
        for (int r = 0; r != rows; r++)
            for (int c = 0; c != 9; c++) {
                if ((r == 0 || r + 1 == rows) && (c == 0 || c + 1 == 9))
                    inventory.add((9 * r) + c, borderCorners);
                else if (c == 0 || c + 1 == 9 || r == 0 || r + 1 == rows)
                    inventory.add((9 * r) + c, borderSides);
            }
        return inventory;
    }

    /**
     * Open the inventory for the player
     *
     * @param player the player to open the menu for
     */
    public void open(Player player) {
        open(0, player);
    }

    /**
     * Open the inventories specific page for the player
     *
     * @param page   the page to open
     * @param player the player to open the menu for
     */
    public void open(int page, Player player) {
        playerPage.put(player.getName(), page);

        player.openInventory(inventories.get(page));
    }

    /**
     * Open the next page in the inventory
     *
     * @param player the player to view the next page
     */
    public void openNextPage(Player player) {
        int current = playerPage.get(player.getName());
        open(current + 1, player);
    }

    /**
     * Open the next page in the inventory
     *
     * @param player the player to view the next page
     */
    public void openPreviousPage(Player player) {
        int current = playerPage.get(player.getName());
        open(current - 1, player);
    }

    /**
     * Get the current page of the player
     *
     * @param player the player
     * @return the page
     */
    public int getCurrentPage(Player player) {
        if (!playerPage.containsKey(player.getName()))
            playerPage.put(player.getName(), 0);
        return playerPage.get(player.getName());
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
    public int getRows(int numberOfItems, double itemsPerRow) {
        return (int) Math.ceil(numberOfItems / itemsPerRow);
    }

    public ClickEvent getClickEvent() {
        return clickEvent;
    }

    public void setClickEvent(ClickEvent clickEvent) {
        this.clickEvent = clickEvent;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }
}
