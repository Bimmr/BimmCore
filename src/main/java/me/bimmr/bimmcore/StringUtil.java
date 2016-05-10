package me.bimmr.bimmcore;

import org.apache.commons.lang.StringUtils;
import org.bukkit.ChatColor;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;


public class StringUtil {

    /**
     * Gets a String with '&' converted to ChatColor#COLOUR_CHAR
     *
     * @param string
     * @return
     */
    public static String addColor(String string) {
        return ChatColor.translateAlternateColorCodes('&', string.replaceAll("&x", "&" + String.valueOf(RandomChatColor.getColor().getChar())).replaceAll("&y", "&" + String.valueOf(RandomChatColor.getFormat().getChar())));
    }

    /**
     * Gets a String with ChatColor#COLOUR_CHAR converted to '&'
     *
     * @param string
     * @return
     */
    public static String replaceToYAMLFriendlyColors(String string) {
        return string.replaceAll("" + ChatColor.COLOR_CHAR, "&");
    }

    /**
     * Combine args between startAt and endAt to a single string joined by ' '
     *
     * @param args
     * @param startAt
     * @param endAt
     * @return
     */
    public static String combineArgs(String[] args, int startAt, int endAt, boolean useComma) {
        endAt++;

        String[] arguments = new String[endAt - startAt];
        for (int j = 0, i = startAt; i != endAt; i++, j++)
            arguments[j] = args[i];
        return StringUtils.join(arguments, useComma ? "," : " ");
    }

    /**
     * Change every individual word in a string to propper case
     *
     * @param line
     * @return
     */
    public static String getPropercase(String line) {
        try {
            if (line.contains(" ")) {

                String[] words = line.split(" ");
                line = "";

                for (String string : words) {
                    string = string.toLowerCase();
                    line = line + string.replaceFirst(String.valueOf(string.charAt(0)), String.valueOf(string.charAt(0)).toUpperCase());
                }
            } else
                line = line.replaceFirst(String.valueOf(line.charAt(0)), String.valueOf(line.charAt(0)).toUpperCase());
        } catch (Exception e) {
        }
        return line;
    }

    /**
     * Get a string splitting at '|' to make a List<String>
     *
     * @param line
     * @return
     */
    public static List<String> getLinesFromString(String line) {
        List<String> lines = new ArrayList<String>();
        for (String s : line.split("\\|")) {
            lines.add(s.replaceAll("_", " "));
        }
        return lines;
    }

    /**
     * Center a string into the minecraft chat(Centers using '*')
     *
     * @param line
     * @return
     */
    public static String getMidMessage(String line) {
        int le = (62 - line.length()) / 2;
        String newLine = "";
        for (int i = 0; i < le; i++) {
            newLine += ChatColor.GOLD + "*";
        }
        newLine += line;
        for (int i = 0; i < le; i++) {
            newLine += ChatColor.GOLD + "*";
        }
        return newLine;
    }

    /**
     * Gets the time in a nicely formated string from the seconds
     * '#D:#H:#M:#S
     *
     * @param seconds
     * @return
     */
    public static String getTime(int seconds) {
        long minutes = seconds / 60;
        seconds %= 60;
        long hours = minutes / 60;
        minutes %= 60;
        long days = hours / 24;
        hours %= 24;
        String times = days + "D:" + hours + "H:" + minutes + "M:" + seconds + "S";
        return times;
    }

    /**
     * Gets the time in a nicely format string from the seconds
     * '# Minutes and # Seconds.'
     *
     * @param seconds
     * @return
     */
    public static String getTime(long seconds) {
        String times = null;
        long minutes = seconds / 60;
        seconds %= 60;
        if (seconds == 0) {
            if (minutes == 0)
                times = "N/A";
            else if (minutes == 1)
                times = minutes + " " + "Minute";
            else
                times = minutes + " " + "Minutes";
        } else if (minutes == 0) {
            if (seconds == 1)
                times = seconds + " " + "Second";
            else
                times = seconds + " " + "Seconds";
        } else
            times = minutes + " Minutes and " + seconds + " Seconds.";
        return times;
    }

    /**
     * Get a string with every char having a diferent color
     *
     * @param string
     * @return
     */
    public static String rainbowChatColor(String string) {
        int lastColor = 0;
        int currColor = 0;
        String newMessage = "";
        String colors = "123456789abcde";
        for (int i = 0; i < string.length(); i++) {
            do {
                currColor = new Random().nextInt(colors.length() - 1) + 1;
            } while (currColor == lastColor);

            newMessage += ChatColor.RESET.toString() + ChatColor.getByChar(colors.charAt(currColor)) + "" + string.charAt(i);

        }
        return newMessage;
    }

    public static class RandomChatColor {

        /**
         * Gets a random ChatColor from the given ChatColors
         * (Black is not a possible outcome because black is hard to read)
         *
         * @param validChatcolors
         * @return
         */
        public static ChatColor getColor(ChatColor... validChatcolors) {
            Random r = new Random();
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
            Random r = new Random();
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

}
