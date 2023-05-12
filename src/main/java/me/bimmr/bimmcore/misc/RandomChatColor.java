package me.bimmr.bimmcore.misc;

import org.bukkit.ChatColor;

import java.util.Random;

/**
 * The type Random chat color.
 */
public class RandomChatColor {

    /**
     * Gets color.
     *
     * @param validChatcolors the valid chatcolors
     * @return the color
     */
    public static ChatColor getColor(ChatColor... validChatcolors) {
        Random r = new Random(System.nanoTime());
        ChatColor[] colors;
        if (validChatcolors.length == 0)
            colors = ChatColor.values();
        else
            colors = validChatcolors;

        int i = r.nextInt(colors.length);
        while (!colors[i].isColor() || colors[i] == ChatColor.BLACK)
            i = r.nextInt(colors.length);
        ChatColor rc = colors[i];
        return rc;
    }

    /**
     * Gets format.
     *
     * @param chatFormats the chat formats
     * @return the format
     */
    public static ChatColor getFormat(ChatColor... chatFormats) {
        Random r = new Random(System.nanoTime());
        ChatColor[] colors;
        if (chatFormats.length == 0)
            colors = ChatColor.values();
        else
            colors = chatFormats;
        int i = r.nextInt(colors.length);
        while (!colors[i].isFormat())
            i = r.nextInt(colors.length);
        ChatColor rc = colors[i];
        return rc;
    }
}
