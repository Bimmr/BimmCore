package me.bimmr.bimmcore;

import org.apache.commons.lang.StringUtils;
import org.bukkit.ChatColor;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;


/**
 * A Utilities class to manage strings
 */
public class StringUtil {

    /**
     * Gets a String with &amp; converted to ChatColor#COLOUR_CHAR
     *
     * @param string
     * @return
     */
    public static String addColor(String string) {
        return ChatColor.translateAlternateColorCodes('&', string.replaceAll("&x", "&" + String.valueOf(RandomChatColor.getColor().getChar())).replaceAll("&y", "&" + String.valueOf(RandomChatColor.getFormat().getChar())));
    }

    /**
     * Gets a String with ChatColor#COLOUR_CHAR converted to '&amp;'
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
     * Change every individual word in a string to proper case
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
     * Get a string splitting at '|' to make a List of type String
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
     * Center a string into the Minecraft chat(Centers using '*')
     *
     * @param line
     * @return
     */
    public static String getMidMessage(String line) {
        return getMidMessage(62, line);
    }

    /**
     * Center a string into any length of message
     * @param size
     * @param line
     * @return
     */
    public static String getMidMessage(int size, String line){

        int le = (size - line.length()) / 2;
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
     * Get a string with every char having a different color
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

    /**
     * Make a string a specific length, filling with white spaces
     *
     * @param string
     * @param length
     * @return
     */
    public static String fixedLengthString(String string, int length) {
        if (length <= string.length())
            return string.substring(0, string.length());
        else
            return String.format("%1$" + length + "s", string);
    }
}
