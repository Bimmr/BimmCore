package me.bimmr.bimmcore.gui.inventory;

import me.bimmr.bimmcore.BimmCore;
import me.bimmr.bimmcore.gui.inventory.helpers.SingleClickEventUtil;
import me.bimmr.bimmcore.items.Items;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * A Utilities class for creating a Inventory GUI
 */
public class Menu {
    /**
     * The constant MAXITEMSPERPAGE.
     */
    public static final int MAXITEMSPERPAGE = 45;
    /**
     * The constant MAXITEMSPERPAGEBORDERED.
     */
    public static final int MAXITEMSPERPAGEBORDERED = 28;

    /**
     * The constant PREVIOUSPAGEITEM.
     */
    public static ItemStack PREVIOUSPAGEITEM;
    /**
     * The constant NEXTPAGEITEM.
     */
    public static ItemStack NEXTPAGEITEM;

    static {
            NEXTPAGEITEM = (new Items(new ItemStack(Material.PLAYER_HEAD))).setDisplayName("" + ChatColor.YELLOW + ChatColor.BOLD + "->").setSkullOwner("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMTdiMDNiNzFkM2Y4NjIyMGVmMTIyZjk4MzFhNzI2ZWIyYjI4MzMxOWM3YjYyZTdkY2QyZDY0ZDk2ODIifX19").getItem();
            PREVIOUSPAGEITEM = (new Items(new ItemStack(Material.PLAYER_HEAD))).setDisplayName("" + ChatColor.YELLOW + ChatColor.BOLD + "<-").setSkullOwner("ewogICJ0aW1lc3RhbXAiIDogMTU4OTg5NTU1NjczNiwKICAicHJvZmlsZUlkIiA6ICI3NTE0NDQ4MTkxZTY0NTQ2OGM5NzM5YTZlMzk1N2JlYiIsCiAgInByb2ZpbGVOYW1lIiA6ICJUaGFua3NNb2phbmciLAogICJzaWduYXR1cmVSZXF1aXJlZCIgOiB0cnVlLAogICJ0ZXh0dXJlcyIgOiB7CiAgICAiU0tJTiIgOiB7CiAgICAgICJ1cmwiIDogImh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNGRjNDVmN2RmNTc4OTlhZTZiYzUzNzQ1MDk2OTc1MWJlYzgxZTQ5Mjk2Y2IwMTI0OWQ4MzQ2MDc2ZTNkMjllOCIKICAgIH0KICB9Cn0=").getItem();

    }

    private String name;
    private int size = -1;
    private ClickEvent clickEvent;

    private boolean centered;
    private boolean bordered;
    private ItemStack borderCorners;
    private ItemStack borderSides;

    private HashMap<Integer, Integer> pageSlot = new HashMap<>();
    private HashMap<String, Integer> playerPage = new HashMap<>();
    private ArrayList<ArrayList<ItemStack>> pages = new ArrayList<>();
    private ArrayList<Inventory> inventories = new ArrayList<>();
    private HashMap<Integer[], ItemStack> toSetItems = new HashMap<>();

    private HashMap<String, ClickEvent> oldClickEvent = new HashMap<>();
    private HashMap<UUID, ClickEvent> clickEvents = new HashMap<>();
    private boolean close = true;
    private boolean destroy = true;

    /**
     * Create a MenuGUI
     * Size is automatic, ClickEvent is null, No borders, No corners
     *
     * @param name The menu's name
     */
    public Menu(String name) {
        this(name, -1, (ClickEvent)null, null, null);
    }

    /**
     * Create a MenuGUI
     * ClickEvent is null, No borders, No corners
     *
     * @param name The menu's name
     * @param size The menu's size (-1 will make it automatic)
     */
    public Menu(String name, int size) {
        this(name, size, (ClickEvent)null, null, null);
    }

    /**
     * Create a MenuGUI
     * Size is automatic, No borders, No corners
     *
     * @param name       The menu's name
     * @param menuClick The ClickEvent
     */
    public Menu(String name, MenuClick menuClick) {
        this(name, -1, new ClickEvent() {
            @Override
            public void click() {
                menuClick.click(this);
            }
        }, null, null);
    }
    /**
     * Create a MenuGUI
     * Size is automatic, No borders, No corners
     *
     * @param name       The menu's name
     * @param clickEvent The ClickEvent
     */
    public Menu(String name, ClickEvent clickEvent) {
        this(name, -1, clickEvent, null, null);
    }

