package me.bimmr.bimmcore.menus.inventory;

/**
 * Created by Randy on 3/5/2016.
 */

import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

public abstract class ClickEvent {

    private Player              player;
    private int                 position;
    private boolean             close;
    private boolean             destroy;
    private ItemStack           item;
    private InventoryClickEvent event;

    public void setup(Player player, int position, ItemStack item, InventoryClickEvent event) {
        this.player = player;
        this.position = position;
        this.close = true;
        this.destroy = false;
        this.item = item;
        this.event = event;
    }

    public void setDestroy(boolean destroy) {
        this.destroy = destroy;
    }

    public void setClose(boolean close) {
        this.close = close;
    }

    public Player getPlayer() {
        return player;
    }

    public int getPosition() {
        return position;
    }

    public boolean willClose() {
        return close;
    }

    public boolean willDestroy() {
        return destroy;
    }

    public ItemStack getItem() {
        return item;
    }

    /**
     * Gets the InventoryClickEvent involved in the GUI click.
     *
     * @return the event
     */
    public InventoryClickEvent getEvent() {
        return event;
    }

    public String getClickedName() {
        return getItem().getItemMeta().hasDisplayName() ? getItem().getItemMeta().getDisplayName() : getItem().getType().name();
    }

    public abstract void click();
}
