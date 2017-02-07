package me.bimmr.bimmcore.menus.chat;

import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Randy on 07/06/16.
 */
public class ChatMenu {

    private List<ChatOption> chatOptions = new ArrayList<ChatOption>();

    public ChatMenu() {
        ChatMenuManager.register(this);
    }

    public ChatMenu addLine(ChatOption chat) {
        chatOptions.add(chat);
        return this;
    }

    public ChatMenu addLine(String text, ChatOptionClick chatOptionClick) {
        chatOptions.add(new ChatOption(text, chatOptionClick));
        return this;
    }

    public ChatMenu addLine(String text) {
        chatOptions.add(new ChatOption(text, null));
        return this;
    }

    public ChatMenu show(Player player) {
        for (ChatOption line : chatOptions)
            line.show(player);
        return this;
    }

    public List<ChatOption> getChatOptions() {
        return this.chatOptions;
    }

    public void destroy() {
        ChatMenuManager.unregister(this);
        for (ChatOption chatOption : chatOptions) {
            chatOption.setChatOptionClick(null);
            chatOption.setFancyMessage(null);
            chatOption.setText(null);
        }
        this.chatOptions = null;
    }
}
