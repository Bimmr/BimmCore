package me.bimmr.bimmcore;

import java.util.HashMap;

/**
 * An easy to use Cooldown class
 */
public class Cooldown {

    private HashMap<String, Long> cooldowns = new HashMap<String, Long>();
    // Change time to how ever long you want
    private long time;

    public Cooldown(long time) {
        this.time = time;
    }

    /**
     * Add the player to the cooldown
     *
     * @param player
     */
    public void addToCooldown(String player) {
        cooldowns.put(player, System.currentTimeMillis());
    }

    /**
     * Get the remaining seconds
     *
     * @param player
     * @return
     */
    public long getTimeRemaining(String player) {
        return time - ((System.currentTimeMillis() - cooldowns.get(player)) / 1000);
    }

    /**
     * Gets if the player's cooldown is over
     *
     * @param player
     * @return
     */
    public boolean isCooledDown(String player) {
        if (!cooldowns.containsKey(player) || (((System.currentTimeMillis() - cooldowns.get(player)) / 1000) >= time))
            return true;
        else return false;
    }

}
