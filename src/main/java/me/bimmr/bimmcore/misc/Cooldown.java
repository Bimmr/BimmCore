package me.bimmr.bimmcore.misc;

import java.util.HashMap;

/**
 * An easy to use Cooldown class
 */
public class Cooldown {

    private HashMap<String, Long> cooldowns = new HashMap<String, Long>();
    // Change time to how ever long you want
    private long time;

    /**
     * Instantiates a new Cooldown.
     *
     * @param time the time
     */
    public Cooldown(long time) {
        this.time = time;
    }

    /**
     * Add the player to the cooldown
     *
     * @param player the player
     */
    public void addToCooldown(String player) {
        cooldowns.put(player, System.currentTimeMillis());
    }

    /**
     * Get the remaining seconds
     *
     * @param player the player
     * @return time remaining
     */
    public long getTimeRemaining(String player) {
        return time - ((System.currentTimeMillis() - cooldowns.get(player)) / 1000);
    }

    /**
     * Gets if the player's cooldown is over
     *
     * @param player the player
     * @return boolean
     */
    public boolean isCooledDown(String player) {
        if (!cooldowns.containsKey(player) || (((System.currentTimeMillis() - cooldowns.get(player)) / 1000) >= time))
            return true;
        else return false;
    }

}
