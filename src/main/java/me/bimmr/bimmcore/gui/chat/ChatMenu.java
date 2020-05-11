package me.bimmr.bimmcore.gui.chat;

import me.bimmr.bimmcore.messages.fancymessage.FancyClickEvent;
import me.bimmr.bimmcore.messages.fancymessage.FancyMessage;
import me.bimmr.bimmcore.utils.StringUtil;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.util.ArrayList;

/**
 * A Utilities class for creating a FancyMessage Chat Menu
 */
public class ChatMenu {
    private final int MAX_CHAT = 18;

    private ArrayList<ArrayList<FancyMessage>> lines = new ArrayList<>();
    private FancyMessage title;
    private FancyMessage footer;
    private ChatColor lineColor;
    private int height;
    private HeightControl heightControl;

    private boolean spacedFormat = false;

    /**
     * Instantiates a new Chat menu.
     */
    public ChatMenu() {
        this((FancyMessage) null, (FancyMessage) null, ChatColor.DARK_GRAY, -1, HeightControl.MAX);
    }

    public ChatMenu(ChatColor lineColor) {
        this((FancyMessage) null, (FancyMessage) null, lineColor, -1, HeightControl.MAX);
    }

    /**
     * Instantiates a new Chat menu.
     *
     * @param heightControl the height control
     */
    public ChatMenu(HeightControl heightControl) {
        this((FancyMessage) null, (FancyMessage) null, ChatColor.DARK_GRAY, -1, heightControl);
    }

    /**
     * Instantiates a new Chat menu.
     *
     * @param title the title
     */
    public ChatMenu(String title) {
        this(new FancyMessage(title), (FancyMessage) null, ChatColor.DARK_GRAY, -1, HeightControl.MAX);
    }

    /**
     * Instantiates a new Chat menu.
     *
     * @param title         the title
     * @param heightControl the height control
     */
    public ChatMenu(String title, HeightControl heightControl) {
        this(new FancyMessage(title), (FancyMessage) null, ChatColor.DARK_GRAY, -1, heightControl);
    }

    /**
     * Instantiates a new Chat menu.
     *
     * @param title     the title
     * @param lineColor the line color
     */
    public ChatMenu(String title, ChatColor lineColor) {
        this(new FancyMessage(title), (FancyMessage) null, lineColor, -1, HeightControl.MAX);
    }

    /**
     * Instantiates a new Chat menu.
     *
     * @param title         the title
     * @param lineColor     the line color
     * @param heightControl the height control
     */
    public ChatMenu(String title, ChatColor lineColor, HeightControl heightControl) {
        this(new FancyMessage(title), (FancyMessage) null, lineColor, -1, heightControl);
    }

    /**
     * Instantiates a new Chat menu.
     *
     * @param title     the title
     * @param lineColor the line color
     * @param height    the height
     */
    public ChatMenu(String title, ChatColor lineColor, int height) {
        this(new FancyMessage(title), (FancyMessage) null, lineColor, height, HeightControl.MAX);
    }

    public ChatMenu(String title, ChatColor lineColor, int height, HeightControl heightControl) {
        this(new FancyMessage(title), (FancyMessage) null, lineColor, height, heightControl);
    }

    /**
     * Instantiates a new Chat menu.
     *
     * @param title     the title
     * @param footer    the footer
     * @param lineColor the line color
     */
    public ChatMenu(String title, String footer, ChatColor lineColor) {
        this(new FancyMessage(title), new FancyMessage(footer), lineColor, -1, HeightControl.MAX);
    }

    /**
     * Instantiates a new Chat menu.
     *
     * @param title         the title
     * @param footer        the footer
     * @param lineColor     the line color
     * @param heightControl the height control
     */
    public ChatMenu(String title, String footer, ChatColor lineColor, HeightControl heightControl) {
        this(new FancyMessage(title), new FancyMessage(footer), lineColor, -1, heightControl);
    }

    /**
     * Instantiates a new Chat menu.
     *
     * @param title     the title
     * @param footer    the footer
     * @param lineColor the line color
     */
    public ChatMenu(String title, FancyMessage footer, ChatColor lineColor) {
        this(new FancyMessage(title), footer, lineColor, -1, HeightControl.MAX);
    }