    /**
     * Create a MenuGUI
     * No borders, No corners
     *
     * @param name       The menu's name
     * @param size       The Size
     * @param menuClick The ClickEvent
     */
    public Menu(String name, int size, MenuClick menuClick) {
        this(name, size, new ClickEvent() {
            @Override
            public void click() {
                menuClick.click(this);
            }
        }, null, null);
    }
    /**
     * Create a MenuGUI
     * No borders, No corners
     *
     * @param name       The menu's name
     * @param size       The Size
     * @param clickEvent The ClickEvent
     */
    public Menu(String name, int size, ClickEvent clickEvent) {
        this(name, size, clickEvent, null, null);
    }

    /**
     * Create a MenuGUI
     * Size is automatic, ClickEvent is null
     *
     * @param name          The menu's name
     * @param borderCorners The ItemStack for the corners
     * @param borderSides   The ItemStack for the borders
     */
    public Menu(String name, ItemStack borderCorners, ItemStack borderSides) {
        this(name, -1, (ClickEvent)null, borderCorners, borderSides);
    }

    /**
     * Create a MenuGUI
     * ClickEvent is null
     *
     * @param name          The menu's name
     * @param size          The menu's size (-1 will make it automatic)
     * @param borderCorners The ItemStack for the corners
     * @param borderSides   The ItemStack for the borders
     */
    public Menu(String name, int size, ItemStack borderCorners, ItemStack borderSides) {
        this(name, size, (ClickEvent)null, borderCorners, borderSides);
    }

    /**
     * Create a MenuGUI
     * Size is automatic
     *
     * @param name          The menu's name
     * @param menuClick    The ClickEvent
     * @param borderCorners The ItemStack for the corners
     * @param borderSides   The ItemStack for the borders
     */
    public Menu(String name, MenuClick menuClick, ItemStack borderCorners, ItemStack borderSides) {
        this(name, -1, new ClickEvent() {
            @Override
            public void click() {
                menuClick.click(this);
            }
        }, borderCorners, borderSides);
    }
    /**
     * Create a MenuGUI
     * Size is automatic
     *
     * @param name          The menu's name
     * @param clickEvent    The ClickEvent
     * @param borderCorners The ItemStack for the corners
     * @param borderSides   The ItemStack for the borders
     */
    public Menu(String name, ClickEvent clickEvent, ItemStack borderCorners, ItemStack borderSides) {
        this(name, -1, clickEvent, borderCorners, borderSides);
    }

    /**
     * Create a MenuGUI
     *
     * @param name          The menu's name
     * @param size          The menu's size (-1 will make it automatic)
     * @param menuClick    The ClickEvent
     * @param borderCorners The ItemStack for the corners
     * @param borderSides   The ItemStack for the borders
     */
    public Menu(String name, int size, MenuClick menuClick, ItemStack borderCorners, ItemStack borderSides) {
        this(name, size, new ClickEvent() {
            @Override
            public void click() {
                menuClick.click(this);
            }
        }, borderCorners, borderSides);
    }
    /**
     * Create a MenuGUI
     *
     * @param name          The menu's name
     * @param size          The menu's size (-1 will make it automatic)
     * @param clickEvent    The ClickEvent
     * @param borderCorners The ItemStack for the corners
     * @param borderSides   The ItemStack for the borders
     */
    public Menu(String name, int size, ClickEvent clickEvent, ItemStack borderCorners, ItemStack borderSides) {
        this.name = name;
        this.size = size;
        this.pages.add(new ArrayList<>());

        if (clickEvent != null)
            setClickEvent(clickEvent);

        border(borderCorners, borderSides);
        MenuManager.register(this);
    }

    public boolean willClose() {
        return close;
    }

    @Deprecated
    public void setCloseOnClick(boolean close) {
        setClose(close);
    }

    public Menu setClose(boolean close) {
        this.close = close;
        return this;
    }

    public boolean willDestroy() {
        return destroy;
    }

    @Deprecated
    public void setDestroyOnClick(boolean destroy) {
        setDestroy(destroy);
    }

    public Menu setDestroy(boolean destroy) {
        this.destroy = destroy;
        return this;
    }

    /**
     * Gets name.
     *
     * @return Get The MenuGUI's name
     */
    public String getName() {
        return this.name;
    }

    /**
     * Set the MenuGUI's name
     *
     * @param name The Menu's name
     * @return The MenuGUI
     */
    public Menu setName(String name) {
        this.name = name;
        return this;
    }

