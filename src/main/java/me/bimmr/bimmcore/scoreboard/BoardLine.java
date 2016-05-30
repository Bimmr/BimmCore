package me.bimmr.bimmcore.scoreboard;

import com.google.common.base.Splitter;
import me.bimmr.bimmcore.StringUtil;
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
        this.text = text.substring(0, Math.min(30, text.length()));
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
        if (lineNo < 16) {
            team = board.getScoreboard().registerNewTeam("" + lineNo);
            if (lineNo > 9) {
                this.key = "" + ChatColor.COLOR_CHAR + (lineNo - 9) + ChatColor.RESET + ChatColor.RESET;
            } else
                this.key = "" + ChatColor.COLOR_CHAR + lineNo + ChatColor.RESET;

            build();
        }

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
        text = StringUtil.addColor(text);
        if (text.length() >= 1) {

            Iterator<String> splitText = Splitter.fixedLength(text.length() > 16 ? 16 : text.length()).split(text).iterator();

            String prefix = splitText.next();
            String suffix = splitText.hasNext() ? splitText.next() : null;

            //If the text is "&4Something &|2Like This"
            if (suffix != null && prefix.charAt(15) == ChatColor.COLOR_CHAR)
                team.setPrefix(prefix.substring(0, 14));

                //If the text is "&4Something &2|Like This"
            else if (suffix != null && prefix.charAt(14) == ChatColor.COLOR_CHAR)
                team.setPrefix(prefix.substring(0, 14));
            else
                team.setPrefix(prefix);

            if (suffix != null) {

                //If the text is "&4Something &|2Like This"
                if (prefix.charAt(15) == ChatColor.COLOR_CHAR)
                    team.setSuffix(ChatColor.COLOR_CHAR + suffix);

                else
                    team.setSuffix(ChatColor.getLastColors(prefix) + suffix);
            }
            if (!team.getEntries().contains(key))
                team.addEntry(key);

        }
        if (score == null)
            score = board.getObjective().getScore(key);

        if (value == -1)
            score.setScore(16 - lineNo);
        else
            score.setScore(value);

    }

    /**
     * Get the team
     *
     * @return
     */
    public Team getTeam() {
        return this.team;
    }

    /**
     * Set the Line's team
     *
     * @param team
     */
    public void setTeam(Team team) {
        this.team = team;
    }

}
