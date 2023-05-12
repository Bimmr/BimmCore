package me.bimmr.bimmcore.misc;

import java.util.HashMap;

/**
 * The type Cooldown.
 *
 * @param <T> the type parameter
 */
public class Cooldown<T> {

    private HashMap<T, Long> cooldowns = new HashMap<>();
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
     * Add to cooldown.
     *
     * @param player the player
     */
    public void addToCooldown(T player) {
        cooldowns.put(player, System.currentTimeMillis());
    }

    /**
     * Gets time remaining.
     *
     * @param player the player
     * @return the time remaining
     */
    public long getTimeRemaining(T player) {
        return time - ((System.currentTimeMillis() - cooldowns.get(player)));
    }

    /**
     * Gets seconds remaining.
     *
     * @param player the player
     * @return the seconds remaining
     */
    public long getSecondsRemaining(T player) {
        return time - ((System.currentTimeMillis() - cooldowns.get(player)) / 1000);
    }

    /**
     * Is cooled down boolean.
     *
     * @param player the player
     * @return the boolean
     */
    public boolean isCooledDown(T player) {
        if (!cooldowns.containsKey(player) || (((System.currentTimeMillis() - cooldowns.get(player))) >= time)) {
            cooldowns.remove(player);
            return true;
        } else
            return false;
    }

}
