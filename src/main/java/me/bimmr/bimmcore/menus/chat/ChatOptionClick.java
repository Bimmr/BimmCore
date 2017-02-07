package me.bimmr.bimmcore.menus.chat;

import org.bukkit.entity.Player;

/**
 * Created by Randy on 07/07/16.
 */
public abstract class ChatOptionClick {

    private ChatOption chatOption;

    public ChatOptionClick() {

    }

    public void setAttatched(ChatOption chatOption) {
        this.chatOption = chatOption;
    }

    public ChatOption getChatOption() {
        return this.chatOption;
    }

    public abstract void run(Player player);
}
