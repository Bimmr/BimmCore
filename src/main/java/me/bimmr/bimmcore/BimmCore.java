package me.bimmr.bimmcore;

import me.bimmr.bimmcore.reflection.Reflection;
import org.bukkit.Bukkit;
import org.bukkit.configuration.ConfigurationSection;
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

    private static BimmCore instance;

    public static BimmCore getInstance() {
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

    /**
     * Load the Time Intervals from the Language.yml
     */
    private void loadTimeUtil() {

        FileManager fileManager = new FileManager(BimmCore.getInstance());
        FileManager.Config config = fileManager.getConfig("Language.yml");
        config.setup();

        ConfigurationSection yearConfig = config.get().getConfigurationSection("Time.Year");
        ConfigurationSection monthConfig = config.get().getConfigurationSection("Time.Month");
        ConfigurationSection dayConfig = config.get().getConfigurationSection("Time.Day");
        ConfigurationSection hourConfig = config.get().getConfigurationSection("Time.Hour");
        ConfigurationSection minuteConfig = config.get().getConfigurationSection("Time.Minute");
        ConfigurationSection secondConfig = config.get().getConfigurationSection("Time.Second");

        TimeUtil.setIntervalStrings(
                new TimeUtil.Interval(yearConfig.getString("Single"), yearConfig.getString("Plural"), yearConfig.getString("Short")),
                new TimeUtil.Interval(monthConfig.getString("Single"), monthConfig.getString("Plural"), monthConfig.getString("Short")),
                new TimeUtil.Interval(dayConfig.getString("Single"), dayConfig.getString("Plural"), dayConfig.getString("Short")),
                new TimeUtil.Interval(hourConfig.getString("Single"), hourConfig.getString("Plural"), hourConfig.getString("Short")),
                new TimeUtil.Interval(minuteConfig.getString("Single"), minuteConfig.getString("Plural"), minuteConfig.getString("Short")),
                new TimeUtil.Interval(secondConfig.getString("Single"), secondConfig.getString("Plural"), secondConfig.getString("Short"))
        );

    }

    @Override
    public void onEnable() {
        instance = this;

        loadTimeUtil();

    }
}
