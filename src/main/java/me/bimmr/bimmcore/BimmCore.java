package me.bimmr.bimmcore;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.logging.Level;

import me.bimmr.bimmcore.hologram.Hologram;
import me.bimmr.bimmcore.messages.FancyMessage;
import me.bimmr.bimmcore.reflection.Reflection;
import net.minecraft.server.v1_15_R1.EntityArmorStand;
import net.minecraft.server.v1_15_R1.PacketPlayOutNamedEntitySpawn;
import net.minecraft.server.v1_15_R1.PacketPlayOutSpawnEntityLiving;
import net.minecraft.server.v1_15_R1.World;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Utility;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.craftbukkit.v1_15_R1.CraftWorld;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

public class BimmCore extends JavaPlugin implements Listener {
    public static boolean oldAPI = false;

    private static BimmCore instance;

    public static BimmCore getInstance() {
        return instance;
    }

    public static boolean checkBimmLibVersion(final Plugin plugin, int versionNeeded) {
        int mcVersion = Integer.valueOf(instance.getDescription().getVersion().replaceAll("\\.", "").substring(0, 3)).intValue();
        if (mcVersion < versionNeeded) {
            Bukkit.getLogger().log(Level.SEVERE, plugin.getName() + " requires a newer BimmLib version.");
            (new BukkitRunnable() {
                public void run() {
                    Bukkit.getPluginManager().disablePlugin(plugin);
                }
            }).runTaskLater((Plugin)instance, 1L);
        }
        return (mcVersion >= versionNeeded);
    }

    public static Player[] getOnlinePlayers() {
        try {
            Collection<? extends Player> p = Bukkit.getOnlinePlayers();
            return p.<Player>toArray(new Player[p.size()]);
        } catch (NoSuchMethodError e) {
            try {
                Player[] players = (Player[])Reflection.getMethod(Bukkit.class, "getOnlinePlayers").invoke(null, new Object[0]);
                return players;
            } catch (SecurityException|IllegalAccessException|IllegalArgumentException|java.lang.reflect.InvocationTargetException e1) {
                e.printStackTrace();
                return null;
            }
        }
    }

    private void loadTimeUtil() {
        FileManager fileManager = new FileManager(getInstance());
        FileManager.Config config = fileManager.getConfig("Language.yml");
        config.setup();
        ConfigurationSection yearConfig = config.get().getConfigurationSection("Time.Year");
        ConfigurationSection monthConfig = config.get().getConfigurationSection("Time.Month");
        ConfigurationSection dayConfig = config.get().getConfigurationSection("Time.Day");
        ConfigurationSection hourConfig = config.get().getConfigurationSection("Time.Hour");
        ConfigurationSection minuteConfig = config.get().getConfigurationSection("Time.Minute");
        ConfigurationSection secondConfig = config.get().getConfigurationSection("Time.Second");
        TimeUtil.setIntervalStrings(new TimeUtil.Interval(yearConfig
                .getString("Single"), yearConfig.getString("Plural"), yearConfig.getString("Short")), new TimeUtil.Interval(monthConfig
                .getString("Single"), monthConfig.getString("Plural"), monthConfig.getString("Short")), new TimeUtil.Interval(dayConfig
                .getString("Single"), dayConfig.getString("Plural"), dayConfig.getString("Short")), new TimeUtil.Interval(hourConfig
                .getString("Single"), hourConfig.getString("Plural"), hourConfig.getString("Short")), new TimeUtil.Interval(minuteConfig
                .getString("Single"), minuteConfig.getString("Plural"), minuteConfig.getString("Short")), new TimeUtil.Interval(secondConfig
                .getString("Single"), secondConfig.getString("Plural"), secondConfig.getString("Short")));
        Bukkit.getPluginManager().registerEvents((Listener)new FancyMessage.FancyMessageListener(), (Plugin)this);
    }

    public void onEnable() {
        instance = this;
        loadTimeUtil();
        Bukkit.getPluginManager().registerEvents(this, (Plugin)this);
        List<String> oldAPIs = Arrays.asList("v1_8", "v1_9", "v1_10", "v1_11", "v1_12");
        String version = Reflection.getVersion();
        version = version.substring(0, version.lastIndexOf('_'));
        if (oldAPIs.contains(version))
            oldAPI = true;
    }

    @EventHandler
    public void command(PlayerCommandPreprocessEvent e) {
        if (e.getMessage().contains("BTest"))
        {
            Hologram h = new Hologram(e.getPlayer().getLocation(), "Test");
            h.showPlayer(e.getPlayer().getName());
        }
    }
}
