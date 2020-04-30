package me.bimmr.bimmcore.gui.chat;

import me.bimmr.bimmcore.messages.fancymessage.FancyClickEvent;
import me.bimmr.bimmcore.messages.fancymessage.FancyMessage;
import me.bimmr.bimmcore.utils.StringUtil;
import me.bimmr.bimmcore.utils.UnicodeUtil;
import org.bukkit.ChatColor;

public class ChatMenus {

    public static ChatMenu getConfirmation(String title, String message, FancyClickEvent confirm, FancyClickEvent deny) {
        ChatMenu menu = new ChatMenu(ChatMenu.HeightControl.AUTO_CENTER);
        int lineIndex = (10 - 6) / 2;
        for (int i = 0; i < lineIndex; i++)
            menu.addBlankLine();
        menu.addLine(StringUtil.getCenteredMessage("" + ChatColor.RED + UnicodeUtil.CAUTION))
                .addBlankLine()
                .addLine(StringUtil.getCenteredMessage(message))
                .addBlankLine()
                .addLine(StringUtil.getCenteredMessage(new FancyMessage(ChatColor.GREEN + " [ CONFIRM ] ").onClick(confirm).then("       ").then(ChatColor.DARK_RED + " [ DENY ] ").onClick(deny)))
                .addBlankLine();
        return menu;
    }
}
