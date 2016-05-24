package me.bimmr.bimmcore;

import me.bimmr.bimmcore.events.timing.TimedEvent;
import me.bimmr.bimmcore.messages.ActionBar;
import me.bimmr.bimmcore.messages.BossBar;
import me.bimmr.bimmcore.messages.MessageDisplay;
import me.bimmr.bimmcore.messages.Title;
import me.bimmr.bimmcore.reflection.Reflection;
import me.bimmr.bimmcore.scoreboard.Board;
import me.bimmr.bimmcore.scoreboard.BoardLine;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.lang.reflect.InvocationTargetException;
import java.util.Collection;
import java.util.logging.Level;

/**
 * Created by Randy on 12/25/2015.
 */
public class BimmCore extends JavaPlugin {

    private static Plugin instance;

    public static Plugin getInstance() {
        return instance;
    }

    public static boolean checkBimmLibVersion(final Plugin plugin, int versionNeeded) {

        int mcVersion = Integer.valueOf(instance.getDescription().getVersion().replaceAll("\\.", "").substring(0, 3));

        if (mcVersion < versionNeeded) {
            Bukkit.getLogger().log(Level.SEVERE, plugin.getName() + " requires a newer BimmLib version.");
            new BukkitRunnable() {
                public void run() {
                    Bukkit.getPluginManager().disablePlugin(plugin);
                }
            }.runTaskLater(instance, 1);
        }
        return (mcVersion >= versionNeeded);

    }

    /**
     * Get the online players returning an array
     * Safe for versions below 1.7.10
     *
     * @return
     */
    public static Player[] getOnlinePlayers() {
        // Try the 1.7.10+ Method of getting players
        try {
            Collection<? extends Player> p = Bukkit.getOnlinePlayers();
            return p.toArray(new Player[p.size()]);
        } catch (NoSuchMethodError e) {
            // Try older way using reflection(This way it works for all other
            // versions)
            try {
                Player[] players = (Player[]) Reflection.getMethod(Bukkit.class, "getOnlinePlayers").invoke(null);
                return players;
            } catch (SecurityException | IllegalAccessException | IllegalArgumentException | InvocationTargetException e1) {
                e.printStackTrace();
                return null;
            }
        }
    }

    @Override
    public void onEnable() {
        instance = this;

        final StringUtil.Scroller scroller = new StringUtil.Scroller("&3Tes&4ting &2Things", 15,5);
        final StringUtil.Scroller scroller2 = new StringUtil.Scroller("&3Tes&4ting &2Things", 10,5);

        TimedEvent timedEvent = new TimedEvent(3) {
            @Override
            public void run() {
                MessageDisplay bar = (MessageDisplay)this.getAttachedObject();
                bar.setText(scroller.next());
            }
        };
        TimedEvent timedEvent2 = new TimedEvent(3) {
            @Override
            public void run() {
                BoardLine board = (BoardLine)getAttachedObject();
                board.setText(scroller2.next());
            }
        };

        Board board = new Board("Test");
        board.add(new BoardLine(scroller2.next(), timedEvent2));
        board.add(new BoardLine("Test2          "));
        board.add(new BoardLine("Test3"));
        board.startTask();


        Title title = new Title("Test", "", 0, 20, timedEvent);
        ActionBar actionBar = new ActionBar("Test", 20, timedEvent.clone());
        BossBar bossBar = new BossBar("Test", 20, timedEvent.clone());
        for(Player player : Bukkit.getOnlinePlayers()){
            title.send(player);
            actionBar.send(player);
            bossBar.send(player);
            board.addPlayer(player);

        }

    }
}
