package me.bimmr.bimmcore.scoreboard;

import java.util.ArrayList;
import java.util.List;
import me.bimmr.bimmcore.BimmCore;
import me.bimmr.bimmcore.TimedObject;
import me.bimmr.bimmcore.events.timing.TimedEvent;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;

public class Board extends TimedObject {
    private List<BoardLine> lines;

    public Board(String title) {
        this(title, -1, (TimedEvent)null, false);
    }

    public Board(String title, int size) {
        this(title, size, (TimedEvent)null, false);
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

    private Scoreboard scoreboard = Bukkit.getScoreboardManager().getNewScoreboard();

    private Objective objective;

    private String name;

    public Board(String name, int size, TimedEvent timedEvent, boolean startTimedEvent) {
        if (!BimmCore.oldAPI) {
            this.objective = this.scoreboard.registerNewObjective("BC" + name.substring(0, Math.min(14, name.length())), "dummy", name);
        } else {
            this.objective = this.scoreboard.registerNewObjective("BC" + name.substring(0, Math.min(14, name.length())), "dummy");
        }
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

    public void setText(int lineNo, String text) {
        BoardLine line;
        if ((line = getBoardLine(lineNo)) != null) {
            line.setText(text);
        } else {
            for (int i = this.lines.size(); i < lineNo; i++)
                addBlankLine();
            add(text);
        }
    }

    public void add(BoardLine boardLine) {
        boardLine.setBoard(this);
        this.lines.add(boardLine);
    }

    public void add(String line) {
        add(line, -1);
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

    public void reset() {
        if (this.objective != null) {
            for (BoardLine line : this.lines) {
                if (line.getTeam() != null) {
                    line.getTeam().unregister();
                    line.setTeam((Team)null);
                }
            }
            this.objective.unregister();
            this.objective = null;
        }
        this.lines.clear();
        if (!BimmCore.oldAPI) {
            this.objective = this.scoreboard.registerNewObjective("BC" + this.name.substring(0, Math.min(14, this.name.length())), "dummy", this.name);
        } else {
            this.objective = this.scoreboard.registerNewObjective("BC" + this.name.substring(0, Math.min(14, this.name.length())), "dummy");
        }
        this.objective.setDisplaySlot(DisplaySlot.SIDEBAR);
        this.objective.setDisplayName(getName());
    }

    public void addBlankLine() {
        add("");
    }

    public void remove(BoardLine line) {
        this.lines.remove(line);
        line.remove();
    }

    public void setDisplayName(String name) {
        this.objective.setDisplayName(name);
    }
}
