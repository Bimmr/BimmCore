package me.bimmr.bimmcore.gui.inventory;

/**
 * Created by Randy on 3/5/2016.
 */

import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

public abstract class ClickEvent {

    private Player player;
    private int position;
    private int page;
    private boolean close;
    private boolean destroy;
    private ItemStack item;
    private InventoryClickEvent event;

    /**
     * Setup the ClickEvent
     *
     * @param player   The player
     * @param page     The page clicked
     * @param position The position clicked
     * @param item     The item clicked
     * @param event    The InventoryClickEvent
     */
    public void setup(Player player, int page, int position, ItemStack item, InventoryClickEvent event) {
        this.player = player;
        this.page = page;
        this.position = position;
        this.close = true;
        this.destroy = false;
        this.item = item;
        this.event = event;
    }

    /**
     * Set destroy
     *
     * @param destroy boolean if to be destroyed
     */
    public void setDestroy(boolean destroy) {
        this.destroy = destroy;
    }

    /**
     * Set close
     *
     * @param close boolean if to be close
     */
    public void setClose(boolean close) {
        this.close = close;
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
     * @return Get if will close
     */
    public boolean willClose() {
        return close;
    }

    /**
     * @return Get if will destroy
     */
    public boolean willDestroy() {
        return destroy;
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
        return getItem().getItemMeta().hasDisplayName() ? getItem().getItemMeta().getDisplayName() : getItem().getType().name();
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
}