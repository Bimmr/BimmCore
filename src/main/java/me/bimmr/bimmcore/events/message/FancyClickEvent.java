package me.bimmr.bimmcore.events.message;

import me.bimmr.bimmcore.BimmCore;
import me.bimmr.bimmcore.messages.FancyMessage;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import java.util.UUID;

/**
 * Created by Randy on 02/06/17.
 */
public abstract class FancyClickEvent {
    private UUID uuid;
    private boolean removeAfter5Min = true;
    private BukkitTask removal;

    /**
     * Deafult Constuructor
     * By default will self delete after 5 minutes from first message. Resets if clicked again within the time
     */
    public FancyClickEvent() {
        this.uuid = UUID.randomUUID();
    }

    /**
     * Constructor to remove the "removeAfter5Min"
     * @param removeAfter5Min
     */
    public FancyClickEvent(boolean removeAfter5Min) {
        this();
        this.removeAfter5Min = removeAfter5Min;
    }

    /**
     * Get the UUID
     * @return
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
                    FancyMessage.FancyMessageListener.chats.remove(this);
                }
            }.runTaskLater(BimmCore.getInstance(), 5 * 60 * 20);
        }
    }
}
