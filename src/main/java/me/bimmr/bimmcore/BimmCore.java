package me.bimmr.bimmcore;

import me.bimmr.bimmcore.messages.ActionBar;
import me.bimmr.bimmcore.messages.Title;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
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
            Class<?> c = Bukkit.class;
            Method method;
            try {
                method = c.getDeclaredMethod("getOnlinePlayers");
                Player[] players = (Player[]) method.invoke(null);
                return players;
            } catch (NoSuchMethodException | SecurityException | IllegalAccessException | IllegalArgumentException | InvocationTargetException e1) {

                return null;
            }
        }
    }

    @Override
    public void onEnable() {
        instance = this;

        ActionBar.ActionBarAPI.setup();
        Title.TitleAPI.setup();

    }
}
