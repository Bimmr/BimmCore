package me.bimmr.bimmcore.gui.inventory;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
/**
 * The ClickEvent that gets called when an inventory item gets clicked
 */
public abstract class ClickEvent {

    private Player player;
    private int position;
    private int page;
    private ItemStack item;
    private InventoryClickEvent event;

    protected boolean doNothing = false;

    protected Menu menuObject;

    /**
     * Setup the ClickEvent
     *
     * @param player   The player
     * @param page     The page clicked
     * @param position The position clicked
     * @param item     The item clicked
     * @param event    The InventoryClickEvent
     */
    protected void setup(Player player, int page, int position, ItemStack item, InventoryClickEvent event) {
        this.player = player;
        this.page = page;
        this.position = position;
        this.item = item;
        this.event = event;
    }
    public void setClose(boolean close){
        this.menuObject.setClose(close);
    }
    public void setDestroy(boolean close){
        this.menuObject.setDestroy(close);
    }

    /**
     * @return Get Player who clicked
     */
    public Player getPlayer() {
        return player;
    }

    /**
     * @return Get Position clicked
     */
    public int getPosition() {
        return position;
    }

    /**
     * @return get ItemStack clicked
     */
    public ItemStack getItem() {
        return item;
    }

    /**
     * Gets the InventoryClickEvent involved in the GUI message.
     *
     * @return the event
     */
    public InventoryClickEvent getEvent() {
        return event;
    }

    /**
     * @return Get the item clicked's name
     */
    public String getClickedName() {
        return ChatColor.stripColor(getItem().getItemMeta().hasDisplayName() ? getItem().getItemMeta().getDisplayName() : getItem().getType().name());
    }

    /**
     * Abstract Click
     */
    public abstract void click();

    /**
     * @return Get the page clicked
     */
    public int getPage() {
        return page;
    }

    public void doNothing() {
        this.doNothing = true;
    }

}
