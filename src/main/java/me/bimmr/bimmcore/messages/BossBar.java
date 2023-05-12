package me.bimmr.bimmcore.messages;

import me.bimmr.bimmcore.BimmCore;
import me.bimmr.bimmcore.utils.timed.TimedEvent;
import org.bukkit.Bukkit;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import java.util.HashMap;

/**
 * The type Boss bar.
 */
public class BossBar extends MessageDisplay {

    private static HashMap<String, BossBar> bars = new HashMap<>();
    private static HashMap<String, BukkitTask> task = new HashMap<>();
    private org.bukkit.boss.BossBar bar;

    /**
     * Instantiates a new Boss bar.
     *
     * @param text the text
     */
    public BossBar(String text) {
        this(text, 2, BarColor.WHITE, BarStyle.SOLID, 1.0, null);
    }


    /**
     * Instantiates a new Boss bar.
     *
     * @param text the text
     * @param time the time
     */
    public BossBar(String text, int time) {
        this(text, time, BarColor.WHITE, BarStyle.SOLID, 1.0, null);
    }

    /**
     * Instantiates a new Boss bar.
     *
     * @param text     the text
     * @param time     the time
     * @param barColor the bar color
     * @param barStyle the bar style
     */
    public BossBar(String text, int time, BarColor barColor, BarStyle barStyle) {
        this(text, time, barColor, barStyle, 1.0, null);
    }

    /**
     * Instantiates a new Boss bar.
     *
     * @param text     the text
     * @param time     the time
     * @param barColor the bar color
     * @param barStyle the bar style
     * @param progress the progress
     */
    public BossBar(String text, int time, BarColor barColor, BarStyle barStyle, Double progress) {
        this(text, time, barColor, barStyle, progress, null);
    }

    /**
     * Instantiates a new Boss bar.
     *
     * @param text       the text
     * @param timedEvent the timed event
     */
    public BossBar(String text, TimedEvent timedEvent) {
        this(text, 2, BarColor.WHITE, BarStyle.SOLID, 1.0, timedEvent);
    }

    /**
     * Instantiates a new Boss bar.
     *
     * @param text       the text
     * @param time       the time
     * @param timedEvent the timed event
     */
    public BossBar(String text, int time, TimedEvent timedEvent) {
        this(text, time, BarColor.WHITE, BarStyle.SOLID, 1.0, timedEvent);
    }

    /**
     * Instantiates a new Boss bar.
     *
     * @param text       the text
     * @param time       the time
     * @param barColor   the bar color
     * @param barStyle   the bar style
     * @param timedEvent the timed event
     */
    public BossBar(String text, int time, BarColor barColor, BarStyle barStyle, TimedEvent timedEvent) {
        this(text, time, barColor, barStyle, 1.0, timedEvent);
    }

    /**
     * Instantiates a new Boss bar.
     *
     * @param text       the text
     * @param time       the time
     * @param barColor   the bar color
     * @param barStyle   the bar style
     * @param progress   the progress
     * @param timedEvent the timed event
     */
    public BossBar(String text, int time, BarColor barColor, BarStyle barStyle, Double progress, TimedEvent timedEvent) {
        bar = Bukkit.createBossBar(text, barColor, BarStyle.SOLID);
        bar.setProgress(progress);
        this.time = time;
        setTimedEvent(timedEvent);
    }

    /**
     * Clear.
     *
     * @param player the player
     */
    public static void clear(Player player) {
        if (isRunning(player)) {
            getBossBar(player).getBukkitBar().removePlayer(player);
            bars.get(player.getName()).getBukkitBar().removePlayer(player);
            task.get(player.getName()).cancel();
            bars.remove(player.getName());
            task.remove(player.getName());
        }
    }

    /**
     * Gets boss bar.
     *
     * @param player the player
     * @return the boss bar
     */
    public static BossBar getBossBar(Player player) {
        if (isRunning(player))
            return bars.get(player.getName());
        else
            return null;
    }

    private static boolean isRunning(Player player) {
        return bars.containsKey(player.getName());
    }

    @Override
    public int getTime() {
        return time;
    }

    @Override
    public void stop(Player player) {
        clear(player);
    }

    /**
     * Gets bukkit bar.
     *
     * @return the bukkit bar
     */
    public org.bukkit.boss.BossBar getBukkitBar() {
        return this.bar;
    }

    @Override
    public TimedEvent getTimedEvent() {
        return this.timedEvent;
    }

    @Override
    public void setTimedEvent(TimedEvent timedEvent) {
        if (timedEvent != null) {
            this.timedEvent = timedEvent;
            this.timedEvent.setAttachedObject(this);
        }
    }

    @Override
    public String getText() {
        return getBukkitBar().getTitle();
    }

    @Override
    public void setText(String text) {
        getBukkitBar().setTitle(text);
    }

    @Override
    public void send(final Player player) {
        clear(player);
        bar.addPlayer(player);

        bars.put(player.getName(), this);
        task.put(player.getName(),
                new BukkitRunnable() {
                    int timeLeft = time * 20;

                    @Override
                    public void run() {
                        if (timedEvent != null && timeLeft % timedEvent.getTicks() == 0)
                            timedEvent.run();

                        if (timeLeft <= 0)
                            clear(player);

                        timeLeft--;
                    }
                }.runTaskTimer(BimmCore.getInstance(), 0L, 1L));
    }
}
