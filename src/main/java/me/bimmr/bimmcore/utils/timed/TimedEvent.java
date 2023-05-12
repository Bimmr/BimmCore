package me.bimmr.bimmcore.utils.timed;

/**
 * The type Timed event.
 *
 * @param <T> the type parameter
 */
public abstract class TimedEvent<T> implements Cloneable {

    private T attachedObject;
    private int    ticks;

    /**
     * Instantiates a new Timed event.
     *
     * @param ticks          the ticks
     * @param attachedObject the attached object
     */
    public TimedEvent(int ticks, T attachedObject) {
        this.ticks = ticks;
        this.attachedObject = attachedObject;
    }


    /**
     * Instantiates a new Timed event.
     *
     * @param ticks the ticks
     */
    public TimedEvent(int ticks) {
        this.ticks = ticks;
    }

    /**
     * Gets attached object.
     *
     * @return the attached object
     */
    public T getAttachedObject() {
        return this.attachedObject;
    }

    /**
     * Sets attached object.
     *
     * @param attachedObject the attached object
     */
    public void setAttachedObject(T attachedObject) {
        this.attachedObject = attachedObject;
    }

    /**
     * Gets ticks.
     *
     * @return the ticks
     */
    public int getTicks() {
        return this.ticks;
    }

    /**
     * Sets ticks.
     *
     * @param ticks the ticks
     */
    public void setTicks(int ticks) {
        this.ticks = ticks;
    }

    /**
     * Run.
     */
    public abstract void run();


    public TimedEvent<T> clone() {
        try {
            return (TimedEvent<T>) super.clone();
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }
        return null;
    }
}
