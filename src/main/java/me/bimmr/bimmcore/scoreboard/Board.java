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
 * Created by Randy on 05/23/16.
 */
public class Board {

    private List<BoardLine> lines;
    private Scoreboard      scoreboard;
    private Objective       objective;
    private BukkitTask      task;
    private TimedEvent      timedEvent;

    /**
     * Create a Board
     *
     * @param title
     */
    public Board(String title) {
        this(title, null);
    }

    /**
     * Create a Board
     *
     * @param title
     * @param timedEvent
     */
    public Board(String title, TimedEvent timedEvent) {

        this.scoreboard = Bukkit.getScoreboardManager().getNewScoreboard();
        this.objective = this.scoreboard.registerNewObjective("BC" + title, "dummy");
        this.objective.setDisplaySlot(DisplaySlot.SIDEBAR);
        this.objective.setDisplayName(title);
        setTimedEvent(timedEvent);

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
        getBoardLine(lineNo).setText(text);
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
        BoardLine boardLine = new BoardLine(line);
        boardLine.setBoard(this);
        lines.add(boardLine);
    }

    /**
     * Get the lines
     *
     * @return
     */
    public List<?> getLines() {
        return this.lines;
    }

    /**
     * Get the boardline
     *
     * @param lineNo
     * @return
     */
    public BoardLine getBoardLine(int lineNo) {
        return lines.get(lineNo);
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
    public void addPlayer(Player player) {
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
    }

    /**
     * Get the scoreboard's title
     *
     * @return
     */
    public String getTitle() {
        return this.objective.getDisplayName();
    }

    /**
     * Set the scoreboard's title
     *
     * @param title
     */
    public void setTitle(String title) {
        this.objective.setDisplayName(title);
    }
}
