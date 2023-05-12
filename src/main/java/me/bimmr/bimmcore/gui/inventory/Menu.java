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
 * The type Menu.
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
     * Instantiates a new Menu.
     *
     * @param name the name
     */
    public Menu(String name) {
        this(name, -1, (ClickEvent)null, null, null);
    }

    /**
     * Instantiates a new Menu.
     *
     * @param name the name
     * @param size the size
     */
    public Menu(String name, int size) {
        this(name, size, (ClickEvent)null, null, null);
    }

    /**
     * Instantiates a new Menu.
     *
     * @param name      the name
     * @param menuClick the menu click
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
     * Instantiates a new Menu.
     *
     * @param name       the name
     * @param clickEvent the click event
     */
    public Menu(String name, ClickEvent clickEvent) {
        this(name, -1, clickEvent, null, null);
    }

    /**
     * Instantiates a new Menu.
     *
     * @param name      the name
     * @param size      the size
     * @param menuClick the menu click
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
     * Instantiates a new Menu.
     *
     * @param name       the name
     * @param size       the size
     * @param clickEvent the click event
     */
    public Menu(String name, int size, ClickEvent clickEvent) {
        this(name, size, clickEvent, null, null);
    }

    /**
     * Instantiates a new Menu.
     *
     * @param name          the name
     * @param borderCorners the border corners
     * @param borderSides   the border sides
     */
    public Menu(String name, ItemStack borderCorners, ItemStack borderSides) {
        this(name, -1, (ClickEvent)null, borderCorners, borderSides);
    }

    /**
     * Instantiates a new Menu.
     *
     * @param name          the name
     * @param size          the size
     * @param borderCorners the border corners
     * @param borderSides   the border sides
     */
    public Menu(String name, int size, ItemStack borderCorners, ItemStack borderSides) {
        this(name, size, (ClickEvent)null, borderCorners, borderSides);
    }

    /**
     * Instantiates a new Menu.
     *
     * @param name          the name
     * @param menuClick     the menu click
     * @param borderCorners the border corners
     * @param borderSides   the border sides
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
     * Instantiates a new Menu.
     *
     * @param name          the name
     * @param clickEvent    the click event
     * @param borderCorners the border corners
     * @param borderSides   the border sides
     */
    public Menu(String name, ClickEvent clickEvent, ItemStack borderCorners, ItemStack borderSides) {
        this(name, -1, clickEvent, borderCorners, borderSides);
    }

    /**
     * Instantiates a new Menu.
     *
     * @param name          the name
     * @param size          the size
     * @param menuClick     the menu click
     * @param borderCorners the border corners
     * @param borderSides   the border sides
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
     * Instantiates a new Menu.
     *
     * @param name          the name
     * @param size          the size
     * @param clickEvent    the click event
     * @param borderCorners the border corners
     * @param borderSides   the border sides
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

    /**
     * Will close boolean.
     *
     * @return the boolean
     */
    public boolean willClose() {
        return close;
    }

    /**
     * Sets close on click.
     *
     * @param close the close
     */
    @Deprecated
    public void setCloseOnClick(boolean close) {
        setClose(close);
    }

    /**
     * Sets close.
     *
     * @param close the close
     * @return the close
     */
    public Menu setClose(boolean close) {
        this.close = close;
        return this;
    }

    /**
     * Will destroy boolean.
     *
     * @return the boolean
     */
    public boolean willDestroy() {
        return destroy;
    }

    /**
     * Sets destroy on click.
     *
     * @param destroy the destroy
     */
    @Deprecated
    public void setDestroyOnClick(boolean destroy) {
        setDestroy(destroy);
    }

    /**
     * Sets destroy.
     *
     * @param destroy the destroy
     * @return the destroy
     */
    public Menu setDestroy(boolean destroy) {
        this.destroy = destroy;
        return this;
    }

    /**
     * Gets name.
     *
     * @return the name
     */
    public String getName() {
        return this.name;
    }

    /**
     * Sets name.
     *
     * @param name the name
     * @return the name
     */
    public Menu setName(String name) {
        this.name = name;
        return this;
    }

    /**
     * Gets border corners.
     *
     * @return the border corners
     */
    public ItemStack getBorderCorners() {
        return this.borderCorners;
    }

    /**
     * Sets border corners.
     *
     * @param borderCorners the border corners
     * @return the border corners
     */
    public Menu setBorderCorners(ItemStack borderCorners) {
        this.borderCorners = borderCorners;
        if (this.borderCorners != null)
            setBordered(true);
        return this;
    }

    /**
     * Clear.
     */
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
     * @return the player page
     */
    public HashMap<String, Integer> getPlayerPage() {
        return this.playerPage;
    }

    /**
     * Gets pages.
     *
     * @return the pages
     */
    public ArrayList<ArrayList<ItemStack>> getPages() {
        return this.pages;
    }

    /**
     * Gets inventories.
     *
     * @return the inventories
     */
    public ArrayList<Inventory> getInventories() {
        return this.inventories;
    }

    /**
     * Gets border sides.
     *
     * @return the border sides
     */
    public ItemStack getBorderSides() {
        return this.borderSides;
    }

    /**
     * Sets border sides.
     *
     * @param borderSides the border sides
     * @return the border sides
     */
    public Menu setBorderSides(ItemStack borderSides) {
        this.borderSides = borderSides;
        if (this.borderSides != null)
            setBordered(true);
        return this;
    }

    /**
     * Border menu.
     *
     * @param borderCorners the border corners
     * @param borderSides   the border sides
     * @return the menu
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
     * Center menu.
     *
     * @return the menu
     */
    public Menu center() {
        return setCentered(true);
    }

    /**
     * Sets centered.
     *
     * @param centered the centered
     * @return the centered
     */
    public Menu setCentered(boolean centered) {
        this.centered = centered;
        return this;
    }

    /**
     * Add item menu.
     *
     * @param page      the page
     * @param itemStack the item stack
     * @return the menu
     */
    public Menu addItem(int page, ItemStack itemStack) {
        return addItem(page, itemStack, (ClickEvent)null);
    }

    /**
     * Add item menu.
     *
     * @param page      the page
     * @param itemStack the item stack
     * @param menuClick the menu click
     * @return the menu
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
     * Add item menu.
     *
     * @param page       the page
     * @param itemStack  the item stack
     * @param clickEvent the click event
     * @return the menu
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
     * Add item menu.
     *
     * @param page  the page
     * @param items the items
     * @return the menu
     */
    public Menu addItem(int page, Items items) {
        return addItem(page, items.getItem(), (ClickEvent)null);
    }

    /**
     * Add item menu.
     *
     * @param items the items
     * @return the menu
     */
    public Menu addItem(Items items) {
        return addItem(0, items.getItem(), (ClickEvent)null);
    }

    /**
     * Add item menu.
     *
     * @param items     the items
     * @param menuClick the menu click
     * @return the menu
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
     * Add item menu.
     *
     * @param items      the items
     * @param clickEvent the click event
     * @return the menu
     */
    public Menu addItem(Items items, ClickEvent clickEvent) {
        return addItem(0, items.getItem(), clickEvent);
    }

    /**
     * Add item menu.
     *
     * @param page      the page
     * @param items     the items
     * @param menuClick the menu click
     * @return the menu
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
     * Add item menu.
     *
     * @param page       the page
     * @param items      the items
     * @param clickEvent the click event
     * @return the menu
     */
    public Menu addItem(int page, Items items, ClickEvent clickEvent) {
        return addItem(page, items.getItem(), clickEvent);
    }

    /**
     * Add item menu.
     *
     * @param itemStack the item stack
     * @return the menu
     */
    public Menu addItem(ItemStack itemStack) {
        addItem(0, itemStack, (ClickEvent)null);
        return this;
    }

    /**
     * Add item menu.
     *
     * @param itemStack the item stack
     * @param menuClick the menu click
     * @return the menu
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
     * Add item menu.
     *
     * @param itemStack  the item stack
     * @param clickEvent the click event
     * @return the menu
     */
    public Menu addItem(ItemStack itemStack, ClickEvent clickEvent) {
        addItem(0, itemStack, clickEvent);
        return this;
    }

    /**
     * Sets item.
     *
     * @param page      the page
     * @param slot      the slot
     * @param item      the item
     * @param menuClick the menu click
     * @return the item
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
     * Sets item.
     *
     * @param page       the page
     * @param slot       the slot
     * @param item       the item
     * @param clickEvent the click event
     * @return the item
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
     * Sets item.
     *
     * @param page the page
     * @param slot the slot
     * @param item the item
     * @return the item
     */
    public Menu setItem(int page, int slot, ItemStack item) {
        return setItem(page, slot, item, (ClickEvent)null);
    }

    /**
     * Sets item.
     *
     * @param page  the page
     * @param slot  the slot
     * @param items the items
     * @return the item
     */
    public Menu setItem(int page, int slot, Items items) {
        return setItem(page, slot, items.getItem());
    }

    /**
     * Sets item.
     *
     * @param page      the page
     * @param slot      the slot
     * @param items     the items
     * @param menuClick the menu click
     * @return the item
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
     * Sets item.
     *
     * @param page       the page
     * @param slot       the slot
     * @param items      the items
     * @param clickEvent the click event
     * @return the item
     */
    public Menu setItem(int page, int slot, Items items, ClickEvent clickEvent) {
        return setItem(page, slot, items.getItem(), clickEvent);
    }

    /**
     * Sets item.
     *
     * @param slot the slot
     * @param item the item
     * @return the item
     */
    public Menu setItem(int slot, ItemStack item) {
        return setItem(0, slot, item, (ClickEvent)null);
    }

    /**
     * Sets item.
     *
     * @param slot      the slot
     * @param item      the item
     * @param menuClick the menu click
     * @return the item
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
     * Sets item.
     *
     * @param slot       the slot
     * @param item       the item
     * @param clickEvent the click event
     * @return the item
     */
    public Menu setItem(int slot, ItemStack item, ClickEvent clickEvent) {
        return setItem(0, slot, item, clickEvent);
    }

    /**
     * Sets item.
     *
     * @param slot  the slot
     * @param items the items
     * @return the item
     */
    public Menu setItem(int slot, Items items) {
        return setItem(0, slot, items, (ClickEvent)null);
    }

    /**
     * Sets item.
     *
     * @param slot      the slot
     * @param items     the items
     * @param menuClick the menu click
     * @return the item
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
     * Sets item.
     *
     * @param slot       the slot
     * @param items      the items
     * @param clickEvent the click event
     * @return the item
     */
    public Menu setItem(int slot, Items items, ClickEvent clickEvent) {
        return setItem(0, slot, items, clickEvent);
    }

    /**
     * Build menu.
     *
     * @return the menu
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
     * Open.
     *
     * @param player the player
     */
    public void open(Player player) {
        open(0, player);
    }

    /**
     * Open.
     *
     * @param page   the page
     * @param player the player
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
     * Open next page.
     *
     * @param player the player
     */
    public void openNextPage(Player player) {
        int current = this.playerPage.get(player.getName());
        open(current + 1, player);
    }

    /**
     * Open previous page.
     *
     * @param player the player
     */
    public void openPreviousPage(Player player) {
        int current = this.playerPage.get(player.getName());
        open(current - 1, player);
    }

    /**
     * Gets current page.
     *
     * @param player the player
     * @return the current page
     */
    public int getCurrentPage(Player player) {
        if (!this.playerPage.containsKey(player.getName()))
            this.playerPage.put(player.getName(), 0);
        return this.playerPage.get(player.getName());
    }

    /**
     * Destroy.
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
     * Gets rows.
     *
     * @param numberOfItems the number of items
     * @param itemsPerRow   the items per row
     * @return the rows
     */
    public int getRows(int numberOfItems, double itemsPerRow) {
        return (int) Math.ceil(numberOfItems / itemsPerRow);
    }

    /**
     * Gets click event.
     *
     * @return the click event
     */
    public ClickEvent getClickEvent() {
        return this.clickEvent;
    }

    /**
     * Sets click event.
     *
     * @param menuClick the menu click
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
     * Sets click event.
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
     * @param itemStack the item stack
     * @return the click event
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
     * @return the size
     */
    public int getSize() {
        return this.size;
    }

    /**
     * Sets size.
     *
     * @param size the size
     */
    public void setSize(int size) {
        this.size = size;
    }

    /**
     * Is bordered boolean.
     *
     * @return the boolean
     */
    public boolean isBordered() {
        return this.bordered;
    }

    /**
     * Sets bordered.
     *
     * @param bordered the bordered
     * @return the bordered
     */
    public Menu setBordered(boolean bordered) {
        this.bordered = false;
        return this;
    }
}
