package me.bimmr.bimmcore.messages;

import me.bimmr.bimmcore.utils.timed.TimedObject;
import org.bukkit.entity.Player;

/**
 * The type Message display.
 */
public abstract class MessageDisplay extends TimedObject {

    /**
     * The Text.
     */
    public String text;
    /**
     * The Time.
     */
    public int    time;

    /**
     * Gets text.
     *
     * @return the text
     */
    public abstract String getText();

    /**
     * Sets text.
     *
     * @param text the text
     */
    public abstract void setText(String text);

    /**
     * Gets time.
     *
     * @return the time
     */
    public abstract int getTime();

    /**
     * Send.
     *
     * @param player the player
     */
    public abstract void send(Player player);

    /**
     * Stop.
     *
     * @param player the player
     */
    public abstract void stop(Player player);

}
