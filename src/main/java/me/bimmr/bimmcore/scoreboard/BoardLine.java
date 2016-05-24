package me.bimmr.bimmcore.scoreboard;

import com.google.common.base.Splitter;
import me.bimmr.bimmcore.events.timing.TimedEvent;
import org.bukkit.ChatColor;
import org.bukkit.scoreboard.Score;
import org.bukkit.scoreboard.Team;

import java.util.Iterator;

/**
 * Created by Randy on 05/23/16.
 */
public class BoardLine {

    private Team       team;
    private String     key;
    private int        lineNo;
    private int        value;
    private String     text;
    private Score      score;
    private Board      board;
    private TimedEvent timedEvent;

    /**
     * Create a BoardLine
     *
     * @param text
     */
    public BoardLine(String text) {
        this(text, -1, null);
    }

    /**
     * Create a BoardLine
     * <p>
     * value = -1
     *
     * @param text
     * @param value
     */
    public BoardLine(String text, int value) {
        this(text, value, null);
    }

    /**
     * Create a BoardLine
     * <p>
     * value = -1
     *
     * @param text
     * @param timedEvent
     */
    public BoardLine(String text, TimedEvent timedEvent) {
        this(text, -1, timedEvent);
    }

    /**
     * Create a BoardLine
     *
     * @param text
     * @param value
     * @param timedEvent
     */
    public BoardLine(String text, int value, TimedEvent timedEvent) {
        this.text = text;
        this.value = value;
        setTimedEvent(timedEvent);
    }

    /**
     * Set the board that this BoardLine belongs to
     *
     * @param board
     */
    public void setBoard(Board board) {
        this.board = board;

        this.lineNo = board.getLines().size();
        team = board.getScoreboard().registerNewTeam("" + lineNo);
        if (lineNo > 9) {
            this.key = "" + ChatColor.COLOR_CHAR + (lineNo - 9) + ChatColor.RESET + ChatColor.RESET;
        } else
            this.key = "" + ChatColor.COLOR_CHAR + lineNo + ChatColor.RESET;

        build();
    }

    /**
     * Get the text
     *
     * @return
     */
    public String getText() {
        return this.text;
    }

    /**
     * Set the text
     *
     * @param text
     */
    public void setText(String text) {
        this.text = text;
        build();
    }

    /**
     * Get the value
     *
     * @return
     */
    public int getValue() {
        return this.value;
    }

    /**
     * Set the value
     *
     * @param value
     */
    public void setValue(int value) {
        this.value = value;

        if (score != null)
            this.score.setScore(value);
    }

    /**
     * Get the TimedEvent
     *
     * @return
     */
    public TimedEvent getTimedEvent() {
        return this.timedEvent;
    }

    /**
     * Set the TimedEvent
     *
     * @param timedEvent
     */
    public void setTimedEvent(TimedEvent timedEvent) {
        if (timedEvent != null) {
            this.timedEvent = timedEvent;
            this.timedEvent.setAttachedObject(this);
        }
    }

    /**
     * Build the BoardLine
     */
    public void build() {

        Iterator<String> splitText;
        if (text.length() > 16)
            splitText = Splitter.fixedLength(16).split(text).iterator();
        else
            splitText = Splitter.fixedLength(text.length()).split(text).iterator();

        team.setPrefix(splitText.next());
        if (splitText.hasNext())
            team.setSuffix(splitText.next());

        if (!team.getEntries().contains(key))
            team.addEntry(key);
        if (score == null)
            score = board.getObjective().getScore(key);

        if (value == -1)
            score.setScore(16 - lineNo);
        else
            score.setScore(value);

    }


}
