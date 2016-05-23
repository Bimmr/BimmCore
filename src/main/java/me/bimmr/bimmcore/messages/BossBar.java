package me.bimmr.bimmcore.messages;

import me.bimmr.bimmcore.BimmCore;
import me.bimmr.bimmcore.events.timing.TimedEvent;
import org.bukkit.Bukkit;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import java.util.HashMap;

/**
 * Created by Randy on 05/10/16.
 */
public class BossBar extends MessageDisplay {

    private static HashMap<String, BossBar>    bars = new HashMap<>();
    private static HashMap<String, BukkitTask> task = new HashMap<>();

    private org.bukkit.boss.BossBar bar;
    private int        time;

    /**
     * Create a BossBar
     *
     * @param text
     * @param time
     * @param barColor
     * @param barStyle
     * @param progress
     */
    public BossBar(String text, int time, BarColor barColor, BarStyle barStyle, Double progress) {
        bar = Bukkit.createBossBar(text, barColor, BarStyle.SOLID);
        bar.setProgress(progress);
        this.time = time;
    }
    public BossBar(String text, int time, BarColor barColor, BarStyle barStyle, Double progress, TimedEvent timedEvent) {
        bar = Bukkit.createBossBar(text, barColor, BarStyle.SOLID);
        bar.setProgress(progress);
        this.time = time;
        setTimedEvent(timedEvent);
    }

    /**
     * Create a BossBar
     * BarStyle defaults to solid
     * Progress defaults to 1.0
     *
     * @param text
     * @param time
     * @param barColor
     */
    public BossBar(String text, int time, BarColor barColor) {
        bar = Bukkit.createBossBar(text, barColor, BarStyle.SOLID);
        bar.setProgress(1.0);
        this.time = time;
    }

    public BossBar(String text, int time, BarColor barColor, TimedEvent timedEvent) {
        bar = Bukkit.createBossBar(text, barColor, BarStyle.SOLID);
        bar.setProgress(1.0);
        this.time = time;
        setTimedEvent(timedEvent);
    }

    /**
     * Create a BossBar
     *
     * @param text
     * @param barColor
     * @param barStyle
     * @param progress
     */
    public BossBar(String text, BarColor barColor, BarStyle barStyle, Double progress) {
        bar = Bukkit.createBossBar(text, barColor, BarStyle.SOLID);
        bar.setProgress(progress);
        this.time = 2;
    }

    public BossBar(String text, BarColor barColor, BarStyle barStyle, Double progress, TimedEvent timedEvent) {
        bar = Bukkit.createBossBar(text, barColor, BarStyle.SOLID);
        bar.setProgress(progress);
        this.time = 2;
        setTimedEvent(timedEvent);
    }

    public BossBar(String text, BarColor barColor) {
        bar = Bukkit.createBossBar(text, barColor, BarStyle.SOLID);
        bar.setProgress(1);
        this.time = 2;
    }

    public BossBar(String text, BarColor barColor, TimedEvent timedEvent) {
        bar = Bukkit.createBossBar(text, barColor, BarStyle.SOLID);
        bar.setProgress(1);
        this.time = 2;
        setTimedEvent(timedEvent);
    }

    public BossBar(String text, int time) {
        bar = Bukkit.createBossBar(text, BarColor.WHITE, BarStyle.SOLID);
        bar.setProgress(1);
        this.time = time;
    }

    public BossBar(String text, int time, TimedEvent timedEvent) {
        bar = Bukkit.createBossBar(text, BarColor.WHITE, BarStyle.SOLID);
        bar.setProgress(1);
        this.time = time;
        setTimedEvent(timedEvent);
    }

    public BossBar(String text) {
        bar = Bukkit.createBossBar(text, BarColor.WHITE, BarStyle.SOLID);
        bar.setProgress(1);
        this.time = 2;
    }

    public BossBar(String text, TimedEvent timedEvent) {
        bar = Bukkit.createBossBar(text, BarColor.WHITE, BarStyle.SOLID);
        bar.setProgress(1);
        this.time = 2;
        setTimedEvent(timedEvent);
    }

    /**
     * Clear the BossBar being shown to the player
     *
     * @param player
     */
    public static void clear(Player player) {
        if (isRunning(player)) {
            bars.get(player.getName()).getBukkitBar().removePlayer(player);
            task.get(player.getName()).cancel();
            bars.remove(player.getName());
            task.remove(player.getName());
        }
    }

    /**
     * Get the BossBar being shown to the player
     *
     * @param player
     * @return
     */
    public static BossBar getBossBar(Player player) {
        if (isRunning(player))
            return bars.get(player.getName());
        else
            return null;
    }

    /**
     * Check if a BossBar is being sent to the player
     *
     * @param player
     * @return
     */
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

    public org.bukkit.boss.BossBar getBukkitBar() {
        return this.bar;
    }

    @Override
    public TimedEvent getTimedEvent() {
        return this.timedEvent;
    }

    /**
     * A function that gets called every second that the BossBar is active for
     *
     * @param timedEvent
     */
    @Override
    public void setTimedEvent(TimedEvent timedEvent) {
        this.timedEvent = timedEvent;
        this.timedEvent.setAttachedObject(this);
    }

    @Override
    public String getText() {
        return getBukkitBar().getTitle();
    }

    @Override
    public void setText(String text) {
        getBukkitBar().setTitle(text);
    }

    /**
     * Sends a BossBar for a set amount of time
     *
     * @param player
     */
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
