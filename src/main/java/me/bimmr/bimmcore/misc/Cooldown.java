package me.bimmr.bimmcore.misc;

import java.util.HashMap;

/**
 * An easy to use Cooldown class
 */
public class Cooldown<T> {

    private HashMap<T, Long> cooldowns = new HashMap<>();
    // Change time to how ever long you want
    private long time;

    /**
     * Instantiates a new Cooldown.
     *
     * @param time the time in milliseconds
     */
    public Cooldown(long time) {
        this.time = time;
    }

    /**
     * Add the player to the cooldown
     *
     * @param player the player
     */
    public void addToCooldown(T player) {
        cooldowns.put(player, System.currentTimeMillis());
    }

    /**
     * Get the remaining time
     *
     * @param player the player
     * @return time remaining
     */
    public long getTimeRemaining(T player) {
        return time - ((System.currentTimeMillis() - cooldowns.get(player)));
    }

    /**
     * Get the remaining seconds
     *
     * @param player the player
     * @return time remaining
     */
    public long getSecondsRemaining(T player) {
        return time - ((System.currentTimeMillis() - cooldowns.get(player)) / 1000);
    }

    /**
     * Gets if the player's cooldown is over
     *
     * @param player the player
     * @return boolean
     */
    public boolean isCooledDown(T player) {
        if (!cooldowns.containsKey(player) || (((System.currentTimeMillis() - cooldowns.get(player))) >= time)) {
            cooldowns.remove(player);
            return true;
        } else
            return false;
    }

}
