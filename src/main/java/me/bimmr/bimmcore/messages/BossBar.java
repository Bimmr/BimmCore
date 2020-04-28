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
 * Created by Randy on 05/10/16.
 */
//
//class BossBarExample {
//
//    public BossBarExample() {
//
//        //Create a scroller so the timed event has something to do
//        final Scroller scroller = new Scroller("Testing Bossbar", 10, 3);
//
//        //Create the timed event
//        TimedEvent timedEvent = new TimedEvent(1) {
//            @Override
//            public void run() {
//                MessageDisplay display = (MessageDisplay) getAttachedObject();
//                display.setText(scroller.next());
//            }
//        };
//
//        //Create the title
//        MessageDisplay display = new BossBar(scroller.current(), 10, timedEvent);
//
//        //Send the title
//        display.send(null);
//    }
//
//}

public class BossBar extends MessageDisplay {

    private static HashMap<String, BossBar> bars = new HashMap<>();
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
     * @param text The Text
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
     * @param text The Text
     * @param time The Time
     */
    public BossBar(String text, int time) {
        this(text, time, BarColor.WHITE, BarStyle.SOLID, 1.0, null);
    }

    /**
     * Create a BossBar
     * progress = 1.0
     *
     * @param text     The text
     * @param time     The time
     * @param barColor the Barcolor
     * @param barStyle The Barstyle
     */
    public BossBar(String text, int time, BarColor barColor, BarStyle barStyle) {
        this(text, time, barColor, barStyle, 1.0, null);
    }

    /**
     * Create a BossBar
     *
     * @param text     The text
     * @param time     The Time
     * @param barColor The Barcolor
     * @param barStyle The BarStyle
     * @param progress The progress
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
     * @param text       The text
     * @param timedEvent The TimedEvent
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
     * @param text       The text
     * @param time       The time
     * @param timedEvent The TimedEvent
     */
    public BossBar(String text, int time, TimedEvent timedEvent) {
        this(text, time, BarColor.WHITE, BarStyle.SOLID, 1.0, timedEvent);
    }

    /**
     * Create a BossBar
     * progress = 1.0
     *
     * @param text       The text
     * @param time       The time
     * @param barColor   The bar color
     * @param barStyle   The bar style
     * @param timedEvent The TimedEvent
     */
    public BossBar(String text, int time, BarColor barColor, BarStyle barStyle, TimedEvent timedEvent) {
        this(text, time, barColor, barStyle, 1.0, timedEvent);
    }

    /**
     * Create a BossBar
     *
     * @param text       The text
     * @param time       The time
     * @param barColor   The bar color
     * @param barStyle   The bar style
     * @param progress   The bar's progress
     * @param timedEvent The TimedEvent
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
     * @param player The player
     * @return Get the BossBar being shown to the player
     */
    public static BossBar getBossBar(Player player) {
        if (isRunning(player))
            return bars.get(player.getName());
        else
            return null;
    }

    /**
     * @param player The player
     * @return Check if a BossBar is being sent to the player
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
     * @param player The player
     */
    @Override
    public void stop(Player player) {
        clear(player);
    }

    /**
     * @return Get the BukkitBossBar
     */
    public org.bukkit.boss.BossBar getBukkitBar() {
        return this.bar;
    }

    /**
     * @return Get the TimedEvent
     */
    @Override
    public TimedEvent getTimedEvent() {
        return this.timedEvent;
    }

    /**
     * Set the TimedEvent
     *
     * @param timedEvent The TimedEvent
     */
    @Override
    public void setTimedEvent(TimedEvent timedEvent) {
        if (timedEvent != null) {
            this.timedEvent = timedEvent;
            this.timedEvent.setAttachedObject(this);
        }
    }

    /**
     * @return Get the text
     */
    @Override
    public String getText() {
        return getBukkitBar().getTitle();
    }

    /**
     * Set the text
     *
     * @param text The text
     */
    @Override
    public void setText(String text) {
        getBukkitBar().setTitle(text);
    }

    /**
     * Sends a BossBar for a set amount of time
     *
     * @param player The player
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

    private class BossBarOldAPI {

    }
}
