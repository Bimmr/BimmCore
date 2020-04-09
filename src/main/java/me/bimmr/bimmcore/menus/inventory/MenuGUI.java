package me.bimmr.bimmcore.menus.inventory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import me.bimmr.bimmcore.BimmCore;
import me.bimmr.bimmcore.StringUtil;
import me.bimmr.bimmcore.items.Items;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class MenuGUI {
    public static final int MAXITEMSPERPAGE = 45;

    public static final int MAXITEMSPERPAGEBORDERED = 28;

    public static ItemStack PREVIOUSPAGEITEM;

    public static ItemStack NEXTPAGEITEM;

    private MenuGUIManager menuGUIManager;

    private String name;

    private ClickEvent clickEvent;

    private boolean centered;

    private boolean bordered;

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ItemStack getBorderCorners() {
        return this.borderCorners;
    }

    public MenuGUIManager getMenuGUIManager() {
        return this.menuGUIManager;
    }

    public HashMap<String, Integer> getPlayerPage() {
        return this.playerPage;
    }

    public ArrayList<ArrayList<ItemStack>> getPages() {
        return this.pages;
    }

    public ArrayList<Inventory> getInventories() {
        return this.inventories;
    }

    public void setBorderCorners(ItemStack borderCorners) {
        this.borderCorners = borderCorners;
    }

    public ItemStack getBorderSides() {
        return this.borderSides;
    }

    public void setBorderSides(ItemStack borderSides) {
        this.borderSides = borderSides;
    }

    private int size = -1;

    private ItemStack borderCorners;

    private ItemStack borderSides;

    private HashMap<String, Integer> playerPage = new HashMap<>();

    private HashMap<Integer[], ItemStack> toSetItems = (HashMap)new HashMap<>();

    private HashMap<UUID, ClickEvent> clickEvents = new HashMap<>();

    private ArrayList<ArrayList<ItemStack>> pages = new ArrayList<>();

    private ArrayList<Inventory> inventories = new ArrayList<>();

    private HashMap<Integer, Integer> pageSlot = new HashMap<>();

    public MenuGUI(MenuGUIManager menuGUIManager, String name, ClickEvent clickEvent) {
        this.menuGUIManager = menuGUIManager;
        this.name = name;
        this.clickEvent = clickEvent;
        this.pages.add(new ArrayList<>());
        if (PREVIOUSPAGEITEM == null)
            if (!BimmCore.oldAPI) {
                PREVIOUSPAGEITEM = (new Items(new ItemStack(Material.PLAYER_HEAD))).setDurability(3).setDisplayName("" + ChatColor.YELLOW + ChatColor.BOLD + "<-").setSkullOwner("MHF_ArrowLeft").getItem();
            } else {
                PREVIOUSPAGEITEM = (new Items(new ItemStack(Material.valueOf("SKULL_ITEM")))).setDurability(3).setDisplayName("" + ChatColor.YELLOW + ChatColor.BOLD + "<-").setSkullOwner("MHF_ArrowLeft").getItem();
            }
        if (NEXTPAGEITEM == null)
            if (!BimmCore.oldAPI) {
                NEXTPAGEITEM = (new Items(new ItemStack(Material.PLAYER_HEAD))).setDurability(3).setDisplayName("" + ChatColor.YELLOW + ChatColor.BOLD + "->").setSkullOwner("MHF_ArrowRight").getItem();
            } else {
                NEXTPAGEITEM = (new Items(new ItemStack(Material.valueOf("SKULL_ITEM")))).setDurability(3).setDisplayName("" + ChatColor.YELLOW + ChatColor.BOLD + "->").setSkullOwner("MHF_ArrowRight").getItem();
            }
        menuGUIManager.menus.add(this);
    }

    public MenuGUI(MenuGUIManager menuGUIManager, String name, int size, ClickEvent clickEvent, ItemStack borderCorners, ItemStack borderSides) {
        this(menuGUIManager, name, clickEvent);
        this.size = size;
        border(borderCorners, borderSides);
    }

    public MenuGUI border(ItemStack borderCorners, ItemStack borderSides) {
        this.borderCorners = format(borderCorners, ChatColor.GRAY + "");
        this.borderSides = format(borderSides, ChatColor.GRAY + "");
        this.bordered = true;
        return this;
    }

    public ItemStack format(ItemStack itemstack, String name, String... lore) {
        ItemMeta im = itemstack.getItemMeta();
        if (im != null) {
            if (name != null)
                im.setDisplayName(StringUtil.addColor(name));
            if (lore != null) {
                ArrayList<String> lores = new ArrayList<>();
                for (String line : lore) {
                    if (line.contains(";")) {
                        for (String part : line.split(";"))
                            lores.add(StringUtil.addColor(part));
                    } else {
                        lores.add(StringUtil.addColor(line));
                    }
                }
                im.setLore(lores);
            }
            itemstack.setItemMeta(im);
        }
        return itemstack;
    }

    public ItemStack format(ItemStack itemstack, String name) {
        return format(itemstack, name, null);
    }

    public MenuGUI setBordered(boolean bordered) {
        this.bordered = false;
        return this;
    }

    public MenuGUI center() {
        this.centered = true;
        return this;
    }

    public MenuGUI setCentered(boolean centered) {
        this.centered = centered;
        return this;
    }

    public MenuGUI addItem(int page, ItemStack itemStack) {
        return addItem(page, itemStack, (ClickEvent)null);
    }

    public MenuGUI addItem(int page, ItemStack itemStack, ClickEvent clickEvent) {
        if (clickEvent != null)
            if (!BimmCore.oldAPI) {
                UUID id = UUID.randomUUID();
                itemStack = SingleClickEventUtil.addUUIDTag(id, itemStack);
                this.clickEvents.put(id, clickEvent);
            }
        while (page < this.pages.size() && ((this.bordered && ((ArrayList)this.pages.get(page)).size() >= 28) || (!this.bordered && ((ArrayList)this.pages.get(page)).size() >= 45)))
            page++;
        if (this.pages.size() <= page)
            this.pages.add(page, new ArrayList<>());
        if (!this.pageSlot.containsKey(Integer.valueOf(page)))
            this.pageSlot.put(Integer.valueOf(page), Integer.valueOf(0));
        int slot = ((Integer)this.pageSlot.get(Integer.valueOf(page))).intValue();
        ((ArrayList<ItemStack>)this.pages.get(page)).add(itemStack);
        this.pageSlot.put(Integer.valueOf(page), Integer.valueOf(slot + 1));
        return this;
    }

    public MenuGUI addItem(int page, ItemStack itemStack, String name, String... lore) {
        return addItem(page, format(itemStack, name, lore));
    }

    public MenuGUI addItem(int page, ItemStack itemStack, String name, String[] lore, ClickEvent clickEvent) {
        return addItem(page, format(itemStack, name, lore), clickEvent);
    }

    public MenuGUI addItem(int page, ItemStack itemStack, String name, String lore, ClickEvent clickEvent) {
        return addItem(page, format(itemStack, name, new String[] { lore }), clickEvent);
    }

    public MenuGUI addItem(ItemStack itemStack) {
        addItem(0, itemStack);
        return this;
    }

    public MenuGUI addItem(ItemStack itemStack, ClickEvent clickEvent) {
        addItem(0, itemStack, clickEvent);
        return this;
    }

    public MenuGUI addItem(ItemStack itemstack, String name, String... lore) {
        return addItem(format(itemstack, name, lore));
    }

    public MenuGUI addItem(ItemStack itemstack, String name, String lore, ClickEvent clickEvent) {
        return addItem(format(itemstack, name, new String[] { lore }), clickEvent);
    }

    public MenuGUI setItem(int page, int slot, ItemStack item, ClickEvent clickEvent) {
        while (slot >= (this.bordered ? 28 : 45)) {
            page++;
            if (page >= this.pages.size()) {
                slot -= this.bordered ? 28 : 45;
                this.pages.add(new ArrayList<>());
            }
        }
        if (clickEvent != null)
            if (!BimmCore.oldAPI) {
                UUID id = UUID.randomUUID();
                item = SingleClickEventUtil.addUUIDTag(id, item);
                this.clickEvents.put(id, clickEvent);
            }
        this.toSetItems.put(new Integer[] { Integer.valueOf(page), Integer.valueOf(slot) }, item);
        return this;
    }

    public MenuGUI setItem(int page, int slot, ItemStack item) {
        return setItem(page, slot, item, (ClickEvent)null);
    }

    public MenuGUI setItem(int page, int slot, ItemStack item, String name, String... lore) {
        return setItem(page, slot, format(item, name, lore));
    }

    public MenuGUI setItem(int page, int slot, ItemStack item, String name, String[] lore, ClickEvent clickEvent) {
        return setItem(page, slot, format(item, name, lore), clickEvent);
    }

    public MenuGUI setItem(int page, int slot, ItemStack item, String name, String lore, ClickEvent clickEvent) {
        return setItem(page, slot, format(item, name, new String[] { lore }), clickEvent);
    }

    public MenuGUI setItem(int slot, ItemStack item) {
        return setItem(0, slot, item);
    }

    public MenuGUI setItem(int slot, ItemStack item, ClickEvent clickEvent) {
        return setItem(0, slot, item, clickEvent);
    }

    public MenuGUI setItem(int slot, ItemStack item, String name, String... lore) {
        return setItem(slot, format(item, name, lore));
    }

    public MenuGUI setItem(int slot, ItemStack item, String name, String lore, ClickEvent clickEvent) {
        return setItem(slot, format(item, name, new String[] { lore }), clickEvent);
    }

    public MenuGUI setItem(int slot, ItemStack item, String name, String[] lore, ClickEvent clickEvent) {
        return setItem(slot, format(item, name, lore), clickEvent);
    }

    public MenuGUI build() {
        for (int i = 0; i < this.pages.size(); i++) {
            ArrayList<ItemStack> items = this.pages.get(i);
            if (this.bordered && items.size() == 0)
                items.add(null);
            if (this.centered &&
                    this.pageSlot.containsKey(Integer.valueOf(i)))
                items = centerLastRow(items, ((Integer)this.pageSlot.get(Integer.valueOf(i))).intValue());
            if (this.bordered)
                items = outline(items);
            HashMap<Integer, ItemStack> toSetAfter = new HashMap<>();
            for (Map.Entry<Integer[], ItemStack> e : this.toSetItems.entrySet()) {
                if (((Integer[])e.getKey())[0].intValue() == i) {
                    int slot = ((Integer[])e.getKey())[1].intValue();
                    if (slot < 0) {
                        toSetAfter.put(Integer.valueOf(slot), e.getValue());
                        continue;
                    }
                    while (slot > items.size() - 2)
                        items.add(null);
                    items.set(slot, e.getValue());
                }
            }
            Inventory inv = Bukkit.createInventory(null, (this.size != -1) ? ((getRows(this.size, 9.0D) + 2) * 9) : ((getRows(items.size(), 9.0D) + (this.bordered ? -1 : 1)) * 9), this.name);
            for (int position = 0; position < items.size(); position++) {
                if (items.get(position) != null)
                    inv.setItem(position, items.get(position));
            }
            if (i > 0)
                inv.setItem(inv.getSize() - 9, PREVIOUSPAGEITEM);
            if (i != this.pages.size() - 1)
                inv.setItem(inv.getSize() - 1, NEXTPAGEITEM);
            for (Map.Entry<Integer, ItemStack> e : toSetAfter.entrySet())
                inv.setItem(inv.getSize() + ((Integer)e.getKey()).intValue(), e.getValue());
            this.inventories.add(inv);
        }
        return this;
    }

    private ArrayList<ItemStack> centerLastRow(ArrayList<ItemStack> items, int lastAddPlace) {
        int count = lastAddPlace;
        int itemsPerRow = this.bordered ? 7 : 9;
        int lastRowCount = count % itemsPerRow;
        int lastRowStart = count - lastRowCount;
        if (lastRowCount == 0)
            return items;
        int toShift = itemsPerRow / 2 - lastRowCount / 2;
        for (int i = 0; i < toShift; i++)
            items.add(lastRowStart, null);
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

    public void open(Player player) {
        open(0, player);
    }

    public void open(int page, Player player) {
        this.playerPage.put(player.getName(), Integer.valueOf(page));
        player.openInventory(this.inventories.get(page));
    }

    public void openNextPage(Player player) {
        int current = ((Integer)this.playerPage.get(player.getName())).intValue();
        open(current + 1, player);
    }

    public void openPreviousPage(Player player) {
        int current = ((Integer)this.playerPage.get(player.getName())).intValue();
        open(current - 1, player);
    }

    public int getCurrentPage(Player player) {
        if (!this.playerPage.containsKey(player.getName()))
            this.playerPage.put(player.getName(), Integer.valueOf(0));
        return ((Integer)this.playerPage.get(player.getName())).intValue();
    }

    public void destroy() {
        this.menuGUIManager.menus.remove(this);
        this.name = null;
        this.pages = null;
        this.inventories = null;
        this.borderSides = null;
        this.borderCorners = null;
        this.clickEvent = null;
    }

    public int getRows(int numberOfItems, double itemsPerRow) {
        return (int)Math.ceil(numberOfItems / itemsPerRow);
    }

    public ClickEvent getClickEvent() {
        return this.clickEvent;
    }

    public ClickEvent getClickEvent(ItemStack itemStack) {
        if (!BimmCore.oldAPI) {
            UUID id = SingleClickEventUtil.getUUIDFromTag(itemStack);
            if (id != null)
                return this.clickEvents.get(id);
        }
        return null;
    }

    public void setClickEvent(ClickEvent clickEvent) {
        this.clickEvent = clickEvent;
    }

    public int getSize() {
        return this.size;
    }

    public void setSize(int size) {
        this.size = size;
    }
}
