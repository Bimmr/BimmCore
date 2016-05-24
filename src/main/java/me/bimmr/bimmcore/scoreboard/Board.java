package me.bimmr.bimmcore.scoreboard;

import me.bimmr.bimmcore.BimmCore;
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
    private List<Player>    players;
    private String          title;
    private Scoreboard      scoreboard;
    private Objective       objective;
    private BukkitTask      task;

    public Board(String title) {
        this.title = title;
        this.scoreboard = Bukkit.getScoreboardManager().getNewScoreboard();
        this.objective = this.scoreboard.registerNewObjective("BC" + title, "dummy");
        this.objective.setDisplaySlot(DisplaySlot.SIDEBAR);
        this.objective.setDisplayName(title);

        this.lines = new ArrayList<>();
        this.players = new ArrayList<>();
    }

    public void add(BoardLine boardLine) {
        lines.add(boardLine);
        boardLine.setBoard(this);
    }

    public List<?> getLines() {
        return this.lines;
    }

    public List<?> getPlayers() {
        return this.players;
    }

    public BoardLine getBoardLine(int lineNo) {
        return lines.get(lineNo);
    }

    public Objective getObjective() {
        return this.objective;
    }

    public Scoreboard getScoreboard() {
        return this.scoreboard;
    }

    public void addPlayer(Player player) {
        player.setScoreboard(this.scoreboard);
        players.add(player);
    }
    public void startTask(){
        task = new BukkitRunnable() {
            long time = 0;

            @Override
            public void run() {
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

}
