package me.bimmr.bimmcore.messages;

import me.bimmr.bimmcore.events.timing.TimedEvent;
import org.bukkit.entity.Player;

/**
 * Created by Randy on 05/23/16.
 */
public abstract class MessageDisplay {

    public String     text;
    public int        time;

    public TimedEvent timedEvent;

    public abstract String getText();

    public abstract void setText(String text);

    public abstract int getTime();

    public abstract void send(Player player);

    public abstract void stop(Player player);

    public abstract TimedEvent getTimedEvent();

    public abstract void setTimedEvent(TimedEvent timedEvent);

}
