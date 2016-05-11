package me.bimmr.bimmcore.messages;

import me.bimmr.bimmcore.BimmCore;
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
public class BossBar {

    private static HashMap<String, BossBar>    bars = new HashMap<>();
    private static HashMap<String, BukkitTask> task = new HashMap<>();

    private org.bukkit.boss.BossBar bar;
    private int                     time;
    private SecondEvent             secondEvent;

    public BossBar(String text, int time, BarColor barColor, BarStyle barStyle, Double progress) {
        bar = Bukkit.createBossBar(text, barColor, BarStyle.SOLID);
        bar.setProgress(progress);
        this.time = time;
    }

    public BossBar(String text, int time, BarColor barColor) {
        bar = Bukkit.createBossBar(text, barColor, BarStyle.SOLID);
        bar.setProgress(1);
        this.time = time;
    }

    public BossBar(String text, BarColor barColor, BarStyle barStyle, Double progress) {
        bar = Bukkit.createBossBar(text, barColor, BarStyle.SOLID);
        bar.setProgress(progress);
        this.time = Integer.MAX_VALUE;
    }

    public BossBar(String text, BarColor barColor) {
        bar = Bukkit.createBossBar(text, barColor, BarStyle.SOLID);
        bar.setProgress(1);
        this.time = Integer.MAX_VALUE;
    }
    public BossBar(String text, int time){
        bar = Bukkit.createBossBar(text, BarColor.WHITE, BarStyle.SOLID);
        bar.setProgress(1);
        this.time = time;
    }

    /**
     * Clear the BossBar being shown to the player
     *
     * @param player
     */
    public static void clear(Player player) {
        if (isRunning(player)) {
            bars.get(player.getName()).getBukkitBar().removePlayer(player);
            stop(player);
            bars.remove(player.getName());
            task.remove(player.getName());
        }
    }

    /**
     * Stop showing an title
     *
     * @param player
     */
    private static void stop(Player player) {
        task.get(player.getName()).cancel();
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

    public org.bukkit.boss.BossBar getBukkitBar() {
        return this.bar;
    }

    /**
     * A function that gets called every second that the BossBar is active for
     *
     * @param secondEvent
     */
    public void setSecondEvent(SecondEvent secondEvent) {
        this.secondEvent = secondEvent;
    }

    /**
     * Sends a BossBar for a set amount of time
     *
     * @param player
     */
    public void send(final Player player) {
        clear(player);

        bars.put(player.getName(), this);
        task.put(player.getName(),
                new BukkitRunnable() {
                    int timeLeft = time;

                    @Override
                    public void run() {
                        if (secondEvent != null)
                            secondEvent.run();

                        if (timeLeft <= 0)
                            clear(player);
                        else
                            timeLeft--;
                    }
                }.runTaskTimer(BimmCore.getInstance(), 0L, 20L));
    }
}
