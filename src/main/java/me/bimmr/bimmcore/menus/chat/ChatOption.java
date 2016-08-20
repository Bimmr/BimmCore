package me.bimmr.bimmcore.menus.chat;

import me.bimmr.bimmcore.messages.FancyMessage;

/**
 * Created by Randy on 07/06/16.
 */
public class ChatOption {

    private FancyMessage fancyMessage;

    public ChatOption(String text, ChatOptionClick chatOptionClick){
        chatOptionClick.setAttatched(this);
    }
}
