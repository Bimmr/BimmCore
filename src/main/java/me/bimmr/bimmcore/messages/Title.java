package me.bimmr.bimmcore.messages;

import me.bimmr.bimmcore.BimmCore;
import me.bimmr.bimmcore.utils.timed.TimedEvent;
import me.bimmr.bimmcore.reflection.Packets;
import me.bimmr.bimmcore.reflection.Reflection;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;

/**
 * The type Title.
 */
public class Title extends MessageDisplay {
    private static HashMap<String, BukkitTask> tasks = new HashMap<>();
    private static HashMap<String, Title> titles = new HashMap<>();
    private String subTitle = "";
    private int fadeIn = 0;
    private int fadeOut = 0;

    /**
     * Instantiates a new Title.
     *
     * @param title the title
     */
    public Title(String title) {
        this(title, "", 0, 2, 0, null);
    }

    /**
     * Instantiates a new Title.
     *
     * @param title the title
     * @param show  the show
     */
    public Title(String title, int show) {
        this(title, "", 0, show, 0, null);
    }

    /**
     * Instantiates a new Title.
     *
     * @param title    the title
     * @param subTitle the sub title
     */
    public Title(String title, String subTitle) {
        this(title, subTitle, 0, 2, 0, null);
    }

    /**
     * Instantiates a new Title.
     *
     * @param title    the title
     * @param subTitle the sub title
     * @param show     the show
     */
    public Title(String title, String subTitle, int show) {
        this(title, subTitle, 0, show, 0, null);
    }

    /**
     * Instantiates a new Title.
     *
     * @param title         the title
     * @param subTitle      the sub title
     * @param animationTime the animation time
     * @param show          the show
     */
    public Title(String title, String subTitle, int animationTime, int show) {
        this(title, subTitle, animationTime, show, animationTime, null);
    }

    /**
     * Instantiates a new Title.
     *
     * @param title    the title
     * @param subTitle the sub title
     * @param fadeIn   the fade in
     * @param show     the show
     * @param fadeOut  the fade out
     */
    public Title(String title, String subTitle, int fadeIn, int show, int fadeOut) {
        this(title, subTitle, fadeIn, show, fadeOut, null);
    }

    /**
     * Instantiates a new Title.
     *
     * @param title      the title
     * @param timedEvent the timed event
     */
    public Title(String title, TimedEvent timedEvent) {
        this(title, "", 0, 2, 0, timedEvent);
    }

    /**
     * Instantiates a new Title.
     *
     * @param title      the title
     * @param show       the show
     * @param timedEvent the timed event
     */
    public Title(String title, int show, TimedEvent timedEvent) {
        this(title, "", 0, show, 0, timedEvent);
    }

    /**
     * Instantiates a new Title.
     *
     * @param title      the title
     * @param subTitle   the sub title
     * @param timedEvent the timed event
     */
    public Title(String title, String subTitle, TimedEvent timedEvent) {
        this(title, subTitle, 0, 2, 0, timedEvent);
    }

    /**
     * Instantiates a new Title.
     *
     * @param title      the title
     * @param subTitle   the sub title
     * @param show       the show
     * @param timedEvent the timed event
     */
    public Title(String title, String subTitle, int show, TimedEvent timedEvent) {
        this(title, subTitle, 0, show, 0, timedEvent);
    }

    /**
     * Instantiates a new Title.
     *
     * @param title         the title
     * @param subTitle      the sub title
     * @param animationTime the animation time
     * @param show          the show
     * @param timedEvent    the timed event
     */
    public Title(String title, String subTitle, int animationTime, int show, TimedEvent timedEvent) {
        this(title, subTitle, animationTime, show, animationTime, timedEvent);
    }

    /**
     * Instantiates a new Title.
     *
     * @param title      the title
     * @param subTitle   the sub title
     * @param fadeIn     the fade in
     * @param show       the show
     * @param fadeOut    the fade out
     * @param timedEvent the timed event
     */
    public Title(String title, String subTitle, int fadeIn, int show, int fadeOut, TimedEvent timedEvent) {
        this.text = title;
        this.subTitle = subTitle;
        this.fadeIn = fadeIn;
        this.time = show;
        this.fadeOut = fadeOut;
        setTimedEvent(timedEvent);
    }

    private static boolean isRunning(Player player) {
        return titles.containsKey(player.getName());
    }

    /**
     * Gets sub title.
     *
     * @return the sub title
     */
    public String getSubTitle() {
        return this.subTitle;
    }

    /**
     * Sets sub title.
     *
     * @param text the text
     */
    public void setSubTitle(String text) {
        this.subTitle = text;
    }

    @Override
    public String getText() {
        return this.text;
    }

    @Override
    public void setText(String text) {
        this.text = text;
    }

    /**
     * Gets fade in.
     *
     * @return the fade in
     */
    public int getFadeIn() {
        return this.fadeIn;
    }

    /**
     * Sets fade in.
     *
     * @param fadeIn the fade in
     */
    public void setFadeIn(int fadeIn) {
        this.fadeIn = fadeIn;
    }

    /**
     * Gets fade out.
     *
     * @return the fade out
     */
    public int getFadeOut() {
        return this.fadeOut;
    }

    /**
     * Sets fade out.
     *
     * @param fadeOut the fade out
     */
    public void setFadeOut(int fadeOut) {
        this.fadeOut = fadeOut;
    }

    @Override
    public int getTime() {
        return this.time;
    }

    @Override
    public void send(final Player player) {
        clear(player);
        titles.put(player.getName(), this);
        if(getTimedEvent() == null){
                player.sendTitle(text, subTitle, fadeIn*20, time*20, fadeOut*20);
        }else {

            tasks.put(player.getName(), new BukkitRunnable() {
                int timeLeft = time * (time == Integer.MAX_VALUE ? 1 : 20);

                @Override
                public void run() {
                    if (timedEvent != null && timeLeft % timedEvent.getTicks() == 0)
                        timedEvent.run();

                    if (timeLeft <= 0)
                        clear(player);

                    else if (timeLeft % 20 == 0 || (timedEvent != null && timeLeft % timedEvent.getTicks() == 0))
                            player.sendTitle(text, subTitle, timeLeft == time * 20 ? fadeIn * 20 : 0, 20, timeLeft - 20 <= 0 ? fadeOut * 20 : 0);

                    timeLeft--;
                }
            }.runTaskTimer(BimmCore.getInstance(), 0L, 1L));
        }
    }

    @Override
    public void stop(Player player) {
        clear(player);
    }

    @Override
    public TimedEvent getTimedEvent() {
        return timedEvent;
    }

    @Override
    public void setTimedEvent(TimedEvent timedEvent) {
        if (timedEvent != null) {
            this.timedEvent = timedEvent;
            this.timedEvent.setAttachedObject(this);
        }
    }

    /**
     * Clear.
     *
     * @param player the player
     */
    public void clear(Player player) {

        if (isRunning(player)) {
                player.sendTitle("","", 0,0,0);
            if(tasks.containsKey(player.getName()))
                tasks.get(player.getName()).cancel();
            tasks.remove(player.getName());
            titles.remove(player.getName());
        }
    }

}