    /**
     * Gets border corners.
     *
     * @return Get the border corner ItemStack
     */
    public ItemStack getBorderCorners() {
        return this.borderCorners;
    }

    /**
     * Set the border corner's ItemStack
     *
     * @param borderCorners The ItemStack for the border corners
     * @return The MenuGUI
     */
    public Menu setBorderCorners(ItemStack borderCorners) {
        this.borderCorners = borderCorners;
        if (this.borderCorners != null)
            setBordered(true);
        return this;
    }

    public void clear() {
        pageSlot = new HashMap<>();
        playerPage = new HashMap<>();
        pages = new ArrayList<>();
        inventories = new ArrayList<>();
        toSetItems = new HashMap<>();

        oldClickEvent = new HashMap<>();
        clickEvents = new HashMap<>();

        this.pages.add(new ArrayList<>());
    }

    /**
     * Gets player page.
     *
     * @return Get the HashMap of page's each player is on
     */
    public HashMap<String, Integer> getPlayerPage() {
        return this.playerPage;
    }

    /**
     * Gets pages.
     *
     * @return Get the Pages for the MenuGUI
     */
    public ArrayList<ArrayList<ItemStack>> getPages() {
        return this.pages;
    }

    /**
     * Gets inventories.
     *
     * @return Get The Inventory's in the MenuGUI
     */
    public ArrayList<Inventory> getInventories() {
        return this.inventories;
    }

    /**
     * Gets border sides.
     *
     * @return Get the border's ItemStack
     */
    public ItemStack getBorderSides() {
        return this.borderSides;
    }

    /**
     * Set the border's ItemStack
     *
     * @param borderSides The ItemStack to set as the border
     * @return The MenuGUI
     */
    public Menu setBorderSides(ItemStack borderSides) {
        this.borderSides = borderSides;
        if (this.borderSides != null)
            setBordered(true);
        return this;
    }

    /**
     * Border the MenuGUI
     *
     * @param borderCorners The corner's ItemStack
     * @param borderSides   The side's ItemStack
     * @return The MenuGUI
     */
    public Menu border(ItemStack borderCorners, ItemStack borderSides) {
        //Validate Items

        if (borderCorners != null)
            this.borderCorners = new Items(borderCorners).setDisplayName(ChatColor.GRAY + "").getItem();
        if (borderSides != null)
            this.borderSides = new Items(borderSides).setDisplayName(ChatColor.GRAY + "").getItem();
        if (this.borderSides != null || this.borderCorners != null)
            this.bordered = true;
        return this;
    }

    /**
     * Center the MenuGUI's Items
     * Calls {@link #setCentered(boolean)}
     *
     * @return The MenuGUI
     */
    public Menu center() {
        return setCentered(true);
    }

    /**
     * Set the MenuGUI's centered value
     *
     * @param centered If the MenuGUI is centered
     * @return The MenuGUI
     */
    public Menu setCentered(boolean centered) {
        this.centered = centered;
        return this;
    }

    /**
     * Add an Item to the MenuGUI
     * Calls {@link #addItem(int, ItemStack, ClickEvent)}
     *
     * @param page      The page to add the Item to
     * @param itemStack The ItemStack to add
     * @return The MenuGUI
     */
    public Menu addItem(int page, ItemStack itemStack) {
        return addItem(page, itemStack, (ClickEvent)null);
    }

    /**
     * Add an Item to the MenuGUI
     *
     * @param page       The page to add the item to
     * @param itemStack  The ItemStack to add
     * @param menuClick The ClickedEvent
     * @return The MenuGUI
     */
    public Menu addItem(int page, ItemStack itemStack, MenuClick menuClick) {
        return addItem(page, itemStack, new ClickEvent() {
            @Override
            public void click() {
                menuClick.click(this);
            }
        });
    }
    /**
     * Add an Item to the MenuGUI
     *
     * @param page       The page to add the item to
     * @param itemStack  The ItemStack to add
     * @param clickEvent The ClickEvent
     * @return The MenuGUI
     */
    @Deprecated
    public Menu addItem(int page, ItemStack itemStack, ClickEvent clickEvent) {

        if (clickEvent != null) {
            clickEvent.menuObject = this;
                UUID id = UUID.randomUUID();
                itemStack = SingleClickEventUtil.addUUIDTag(id, itemStack);
                this.clickEvents.put(id, clickEvent);

        }
        while (page < this.pages.size() && ((this.bordered && (this.pages.get(page)).size() >= 28) || (!this.bordered && (this.pages.get(page)).size() >= 45)))
            page++;
        while (this.pages.size() < page)
            this.pages.add(new ArrayList<>());

        if (this.pages.size() <= page)
            this.pages.add(page, new ArrayList<>());
        if (!this.pageSlot.containsKey(page))
            this.pageSlot.put(page, 0);
        int slot = this.pageSlot.get(page);
        this.pages.get(page).add(itemStack);
        this.pageSlot.put(page, slot + 1);
        return this;
    }

