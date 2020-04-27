package me.bimmr.bimmcore.timed;

/**
 * Created by Randy on 05/23/16.
 */
public abstract class TimedEvent implements Cloneable {

    private Object attachedObject;
    private int    ticks;

    public TimedEvent(int ticks, Object attachedObject) {
        this.ticks = ticks;
        this.attachedObject = attachedObject;
    }


    public TimedEvent(int ticks) {
        this.ticks = ticks;
    }

    /**
     * Get the object this TimedEvent is attached to
     *
     * @return
     */
    public Object getAttachedObject() {
        return this.attachedObject;
    }

    /**
     * Set the attached object
     *
     * @param attachedObject
     */
    public void setAttachedObject(Object attachedObject) {
        this.attachedObject = attachedObject;
    }

    /**
     * Get the ticks
     *
     * @return
     */
    public int getTicks() {
        return this.ticks;
    }

    /**
     * Set the ticks
     *
     * @param ticks
     */
    public void setTicks(int ticks) {
        this.ticks = ticks;
    }

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
