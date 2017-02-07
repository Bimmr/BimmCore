package me.bimmr.bimmcore.scoreboard;

import me.bimmr.bimmcore.BimmCore;
import me.bimmr.bimmcore.events.timing.TimedEvent;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;

import java.util.ArrayList;
import java.util.List;

/**
 * The Board
 */
public class Board {

    private List<BoardLine> lines;
    private Scoreboard      scoreboard;
    private Objective       objective;
    private BukkitTask      task;
    private TimedEvent      timedEvent;
    private String          name;

    /**
     * Create a Board
     *
     * @param title
     */
    public Board(String title) {
        this(title, -1, null);
    }

    /**
     * Create a Board
     *
     * @param title
     * @param size
     */
    public Board(String title, int size) {
        this(title, size, null);
    }

    /**
     * Create a Board
     *
     * @param title
     * @param timedEvent
     */
    public Board(String title, TimedEvent timedEvent) {
        this(title, -1, timedEvent);
    }

    /**
     * Create a Board
     *
     * @param name
     * @param timedEvent
     */
    public Board(String name, int size, TimedEvent timedEvent) {
        this.scoreboard = Bukkit.getScoreboardManager().getNewScoreboard();
        this.objective = this.scoreboard.registerNewObjective("BC" + name.substring(0, Math.min(14, name.length())), "dummy");
        this.objective.setDisplaySlot(DisplaySlot.SIDEBAR);
        this.objective.setDisplayName(name);
        setTimedEvent(timedEvent);
        this.name = name;
        if (size > 0)
            for (int i = 0; i < size; i++)
                addBlankLine();

        this.lines = new ArrayList<>();
    }

    /**
     * Get the TimedEvent
     *
     * @return
     */
    public TimedEvent getTimedEvent() {
        return timedEvent;
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
     * Remove a line from the board
     *
     * @param lineNo
     */
    public void remove(int lineNo) {
        lines.remove(lineNo);
    }

    /**
     * Set the text of a line
     *
     * @param lineNo
     * @param text
     */
    public void setText(int lineNo, String text) {

        BoardLine line;
        if ((line = getBoardLine(lineNo)) != null)
            line.setText(text);
        else {
            for (int i = lines.size(); i < lineNo; i++)
                addBlankLine();
            add(text);
        }
    }

    /**
     * Add a BoardLine
     *
     * @param boardLine
     */
    public void add(BoardLine boardLine) {
        boardLine.setBoard(this);
        lines.add(boardLine);
    }

    /**
     * Add a BoardLine
     *
     * @param line
     */
    public void add(String line) {
        add(line, -1);
    }

    /**
     * Add a line at a specific index
     *
     * @param index
     * @param line
     */
    public void add(int index, String line) {
        add(index, line, -1);
    }

    /**
     * Add a Boardline
     *
     * @param index
     * @param line
     * @param value
     */
    public void add(int index, String line, int value) {
        BoardLine boardLine = new BoardLine(line, value);
        boardLine.setBoard(this);
        if (index >= lines.size())
            index = lines.size() - 1;
        lines.add(index, boardLine);
    }

    /**
     * Add a Boardline
     *
     * @param line
     * @param value
     */
    public void add(String line, int value) {
        BoardLine boardLine = new BoardLine(line, value);
        boardLine.setBoard(this);
        lines.add(boardLine);
    }

    /**
     * Get the lines
     *
     * @return
     */
    public List<BoardLine> getLines() {
        return this.lines;
    }

    /**
     * Get the boardline
     *
     * @param lineNo
     * @return
     */
    public BoardLine getBoardLine(int lineNo) {
        if (lines.size() > lineNo)
            return lines.get(lineNo);
        return null;
    }

    /**
     * Get the objective
     *
     * @return
     */
    public Objective getObjective() {
        return this.objective;
    }

    /**
     * Get the scoreboard
     *
     * @return
     */
    public Scoreboard getScoreboard() {
        return this.scoreboard;
    }

    /**
     * Add a player to see this scoreboard
     *
     * @param player
     */
    public void send(Player player) {
        player.setScoreboard(this.scoreboard);
    }

    /**
     * Start a task that will allow BoardLine to update
     */
    public void startTask() {
        task = new BukkitRunnable() {
            long time = 0;

            @Override
            public void run() {
                if (timedEvent != null && time % timedEvent.getTicks() == 0)
                    timedEvent.run();

                for (BoardLine line : lines) {
                    if (line.getTimedEvent() != null && time % line.getTimedEvent().getTicks() == 0)
                        line.getTimedEvent().run();
                }

                if (time == Integer.MAX_VALUE)
                    time = 0;
                time++;
            }
        }.runTaskTimer(BimmCore.getInstance(), 0L, 1L);
    }

    /**
     * Stop the TimedEvent
     */
    public void stopTask() {
        task.cancel();
        task = null;
    }

    /**
     * Is the task running
     *
     * @return
     */
    public boolean isTaskRunning() {
        return task != null;
    }

    /**
     * Get the scoreboard's title
     *
     * @return
     */
    public String getName() {

        return this.name;
    }

    /**
     * Set the scoreboard's title
     *
     * @param title
     */
    public void setTitle(String title) {
        this.objective.setDisplayName(title);
    }

    /**
     * Reset the board
     */
    public void reset() {

        if (this.objective != null) {
            for (BoardLine line : lines)
                if (line.getTeam() != null) {
                    line.getTeam().unregister();
                    line.setTeam(null);
                }
            this.objective.unregister();
            this.objective = null;
        }
        this.lines.clear();
        this.objective = this.scoreboard.registerNewObjective("BC" + name.substring(0, Math.min(14, name.length())), "dummy");
        this.objective.setDisplaySlot(DisplaySlot.SIDEBAR);
        this.objective.setDisplayName(getName());
    }

    /**
     * Add a blank line
     */
    public void addBlankLine() {
        add("");
    }

    /**
     * Remove a BoardLine
     *
     * @param line
     */
    public void remove(BoardLine line) {
        lines.remove(line);
        line.remove();
    }


    public void setDisplayName(String name) {
        this.objective.setDisplayName(name);
    }

}