    /**
     * Add a formatted Item
     * ClickEvent is null
     * Call {@link #addItem(int, ItemStack, ClickEvent)}
     *
     * @param page  The page to add the item to
     * @param items The ItemStack
     * @return The MenuGUI
     */
    public Menu addItem(int page, Items items) {
        return addItem(page, items.getItem(), (ClickEvent)null);
    }

    /**
     * Add a formatted Item
     * Page is 0, ClickEvent is null
     * Call {@link #addItem(int, ItemStack, ClickEvent)}
     *
     * @param items The ItemStack
     * @return The MenuGUI
     */
    public Menu addItem(Items items) {
        return addItem(0, items.getItem(), (ClickEvent)null);
    }

    /**
     * Add a formatted Item
     * Page is 0
     * Call {@link #addItem(int, ItemStack, ClickEvent)}
     *
     * @param items The ItemStack
     * @param menuClick  The MenuItemClick
     * @return The MenuGUI
     */
    public Menu addItem(Items items, MenuClick menuClick) {
        return addItem(0, items.getItem(), new ClickEvent() {
            @Override
            public void click() {
                menuClick.click(this);
            }
        });
    }

    /**
     * Add a formatted Item
     * Page is 0
     * Call {@link #addItem(int, ItemStack, ClickEvent)}
     *
     * @param items The ItemStack
     * @param clickEvent  The ClickEvent
     * @return The MenuGUI
     */
    public Menu addItem(Items items, ClickEvent clickEvent) {
        return addItem(0, items.getItem(), clickEvent);
    }

    /**
     * Add a formatted Item
     * Call {@link #addItem(int, ItemStack, ClickEvent)}
     *
     * @param page       The page to add the item to
     * @param items      The ItemStack
     * @param menuClick The ClickedEvent
     * @return The MenuGUI
     */
    public Menu addItem(int page, Items items, MenuClick menuClick) {
        return addItem(page, items, new ClickEvent() {
            @Override
            public void click() {
                menuClick.click(this);
            }
        });
    }
        /**
         * Add a formatted Item
         * Call {@link #addItem(int, ItemStack, ClickEvent)}
         *
         * @param page       The page to add the item to
         * @param items      The ItemStack
         * @param clickEvent The ClickEvent
         * @return The MenuGUI
         */
        public Menu addItem(int page, Items items, ClickEvent clickEvent) {
        return addItem(page, items.getItem(), clickEvent);
    }

    /**
     * Add an ItemStack
     * ClickEvent is null
     * Call {@link #addItem(int, ItemStack, ClickEvent)}
     *
     * @param itemStack The ItemStack
     * @return The MenuGUI
     */
    public Menu addItem(ItemStack itemStack) {
        addItem(0, itemStack, (ClickEvent)null);
        return this;
    }

    /**
     * Add an ItemStack
     * Call {@link #addItem(int, ItemStack, ClickEvent)}
     *
     * @param itemStack  The ItemStack
     * @param menuClick The ClickedEvent
     * @return The MenuGUI
     */
    public Menu addItem(ItemStack itemStack, MenuClick menuClick) {
        return addItem(itemStack, new ClickEvent() {
            @Override
            public void click() {
                menuClick.click(this);
            }
        });
    }
    /**
     * Add an ItemStack
     * Call {@link #addItem(int, ItemStack, ClickEvent)}
     *
     * @param itemStack  The ItemStack
     * @param clickEvent The ClickEvent
     * @return The MenuGUI
     */
    public Menu addItem(ItemStack itemStack, ClickEvent clickEvent) {
        addItem(0, itemStack, clickEvent);
        return this;
    }

