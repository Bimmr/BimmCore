package me.bimmr.bimmcore.scoreboard;

import com.google.common.base.Splitter;
import me.bimmr.bimmcore.BimmCore;
import me.bimmr.bimmcore.utils.timed.TimedEvent;
import me.bimmr.bimmcore.utils.timed.TimedObject;
import org.bukkit.ChatColor;
import org.bukkit.scoreboard.Score;
import org.bukkit.scoreboard.Team;

import java.util.Iterator;

/**
 * The type Board line.
 */
public class BoardLine extends TimedObject {

    private Team team;
    private String key;
    private int lineNo;

    private String text;

    private Score score;
    private int value;

    private Board board;

    /**
     * Instantiates a new Board line.
     *
     * @param text the text
     */
    public BoardLine(String text) {
        this(text, -1, null, false);
    }

    /**
     * Instantiates a new Board line.
     *
     * @param text  the text
     * @param value the value
     */
    public BoardLine(String text, int value) {
        this(text, value, null, false);
    }

    /**
     * Instantiates a new Board line.
     *
     * @param text       the text
     * @param timedEvent the timed event
     */
    public BoardLine(String text, TimedEvent timedEvent) {
        this(text, -1, timedEvent, false);
    }

    /**
     * Instantiates a new Board line.
     *
     * @param text                the text
     * @param timedEvent          the timed event
     * @param autoStartTimedEvent the auto start timed event
     */
    public BoardLine(String text, TimedEvent timedEvent, boolean autoStartTimedEvent) {
        this(text, -1, timedEvent, autoStartTimedEvent);
    }

    /**
     * Instantiates a new Board line.
     *
     * @param text       the text
     * @param value      the value
     * @param timedEvent the timed event
     */
    public BoardLine(String text, int value, TimedEvent timedEvent) {
        this(text, value, timedEvent, false);
    }

    /**
     * Instantiates a new Board line.
     *
     * @param text                the text
     * @param value               the value
     * @param timedEvent          the timed event
     * @param autoStartTimedEvent the auto start timed event
     */
    public BoardLine(String text, int value, TimedEvent timedEvent, boolean autoStartTimedEvent) {
//        if (BimmCore.supports(13))
//            this.text = text.substring(0, Math.min(120, text.length()));
//        else
//            this.text = text.substring(0, Math.min(30, text.length()));

        this.text= text;

        this.value = value;
        setTimedEvent(timedEvent, autoStartTimedEvent);
    }

    /**
     * Sets board.
     *
     * @param board the board
     */
    public void setBoard(Board board) {
        this.board = board;

        this.lineNo = board.getLines().size();
        this.key = ""+ChatColor.COLOR_CHAR+"B"+ChatColor.COLOR_CHAR+"C"+ChatColor.COLOR_CHAR+"-" + ChatColor.COLOR_CHAR + Integer.toHexString(lineNo);
        team = board.getScoreboard().registerNewTeam("" + key);
        build();
    }


    /**
     * Gets text.
     *
     * @return the text
     */
    public String getText() {
        return this.text;
    }

    /**
     * Sets text.
     *
     * @param text the text
     */
    public void setText(String text) {
            this.text = text.substring(0, Math.min(120, text.length()));
        update();
    }

    /**
     * Gets value.
     *
     * @return the value
     */
    public int getValue() {
        return this.value;
    }

    /**
     * Sets value.
     *
     * @param value the value
     */
    public void setValue(int value) {
        this.value = value;

        build();
    }

    /**
     * Build.
     */
    public void build() {

        if (this.team == null)
            if ((this.team = this.board.getScoreboard().getTeam("" + this.key)) == null)
                this.team = this.board.getScoreboard().registerNewTeam("" + this.key);

        if (!this.team.hasEntry(this.key))
            team.addEntry(this.key);

        if (this.score == null)
            this.score = this.board.getObjective().getScore(this.key);

        if (this.value == -1)
            this.score.setScore(16 - this.lineNo);
        else
            this.score.setScore(this.value);
    }

    /**
     * Update.
     */
    public void update() {
        Iterator<String> iterator = Splitter.fixedLength(64).split(this.text).iterator();
        String prefix = iterator.next();

        if(prefix.length() > 0 && prefix.charAt(prefix.length()-1) == ChatColor.COLOR_CHAR){
            iterator = Splitter.fixedLength(63).split(this.text).iterator();
            prefix = iterator.next();
        }
        this.team.setPrefix(prefix);

        if (iterator.hasNext())
            this.team.setSuffix(ChatColor.getLastColors(prefix) + iterator.next());
    }

    /**
     * Gets team.
     *
     * @return the team
     */
    public Team getTeam() {
        return this.team;
    }


    /**
     * Sets team.
     *
     * @param team the team
     */
    public void setTeam(Team team) {
        this.team = team;
    }

    /**
     * Clear.
     */
    public void clear() {
        if (this.key != null)
            this.board.getScoreboard().resetScores(this.key);
        if (this.team != null)
            this.team.unregister();
    }

    /**
     * Reset.
     */
    public void reset() {
        clear();
        if (getTimedEvent() != null)
            stopTask();
    }

}