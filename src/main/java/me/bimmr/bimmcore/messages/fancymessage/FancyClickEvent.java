package me.bimmr.bimmcore.messages.fancymessage;

import me.bimmr.bimmcore.BimmCore;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import java.util.UUID;

/**
 * Created by Randy on 02/06/17.
 */
public abstract class FancyClickEvent {
    private UUID uuid;
    private Player player;
    private boolean removeAfter5Min = true;
    private BukkitTask removal;

    /**
     * Default Constructor
     * By default will self delete after 5 minutes from first message. Resets if clicked again within the time
     */
    public FancyClickEvent() {
        this(true);
    }

    /**
     * Constructor to remove the "removeAfter5Min"
     *
     * @param removeAfter5Min If the FancyClick is auto removed after 5 minutes
     */
    public FancyClickEvent(boolean removeAfter5Min) {
        this.uuid = UUID.randomUUID();
        this.removeAfter5Min = removeAfter5Min;
    }

    /**
     * @return Get the Player
     */
    public Player getPlayer() {
        return player;
    }

    /**
     * Set the player
     *
     * @param player The Player
     */
    public void setPlayer(Player player) {
        this.player = player;
    }

    /**
     *
     *
     * @return Get the UUID
     */
    public UUID getUUID() {
        return uuid;
    }

    /**
     * Abstract CallBack
     */
    public abstract void onClick();


    /**
     * Remove the Event
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