    /**
     * Set an Item in the Menu
     *
     * @param page       The page
     * @param slot       The slot number
     * @param item       The ItemStack
     * @param menuClick The ClickEvent
     * @return The MenuGUI
     */
    public Menu setItem(int page, int slot, ItemStack item, MenuClick menuClick) {
        return setItem(page, slot, item, new ClickEvent() {
            @Override
            public void click() {
                menuClick.click(this);
            }
        });
    }
    /**
     * Set an Item in the Menu
     *
     * @param page       The page
     * @param slot       The slot number
     * @param item       The ItemStack
     * @param clickEvent The ClickEvent
     * @return The MenuGUI
     */
    public Menu setItem(int page, int slot, ItemStack item, ClickEvent clickEvent) {
        while (this.pages.size() < page) {
            this.pages.add(new ArrayList<>());
        }

        if (clickEvent != null) {
            clickEvent.menuObject = this;
                UUID id = UUID.randomUUID();
                item = SingleClickEventUtil.addUUIDTag(id, item);
                this.clickEvents.put(id, clickEvent);

        }
        this.toSetItems.put(new Integer[]{page, slot}, item);
        return this;
    }

    /**
     * Set an Item in the Menu
     * ClickEvent is null
     * Calls {@link #setItem(int, int, ItemStack, ClickEvent)}
     *
     * @param page The page
     * @param slot The slot number
     * @param item The ItemStack
     * @return The MenuGUI
     */
    public Menu setItem(int page, int slot, ItemStack item) {
        return setItem(page, slot, item, (ClickEvent)null);
    }

    /**
     * Set an Item in the Menu
     * Calls {@link #setItem(int, int, ItemStack, ClickEvent)}
     *
     * @param page  The page
     * @param slot  The slot number
     * @param items The ItemStack
     * @return The MenuGUI
     */
    public Menu setItem(int page, int slot, Items items) {
        return setItem(page, slot, items.getItem());
    }

    /**
     * Set an Item in the Menu
     * Calls {@link #setItem(int, int, ItemStack, ClickEvent)}
     *
     * @param page       The page
     * @param slot       The slot number
     * @param items      The ItemStack
     * @param menuClick The ClickEvent
     * @return The MenuGUI
     */
    public Menu setItem(int page, int slot, Items items, MenuClick menuClick) {
        return setItem(page, slot, items.getItem(), new ClickEvent() {
            @Override
            public void click() {
                menuClick.click(this);
            }
        });
    }
    /**
     * Set an Item in the Menu
     * Calls {@link #setItem(int, int, ItemStack, ClickEvent)}
     *
     * @param page       The page
     * @param slot       The slot number
     * @param items      The ItemStack
     * @param clickEvent The ClickEvent
     * @return The MenuGUI
     */
    public Menu setItem(int page, int slot, Items items, ClickEvent clickEvent) {
        return setItem(page, slot, items.getItem(), clickEvent);
    }

    /**
     * Set an Item in the Menu
     * Page is 0, ClickEvent is null
     * Calls {@link #setItem(int, int, ItemStack, ClickEvent)}
     *
     * @param slot The slot number
     * @param item The ItemStack
     * @return The MenuGUI
     */
    public Menu setItem(int slot, ItemStack item) {
        return setItem(0, slot, item, (ClickEvent)null);
    }

    /**
     * Set an Item in the Menu
     * Page is 0
     * Calls {@link #setItem(int, int, ItemStack, ClickEvent)}
     *
     * @param slot       The slot number
     * @param item       The ItemStack
     * @param menuClick The ClickEvent
     * @return The MenuGUI
     */
    public Menu setItem(int slot, ItemStack item, MenuClick menuClick) {
        return setItem(0, slot, item, new ClickEvent() {
            @Override
            public void click() {
                menuClick.click(this);
            }
        });
    }
    /**
     * Set an Item in the Menu
     * Page is 0
     * Calls {@link #setItem(int, int, ItemStack, ClickEvent)}
     *
     * @param slot       The slot number
     * @param item       The ItemStack
     * @param clickEvent The ClickEvent
     * @return The MenuGUI
     */
    public Menu setItem(int slot, ItemStack item, ClickEvent clickEvent) {
        return setItem(0, slot, item, clickEvent);
    }

    /**
     * Set an Item in the Menu
     * Page is 0, ClickEvent is null
     * Calls {@link #setItem(int, int, ItemStack, ClickEvent)}
     *
     * @param slot  The slot number
     * @param items The ItemStack
     * @return The MenuGUI
     */
    public Menu setItem(int slot, Items items) {
        return setItem(0, slot, items, (ClickEvent)null);
    }