    /**
     * Instantiates a new Chat menu.
     *
     * @param title         the title
     * @param footer        the footer
     * @param lineColor     the line color
     * @param heightControl the height control
     */
    public ChatMenu(String title, FancyMessage footer, ChatColor lineColor, HeightControl heightControl) {
        this(new FancyMessage(title), footer, lineColor, -1, heightControl);
    }

    /**
     * Instantiates a new Chat menu.
     *
     * @param title     the title
     * @param footer    the footer
     * @param lineColor the line color
     */
    public ChatMenu(FancyMessage title, String footer, ChatColor lineColor) {
        this(title, new FancyMessage(footer), lineColor, -1, HeightControl.MAX);
    }

    /**
     * Instantiates a new Chat menu.
     *
     * @param title         the title
     * @param footer        the footer
     * @param lineColor     the line color
     * @param heightControl the height control
     */
    public ChatMenu(FancyMessage title, String footer, ChatColor lineColor, HeightControl heightControl) {
        this(title, new FancyMessage(footer), lineColor, -1, heightControl);
    }

    /**
     * Instantiates a new Chat menu.
     *
     * @param title the title
     */
    public ChatMenu(FancyMessage title) {
        this(title, (FancyMessage) null, ChatColor.DARK_GRAY, -1, HeightControl.MAX);
    }

    /**
     * Instantiates a new Chat menu.
     *
     * @param title         the title
     * @param heightControl the height control
     */
    public ChatMenu(FancyMessage title, HeightControl heightControl) {
        this(title, (FancyMessage) null, ChatColor.DARK_GRAY, -1, heightControl);
    }

    /**
     * Instantiates a new Chat menu.
     *
     * @param title     the title
     * @param lineColor the line color
     */
    public ChatMenu(FancyMessage title, ChatColor lineColor) {
        this(title, (FancyMessage) null, lineColor, -1, HeightControl.MAX);
    }

    /**
     * Instantiates a new Chat menu.
     *
     * @param title         the title
     * @param lineColor     the line color
     * @param heightControl the height control
     */
    public ChatMenu(FancyMessage title, ChatColor lineColor, HeightControl heightControl) {
        this(title, (FancyMessage) null, lineColor, -1, heightControl);
    }

    /**
     * Instantiates a new Chat menu.
     *
     * @param title     the title
     * @param lineColor the line color
     * @param height    the height
     */
    public ChatMenu(FancyMessage title, ChatColor lineColor, int height) {
        this(title, (FancyMessage) null, lineColor, height, HeightControl.MANUAL);
    }

    public ChatMenu(FancyMessage title, ChatColor lineColor, int height, HeightControl heightControl) {
        this(title, (FancyMessage) null, lineColor, height, heightControl);
    }

    /**
     * Instantiates a new Chat menu.
     *
     * @param title     the title
     * @param footer    the footer
     * @param lineColor the line color
     */
    public ChatMenu(FancyMessage title, FancyMessage footer, ChatColor lineColor) {
        this(title, footer, lineColor, -1, HeightControl.MAX);
    }

    /**
     * Instantiates a new Chat menu.
     *
     * @param title         the title
     * @param footer        the footer
     * @param lineColor     the line color
     * @param heightControl the height control
     */
    public ChatMenu(FancyMessage title, FancyMessage footer, ChatColor lineColor, HeightControl heightControl) {
        this(title, footer, lineColor, -1, heightControl);
    }

    /**
     * Instantiates a new Chat menu.
     *
     * @param title     the title
     * @param footer    the footer
     * @param lineColor the line color
     * @param height    the height
     */
    public ChatMenu(String title, String footer, ChatColor lineColor, int height) {
        this(new FancyMessage(title), new FancyMessage(footer), lineColor, height, HeightControl.MANUAL);
    }

    public ChatMenu(String title, String footer, ChatColor lineColor, int height, HeightControl heightControl) {
        this(new FancyMessage(title), new FancyMessage(footer), lineColor, height, heightControl);
    }

    /**
     * Instantiates a new Chat menu.
     *
     * @param title     the title
     * @param footer    the footer
     * @param lineColor the line color
     * @param height    the height
     */
    public ChatMenu(String title, FancyMessage footer, ChatColor lineColor, int height) {
        this(new FancyMessage(title), footer, lineColor, height, HeightControl.MANUAL);
    }

    public ChatMenu(String title, FancyMessage footer, ChatColor lineColor, int height, HeightControl heightControl) {
        this(new FancyMessage(title), footer, lineColor, height, heightControl);
    }

