package me.bimmr.bimmcore.utils;

import me.bimmr.bimmcore.messages.fancymessage.FancyMessage;
import me.bimmr.bimmcore.misc.RandomChatColor;
import org.apache.commons.lang.StringUtils;
import org.bukkit.ChatColor;

import java.awt.*;
import java.util.*;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


/**
 * A Utilities class to manage strings
 */
public class StringUtil {
    private final static int CENTER_PX = 154;

    /**
     * Adds colour to the string
     * <br> - Supports &1 Old Colour code
     * <br> - Supports &123456 Hex Colour code
     * <br - Supports <&#123456>Gradient Colours</&#654321>
     *
     * @param string the string
     * @return string
     */
    public static String addColor(String string) {

        String msg = ChatColor.translateAlternateColorCodes('&',string)
                .replaceAll("&x", "&" + String.valueOf(RandomChatColor.getColor().getChar()))
                .replaceAll("&y", "&" + String.valueOf(RandomChatColor.getFormat().getChar()));

        //Add Gradients
        Pattern gradient = Pattern.compile("<&#[0-9a-fA-F]{6}>[^<]*</&#[0-9a-fA-F]{6}>");
        Matcher gradientMatcher = gradient.matcher(msg);
        while(gradientMatcher.find()) {
            String gradientMsg = gradientMatcher.group();

            String message = gradientMsg.substring(10,gradientMsg.length()-11);
            String start = gradientMsg.substring(3,9);
            String end = gradientMsg.substring(gradientMsg.length()-7, gradientMsg.length()-1);

            msg = msg.replace(gradientMsg, addGradients(message, start, end));
        }

        //Custom Hex Colours
        Pattern hex = Pattern.compile("&#[0-9a-fA-F]{6}");
        Matcher hexMatcher = hex.matcher(msg);

        while(hexMatcher.find()){
            String hexCode = hexMatcher.group();
            msg = msg.replace(hexCode, ""+net.md_5.bungee.api.ChatColor.of(hexCode.substring(1)));
        }

        return msg;
    }

    public static String addGradient(String string, String startHex, String endHex){
        String msg = addGradients(string, startHex, endHex);
        msg = StringUtil.addColor(msg);
        return msg;
    }

    private static String addGradients(String string, String startHex, String endHex){
        StringBuilder sb = new StringBuilder();

        int startRed = Integer.parseInt(startHex.substring(0, 2), 16);
        int endRed = Integer.parseInt(endHex.substring(0, 2), 16);
        int startGreen = Integer.parseInt(startHex.substring(2, 4), 16);
        int endGreen= Integer.parseInt(endHex.substring(2, 4), 16);
        int startBlue = Integer.parseInt(startHex.substring(4, 6), 16);
        int endBlue = Integer.parseInt(endHex.substring(4, 6), 16);

        for(int i = 0; i <string.length(); i++){
            if(string.toCharArray()[i] == ChatColor.COLOR_CHAR || (i != 0 && string.toCharArray()[i-1] == ChatColor.COLOR_CHAR )) {
                sb.append(string.toCharArray()[i]);
                continue;
            }

            int red = (int)(startRed + (float)(endRed - startRed) / (string.length() - 1) * i);
            int green = (int)(startGreen + (float)(endGreen - startGreen) / (string.length() - 1) * i);
            int blue = (int)(startBlue + (float)(endBlue - startBlue) / (string.length() - 1) * i);

            StringBuilder color = new StringBuilder(Integer.toHexString(red) + Integer.toHexString(green) + Integer.toHexString(blue));
            while(color.length() < 6) color.append("0");

            sb.append("&#" + color + string.toCharArray()[i]);
        }
        return sb.toString();
    }

    /**
     * Add color list.
     *
     * @param strings List of Strings
     * @return Get the list of strings with colours added
     */
    public static List<String> addColor(List<String> strings) {
        for (int i = 0; i < strings.size(); i++)
            strings.set(i, addColor(strings.get(i)));
        return strings;
    }

