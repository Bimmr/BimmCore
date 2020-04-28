package me.bimmr.bimmcore;

import me.bimmr.bimmcore.files.Config;
import me.bimmr.bimmcore.items.Items;
import me.bimmr.bimmcore.items.helpers.GlowEnchant;
import me.bimmr.bimmcore.messages.fancymessage.FancyClickEvent;
import me.bimmr.bimmcore.messages.fancymessage.FancyMessageListener;
import me.bimmr.bimmcore.files.FileManager;
import me.bimmr.bimmcore.timed.TimedEvent;
import me.bimmr.bimmcore.hologram.Hologram;
import me.bimmr.bimmcore.hologram.HologramLine;
import me.bimmr.bimmcore.gui.book.Book;
import me.bimmr.bimmcore.gui.chat.ChatMenu;
import me.bimmr.bimmcore.gui.inventory.ClickEvent;
import me.bimmr.bimmcore.gui.inventory.Menu;
import me.bimmr.bimmcore.gui.inventory.MenuManager;
import me.bimmr.bimmcore.messages.*;
import me.bimmr.bimmcore.messages.fancymessage.FancyMessage;
import me.bimmr.bimmcore.misc.Scroller;
import me.bimmr.bimmcore.utils.TimeUtil;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

public class BimmCore extends JavaPlugin implements Listener {

    /**
     * @deprecated use {@link #supports(int)} instead
     */
    @Deprecated
    public static boolean oldAPI = !supports(13);

    private static BimmCore instance;

    public static BimmCore getInstance() {
        return instance;
    }

    /**
     * @param plugin        The Plugin's Insance
     * @param versionNeeded The version needed
     * @return If the BimmCore version is equal or newer
     */
    public static boolean checkBimmCoreVersion(final Plugin plugin, int versionNeeded) {
        int bcVersion = Integer.parseInt(instance.getDescription().getVersion().replaceAll("\\.", ""));
        if (bcVersion < versionNeeded) {
            System.out.println(ChatColor.DARK_RED + plugin.getName() + " requires at least BimmCore version " + versionNeeded);
            (new BukkitRunnable() {
                public void run() {
                    Bukkit.getPluginManager().disablePlugin(plugin);
                }
            }).runTaskLater((Plugin) instance, 1L);
        }
        return (bcVersion >= versionNeeded);
    }

    private void loadTimeUtil() {
        FileManager fileManager = new FileManager(getInstance());
        Config config = fileManager.getConfig("Language.yml").setup();
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
    }

    public void onEnable() {
        instance = this;
        loadTimeUtil();

        Bukkit.getPluginManager().registerEvents(this, this);
        Bukkit.getPluginManager().registerEvents(new FancyMessageListener(), this);
        Bukkit.getPluginManager().registerEvents(new MenuManager(), this);
    }

    @EventHandler
    public void command(PlayerCommandPreprocessEvent e) {
       /* if (e.getMessage().contains("BTest Menu")) {
            Menu menu = new Menu("Test");
            menu.addItem(new ItemStack(Material.GOLD_BLOCK), "Testing Add");
            menu.setItem(1, 3, new ItemStack(Material.BOOK), "Testing Set");
            menu.addItem(2, new ItemStack(Material.DIAMOND_BLOCK), "Testing Add", new ClickEvent() {
                @Override
                public void click() {
                    e.getPlayer().sendMessage("test");
                }
            });
            menu.build();
            menu.open(e.getPlayer());
        }
        if (e.getMessage().contains("BTest Book")) {
            Book book = new Book();
            book.addLine("Test");
            book.addBlankLine();
            book.setLine(3, "testing Set");

            book.addLine(new FancyMessage("test").onClick(new FancyClickEvent() {
                @Override
                public void onClick() {
                    e.getPlayer().sendMessage("test");
                }
            }));
            book.openFor(e.getPlayer());
        }
        if (e.getMessage().contains("BTest Chat")) {
            ChatMenu chatMenu = new ChatMenu();
            chatMenu.addLine("Test");
            chatMenu.addBlankLine();
            chatMenu.addLine("test", new FancyClickEvent() {
                @Override
                public void onClick() {
                    e.getPlayer().sendMessage("test");
                }
            });
            chatMenu.show(e.getPlayer());
        }
        if (e.getMessage().contains("BTest Hologram")) {
            Scroller scroller = new Scroller("test message", 7, 2);
            Hologram hologram = new Hologram(e.getPlayer().getLocation(), "Test");
            hologram.addBlankLine();
            hologram.addText(scroller.current(), new TimedEvent(5, this) {
                @Override
                public void run() {
                    HologramLine line = (HologramLine) getAttachedObject();
                    line.setText(scroller.next());
                }
            }, true);
        }
        if (e.getMessage().contains("BTest Action")) {
            Scroller scroller = new Scroller("test message", 7, 2);
            new ActionBar(scroller.current(), 10, new TimedEvent(5) {
                @Override
                public void run() {
                    ((MessageDisplay) getAttachedObject()).setText(scroller.next());
                }
            }).send(e.getPlayer());
        }
        if (e.getMessage().contains("BTest Boss")) {
            Scroller scroller = new Scroller("test message", 7, 2);
            new BossBar(scroller.current(), 10, BarColor.GREEN, BarStyle.SEGMENTED_10, new TimedEvent(5) {
                @Override
                public void run() {
                    ((MessageDisplay) getAttachedObject()).setText(scroller.next());
                }
            }).send(e.getPlayer());
        }
        if (e.getMessage().contains("BTest Title")) {
            Scroller scroller = new Scroller("test message", 7, 2);
            new Title(scroller.current(), 10, new TimedEvent(5) {
                @Override
                public void run() {
                    ((MessageDisplay) getAttachedObject()).setText(scroller.next());
                }
            }).send(e.getPlayer());
        }*/

    }

    /**
     * Check if the server is a version
     * 1.15 -&gt; 15
     * 1.12 -&gt; 12
     * 1.8 -&gt; 8
     *
     * @param v The Version to support
     * @return If the server is of version
     */
    public static boolean supports(double v) {
        String serverVersion = Bukkit.getBukkitVersion();
        int index = serverVersion.indexOf('-');
        serverVersion = serverVersion.substring(0, index);

        int lastDot = serverVersion.lastIndexOf('.');
        if (v != Math.floor(v) && serverVersion.indexOf('.') != lastDot)
            serverVersion = serverVersion.substring(0, lastDot);

        serverVersion = serverVersion.substring(serverVersion.indexOf('.') + 1);
        double version = Double.parseDouble(serverVersion);
        //     15  <= 15.2
        return v <= version;
    }

    /**
     * Calls {@link #supports(double)}
     *
     * @param v The Version to support
     * @return If the server is of version
     */
    public static boolean supports(int v) {
        return supports(((double) v));
    }
}