    /**
     * Instantiates a new Chat menu.
     *
     * @param title     the title
     * @param footer    the footer
     * @param lineColor the line color
     * @param height    the height
     */
    public ChatMenu(FancyMessage title, String footer, ChatColor lineColor, int height) {
        this(title, new FancyMessage(footer), lineColor, height, HeightControl.MANUAL);
    }

    public ChatMenu(FancyMessage title, String footer, ChatColor lineColor, int height, HeightControl heightControl) {
        this(title, new FancyMessage(footer), lineColor, height, heightControl);
    }

    /**
     * Instantiates a new Chat menu.
     *
     * @param title     the title
     * @param footer    the footer
     * @param lineColor the line color
     * @param height    the height
     */
    public ChatMenu(FancyMessage title, FancyMessage footer, ChatColor lineColor, int height) {
        this(title, footer, lineColor, height, HeightControl.MANUAL);
    }

    /**
     * Instantiates a new Chat menu.
     *
     * @param title     the title
     * @param footer    the footer
     * @param lineColor the line color
     * @param height    the height
     */
    public ChatMenu(FancyMessage title, FancyMessage footer, ChatColor lineColor, int height, HeightControl heightControl) {
        this.title = title;
        this.footer = footer;
        this.lineColor = lineColor;
        this.heightControl = heightControl;
        //Add 2 for header and footer
        this.height = height;

        if (this.heightControl.isManual() && this.height < 1)
            this.height = MAX_CHAT;

        nextPage();
    }

    /**
     * Gets lines.
     *
     * @return the lines
     */
    public ArrayList<ArrayList<FancyMessage>> getLines() {
        return lines;
    }

    /**
     * Gets lines.
     *
     * @param page the page
     * @return the lines
     */
    public ArrayList<FancyMessage> getLines(int page) {
        if (page >= getLines().size())
            nextPage();
        return getLines().get(page);
    }

    public void clear() {
        this.lines = new ArrayList<>();
        nextPage();
    }

    /**
     * Gets formatted title.
     *
     * @return the formatted title
     */
    public FancyMessage getFormattedTitle() {
        if (getTitle() != null)
            return StringUtil.getCenteredMessage(new FancyMessage(lineColor + "[ ").then(getTitle()).then(lineColor + " ]"), "" + lineColor + ChatColor.STRIKETHROUGH);
        else
            return StringUtil.getCenteredMessage(new FancyMessage(""), "" + lineColor + ChatColor.STRIKETHROUGH);
    }

    /**
     * Gets formatted footer.
     *
     * @return the formatted footer
     */
    public FancyMessage getFormattedFooter() {
        return StringUtil.getCenteredMessage(getFooter(), "" + lineColor + ChatColor.STRIKETHROUGH);
    }

    /**
     * Gets formatted.
     *
     * @param fancyMessage the fancy message
     * @return the formatted
     */
    public FancyMessage getFormatted(FancyMessage fancyMessage) {
        return StringUtil.getCenteredMessage(fancyMessage, "" + lineColor + ChatColor.STRIKETHROUGH);
    }

    /**
     * Gets title.
     *
     * @return the title
     */
    public FancyMessage getTitle() {
        return title;
    }

    public void setTitle(FancyMessage title) {
        this.title = title;
    }

    public void setTitle(String title) {
        setTitle(new FancyMessage(title));
    }

    /**
     * Gets footer.
     *
     * @return the footer
     */
    public FancyMessage getFooter() {
        return footer;
    }

    public void setFooter(FancyMessage footer) {
        this.footer = footer;
    }

    public void setFooter(String footer) {
        setFooter(new FancyMessage(footer));
    }

    /**
     * Gets line color.
     *
     * @return the line color
     */
    public ChatColor getLineColor() {
        return lineColor;
    }

    public void setLineColor(ChatColor lineColor) {
        this.lineColor = lineColor;
    }

    public int getMaxHeight() {
        int h = -1;
        if (this.heightControl.isManual())
            h = this.height + 2;
        else if (this.heightControl.isAuto())
            if (this.height > 0)
                h = this.height + 2;
            else
                h = MAX_CHAT;
        else if (this.heightControl.isFillingInner())
            h = MAX_CHAT;


        //Give space if requested
        if (spacedFormat)
            h -= 2;
        return h;
    }

    /**
     * Add line chat menu.
     *
     * @param message the message
     * @return the chat menu
     */
    public ChatMenu addLine(String message) {
        return addLine(getCurrentPage(), new FancyMessage(message));
    }

