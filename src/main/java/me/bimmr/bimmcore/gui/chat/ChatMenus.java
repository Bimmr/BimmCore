package me.bimmr.bimmcore.gui.chat;

import me.bimmr.bimmcore.messages.fancymessage.FancyClickEvent;
import me.bimmr.bimmcore.messages.fancymessage.FancyMessage;
import me.bimmr.bimmcore.utils.StringUtil;
import me.bimmr.bimmcore.utils.UnicodeUtil;
import org.bukkit.ChatColor;

/**
 * Pre-made ChatMenus
 */
public class ChatMenus {

    public static ChatMenu getConfirmation(String message, FancyClickEvent confirm, FancyClickEvent deny) {
        return getConfirmation(message, null, confirm, deny);
    }

    public static ChatMenu getConfirmation(String message, String subMessage, FancyClickEvent confirm, FancyClickEvent deny) {
        ChatMenu menu = new ChatMenu(ChatMenu.HeightControl.AUTO_CENTER);
        menu.addLine(StringUtil.getCenteredMessage("" + ChatColor.RED + UnicodeUtil.CAUTION))
                .addBlankLine()
                .addLine(StringUtil.getCenteredMessage(message));
        if (subMessage != null)
            menu.addLine(StringUtil.getCenteredMessage(subMessage));
        menu.addBlankLine()
                .addLine(StringUtil.getCenteredMessage(new FancyMessage(ChatColor.GREEN + " [ CONFIRM ] ").onClick(confirm).then("       ").then(ChatColor.RED + " [ DENY ] ").onClick(deny)))
                .addBlankLine();
        return menu;
    }
}
