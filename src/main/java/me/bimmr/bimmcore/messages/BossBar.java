package me.bimmr.bimmcore.messages;

import me.bimmr.bimmcore.BimmCore;
import me.bimmr.bimmcore.Scroller;
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

class BossBarExample {

    public BossBarExample() {

        //Create a scroller so the timed event has something to do
        final Scroller scroller = new Scroller("Testing Bossbar", 10, 3);

        //Create the timed event
        TimedEvent timedEvent = new TimedEvent(1) {
            @Override
            public void run() {
                MessageDisplay display = (MessageDisplay) getAttachedObject();
                display.setText(scroller.next());
            }
        };

        //Create the title
        MessageDisplay display = new BossBar(scroller.current(), 10, timedEvent);

        //Send the title
        display.send(null);
    }

}
public class BossBar extends MessageDisplay {

    private static HashMap<String, BossBar>    bars = new HashMap<>();
    private static HashMap<String, BukkitTask> task = new HashMap<>();
    private org.bukkit.boss.BossBar bar;

    /**
     * Create a Bossbar
     * <p>
     * time = 2
     * color = white
     * style = solid
     * progress = 1.0
     *
     * @param text
     */
    public BossBar(String text) {
        this(text, 2, BarColor.WHITE, BarStyle.SOLID, 1.0, null);
    }


    /**
     * Create a BossBar
     * <p>
     * color = white
     * style = solid
     * progress = 1.0
     *
     * @param text
     * @param time
     */
    public BossBar(String text, int time) {
        this(text, time, BarColor.WHITE, BarStyle.SOLID, 1.0, null);
    }

    /**
     * Create a BossBar
     * progress = 1.0
     *
     * @param text
     * @param time
     * @param barColor
     * @param barStyle
     */
    public BossBar(String text, int time, BarColor barColor, BarStyle barStyle) {
        this(text, time, barColor, barStyle, 1.0, null);
    }

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
        this(text, time, barColor, barStyle, progress, null);
    }

    /**
     * Create a Bossbar
     * <p>
     * time = 2
     * color = white
     * style = solid
     * progress = 1.0
     *
     * @param text
     * @param timedEvent
     */
    public BossBar(String text, TimedEvent timedEvent) {
        this(text, 2, BarColor.WHITE, BarStyle.SOLID, 1.0, timedEvent);
    }

    /**
     * Create a BossBar
     * <p>
     * color = white
     * style = solid
     * progress = 1.0
     *
     * @param text
     * @param time
     * @param timedEvent
     */
    public BossBar(String text, int time, TimedEvent timedEvent) {
        this(text, time, BarColor.WHITE, BarStyle.SOLID, 1.0, timedEvent);
    }

    /**
     * Create a BossBar
     * progress = 1.0
     *
     * @param text
     * @param time
     * @param barColor
     * @param barStyle
     * @param timedEvent
     */
    public BossBar(String text, int time, BarColor barColor, BarStyle barStyle, TimedEvent timedEvent) {
        this(text, time, barColor, barStyle, 1.0, timedEvent);
    }

    /**
     * Create a BossBar
     *
     * @param text
     * @param time
     * @param barColor
     * @param barStyle
     * @param progress
     * @param timedEvent
     */
    public BossBar(String text, int time, BarColor barColor, BarStyle barStyle, Double progress, TimedEvent timedEvent) {
        bar = Bukkit.createBossBar(text, barColor, BarStyle.SOLID);
        bar.setProgress(progress);
        this.time = time;
        setTimedEvent(timedEvent);
    }

    /**
     * Clear the BossBar being shown to the player
     *
     * @param player
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

    /**
     * Get the time
     *
     * @return
     */
    @Override
    public int getTime() {
        return time;
    }

    /**
     * Clear the bossbar off the players screen
     *
     * @param player
     */
    @Override
    public void stop(Player player) {
        clear(player);
    }

    /**
     * Get the BukkitBossBar
     *
     * @return
     */
    public org.bukkit.boss.BossBar getBukkitBar() {
        return this.bar;
    }

    /**
     * Get the TimedEvent
     *
     * @return
     */
    @Override
    public TimedEvent getTimedEvent() {
        return this.timedEvent;
    }

    /**
     * Set the TimedEvent
     *
     * @param timedEvent
     */
    @Override
    public void setTimedEvent(TimedEvent timedEvent) {
        if (timedEvent != null) {
            this.timedEvent = timedEvent;
            this.timedEvent.setAttachedObject(this);
        }
    }

    /**
     * Get the text
     *
     * @return
     */
    @Override
    public String getText() {
        return getBukkitBar().getTitle();
    }

    /**
     * Get the text
     *
     * @param text
     */
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