    public ChatMenu addLine(int page, String message, FancyClickEvent fancyClickEvent) {
        return addLine(page, new FancyMessage(message).onClick(fancyClickEvent));
    }

    public ChatMenu addLine(String message, FancyClickEvent fancyClickEvent) {
        return addLine(getCurrentPage(), message, fancyClickEvent);
    }

    /**
     * Add line chat menu.
     *
     * @param message the message
     * @return the chat menu
     */
    public ChatMenu addLine(FancyMessage message) {
        return addLine(getCurrentPage(), message);
    }

    private int getCurrentPage() {
        return getLines().size() - 1;
    }

    /**
     * Add line chat menu.
     *
     * @param page    the page
     * @param message the message
     * @return the chat menu
     */
    public ChatMenu addLine(int page, String message) {
        return addLine(page, new FancyMessage(message));
    }

    /**
     * Add line chat menu.
     *
     * @param page    the page
     * @param message the message
     * @return the chat menu
     */
    public ChatMenu addLine(int page, FancyMessage message) {
        while (lines.size() <= page)
            nextPage();
        if (getLines(page).size() >= getMaxHeight())
            return addLine(++page, message);

        lines.get(page).add(message);

        return this;
    }


    /**
     * Sets line.
     *
     * @param line    the line
     * @param message the message
     * @return the line
     */
    public ChatMenu setLine(int line, String message) {
        return setLine(getCurrentPage(), line, new FancyMessage(message));
    }

    public ChatMenu setLine(int page, int line, String message, FancyClickEvent fancyClickEvent) {
        return setLine(page, line, new FancyMessage(message).onClick(fancyClickEvent));
    }

    public ChatMenu setLine(int line, String message, FancyClickEvent fancyClickEvent) {
        return setLine(getCurrentPage(), line, message, fancyClickEvent);
    }

    /**
     * Sets line.
     *
     * @param page    the page
     * @param line    the line
     * @param message the message
     * @return the line
     */
    public ChatMenu setLine(int page, int line, String message) {
        return setLine(page, line, new FancyMessage(message));
    }

    /**
     * Sets line.
     *
     * @param line    the line
     * @param message the message
     * @return the line
     */
    public ChatMenu setLine(int line, FancyMessage message) {
        return setLine(getCurrentPage(), line, message);
    }

    /**
     * Sets line.
     *
     * @param page    the page
     * @param line    the line
     * @param message the message
     * @return the line
     */
    public ChatMenu setLine(int page, int line, FancyMessage message) {
        ArrayList<FancyMessage> list = lines.get(page);
        while (line > list.size())
            list.add(new FancyMessage());

        list.add(message);
        lines.add(page, list);
        lines.remove(page + 1);
        return this;
    }

    /**
     * Next page chat menu.
     *
     * @return the chat menu
     */
    public ChatMenu nextPage() {
        lines.add(new ArrayList());

        return this;
    }

    /**
     * Add blank line chat menu.
     *
     * @return the chat menu
     */
    public ChatMenu addBlankLine() {
        return addBlankLine(getCurrentPage());
    }

    /**
     * Add blank line chat menu.
     *
     * @return the chat menu
     */
    public ChatMenu addBlankLine(int page) {
        return addLine(page, "");
    }

    /**
     * Show.
     *
     * @param player the player
     */
    public void show(Player player) {
        show(0, player);
    }

    public HeightControl getHeightControl() {
        return heightControl;
    }

