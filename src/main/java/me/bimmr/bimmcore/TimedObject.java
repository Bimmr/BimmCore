package me.bimmr.bimmcore;

import me.bimmr.bimmcore.events.timing.TimedEvent;

public abstract class TimedObject {

    public TimedEvent timedEvent;

    public TimedEvent getTimedEvent(){return this.timedEvent;}

    public void setTimedEvent(TimedEvent timedEvent){this.timedEvent = timedEvent;}

}
