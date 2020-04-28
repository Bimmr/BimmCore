package me.bimmr.bimmcore.utils.timed;

/**
 * Created by Randy on 05/23/16.
 */
public abstract class TimedEvent implements Cloneable {

    private Object attachedObject;
    private int    ticks;

    /**
     * Instantiates a new Timed event.
     *
     * @param ticks          the ticks
     * @param attachedObject the attached object
     */
    public TimedEvent(int ticks, Object attachedObject) {
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
     * Get the object this TimedEvent is attached to
     *
     * @return attached object
     */
    public Object getAttachedObject() {
        return this.attachedObject;
    }

    /**
     * Set the attached object
     *
     * @param attachedObject the attached object
     */
    public void setAttachedObject(Object attachedObject) {
        this.attachedObject = attachedObject;
    }

    /**
     * Get the ticks
     *
     * @return ticks
     */
    public int getTicks() {
        return this.ticks;
    }

    /**
     * Set the ticks
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


    public TimedEvent clone() {
        try {
            return (TimedEvent) super.clone();
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }
        return null;
    }
}
