package me.bimmr.bimmcore.gui.inventory;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

/**
 * The type Click event.
 */
public abstract class ClickEvent {

    private Player player;
    private int position;
    private int page;
    private ItemStack item;
    private InventoryClickEvent event;

    /**
     * The Do nothing.
     */
    protected boolean doNothing = false;

    /**
     * The Menu object.
     */
    protected Menu menuObject;

    /**
     * Sets .
     *
     * @param player   the player
     * @param page     the page
     * @param position the position
     * @param item     the item
     * @param event    the event
     */
    protected void setup(Player player, int page, int position, ItemStack item, InventoryClickEvent event) {
        this.player = player;
        this.page = page;
        this.position = position;
        this.item = item;
        this.event = event;
    }

    /**
     * Set close.
     *
     * @param close the close
     */
    public void setClose(boolean close){
        this.menuObject.setClose(close);
    }

    /**
     * Set destroy.
     *
     * @param close the close
     */
    public void setDestroy(boolean close){
        this.menuObject.setDestroy(close);
    }

    /**
     * Gets player.
     *
     * @return the player
     */
    public Player getPlayer() {
        return player;
    }

    /**
     * Gets position.
     *
     * @return the position
     */
    public int getPosition() {
        return position;
    }

    /**
     * Gets item.
     *
     * @return the item
     */
    public ItemStack getItem() {
        return item;
    }

    /**
     * Gets event.
     *
     * @return the event
     */
    public InventoryClickEvent getEvent() {
        return event;
    }

    /**
     * Gets clicked name.
     *
     * @return the clicked name
     */
    public String getClickedName() {
        return ChatColor.stripColor(getItem().getItemMeta().hasDisplayName() ? getItem().getItemMeta().getDisplayName() : getItem().getType().name());
    }

    /**
     * Click.
     */
    public abstract void click();

    /**
     * Gets page.
     *
     * @return the page
     */
    public int getPage() {
        return page;
    }

    /**
     * Do nothing.
     */
    public void doNothing() {
        this.doNothing = true;
    }

}
