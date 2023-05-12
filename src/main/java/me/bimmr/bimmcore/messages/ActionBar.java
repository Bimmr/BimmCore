package me.bimmr.bimmcore.messages;

import me.bimmr.bimmcore.BimmCore;
import me.bimmr.bimmcore.utils.timed.Timed;
import me.bimmr.bimmcore.utils.timed.TimedEvent;
import me.bimmr.bimmcore.reflection.Packets;
import me.bimmr.bimmcore.reflection.Reflection;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;

/**
 * The type Action bar.
 */
public class ActionBar extends MessageDisplay {

    private static HashMap<String, BukkitTask> tasks = new HashMap<>();
    private static HashMap<String, ActionBar>  bars  = new HashMap<>();

    /**
     * Instantiates a new Action bar.
     *
     * @param text the text
     */
    public ActionBar(String text) {
        this(text, 2, null);
    }

    /**
     * Instantiates a new Action bar.
     *
     * @param text the text
     * @param time the time
     */
    public ActionBar(String text, int time) {
        this(text, time, null);
    }

    /**
     * Instantiates a new Action bar.
     *
     * @param text  the text
     * @param timed the timed
     * @param timer the timer
     */
    public ActionBar(String text, Timed timed, int timer ) {
        this(text, 2, new TimedEvent(timer) {
            @Override
            public void run() {
                timed.onRun(this);
            }
        }, false);
    }

    /**
     * Instantiates a new Action bar.
     *
     * @param text       the text
     * @param timedEvent the timed event
     */
    public ActionBar(String text, TimedEvent timedEvent) {
        this(text, 2, timedEvent, false);
    }

    /**
     * Instantiates a new Action bar.
     *
     * @param text                the text
     * @param timed               the timed
     * @param timer               the timer
     * @param autoStartTimedEvent the auto start timed event
     */
    public ActionBar(String text, Timed timed, int timer, boolean autoStartTimedEvent) {
        this(text, 2, new TimedEvent(timer) {
            @Override
            public void run() {
                timed.onRun(this);
            }
        }, autoStartTimedEvent);
    }

    /**
     * Instantiates a new Action bar.
     *
     * @param text                the text
     * @param timedEvent          the timed event
     * @param autoStartTimedEvent the auto start timed event
     */
    public ActionBar(String text, TimedEvent timedEvent, boolean autoStartTimedEvent) {
        this(text, 2, timedEvent, autoStartTimedEvent);
    }

    /**
     * Instantiates a new Action bar.
     *
     * @param text  the text
     * @param time  the time
     * @param timed the timed
     * @param timer the timer
     */
    public ActionBar(String text, int time, Timed timed, int timer) {
        this(text, time,new TimedEvent(timer) {
            @Override
            public void run() {
                timed.onRun(this);
            }
        }, false);
    }

    /**
     * Instantiates a new Action bar.
     *
     * @param text       the text
     * @param time       the time
     * @param timedEvent the timed event
     */
    public ActionBar(String text, int time, TimedEvent timedEvent) {
        this(text,time,timedEvent, false);
    }

    /**
     * Instantiates a new Action bar.
     *
     * @param text                the text
     * @param time                the time
     * @param timed               the timed
     * @param timer               the timer
     * @param autoStartTimedEvent the auto start timed event
     */
    public ActionBar(String text, int time, Timed timed, int timer, boolean autoStartTimedEvent) {
        this.text = text;
        this.time = time;

        this.setTimedEvent(timed, timer, autoStartTimedEvent);
    }

    /**
     * Instantiates a new Action bar.
     *
     * @param text                the text
     * @param time                the time
     * @param timedEvent          the timed event
     * @param autoStartTimedEvent the auto start timed event
     */
    public ActionBar(String text, int time, TimedEvent timedEvent, boolean autoStartTimedEvent) {
        this.text = text;
        this.time = time;

        setTimedEvent(timedEvent, autoStartTimedEvent);
    }

    private static boolean isRunning(Player player) {
        return tasks.containsKey(player.getName());
    }

    /**
     * Clear.
     *
     * @param player the player
     */
    public static void clear(Player player) {
        if (isRunning(player)) {
                player.spigot().sendMessage(ChatMessageType.ACTION_BAR, new TextComponent(""));

            tasks.get(player.getName()).cancel();
            tasks.remove(player.getName());
            bars.remove(player.getName());
        }
    }

    /**
     * Gets playing action bar.
     *
     * @param player the player
     * @return the playing action bar
     */
    public static ActionBar getPlayingActionBar(Player player) {
        if (isRunning(player))
            return bars.get(player.getName());
        else
            return null;
    }

    @Override
    public String getText() {
        return text;
    }

    @Override
    public void setText(String text) {
        this.text = text;
    }

    @Override
    public int getTime() {
        return time;
    }

    @Override
    public TimedEvent getTimedEvent() {
        return timedEvent;
    }

    public void setTimedEvent(Timed timed, int time) {
        this.setTimedEvent(new TimedEvent(time) {
            @Override
            public void run() {
                timed.onRun(this);
            }
        });
    }
    @Override
    public void setTimedEvent(TimedEvent timedEvent) {
        if (timedEvent != null) {
            this.timedEvent = timedEvent;
            this.timedEvent.setAttachedObject(this);
        }
    }

    @Override
    public void stop(Player player) {
        clear(player);
    }

    @Override
    public void send(final Player player) {
        clear(player);
        bars.put(player.getName(), this);
        tasks.put(player.getName(), new BukkitRunnable() {
            int timeLeft = time * (time == Integer.MAX_VALUE ? 1 : 20);

            @Override
            public void run() {
                if (timedEvent != null && timeLeft % timedEvent.getTicks() == 0)
                    timedEvent.run();

                if (timeLeft <= 0)
                    clear(player);

                else if (timeLeft % 20 == 0 || (timedEvent != null && timeLeft % timedEvent.getTicks() == 0))
                        player.spigot().sendMessage(ChatMessageType.ACTION_BAR, new TextComponent(text));

                timeLeft--;
            }
        }.runTaskTimer(BimmCore.getInstance(), 0L, 1L));
    }

}
