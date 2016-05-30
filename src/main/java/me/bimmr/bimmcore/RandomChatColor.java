package me.bimmr.bimmcore;

import org.bukkit.ChatColor;

import java.util.Random;

/**
 * Created by Randy on 05/27/16.
 */
public class RandomChatColor {

    /**
     * Gets a random ChatColor from the given ChatColors
     * (Black is not a possible outcome because black is hard to read)
     *
     * @param validChatcolors
     * @return
     */
    public static ChatColor getColor(ChatColor... validChatcolors) {
        Random r = new Random(System.currentTimeMillis());
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
     * @param chatFormats
     * @return
     */
    public static ChatColor getFormat(ChatColor... chatFormats) {
        Random r = new Random(System.currentTimeMillis());
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
