package me.bimmr.bimmcore.scoreboard;

import me.bimmr.bimmcore.BimmCore;
import me.bimmr.bimmcore.utils.timed.TimedEvent;
import me.bimmr.bimmcore.utils.timed.TimedObject;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.Criteria;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;

import java.util.ArrayList;
import java.util.List;

/**
 * The type Board.
 */
public class Board extends TimedObject {
    private List<BoardLine> lines;
    private Scoreboard scoreboard = Bukkit.getScoreboardManager().getNewScoreboard();
    private Objective objective;
    private String name;

    /**
     * Instantiates a new Board.
     *
     * @param title the title
     */
    public Board(String title) {
        this(title, -1, (TimedEvent) null, false);
    }

    /**
     * Instantiates a new Board.
     *
     * @param title the title
     * @param size  the size
     */
    public Board(String title, int size) {
        this(title, size, (TimedEvent) null, false);
    }

    /**
     * Instantiates a new Board.
     *
     * @param title      the title
     * @param timedEvent the timed event
     */
    public Board(String title, TimedEvent timedEvent) {
        this(title, -1, timedEvent, false);
    }

    /**
     * Instantiates a new Board.
     *
     * @param title           the title
     * @param timedEvent      the timed event
     * @param startTimedEvent the start timed event
     */
    public Board(String title, TimedEvent timedEvent, boolean startTimedEvent) {
        this(title, -1, timedEvent, startTimedEvent);
    }

    /**
     * Instantiates a new Board.
     *
     * @param name       the name
     * @param size       the size
     * @param timedEvent the timed event
     */
    public Board(String name, int size, TimedEvent timedEvent) {
        this(name, size, timedEvent, false);
    }

    /**
     * Instantiates a new Board.
     *
     * @param name            the name
     * @param size            the size
     * @param timedEvent      the timed event
     * @param startTimedEvent the start timed event
     */
    @SuppressWarnings("deprecation")
    public Board(String name, int size, TimedEvent timedEvent, boolean startTimedEvent) {
        String boardName = "BC" + name.substring(0, Math.min(14, name.length()));
            this.objective = this.scoreboard.registerNewObjective(boardName, "dummy", name);


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

    /**
     * Remove.
     *
     * @param lineNo the line no
     */
    public void remove(int lineNo) {
        this.lines.remove(lineNo);
    }

    /**
     * Sets text.
     *
     * @param lineNo the line no
     * @param text   the text
     * @return the text
     */
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

    /**
     * Add.
     *
     * @param boardLine the board line
     */
    public void add(BoardLine boardLine) {
        boardLine.setBoard(this);
        this.lines.add(boardLine);
    }

    /**
     * Add board line.
     *
     * @param line the line
     * @return the board line
     */
    public BoardLine add(String line) {
        BoardLine boardLine = new BoardLine(line, -1);
        add(boardLine);
        return boardLine;
    }

    /**
     * Add.
     *
     * @param index the index
     * @param line  the line
     */
    public void add(int index, String line) {

        add(index, line, -1);
    }

    /**
     * Add.
     *
     * @param index the index
     * @param line  the line
     * @param value the value
     */
    public void add(int index, String line, int value) {
        BoardLine boardLine = new BoardLine(line, value);
        boardLine.setBoard(this);
        if (index >= this.lines.size())
            index = this.lines.size() - 1;
        this.lines.add(index, boardLine);
    }

    /**
     * Add.
     *
     * @param line  the line
     * @param value the value
     */
    public void add(String line, int value) {
        BoardLine boardLine = new BoardLine(line, value);
        boardLine.setBoard(this);
        this.lines.add(boardLine);
    }

    /**
     * Gets lines.
     *
     * @return the lines
     */
    public List<BoardLine> getLines() {
        return this.lines;
    }

    /**
     * Gets board line.
     *
     * @param lineNo the line no
     * @return the board line
     */
    public BoardLine getBoardLine(int lineNo) {
        if (this.lines.size() > lineNo)
            return this.lines.get(lineNo);
        return null;
    }

    /**
     * Gets objective.
     *
     * @return the objective
     */
    public Objective getObjective() {
        return this.objective;
    }

    /**
     * Gets scoreboard.
     *
     * @return the scoreboard
     */
    public Scoreboard getScoreboard() {
        return this.scoreboard;
    }

    /**
     * Send.
     *
     * @param player the player
     */
    public void send(Player player) {
        player.setScoreboard(this.scoreboard);
    }

    public void onTaskRun(long time) {
        for (BoardLine line : this.lines) {
            if (line.getTimedEvent() != null && time % line.getTimedEvent().getTicks() == 0L)
                line.getTimedEvent().run();
        }
    }

    /**
     * Gets name.
     *
     * @return the name
     */
    public String getName() {
        return this.name;
    }

    /**
     * Sets title.
     *
     * @param title the title
     */
    public void setTitle(String title) {
        this.objective.setDisplayName(title);
    }

    /**
     * Clear.
     */
    public void clear(){
        if (this.objective != null) {
            for (BoardLine line : this.lines) {
                line.reset();
            }
        }
        this.lines.clear();
    }


    /**
     * Reset.
     */
    public void reset() {
        clear();
        if (this.objective != null) {
            this.objective.unregister();
            this.objective = null;
        }
        this.objective = this.scoreboard.registerNewObjective("BC" + this.name.substring(0, Math.min(14, this.name.length())), Criteria.DUMMY, this.name);

        this.objective.setDisplaySlot(DisplaySlot.SIDEBAR);
        this.objective.setDisplayName(getName());

        if(timedEvent != null)
            stopTask();
    }

    /**
     * Add blank line.
     */
    public void addBlankLine() {
        add("");
    }

    /**
     * Remove.
     *
     * @param line the line
     */
    public void remove(BoardLine line) {
        this.lines.remove(line);
        line.reset();
    }

    /**
     * Sets display name.
     *
     * @param name the name
     */
    public void setDisplayName(String name) {
        this.objective.setDisplayName(name);
    }
}