    /**
     * Set an Item in the Menu
     * Page is 0
     * Calls {@link #setItem(int, int, ItemStack, ClickEvent)}
     *
     * @param slot  The slot number
     * @param items The ItemStack
     * @param menuClick The ClickEvent
     * @return The MenuGUI
     */
    public Menu setItem(int slot, Items items, MenuClick menuClick) {
        return setItem(0, slot, items, new ClickEvent() {
            @Override
            public void click() {
                menuClick.click(this);
            }
        });
    }
    /**
     * Set an Item in the Menu
     * Page is 0
     * Calls {@link #setItem(int, int, ItemStack, ClickEvent)}
     *
     * @param slot  The slot number
     * @param items The ItemStack
     * @param clickEvent The ClickEvent
     * @return The MenuGUI
     */
    public Menu setItem(int slot, Items items, ClickEvent clickEvent) {
        return setItem(0, slot, items, clickEvent);
    }

    /**
     * Build the MenuGUI
     *
     * @return The MenuGUI
     */
    public Menu build() {
        for (int pageIndex = 0; pageIndex < this.pages.size(); pageIndex++) {
            ArrayList<ItemStack> items = this.pages.get(pageIndex);

            if (bordered)
                while (getRows(items.size(), 7) * 9 < this.size) {
                    items.add(null);
                }
            else
                while (getRows(items.size(), 9) * 9 < this.size) {
                    items.add(null);
                }

            if (this.centered && this.pageSlot.containsKey(pageIndex))
                items = centerLastRow(items, this.pageSlot.get(pageIndex));

            if (this.bordered)
                items = outline(items);

            HashMap<Integer, ItemStack> toSetAfter = new HashMap<>();
            for (Map.Entry<Integer[], ItemStack> e : this.toSetItems.entrySet()) {
                if (e.getKey()[0] == pageIndex) {
                    int slot = e.getKey()[1];
                    if (slot < 0) {
                        toSetAfter.put(slot, e.getValue());
                        continue;
                    }
                    while (slot > items.size() - 2)
                        items.add(null);
                    items.set(slot, e.getValue());
                }
            }
            int invSize;

            //If automatic size
            if (this.size == -1)
                invSize = getRows(items.size(), 9.0D) + (this.bordered ? -1 : pages.size() > 1 ? 1 : 0);

                //If manual size
            else
                invSize = getRows(this.size, bordered ? 7.0D : 9.0D) + (bordered ? 2 : pages.size() > 1 ? 1 : 0);

            invSize *= 9;

            Inventory inv = Bukkit.createInventory(null, invSize, this.name);
            for (int position = 0; position < items.size(); position++) {
                if (position < invSize)
                    if (items.get(position) != null)
                        inv.setItem(position, items.get(position));
            }
            if (pageIndex > 0 && pages.size() > 1)
                inv.setItem(inv.getSize() - 9, PREVIOUSPAGEITEM);
            if (pages.size() > 1 && pageIndex != this.pages.size() - 1)
                inv.setItem(inv.getSize() - 1, NEXTPAGEITEM);
            for (Map.Entry<Integer, ItemStack> e : toSetAfter.entrySet())
                inv.setItem(inv.getSize() + e.getKey(), e.getValue());
            this.inventories.add(inv);
        }
        return this;
    }

    /**
     * Center the last row of an Inventory
     *
     * @param items        The Items ArrayList
     * @param lastAddPlace Last Place Index
     * @return An adjusted ArrayList Of Items
     */
    private ArrayList<ItemStack> centerLastRow(ArrayList<ItemStack> items, int lastAddPlace) {
        int count = lastAddPlace;
        int itemsPerRow = this.bordered ? 7 : 9;
        boolean spaceCenter = false;
        int lastRowCount = count % itemsPerRow;
        int lastRowStart = count - lastRowCount;

        if (lastRowCount == 0)
            return items;

        if (lastRowCount % 2 == 0)
            spaceCenter = true;


        int toShift = itemsPerRow / 2 - lastRowCount / 2;

        for (int i = 0; i < toShift; i++)
            items.add(lastRowStart, null);
        if (spaceCenter) {
            items.add(lastRowStart+toShift+(lastRowCount/2), null);
        }
        return items;
    }

