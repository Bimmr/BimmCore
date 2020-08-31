package me.bimmr.bimmcore.scoreboard;

import me.bimmr.bimmcore.BimmCore;
import me.bimmr.bimmcore.utils.timed.TimedEvent;
import me.bimmr.bimmcore.utils.timed.TimedObject;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;

import java.util.ArrayList;
import java.util.List;

/**
 * The Scoreboard wrapper API
 */
public class Board extends TimedObject {
    private List<BoardLine> lines;
    private Scoreboard scoreboard = Bukkit.getScoreboardManager().getNewScoreboard();
    private Objective objective;
    private String name;

    public Board(String title) {
        this(title, -1, (TimedEvent) null, false);
    }

    public Board(String title, int size) {
        this(title, size, (TimedEvent) null, false);
    }

    public Board(String title, TimedEvent timedEvent) {
        this(title, -1, timedEvent, false);
    }

    public Board(String title, TimedEvent timedEvent, boolean startTimedEvent) {
        this(title, -1, timedEvent, startTimedEvent);
    }

    public Board(String name, int size, TimedEvent timedEvent) {
        this(name, size, timedEvent, false);
    }

    @SuppressWarnings("deprecation")
    public Board(String name, int size, TimedEvent timedEvent, boolean startTimedEvent) {
        String boardName = "BC" + name.substring(0, Math.min(14, name.length()));
        if (BimmCore.supports(13))
            this.objective = this.scoreboard.registerNewObjective(boardName, "dummy", name);
        else
            this.objective = this.scoreboard.registerNewObjective(boardName, "dummy");

        this.objective.setDisplaySlot(DisplaySlot.SIDEBAR);
        this.objective.setDisplayName(name);
        this.name = name;
        if (size > 0)
            for (int i = 0; i < size; i++)
                addBlankLine();
        this.lines = new ArrayList<>();
        setTimedEvent(timedEvent);
        if (startTimedEvent)
            startTask();
    }

    public void remove(int lineNo) {
        this.lines.remove(lineNo);
    }

    public BoardLine setText(int lineNo, String text) {
        BoardLine line;
        if ((line = getBoardLine(lineNo)) != null) {
            line.setText(text);
        } else {
            for (int i = this.lines.size(); i < lineNo; i++)
                addBlankLine();
            line = new BoardLine(text);
            add(line);
        }
        return line;
    }

    public void add(BoardLine boardLine) {
        boardLine.setBoard(this);
        this.lines.add(boardLine);
    }

    public BoardLine add(String line) {
        BoardLine boardLine = new BoardLine(line, -1);
        add(boardLine);
        return boardLine;
    }

    public void add(int index, String line) {

        add(index, line, -1);
    }

    public void add(int index, String line, int value) {
        BoardLine boardLine = new BoardLine(line, value);
        boardLine.setBoard(this);
        if (index >= this.lines.size())
            index = this.lines.size() - 1;
        this.lines.add(index, boardLine);
    }

    public void add(String line, int value) {
        BoardLine boardLine = new BoardLine(line, value);
        boardLine.setBoard(this);
        this.lines.add(boardLine);
    }

    public List<BoardLine> getLines() {
        return this.lines;
    }

    public BoardLine getBoardLine(int lineNo) {
        if (this.lines.size() > lineNo)
            return this.lines.get(lineNo);
        return null;
    }

    public Objective getObjective() {
        return this.objective;
    }

    public Scoreboard getScoreboard() {
        return this.scoreboard;
    }

    public void send(Player player) {
        player.setScoreboard(this.scoreboard);
    }

    public void onTaskRun(long time) {
        for (BoardLine line : this.lines) {
            if (line.getTimedEvent() != null && time % line.getTimedEvent().getTicks() == 0L)
                line.getTimedEvent().run();
        }
    }

    public String getName() {
        return this.name;
    }

    public void setTitle(String title) {
        this.objective.setDisplayName(title);
    }

    public void clear(){
        if (this.objective != null) {
            for (BoardLine line : this.lines) {
                line.reset();
            }
        }
        this.lines.clear();
    }


    @SuppressWarnings("deprecation")
    public void reset() {
        clear();
        if (this.objective != null) {
            this.objective.unregister();
            this.objective = null;
        }
        if (BimmCore.supports(13))
            this.objective = this.scoreboard.registerNewObjective("BC" + this.name.substring(0, Math.min(14, this.name.length())), "dummy", this.name);
        else
            this.objective = this.scoreboard.registerNewObjective("BC" + this.name.substring(0, Math.min(14, this.name.length())), "dummy");

        this.objective.setDisplaySlot(DisplaySlot.SIDEBAR);
        this.objective.setDisplayName(getName());

        if(timedEvent != null)
            stopTask();
    }

    public void addBlankLine() {
        add("");
    }

    public void remove(BoardLine line) {
        this.lines.remove(line);
        line.reset();
    }

    public void setDisplayName(String name) {
        this.objective.setDisplayName(name);
    }
}