    /**
     * Gets a String with ChatColor#COLOUR_CHAR converted to '&amp;'
     *
     * @param string the string
     * @return string
     */
    public static String replaceToYAMLFriendlyColors(String string) {
        return string.replaceAll("" + ChatColor.COLOR_CHAR, "&");
    }

    /**
     * Combine args between startAt and endAt to a single string joined by ' '
     *
     * @param args     the args
     * @param startAt  the start at
     * @param endAt    the end at
     * @param useComma the use comma
     * @return string
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
     * @param line the line
     * @return propercase
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
     * @param line the line
     * @return lines from string
     */
    public static List<String> getLinesFromString(String line) {
        List<String> lines = new ArrayList<String>();
        for (String s : line.split("\\|")) {
            lines.add(s.replaceAll("_", " "));
        }
        return lines;
    }

    /**
     * Get a string with every char having a different color
     *
     * @param string the message
     * @return the message with each letter as a different colour
     */
    public static String rainbowChatColor(String string) {
        int lastColor = 0;
        int currColor = 0;
        StringBuilder newMessage = new StringBuilder();
        String colors = "123456789abcde";
        for (int i = 0; i < string.length(); i++) {
            do {
                currColor = new Random().nextInt(colors.length() - 1) + 1;
            } while (currColor == lastColor);

            newMessage.append(ChatColor.RESET.toString()).append(ChatColor.getByChar(colors.charAt(currColor))).append(string.charAt(i));

        }
        return newMessage.toString();
    }

    /**
     * Gets indexed colour.
     *
     * @param index the index
     * @return the indexed colour
     */
    public static ChatColor getIndexedColour(int index) {
        return getIndexedColour(index, "123456789abcde");
    }

    /**
     * Gets indexed colour.
     *
     * @param index   the index
     * @param colours the colours
     * @return the indexed colour
     */
    public static ChatColor getIndexedColour(int index, String colours) {
        index = Math.min(index, index % colours.length());
        char colour = colours.toCharArray()[index];
        return ChatColor.getByChar(colour);
    }

    /**
     * Make a string a specific length, filling with white spaces
     *
     * @param string the string
     * @param length the length
     * @return the string as the length
     */
    public static String fixedLengthString(String string, int length) {
        if (length <= string.length())
            return string.substring(0, string.length());
        else
            return String.format("%1$" + length + "s", string);
    }

    /**
     * Check if the string is in the list of strings
     *
     * @param caseSensitive is it case sensitive
     * @param string        the string to check
     * @param strings       all the possible matches
     * @return if the string matches
     */
    public static boolean equalsStrings(boolean caseSensitive, String string, String... strings) {
        for (String item : strings)
            if (caseSensitive && string.equals(item))
                return true;
            else if (!caseSensitive && string.equalsIgnoreCase(item))
                return true;
        return false;
    }

    /**
     * Equals strings boolean.
     *
     * @param string  the string
     * @param strings the strings
     * @return the boolean
     */
    public static boolean equalsStrings(String string, String... strings) {
        return equalsStrings(false, string, strings);
    }


    /**
     * String from map string.
     *
     * @param map the map
     * @return the string
     */
    public static String stringFromMap(Map<String, Object> map) {
        StringBuilder result = new StringBuilder("");
        for (Object key : map.keySet()) {
            result.append(" " + key.toString() + ":" + map.get(key).toString());
        }
        result.substring(1);
        return result.toString();
    }

    /**
     * Map from string map.
     *
     * @param string the string
     * @return the map
     */
    public static Map<String, Object> mapFromString(String string) {
        Map<String, Object> map = new HashMap<>();
        for (String part : string.split(" ")) {
            Object[] set = part.split(":", 1);
            map.put(set[0].toString(), set[1]);
        }
        return map;
    }

    /**
     * Gets centered message.
     *
     * @param message the message
     * @return the centered message
     */
    public static FancyMessage getCenteredMessage(FancyMessage message) {
        return getCenteredMessage(message, "");
    }

    /**
     * Gets centered message.
     *
     * @param message the message
     * @return the centered message
     */
    public static String getCenteredMessage(String message) {
        return getCenteredMessage(message, "");
    }

