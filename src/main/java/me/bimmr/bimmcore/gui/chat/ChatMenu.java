package me.bimmr.bimmcore.gui.chat;

import me.bimmr.bimmcore.messages.fancymessage.FancyClickEvent;
import me.bimmr.bimmcore.messages.fancymessage.FancyMessage;
import me.bimmr.bimmcore.utils.StringUtil;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.util.ArrayList;

/**
 * Created by Randy on 07/06/16.
 */
public class ChatMenu {
    private final int MAX_CHAT_HEIGHT = 18;

    private ArrayList<ArrayList<FancyMessage>> lines = new ArrayList<>();
    private FancyMessage title;
    private FancyMessage footer;
    private ChatColor lineColor;
    private int height;
    private HeightControl heightControl;

    /**
     * Instantiates a new Chat menu.
     */
    public ChatMenu() {
        this((FancyMessage) null, (FancyMessage) null, ChatColor.DARK_GRAY, -1);
        this.heightControl = HeightControl.MAX;
    }

    /**
     * Instantiates a new Chat menu.
     *
     * @param heightControl the height control
     */
    public ChatMenu(HeightControl heightControl) {
        this((FancyMessage) null, (FancyMessage) null, ChatColor.DARK_GRAY, -1);
        this.heightControl = heightControl;
    }

    /**
     * Instantiates a new Chat menu.
     *
     * @param title the title
     */
    public ChatMenu(String title) {
        this(title, (FancyMessage) null, ChatColor.DARK_GRAY, -1);
        this.heightControl = HeightControl.MAX;
    }

    /**
     * Instantiates a new Chat menu.
     *
     * @param title         the title
     * @param heightControl the height control
     */
    public ChatMenu(String title, HeightControl heightControl) {
        this(title, (FancyMessage) null, ChatColor.DARK_GRAY, -1);
        this.heightControl = heightControl;
    }

    /**
     * Instantiates a new Chat menu.
     *
     * @param title     the title
     * @param lineColor the line color
     */
    public ChatMenu(String title, ChatColor lineColor) {
        this(title, (FancyMessage) null, lineColor, -1);
        this.heightControl = HeightControl.MAX;
    }

    /**
     * Instantiates a new Chat menu.
     *
     * @param title         the title
     * @param lineColor     the line color
     * @param heightControl the height control
     */
    public ChatMenu(String title, ChatColor lineColor, HeightControl heightControl) {
        this(title, (FancyMessage) null, lineColor, -1);
        this.heightControl = heightControl;
    }

    /**
     * Instantiates a new Chat menu.
     *
     * @param title     the title
     * @param lineColor the line color
     * @param height    the height
     */
    public ChatMenu(String title, ChatColor lineColor, int height) {
        this(title, (FancyMessage) null, lineColor, height);
    }

    /**
     * Instantiates a new Chat menu.
     *
     * @param title     the title
     * @param footer    the footer
     * @param lineColor the line color
     */
    public ChatMenu(String title, String footer, ChatColor lineColor) {
        this(title, footer, lineColor, -1);
        this.heightControl = HeightControl.MAX;
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
        this(title, footer, lineColor, -1);
        this.heightControl = heightControl;
    }

    /**
     * Instantiates a new Chat menu.
     *
     * @param title     the title
     * @param footer    the footer
     * @param lineColor the line color
     */
    public ChatMenu(String title, FancyMessage footer, ChatColor lineColor) {
        this(title, footer, lineColor, -1);
        this.heightControl = HeightControl.MAX;
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
        this(title, footer, lineColor, -1);
        this.heightControl = heightControl;
    }

    /**
     * Instantiates a new Chat menu.
     *
     * @param title     the title
     * @param footer    the footer
     * @param lineColor the line color
     */
    public ChatMenu(FancyMessage title, String footer, ChatColor lineColor) {
        this(title, footer, lineColor, -1);
        this.heightControl = HeightControl.MAX;
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
        this(title, footer, lineColor, -1);
        this.heightControl = heightControl;
    }

    /**
     * Instantiates a new Chat menu.
     *
     * @param title the title
     */
    public ChatMenu(FancyMessage title) {
        this(title, (FancyMessage) null, ChatColor.DARK_GRAY, -1);
        this.heightControl = HeightControl.MAX;
    }

    /**
     * Instantiates a new Chat menu.
     *
     * @param title         the title
     * @param heightControl the height control
     */
    public ChatMenu(FancyMessage title, HeightControl heightControl) {
        this(title, (FancyMessage) null, ChatColor.DARK_GRAY, -1);
        this.heightControl = heightControl;
    }

