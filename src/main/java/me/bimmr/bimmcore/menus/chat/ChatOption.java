package me.bimmr.bimmcore.menus.chat;

import me.bimmr.bimmcore.messages.FancyMessage;
import org.bukkit.entity.Player;

import java.util.UUID;

/**
 * Created by Randy on 07/06/16.
 */
public class ChatOption {

    private FancyMessage fancyMessage;
    private String text;
    private ChatOptionClick chatOptionClick;
    private UUID            uuid;

    public ChatOption(String text, ChatOptionClick chatOptionClick) {
        this.uuid = UUID.randomUUID();
        this.text = text;
        this.fancyMessage = new FancyMessage(text).command("bcore " + uuid);

        this.chatOptionClick = chatOptionClick;
        if (this.chatOptionClick != null)
            this.chatOptionClick.setAttatched(this);
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

    public FancyMessage getFancyMessage() {
        return this.fancyMessage;
    }

    public void setFancyMessage(FancyMessage fancyMessage) {
        this.fancyMessage = fancyMessage;
    }

    public UUID getUUID() {
        return this.uuid;
    }

    public ChatOptionClick getChatOptionClick() {
        return this.chatOptionClick;
    }

    public void setChatOptionClick(ChatOptionClick chatOptionClick) {
        this.chatOptionClick = chatOptionClick;
    }

}
