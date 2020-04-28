package me.bimmr.bimmcore.gui.chat;

import me.bimmr.bimmcore.messages.fancymessage.FancyClickEvent;
import me.bimmr.bimmcore.messages.fancymessage.FancyMessage;
import org.bukkit.entity.Player;

/**
 * Created by Randy on 07/06/16.
 */
public class ChatOption {

    private FancyMessage fancyMessage;
    private String text;
    private FancyClickEvent clickEvent;

    public ChatOption(String text, FancyClickEvent clickEvent) {
        this.text = text;
        this.fancyMessage = new FancyMessage(text);

        if (clickEvent != null)
            this.fancyMessage.onClick(clickEvent);
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public void show(Player player) {
        this.fancyMessage.send(player);
    }

    public void destroy() {
        if (this.clickEvent != null) {
            this.clickEvent.startRemoval();
            this.clickEvent = null;
        }
        this.fancyMessage = null;
        this.text = null;
    }

}
