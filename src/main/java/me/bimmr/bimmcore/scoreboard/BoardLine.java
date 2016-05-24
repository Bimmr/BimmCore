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

    public BoardLine(String text) {
        this(text, -1, null);
    }

    public BoardLine(String text, int value) {
        this(text, value, null);
    }

    public BoardLine(String text, TimedEvent timedEvent) {
        this(text, -1, timedEvent);
    }

    public BoardLine(String text, int value, TimedEvent timedEvent) {
        this.text = text;
        this.value = value;
        setTimedEvent(timedEvent);
    }

    public void setBoard(Board board) {
        this.board = board;

        this.lineNo = board.getLines().size();
        team = board.getScoreboard().registerNewTeam("" + lineNo);
        this.key = "" + ChatColor.COLOR_CHAR + lineNo + ChatColor.RESET;

        build();
    }

    public void setText(String text) {
        this.text = text;
        build();
    }

    public void setValue(int value) {
        this.value = value;
    }

    public TimedEvent getTimedEvent() {
        return this.timedEvent;
    }

    public void setTimedEvent(TimedEvent timedEvent) {
        if (timedEvent != null) {
            this.timedEvent = timedEvent;
            this.timedEvent.setAttachedObject(this);
        }
    }

    public void build() {

        Iterator<String> splitText;
        if (text.length() > 16)
            splitText = Splitter.fixedLength(16).split(text).iterator();
        else
            splitText = Splitter.fixedLength(text.length()).split(text).iterator();

        team.setPrefix(splitText.next());
        if(splitText.hasNext())
            team.setSuffix(splitText.next());

        if(!team.getEntries().contains(key))
            team.addEntry(key);
        if(score == null)
            score = board.getObjective().getScore(key);

        if (value == -1)
            score.setScore(16 - lineNo);

    }


}
