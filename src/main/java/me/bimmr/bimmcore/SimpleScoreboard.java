package me.bimmr.bimmcore;

import com.google.common.base.Preconditions;
import com.google.common.base.Splitter;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;

import java.util.*;

public class SimpleScoreboard {

    private Scoreboard scoreboard;

    private String               name;
    private String               displayName;
    private Map<String, Integer> scores;
    private List<Team>           teams;
    private Objective            obj;

    public SimpleScoreboard(String name) {
        this.scoreboard = Bukkit.getScoreboardManager().getNewScoreboard();

        this.name = name;
        this.displayName = name;
        this.scores = Maps.newLinkedHashMap();
        this.teams = Lists.newArrayList();
    }

    public void blankLine() {
        add(ChatColor.RED.toString());
    }

    public void add(String text) {
        add(text, null);
    }

    public void add(String text, Integer score) {
        Preconditions.checkArgument(text.length() < 48, "text cannot be over 48 characters in length");
        text = fixDuplicates(text);
        scores.put(text, score);
    }

    private String fixDuplicates(String text) {
        while (scores.containsKey(text))
            text += ChatColor.RESET;
        if (text.length() > 48)
            text = text.substring(0, 47);
        return text;
    }

    private Map.Entry<Team, String> createTeam(String text) {
        String result = "";
        if (text.length() <= 16)
            return new AbstractMap.SimpleEntry<>(null, text);
        Team team = scoreboard.registerNewTeam("BLib-" + scoreboard.getTeams().size());
        Iterator<String> iterator = Splitter.fixedLength(16).split(text).iterator();
        team.setPrefix(iterator.next());
        result = iterator.next();
        if (text.length() > 32)
            team.setSuffix(iterator.next());
        teams.add(team);
        return new AbstractMap.SimpleEntry<>(team, result);
    }

    @SuppressWarnings("deprecation")
    public void build() {

        obj = scoreboard.registerNewObjective((name.length() > 16 ? name.substring(0, 15) : name), "dummy");
        obj.setDisplayName(displayName);
        obj.setDisplaySlot(DisplaySlot.SIDEBAR);
        int index = scores.size();

        for (Map.Entry<String, Integer> text : scores.entrySet()) {
            Map.Entry<Team, String> team = createTeam(text.getKey());
            Integer score = text.getValue() != null ? text.getValue() : index;
            // Modification start
            final Map.Entry<Team, String> teamf = team;
            OfflinePlayer player = new OfflinePlayer() {
                @Override
                public Map<String, Object> serialize() {
                    return null;
                }

                @Override
                public boolean isOp() {
                    return false;
                }

                @Override
                public void setOp(boolean arg0) {
                }

                @Override
                public boolean isWhitelisted() {
                    return false;
                }

                @Override
                public void setWhitelisted(boolean arg0) {
                }

                @Override
                public boolean isOnline() {
                    return false;
                }

                @Override
                public boolean isBanned() {
                    return false;
                }

                @Override
                public void setBanned(boolean arg0) {
                }

                @Override
                public boolean hasPlayedBefore() {
                    return false;
                }

                @Override
                public UUID getUniqueId() {
                    return null;
                }

                @Override
                public Player getPlayer() {
                    return null;
                }

                @Override
                public String getName() {
                    return teamf.getValue();
                }

                @Override
                public long getLastPlayed() {
                    return 0;
                }

                @Override
                public long getFirstPlayed() {
                    return 0;
                }

                @Override
                public Location getBedSpawnLocation() {
                    return null;
                }
            };
            // Modification end
            if (team.getKey() != null)
                team.getKey().addPlayer(player);
            obj.getScore(player).setScore(score);
            index -= 1;
        }
    }

    public void reset() {
        if (obj != null) {
            obj.unregister();
            for (Team t : teams)
                t.unregister();
        }
        teams.clear();
        scores.clear();

    }

    public Scoreboard getScoreboard() {
        return scoreboard;
    }

    public void send(Player... players) {
        for (Player p : players)
            p.setScoreboard(scoreboard);
    }

    public String getName() {
        return this.name;
    }

    public void setDisplayName(String name) {
        displayName = name;
    }

    public void removeScoreBeforeBuild(String text) {
        scores.remove(text);
    }

    public int getScoreBeforeBuild(String text) {
        return scores.get(text);
    }

    public Map<String, Integer> getScoresBeforeBuild() {
        return scores;
    }
}
