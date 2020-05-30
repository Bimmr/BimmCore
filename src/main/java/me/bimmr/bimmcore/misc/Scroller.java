package me.bimmr.bimmcore.misc;

import me.bimmr.bimmcore.utils.StringUtil;
import org.bukkit.ChatColor;

import java.util.ArrayList;
import java.util.List;

/**
 * A Utilities class to create a scrolling message
 */
public class Scroller {

    private String originalMessage;
    private int    width;
    private int    spaceBetween;
    private ChatColor chatColor = ChatColor.RESET;
    private String last;

    private int          position;
    private List<String> positions;

    /**
     * Instantiates a new Scroller.
     *
     * @param message      the message
     * @param width        the width
     * @param spaceBetween the space between
     */
    public Scroller(String message, int width, int spaceBetween) {
        this.positions = new ArrayList<>();
        this.originalMessage = StringUtil.addColor(message);

        width -= 2;

        //Add Spaces
        for (int i = 0; i < spaceBetween; i++)
            originalMessage += " ";

        //Double the originalMessage so it looks like it's scrolling
        originalMessage += originalMessage;


        while (width > originalMessage.length())
            originalMessage += originalMessage;

        //Add all positions to list
        for (int i = 0; i < originalMessage.length() - width; i++)
            if (i > 0 && originalMessage.substring(i - 1, i).charAt(0) != ChatColor.COLOR_CHAR) {
                int colours = (int)originalMessage.substring(i, i + width).chars().filter(c-> c == ChatColor.COLOR_CHAR).count()*2;
                positions.add(originalMessage.substring(i, i + width+colours));
            }

        StringBuilder line = new StringBuilder(positions.get(position));
        if (line.charAt(line.length() - 1) == ChatColor.COLOR_CHAR)
            line.setCharAt(line.length() - 1, ' ');

        if (line.charAt(0) == ChatColor.COLOR_CHAR)
            chatColor = ChatColor.getByChar(line.charAt(1));

        last = chatColor + line.toString();

    }

    /**
     * Current string.
     *
     * @return the string
     */
    public String current() {
        return last;
    }

    /**
     * Get the next originalMessage
     *
     * @return string
     */
    public String next() {
        StringBuilder line = new StringBuilder(getNext());
        if (line.charAt(line.length() - 1) == ChatColor.COLOR_CHAR)
            line.setCharAt(line.length() - 1, ' ');

        if (line.charAt(0) == ChatColor.COLOR_CHAR)
            chatColor = ChatColor.getByChar(line.charAt(1));


        return last = chatColor + line.toString();
    }

    /**
     * Previous string.
     *
     * @return the string
     */
    public String previous() {
        StringBuilder line = new StringBuilder(getPrevious());
        if (line.charAt(line.length() - 1) == ChatColor.COLOR_CHAR)
            line.setCharAt(line.length() - 1, ' ');

        if (line.charAt(0) == ChatColor.COLOR_CHAR)
            chatColor = ChatColor.getByChar(line.charAt(1));

        return last = chatColor + line.toString();
    }

    /**
     * Get the next position
     *
     * @return
     */
    private String getNext() {
        position++;
        if (position == originalMessage.length() / 2)
            position = 1;
        return positions.get(position);
    }

    private String getPrevious(){
        position--;
        if(position == -1)
            position = originalMessage.length() / 2;
        return positions.get(position);
    }
}