    /**
     * Gets centered message.
     *
     * @param fancyMessage the fancy message
     * @param format       the format
     * @return the centered message
     */
    public static FancyMessage getCenteredMessage(FancyMessage fancyMessage, String format) {
        if (fancyMessage == null)
            fancyMessage = new FancyMessage("");

        FancyMessage fm = new FancyMessage();

        int messagePxSize = 0;
        boolean previousCode = false;
        boolean isBold = false;

        for (char c : fancyMessage.toPlainText().toCharArray()) {
            if (c == '§') {
                previousCode = true;
            } else if (previousCode) {
                previousCode = false;
                isBold = c == 'l';
            } else {
                DefaultFontInfo dFI = DefaultFontInfo.getDefaultFontInfo(c);
                messagePxSize = isBold ? messagePxSize + dFI.getBoldLength() : messagePxSize + dFI.getLength();
                messagePxSize++;
            }
        }
        int toCompensate = CENTER_PX - messagePxSize / 2;
        int end = (CENTER_PX * 2) - messagePxSize;
        int spaceLength = DefaultFontInfo.SPACE.getLength() + 1;
        int compensated = 0;
        StringBuilder sb = new StringBuilder();
        while (compensated < toCompensate) {
            sb.append(format + " ");
            compensated += spaceLength;
        }
        fm.then(sb.toString());
        fm.then(fancyMessage);
        sb = new StringBuilder();
        if (!format.equals("")) {
            while (compensated < end) {
                sb.append(format + " ");
                compensated += spaceLength;
            }
        }
        fm.then(sb.toString());
        return fm;
    }

    /**
     * Line wraps int.
     *
     * @param message the message
     * @return the int
     */
    public static int lineWraps(String message) {

        if (message == null || message.length() == 0)
            return 0;

        int maxLength = CENTER_PX * 2;
        int messagePxSize = 0;
        boolean previousCode = false;
        boolean isBold = false;

        for (char c : message.toCharArray()) {
            if (c == '§') {
                previousCode = true;
            } else if (previousCode) {
                previousCode = false;
                isBold = c == 'l';
            } else {
                DefaultFontInfo dFI = DefaultFontInfo.getDefaultFontInfo(c);
                messagePxSize = isBold ? messagePxSize + dFI.getBoldLength() : messagePxSize + dFI.getLength();
                messagePxSize++;
            }
        }
        return messagePxSize / (maxLength + 1);
    }

    /**
     * Gets centered message.
     *
     * @param message the message
     * @param format  the format
     * @return the centered message
     */
    public static String getCenteredMessage(String message, String format) {
        if (message == null)
            message = "";
        String[] lines = ChatColor.translateAlternateColorCodes('&', message).split("\n", 40);
        StringBuilder returnMessage = new StringBuilder();


        for (int i = 0; i < lines.length; i++) {
            String line = lines[i];
            int messagePxSize = 0;
            boolean previousCode = false;
            boolean isBold = false;

            for (char c : line.toCharArray()) {
                if (c == '§') {
                    previousCode = true;
                } else if (previousCode) {
                    previousCode = false;
                    isBold = c == 'l';
                } else {
                    DefaultFontInfo dFI = DefaultFontInfo.getDefaultFontInfo(c);
                    messagePxSize = isBold ? messagePxSize + dFI.getBoldLength() : messagePxSize + dFI.getLength();
                    messagePxSize++;
                }
            }
            int toCompensate = CENTER_PX - messagePxSize / 2;
            int end = (CENTER_PX * 2) - messagePxSize;
            int spaceLength = DefaultFontInfo.SPACE.getLength() + 1;
            int compensated = 0;
            StringBuilder sb = new StringBuilder();
            while (compensated < toCompensate) {
                sb.append(format + " ");
                compensated += spaceLength;
            }
            sb.append(ChatColor.RESET + line);
            if (!format.equals("")) {
                while (compensated < end) {
                    sb.append(format + " ");
                    compensated += spaceLength;
                }
            }
            returnMessage.append(sb.toString());
            if (i < lines.length - 2)
                returnMessage.append("\n");

        }

        return returnMessage.toString();
    }

