package me.bimmr.bimmcore.utils.timed;

@FunctionalInterface
public interface Timed {

    void onRun(TimedEvent timedEvent);
}
