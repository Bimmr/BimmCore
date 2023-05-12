package me.bimmr.bimmcore.messages.fancymessage;

import me.bimmr.bimmcore.BimmCore;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import java.util.UUID;

/**
 * The type Fancy click event.
 */
public abstract class FancyClickEvent {
    private UUID uuid;
    private Player player;
    private boolean removeAfter5Min = true;
    private BukkitTask removal;

    /**
     * Instantiates a new Fancy click event.
     */
    public FancyClickEvent() {
        this(true);
    }

    /**
     * Instantiates a new Fancy click event.
     *
     * @param removeAfter5Min the remove after 5 min
     */
    public FancyClickEvent(boolean removeAfter5Min) {
        this.uuid = UUID.randomUUID();
        this.removeAfter5Min = removeAfter5Min;
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
     * Sets player.
     *
     * @param player the player
     */
    public void setPlayer(Player player) {
        this.player = player;
    }

    /**
     * Gets uuid.
     *
     * @return the uuid
     */
    public UUID getUUID() {
        return uuid;
    }

    /**
     * On click.
     */
    public abstract void onClick();


    /**
     * Start removal.
     */
    public void startRemoval() {
        if (removeAfter5Min) {
            if (removal != null)
                removal.cancel();

            removal = new BukkitRunnable() {
                @Override
                public void run() {
                    FancyMessageListener.chats.remove(this);
                }
            }.runTaskLater(BimmCore.getInstance(), 5 * 60 * 20);
        }
    }
}