    private enum DefaultFontInfo {

        /**
         * A default font info.
         */
        A('A', 5),
        /**
         * A default font info.
         */
        a('a', 5),
        /**
         * B default font info.
         */
        B('B', 5),
        /**
         * B default font info.
         */
        b('b', 5),
        /**
         * C default font info.
         */
        C('C', 5),
        /**
         * C default font info.
         */
        c('c', 5),
        /**
         * D default font info.
         */
        D('D', 5),
        /**
         * D default font info.
         */
        d('d', 5),
        /**
         * E default font info.
         */
        E('E', 5),
        /**
         * E default font info.
         */
        e('e', 5),
        /**
         * F default font info.
         */
        F('F', 5),
        /**
         * F default font info.
         */
        f('f', 4),
        /**
         * G default font info.
         */
        G('G', 5),
        /**
         * G default font info.
         */
        g('g', 5),
        /**
         * H default font info.
         */
        H('H', 5),
        /**
         * H default font info.
         */
        h('h', 5),
        /**
         * default font info.
         */
        I('I', 3),
        /**
         * default font info.
         */
        i('i', 1),
        /**
         * J default font info.
         */
        J('J', 5),
        /**
         * J default font info.
         */
        j('j', 5),
        /**
         * K default font info.
         */
        K('K', 5),
        /**
         * K default font info.
         */
        k('k', 4),
        /**
         * L default font info.
         */
        L('L', 5),
        /**
         * L default font info.
         */
        l('l', 1),
        /**
         * M default font info.
         */
        M('M', 5),
        /**
         * M default font info.
         */
        m('m', 5),
        /**
         * N default font info.
         */
        N('N', 5),
        /**
         * N default font info.
         */
        n('n', 5),
        /**
         * O default font info.
         */
        O('O', 5),
        /**
         * O default font info.
         */
        o('o', 5),
        /**
         * P default font info.
         */
        P('P', 5),
        /**
         * P default font info.
         */
        p('p', 5),
        /**
         * Q default font info.
         */
        Q('Q', 5),
        /**
         * Q default font info.
         */
        q('q', 5),
        /**
         * R default font info.
         */
        R('R', 5),
        /**
         * R default font info.
         */
        r('r', 5),
        /**
         * S default font info.
         */
        S('S', 5),
        /**
         * S default font info.
         */
        s('s', 5),
        /**
         * T default font info.
         */
        T('T', 5),
        /**
         * T default font info.
         */
        t('t', 4),
        /**
         * U default font info.
         */
        U('U', 5),
        /**
         * U default font info.
         */
        u('u', 5),
        /**
         * V default font info.
         */
        V('V', 5),
        /**
         * V default font info.
         */
        v('v', 5),
        /**
         * W default font info.
         */
        W('W', 5),
        /**
         * W default font info.
         */
        w('w', 5),
        /**
         * X default font info.
         */
        X('X', 5),
        /**
         * X default font info.
         */
        x('x', 5),
        /**
         * Y default font info.
         */
        Y('Y', 5),
        /**
         * Y default font info.
         */
        y('y', 5),
        /**
         * Z default font info.
         */
        Z('Z', 5),
        /**
         * Z default font info.
         */
        z('z', 5),
        /**
         * Num 1 default font info.
         */
        NUM_1('1', 5),
        /**
         * Num 2 default font info.
         */
        NUM_2('2', 5),
        /**
         * Num 3 default font info.
         */
        NUM_3('3', 5),
        /**
         * Num 4 default font info.
         */
        NUM_4('4', 5),
        /**
         * Num 5 default font info.
         */
        NUM_5('5', 5),
        /**
         * Num 6 default font info.
         */
        NUM_6('6', 5),
        /**
         * Num 7 default font info.
         */
        NUM_7('7', 5),
        /**
         * Num 8 default font info.
         */
        NUM_8('8', 5),
        /**
         * Num 9 default font info.
         */
        NUM_9('9', 5),
        /**
         * Num 0 default font info.
         */
        NUM_0('0', 5),
        /**
         * Exclamation point default font info.
         */
        EXCLAMATION_POINT('!', 1),
        /**
         * At symbol default font info.
         */
        AT_SYMBOL('@', 6),
        /**
         * Num sign default font info.
         */
        NUM_SIGN('#', 5),
        /**
         * Dollar sign default font info.
         */
        DOLLAR_SIGN('$', 5),
        /**
         * Percent default font info.
         */
        PERCENT('%', 5),
        /**
         * Up arrow default font info.
         */
        UP_ARROW('^', 5),
        /**
         * Ampersand default font info.
         */
        AMPERSAND('&', 5),
        /**
         * Asterisk default font info.
         */
        ASTERISK('*', 5),
        /**
         * Left parenthesis default font info.
         */
        LEFT_PARENTHESIS('(', 4),
        /**
         * Right perenthesis default font info.
         */
        RIGHT_PERENTHESIS(')', 4),
        /**
         * Minus default font info.
         */
        MINUS('-', 5),
        /**
         * Underscore default font info.
         */
        UNDERSCORE('_', 5),
        /**
         * Plus sign default font info.
         */
        PLUS_SIGN('+', 5),
        /**
         * Equals sign default font info.
         */
        EQUALS_SIGN('=', 5),
        /**
         * Left curl brace default font info.
         */
        LEFT_CURL_BRACE('{', 4),
        /**
         * Right curl brace default font info.
         */
        RIGHT_CURL_BRACE('}', 4),
        /**
         * Left bracket default font info.
         */
        LEFT_BRACKET('[', 3),
        /**
         * Right bracket default font info.
         */
        RIGHT_BRACKET(']', 3),
        /**
         * Colon default font info.
         */
        COLON(':', 1),
        /**
         * Semi colon default font info.
         */
        SEMI_COLON(';', 1),
        /**
         * Double quote default font info.
         */
        DOUBLE_QUOTE('"', 3),
        /**
         * Single quote default font info.
         */
        SINGLE_QUOTE('\'', 1),
        /**
         * Left arrow default font info.
         */
        LEFT_ARROW('<', 4),
        /**
         * Right arrow default font info.
         */
        RIGHT_ARROW('>', 4),
        /**
         * Question mark default font info.
         */
        QUESTION_MARK('?', 5),
        /**
         * Slash default font info.
         */
        SLASH('/', 5),
        /**
         * Back slash default font info.
         */
        BACK_SLASH('\\', 5),
        /**
         * Line default font info.
         */
        LINE('|', 1),
        /**
         * Tilde default font info.
         */
        TILDE('~', 5),
        /**
         * Tick default font info.
         */
        TICK('`', 2),
        /**
         * Period default font info.
         */
        PERIOD('.', 1),
        /**
         * Comma default font info.
         */
        COMMA(',', 1),
        /**
         * Space default font info.
         */
        SPACE(' ', 3),
        /**
         * Left one quarter block default font info.
         */
        LEFT_ONE_QUARTER_BLOCK('▎', 2),
        /**
         * Default default font info.
         */
        DEFAULT('a', 4);

        private char character;
        private int length;

        DefaultFontInfo(char character, int length) {
            this.character = character;
            this.length = length;
        }

        /**
         * Gets default font info.
         *
         * @param c the c
         * @return the default font info
         */
        public static DefaultFontInfo getDefaultFontInfo(char c) {
            for (DefaultFontInfo dFI : DefaultFontInfo.values()) {
                if (dFI.getCharacter() == c) return dFI;
            }
            return DefaultFontInfo.DEFAULT;
        }

        /**
         * Gets character.
         *
         * @return the character
         */
        public char getCharacter() {
            return this.character;
        }

        /**
         * Gets length.
         *
         * @return the length
         */
        public int getLength() {
            return this.length;
        }

        /**
         * Gets bold length.
         *
         * @return the bold length
         */
        public int getBoldLength() {
            if (this == DefaultFontInfo.SPACE) return this.getLength();
            return this.length + 1;
        }
    }
}
