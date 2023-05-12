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
 * A utilities class to create a title
 */
public class Title extends MessageDisplay {
    private static HashMap<String, BukkitTask> tasks = new HashMap<>();
    private static HashMap<String, Title> titles = new HashMap<>();
    private String subTitle = "";
    private int fadeIn = 0;
    private int fadeOut = 0;

    /**
     * Create a title
     *
     * @param title the title
     */
    public Title(String title) {
        this(title, "", 0, 2, 0, null);
    }

    /**
     * Create a title
     *
     * @param title the title
     * @param show  the show
     */
    public Title(String title, int show) {
        this(title, "", 0, show, 0, null);
    }

    /**
     * Create a title
     *
     * @param title    the title
     * @param subTitle the sub title
     */
    public Title(String title, String subTitle) {
        this(title, subTitle, 0, 2, 0, null);
    }

    /**
     * Create a title
     *
     * @param title    the title
     * @param subTitle the sub title
     * @param show     the show
     */
    public Title(String title, String subTitle, int show) {
        this(title, subTitle, 0, show, 0, null);
    }

    /**
     * Create a title
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
     * Create a title
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
     * Create a title
     *
     * @param title      the title
     * @param timedEvent the timed event
     */
    public Title(String title, TimedEvent timedEvent) {
        this(title, "", 0, 2, 0, timedEvent);
    }

    /**
     * Create a title
     *
     * @param title      the title
     * @param show       the show
     * @param timedEvent the timed event
     */
    public Title(String title, int show, TimedEvent timedEvent) {
        this(title, "", 0, show, 0, timedEvent);
    }

    /**
     * Create a title
     *
     * @param title      the title
     * @param subTitle   the sub title
     * @param timedEvent the timed event
     */
    public Title(String title, String subTitle, TimedEvent timedEvent) {
        this(title, subTitle, 0, 2, 0, timedEvent);
    }

    /**
     * Create a title
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
     * Create a title
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
     * Create a title
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

    /**
     * Check if a Title is being sent to the player
     *
     * @param player
     * @return
     */
    private static boolean isRunning(Player player) {
        return titles.containsKey(player.getName());
    }

    /**
     * Get the SubTitle text
     *
     * @return sub title
     */
    public String getSubTitle() {
        return this.subTitle;
    }

    /**
     * Set the SubTitle text
     *
     * @param text the text
     */
    public void setSubTitle(String text) {
        this.subTitle = text;
    }

    /**
     * Get the Title text
     *
     * @return the text
     */
    @Override
    public String getText() {
        return this.text;
    }

    /**
     * Set the Title text
     *
     * @param text the text
     */
    @Override
    public void setText(String text) {
        this.text = text;
    }

    /**
     * Get the FadeIn time
     *
     * @return fade in
     */
    public int getFadeIn() {
        return this.fadeIn;
    }

    /**
     * Set the FadeIn time
     *
     * @param fadeIn the fade in
     */
    public void setFadeIn(int fadeIn) {
        this.fadeIn = fadeIn;
    }

    /**
     * Get the FadeOut time
     *
     * @return fade out
     */
    public int getFadeOut() {
        return this.fadeOut;
    }

    /**
     * Set the FadeOut time
     *
     * @param fadeOut the fade out
     */
    public void setFadeOut(int fadeOut) {
        this.fadeOut = fadeOut;
    }

    /**
     * Get the Show time
     *
     * @return the time
     */
    @Override
    public int getTime() {
        return this.time;
    }

    /**
     * Send the Title to the player
     *
     * @param player the player
     */
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

    /**
     * Stop sending the title to the player
     *
     * @param player the player
     */
    @Override
    public void stop(Player player) {
        clear(player);
    }

    /**
     * Get the TimedEvent
     *
     * @return timed event
     */
    @Override
    public TimedEvent getTimedEvent() {
        return timedEvent;
    }

    /**
     * Set the TimedEvent
     *
     * @param timedEvent the timed event
     */
    @Override
    public void setTimedEvent(TimedEvent timedEvent) {
        if (timedEvent != null) {
            this.timedEvent = timedEvent;
            this.timedEvent.setAttachedObject(this);
        }
    }

    /**
     * Clear the title off the player's screen
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
