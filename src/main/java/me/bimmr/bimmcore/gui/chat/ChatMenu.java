package me.bimmr.bimmcore.gui.chat;

import me.bimmr.bimmcore.messages.fancymessage.FancyClickEvent;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Randy on 07/06/16.
 */
public class ChatMenu {

    private List<ChatOption> chatOptions = new ArrayList<ChatOption>();
    private boolean keep;

    public ChatMenu() {
        ChatMenuManager.register(this);
    }

    public ChatMenu addLine(ChatOption chat) {
        chatOptions.add(chat);
        return this;
    }

    public ChatMenu addLine(String text, FancyClickEvent chatOptionClick) {
        chatOptions.add(new ChatOption(text, chatOptionClick));
        return this;
    }

    public ChatMenu addLine(String text) {
        chatOptions.add(new ChatOption(text, null));
        return this;
    }
    public ChatMenu addBlankLine(){
        chatOptions.add(new ChatOption("", null));
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
            chatOption.destroy();
        }
        this.chatOptions = null;
    }
}
