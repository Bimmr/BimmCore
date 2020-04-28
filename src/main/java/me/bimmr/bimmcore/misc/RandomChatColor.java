package me.bimmr.bimmcore.misc;

import org.bukkit.ChatColor;

import java.util.Random;

/**
 * Utilities class to get a random color/format
 */
public class RandomChatColor {

    /**
     * Gets a random ChatColor from the given ChatColors
     * (Black is not a possible outcome because black is hard to read)
     *
     * @param validChatcolors the valid chatcolors
     * @return color
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
     * Get a random ChatColor(Format) from the given ChatColor(Format)s
     *
     * @param chatFormats the chat formats
     * @return format
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
