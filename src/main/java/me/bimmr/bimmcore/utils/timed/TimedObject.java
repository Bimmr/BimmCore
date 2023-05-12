package me.bimmr.bimmcore.utils.timed;

import me.bimmr.bimmcore.BimmCore;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

/**
 * The type Timed object.
 */
public abstract class TimedObject {
    /**
     * The Timed event.
     */
    public TimedEvent timedEvent;

    private BukkitTask task;

    /**
     * Gets timed event.
     *
     * @return the timed event
     */
    public TimedEvent getTimedEvent() {
        return this.timedEvent;
    }

    /**
     * Sets timed event.
     *
     * @param timedEvent the timed event
     */
    public void setTimedEvent(TimedEvent timedEvent) {
        setTimedEvent(timedEvent, false);
    }

    /**
     * Sets timed event.
     *
     * @param timed the timed
     * @param time  the time
     */
    public void setTimedEvent(Timed timed, int time) {
        setTimedEvent(timed, time, false);
    }

    /**
     * Sets timed event.
     *
     * @param timed     the timed
     * @param time      the time
     * @param autoStart the auto start
     */
    public void setTimedEvent(Timed timed, int time, boolean autoStart) {
        TimedEvent timedEvent = new TimedEvent(time) {
            @Override
            public void run() {
                timed.onRun(this);
            }
        };
        setTimedEvent(timedEvent, autoStart);
    }

    /**
     * Sets timed event.
     *
     * @param timedEvent the timed event
     * @param autoStart  the auto start
     */
    public void setTimedEvent(TimedEvent timedEvent, boolean autoStart) {
        if (timedEvent != null) {
            this.timedEvent = timedEvent;
            this.timedEvent.setAttachedObject(this);
            if (autoStart)
                startTask();
        }
    }

    /**
     * Start task.
     */
    public void startTask() {

        if (TimedObject.this.timedEvent != null)
            TimedObject.this.timedEvent.run();
        this.task = (new BukkitRunnable() {
            long time = 0L;

            public void run() {
                TimedObject.this.onTaskRun(this.time);

                TimedObject.this.timedEvent.run();
                if (this.time == 2147483647L)
                    this.time = 0L;
                this.time++;
            }
        }).runTaskTimer(BimmCore.getInstance(), 0L, TimedObject.this.timedEvent.getTicks());
    }

    /**
     * On task run.
     *
     * @param time the time
     */
    public void onTaskRun(long time) {
    }

    /**
     * Stop task.
     */
    public void stopTask() {
        if (this.task != null)
            this.task.cancel();
        this.task = null;
    }

    /**
     * Is task running boolean.
     *
     * @return the boolean
     */
    public boolean isTaskRunning() {
        return (this.task != null);
    }
}
