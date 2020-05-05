package me.bimmr.bimmcore.utils;

import me.bimmr.bimmcore.messages.fancymessage.FancyMessage;
import me.bimmr.bimmcore.misc.RandomChatColor;
import org.apache.commons.lang.StringUtils;
import org.bukkit.ChatColor;

import java.util.*;


/**
 * A Utilities class to manage strings
 */
public class StringUtil {
    private final static int CENTER_PX = 157;

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
     * Get a string with every char having a different color
     *
     * @param string
     * @return
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

    /**
     * Check if the string is in the list of strings
     *
     * @param caseSensitive
     * @param string
     * @param strings
     * @return
     */
    public static boolean equalsStrings(boolean caseSensitive, String string, String... strings) {
        for (String item : strings)
            if (caseSensitive && string.equals(item))
                return true;
            else if (!caseSensitive && string.equalsIgnoreCase(item))
                return true;
        return false;
    }

    public static boolean equalsStrings(String string, String... strings) {
        return equalsStrings(false, string, strings);
    }


    public static String stringFromMap(Map<String, Object> map) {
        StringBuilder result = new StringBuilder("");
        for (Object key : map.keySet()) {
            result.append(" " + key.toString() + ":" + map.get(key).toString());
        }
        result.substring(1);
        return result.toString();
    }

    public static Map<String, Object> mapFromString(String string) {
        Map<String, Object> map = new HashMap<>();
        for (String part : string.split(" ")) {
            Object[] set = part.split(":", 1);
            map.put(set[0].toString(), set[1]);
        }
        return map;
    }

    public static FancyMessage getCenteredMessage(FancyMessage message) {
        return getCenteredMessage(message, "");
    }

    public static String getCenteredMessage(String message) {
        return getCenteredMessage(message, "");
    }

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
        return messagePxSize / (maxLength+1);
    }

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

        A('A', 5),
        a('a', 5),
        B('B', 5),
        b('b', 5),
        C('C', 5),
        c('c', 5),
        D('D', 5),
        d('d', 5),
        E('E', 5),
        e('e', 5),
        F('F', 5),
        f('f', 4),
        G('G', 5),
        g('g', 5),
        H('H', 5),
        h('h', 5),
        I('I', 3),
        i('i', 1),
        J('J', 5),
        j('j', 5),
        K('K', 5),
        k('k', 4),
        L('L', 5),
        l('l', 1),
        M('M', 5),
        m('m', 5),
        N('N', 5),
        n('n', 5),
        O('O', 5),
        o('o', 5),
        P('P', 5),
        p('p', 5),
        Q('Q', 5),
        q('q', 5),
        R('R', 5),
        r('r', 5),
        S('S', 5),
        s('s', 5),
        T('T', 5),
        t('t', 4),
        U('U', 5),
        u('u', 5),
        V('V', 5),
        v('v', 5),
        W('W', 5),
        w('w', 5),
        X('X', 5),
        x('x', 5),
        Y('Y', 5),
        y('y', 5),
        Z('Z', 5),
        z('z', 5),
        NUM_1('1', 5),
        NUM_2('2', 5),
        NUM_3('3', 5),
        NUM_4('4', 5),
        NUM_5('5', 5),
        NUM_6('6', 5),
        NUM_7('7', 5),
        NUM_8('8', 5),
        NUM_9('9', 5),
        NUM_0('0', 5),
        EXCLAMATION_POINT('!', 1),
        AT_SYMBOL('@', 6),
        NUM_SIGN('#', 5),
        DOLLAR_SIGN('$', 5),
        PERCENT('%', 5),
        UP_ARROW('^', 5),
        AMPERSAND('&', 5),
        ASTERISK('*', 5),
        LEFT_PARENTHESIS('(', 4),
        RIGHT_PERENTHESIS(')', 4),
        MINUS('-', 5),
        UNDERSCORE('_', 5),
        PLUS_SIGN('+', 5),
        EQUALS_SIGN('=', 5),
        LEFT_CURL_BRACE('{', 4),
        RIGHT_CURL_BRACE('}', 4),
        LEFT_BRACKET('[', 3),
        RIGHT_BRACKET(']', 3),
        COLON(':', 1),
        SEMI_COLON(';', 1),
        DOUBLE_QUOTE('"', 3),
        SINGLE_QUOTE('\'', 1),
        LEFT_ARROW('<', 4),
        RIGHT_ARROW('>', 4),
        QUESTION_MARK('?', 5),
        SLASH('/', 5),
        BACK_SLASH('\\', 5),
        LINE('|', 1),
        TILDE('~', 5),
        TICK('`', 2),
        PERIOD('.', 1),
        COMMA(',', 1),
        SPACE(' ', 3),
        LEFT_ONE_QUARTER_BLOCK('▎', 2),
        DEFAULT('a', 4);

        private char character;
        private int length;

        DefaultFontInfo(char character, int length) {
            this.character = character;
            this.length = length;
        }

        public static DefaultFontInfo getDefaultFontInfo(char c) {
            for (DefaultFontInfo dFI : DefaultFontInfo.values()) {
                if (dFI.getCharacter() == c) return dFI;
            }
            return DefaultFontInfo.DEFAULT;
        }

        public char getCharacter() {
            return this.character;
        }

        public int getLength() {
            return this.length;
        }

        public int getBoldLength() {
            if (this == DefaultFontInfo.SPACE) return this.getLength();
            return this.length + 1;
        }
    }
}