    /**
     * Instantiates a new Chat menu.
     *
     * @param title     the title
     * @param lineColor the line color
     */
    public ChatMenu(FancyMessage title, ChatColor lineColor) {
        this(title, (FancyMessage) null, lineColor, -1);
        this.heightControl = HeightControl.MAX;
    }

    /**
     * Instantiates a new Chat menu.
     *
     * @param title         the title
     * @param lineColor     the line color
     * @param heightControl the height control
     */
    public ChatMenu(FancyMessage title, ChatColor lineColor, HeightControl heightControl) {
        this(title, (FancyMessage) null, lineColor, -1);
        this.heightControl = heightControl;
    }

    /**
     * Instantiates a new Chat menu.
     *
     * @param title     the title
     * @param lineColor the line color
     * @param height    the height
     */
    public ChatMenu(FancyMessage title, ChatColor lineColor, int height) {
        this(title, (FancyMessage) null, lineColor, height);
    }

    /**
     * Instantiates a new Chat menu.
     *
     * @param title     the title
     * @param footer    the footer
     * @param lineColor the line color
     */
    public ChatMenu(FancyMessage title, FancyMessage footer, ChatColor lineColor) {
        this(title, footer, lineColor, -1);
        this.heightControl = HeightControl.MAX;
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
        this(title, footer, lineColor, -1);
        this.heightControl = heightControl;
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
        this(new FancyMessage(title), new FancyMessage(footer), lineColor, height);
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
        this(new FancyMessage(title), footer, lineColor, height);
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
        this(title, new FancyMessage(footer), lineColor, height);
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
        this.title = title;
        this.footer = footer;
        this.lineColor = lineColor;
        this.height = height;
        this.heightControl = HeightControl.MANUAL;
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
        return getLines().get(page);
    }

    /**
     * Gets formatted title.
     *
     * @return the formatted title
     */
    public FancyMessage getFormattedTitle() {
        return StringUtil.getCenteredMessage(getTitle(), "" + lineColor + ChatColor.STRIKETHROUGH);
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

    /**
     * Gets footer.
     *
     * @return the footer
     */
    public FancyMessage getFooter() {
        return footer;
    }

    /**
     * Gets line color.
     *
     * @return the line color
     */
    public ChatColor getLineColor() {
        return lineColor;
    }

    /**
     * Gets height.
     *
     * @return the height
     */
    public int getHeight() {
        int height = this.height;
        if (this.heightControl == HeightControl.MAX)
            height = MAX_CHAT_HEIGHT;
        else if (this.heightControl == HeightControl.AUTO)
            height = Integer.MAX_VALUE;
        return height;
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
        if (lines.size() <= page)
            nextPage();
        if (getLines(page).size() >= getHeight() || getLines(page).size() >= MAX_CHAT_HEIGHT)
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

    /**
     * Show.
     *
     * @param page   the page
     * @param player the player
     */
    public void show(int page, Player player) {
        getFormattedTitle().send(player);

        for (int i = 0; i < getLines(page).size() && i < MAX_CHAT_HEIGHT; i++)
            getLines(page).get(i).send(player);

        if (this.heightControl != HeightControl.AUTO)
            for (int i = getLines(page).size(); i < getHeight(); i++) {
                player.sendMessage("");
            }


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
                pageNav.then("     ").reset();
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
                    pageNav.then(""+ChatColor.GRAY+ ChatColor.STRIKETHROUGH + "<< Back");
                pageNav.then(" ]").then(""+lineColor+ChatColor.STRIKETHROUGH+"       ").then(" ").then(getFooter().reset()).then(" ").then(""+lineColor+ChatColor.STRIKETHROUGH+"       ").then("[ ").reset();
                if (page + 1 < getLines().size())
                    pageNav.then(" Next >>").onClick(new FancyClickEvent() {
                        @Override
                        public void onClick() {
                            ChatMenu.this.show(page + 1, player);
                        }
                    });
                else
                    pageNav.then(""+ChatColor.GRAY+ ChatColor.STRIKETHROUGH + " Next >>");
                pageNav.then(" ]");
                getFormatted(pageNav).send(player);
            } else
                getFormattedFooter().send(player);
        }
    }

    /**
     * To bottom chat menu.
     *
     * @param space the space
     * @return the chat menu
     */
    public ChatMenu toBottom(int space) {
        if (this.heightControl == HeightControl.AUTO)
            heightControl = HeightControl.MAX;

        int line = getHeight() - space - 1;

        return setLine(getCurrentPage(), line, "");
    }

    /**
     * The enum Height control.
     */
    public enum HeightControl {
        /**
         * Max height control.
         */
        MAX,
        /**
         * Auto height control.
         */
        AUTO,
        /**
         * Manual height control.
         */
        MANUAL
    }

}
