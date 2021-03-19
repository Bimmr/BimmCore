package me.bimmr.bimmcore.utils.timed;

@FunctionalInterface
public interface Timed<T> {

    void onRun(TimedEvent<T> timedEvent);
}