    /**
     * Outline an ArrayList of an inventory
     *
     * @param inventory The Items Arraylist
     * @return An adjusted ArrayList of Items
     */
    private ArrayList<ItemStack> outline(ArrayList<ItemStack> inventory) {
        int rows = getRows(inventory.size(), 7.0D) + 2;
        for (int i = 0; i < 9; i++)
            inventory.add(null);
        for (int r = 0; r != rows; r++) {
            for (int c = 0; c != 9; c++) {
                if ((r == 0 || r + 1 == rows) && (c == 0 || c + 1 == 9)) {
                    inventory.add(9 * r + c, this.borderCorners);
                } else if (c == 0 || c + 1 == 9 || r == 0 || r + 1 == rows) {
                    inventory.add(9 * r + c, this.borderSides);
                }
            }
        }
        return inventory;
    }

    /**
     * Open the inventory for the player
     * Page is 0
     * Calls {@link #open(int, Player)}
     *
     * @param player The player
     */
    public void open(Player player) {
        open(0, player);
    }

    /**
     * Open the inventory for the player
     * Will {@link #build()} if not ran
     *
     * @param page   The page to open
     * @param player The player
     */
    public void open(int page, Player player) {
        if (this.inventories.isEmpty())
            build();

        //Re-register if not registered
        if (MenuManager.getMenuGUI(this.inventories.get(page)) == null)
            MenuManager.register(this);
        this.playerPage.put(player.getName(), page);
        player.openInventory(this.inventories.get(page));
    }

    /**
     * Open the next page of the MenuGUI
     * Calls {@link #open(int, Player)}
     *
     * @param player The player
     */
    public void openNextPage(Player player) {
        int current = this.playerPage.get(player.getName());
        open(current + 1, player);
    }

    /**
     * Open the previous page of the MenuGUI
     * Calls {@link #open(int, Player)}
     *
     * @param player The player
     */
    public void openPreviousPage(Player player) {
        int current = this.playerPage.get(player.getName());
        open(current - 1, player);
    }

    /**
     * Gets current page.
     *
     * @param player The player
     * @return The current page
     */
    public int getCurrentPage(Player player) {
        if (!this.playerPage.containsKey(player.getName()))
            this.playerPage.put(player.getName(), 0);
        return this.playerPage.get(player.getName());
    }

    /**
     * Destroy the MenuGUI
     */
    public void destroy() {
        MenuManager.unregister(this);
        this.name = null;
        this.pages = null;
        this.inventories = null;
        this.borderSides = null;
        this.borderCorners = null;
        this.clickEvent = null;
    }

    /**
     * Get the Rows based on the number of items with a specific number of items per row
     *
     * @param numberOfItems The total number of items
     * @param itemsPerRow   the amount of items per row
     * @return The total Rows
     */
    public int getRows(int numberOfItems, double itemsPerRow) {
        return (int) Math.ceil(numberOfItems / itemsPerRow);
    }

    /**
     * Gets click event.
     *
     * @return Get the ClickEvent
     */
    public ClickEvent getClickEvent() {
        return this.clickEvent;
    }

    /**
     * Set the ClickEvent
     *
     * @param menuClick the click event
     */
    public void setClickEvent(MenuClick menuClick) {
        setClickEvent(new ClickEvent() {
            @Override
            public void click() {
                menuClick.click(this);
            }
        });
    }
    /**
     * Set the ClickEvent
     *
     * @param clickEvent the click event
     */
    public void setClickEvent(ClickEvent clickEvent) {
        if (clickEvent != null)
            clickEvent.menuObject = this;
        this.clickEvent = clickEvent;
    }

    /**
     * Gets click event.
     *
     * @param itemStack The ItemStack
     * @return Get the ClickEvent for an item
     */
    public ClickEvent getClickEvent(ItemStack itemStack) {
            UUID id = SingleClickEventUtil.getUUIDFromTag(itemStack);
            if (id != null)
                return this.clickEvents.get(id);


        return null;
    }

    /**
     * Gets size.
     *
     * @return Get the MenuGUI's size
     */
    public int getSize() {
        return this.size;
    }

    /**
     * Set the MenuGUI's size
     *
     * @param size The size
     */
    public void setSize(int size) {
        this.size = size;
    }

    public boolean isBordered() {
        return this.bordered;
    }

    /**
     * Set the MenuGUI being bordered - includes corners
     *
     * @param bordered If being bordered
     * @return The MenuGUI
     */
    public Menu setBordered(boolean bordered) {
        this.bordered = false;
        return this;
    }
}
