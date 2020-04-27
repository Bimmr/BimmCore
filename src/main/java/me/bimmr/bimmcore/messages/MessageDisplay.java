package me.bimmr.bimmcore.messages;

import me.bimmr.bimmcore.timed.TimedObject;
import org.bukkit.entity.Player;

/**
 * Created by Randy on 05/23/16.
 */
public abstract class MessageDisplay extends TimedObject {

    public String text;
    public int    time;


    public abstract String getText();

    public abstract void setText(String text);

    public abstract int getTime();

    public abstract void send(Player player);

    public abstract void stop(Player player);

}
