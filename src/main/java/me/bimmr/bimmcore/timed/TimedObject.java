package me.bimmr.bimmcore.timed;

import me.bimmr.bimmcore.BimmCore;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

public abstract class TimedObject {
    public TimedEvent timedEvent;

    private BukkitTask task;

    public TimedEvent getTimedEvent() {
        return this.timedEvent;
    }

    public void setTimedEvent(TimedEvent timedEvent) {
        setTimedEvent(timedEvent, false);
    }

    public void setTimedEvent(TimedEvent timedEvent, boolean autoStart) {
        if (timedEvent != null) {
            this.timedEvent = timedEvent;
            this.timedEvent.setAttachedObject(this);
            if (autoStart)
                startTask();
        }
    }

    public void startTask() {
        this.task = (new BukkitRunnable() {
            long time = 0L;

            public void run() {
                TimedObject.this.onTaskRun(this.time);
                if (TimedObject.this.timedEvent != null && this.time % TimedObject.this.timedEvent.getTicks() == 0L)
                    TimedObject.this.timedEvent.run();
                if (this.time == 2147483647L)
                    this.time = 0L;
                this.time++;
            }
        }).runTaskTimer((Plugin) BimmCore.getInstance(), 0L, 1L);
    }

    public void onTaskRun(long time) {}

    public void stopTask() {
        if (this.task != null)
            this.task.cancel();
        this.task = null;
    }

    public boolean isTaskRunning() {
        return (this.task != null);
    }
}
