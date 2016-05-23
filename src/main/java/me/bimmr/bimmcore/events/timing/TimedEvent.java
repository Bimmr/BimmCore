package me.bimmr.bimmcore.events.timing;

/**
 * Created by Randy on 05/23/16.
 */
public abstract class TimedEvent {

    private Object attatchedObject;
    private int    ticks;

    public TimedEvent(int ticks, Object attatchedObject) {
        this.ticks = ticks;
        this.attatchedObject = attatchedObject;
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
        return this.attatchedObject;
    }

    /**
     * Set the attached object
     *
     * @param attatchedObject
     */
    public void setAttachedObject(Object attatchedObject) {
        this.attatchedObject = attatchedObject;
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
}
