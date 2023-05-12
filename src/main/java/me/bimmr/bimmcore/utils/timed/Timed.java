package me.bimmr.bimmcore.utils.timed;

/**
 * The interface Timed.
 *
 * @param <T> the type parameter
 */
@FunctionalInterface
public interface Timed<T> {

    /**
     * On run.
     *
     * @param timedEvent the timed event
     */
    void onRun(TimedEvent<T> timedEvent);
}