    /**
     * Show.
     *
     * @param page   the page
     * @param player the player
     */
    public void show(int page, Player player) {
        int lineIndex = 0;

        if (this.heightControl.isFillingTop() || this.heightControl.isFillingTopAndBottom())
            for (int i = 0; i < MAX_CHAT - getLines(page).size(); i++)
                player.sendMessage("");

        getFormattedTitle().send(player);
        if (isSpacedFormat())
            player.sendMessage("");

        for (; lineIndex < getLines(page).size(); lineIndex++)
            getLines(page).get(lineIndex).send(player);


        if (getHeightControl().isFillingInner())
            for (; lineIndex < getMaxHeight(); lineIndex++) {
                player.sendMessage("");
            }

        if (isSpacedFormat())
            player.sendMessage("");

        if (getFooter() == null && getCurrentPage() > 0) {
            FancyMessage pageNav = new FancyMessage("[ ");
            if (page > 0 && getLines().size() > 1)
                pageNav.then("<< Back").onClick(new FancyClickEvent() {
                    @Override
                    public void onClick() {
                        ChatMenu.this.show(page - 1, player);
                    }
                });
            if (page > 0 && page + 1 < getLines().size())
                pageNav.then(" ]" + lineColor + ChatColor.STRIKETHROUGH + "     ").then("[ ");
            if (page + 1 < getLines().size())
                pageNav.then(" Next >>").onClick(new FancyClickEvent() {
                    @Override
                    public void onClick() {
                        ChatMenu.this.show(page + 1, player);
                    }
                });
            pageNav.then(" ]");
            getFormatted(pageNav).send(player);
        } else {
            if (getCurrentPage() > 0) {
                FancyMessage pageNav = new FancyMessage("[ ");
                if (page > 0 && getLines().size() > 1)
                    pageNav.then("<< Back").onClick(new FancyClickEvent() {
                        @Override
                        public void onClick() {
                            ChatMenu.this.show(page - 1, player);
                        }
                    });
                else
                    pageNav.then("" + ChatColor.GRAY + ChatColor.STRIKETHROUGH + "<< Back");
                pageNav.then(" ]").then("" + lineColor + ChatColor.STRIKETHROUGH + "       ").then(" ").then(getFooter()).then(" ").then("" + lineColor + ChatColor.STRIKETHROUGH + "       ").then("[ ");
                if (page + 1 < getLines().size())
                    pageNav.then(" Next >>").onClick(new FancyClickEvent() {
                        @Override
                        public void onClick() {
                            ChatMenu.this.show(page + 1, player);
                        }
                    });
                else
                    pageNav.then("" + ChatColor.GRAY + ChatColor.STRIKETHROUGH + " Next >>");
                pageNav.then(" ]");
                getFormatted(pageNav).send(player);
            } else
                getFormattedFooter().send(player);
        }
        if (getHeightControl().isFillingTopAndBottom()) {
            int max = MAX_CHAT;
            if (isSpacedFormat())
                max -= 2;
            for (int i = (MAX_CHAT - lineIndex) / 2; i > 0; i--)
                player.sendMessage("");
        }
    }

    /**
     * To bottom chat menu.
     *
     * @param space the space
     * @return the chat menu
     */
    public ChatMenu toBottom(int space) {
        if (this.heightControl.isAuto())
            heightControl = HeightControl.MAX;

        int line = getMaxHeight() - space - 1;

        return setLine(getCurrentPage(), line, "");
    }

    public boolean isSpacedFormat() {
        return spacedFormat;
    }

    public ChatMenu setSpacedFormat(boolean spacedFormat) {
        this.spacedFormat = spacedFormat;
        return this;
    }

    /**
     * The enum Height control.
     */
    public enum HeightControl {
        /**
         * Inner fills top to max height
         */
        MAX,
        /**
         * Fills inner to set height
         */
        MANUAL,
        /**
         * No auto fill at all
         */
        AUTO,
        /**
         * No inner filling, fill top till max height
         */
        AUTO_EXTERNAL,
        /**
         * No inner filling, fill top and bottom till max height
         */
        AUTO_CENTER,
        /**
         * Fills inner till set height,  fill top till max height
         */
        MANUAL_EXTERNAL,
        /**
         * Fills inner till set height, fill top and bottom till max height
         */
        MANUAL_CENTER;

        public boolean isFillingTop() {
            return this == HeightControl.MANUAL_EXTERNAL ||
                    this == HeightControl.AUTO_EXTERNAL;
        }

        public boolean isFillingTopAndBottom() {
            return this == HeightControl.MANUAL_CENTER ||
                    this == HeightControl.AUTO_CENTER;
        }

        public boolean isFillingInner() {
            return this == HeightControl.MAX || isManual();
        }

        public boolean isNotFilling() {
            return this == HeightControl.MANUAL ||
                    this == HeightControl.AUTO;
        }

        public boolean isAuto() {
            return this == HeightControl.AUTO ||
                    this == HeightControl.AUTO_CENTER ||
                    this == HeightControl.AUTO_EXTERNAL;
        }

        public boolean isManual() {
            return this == HeightControl.MANUAL ||
                    this == HeightControl.MANUAL_CENTER ||
                    this == HeightControl.MANUAL_EXTERNAL;
        }

    }

}
