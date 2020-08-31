package me.bimmr.bimmcore;

import me.bimmr.bimmcore.files.Config;
import me.bimmr.bimmcore.files.FileManager;
import me.bimmr.bimmcore.gui.anvil.Anvil;
import me.bimmr.bimmcore.gui.book.Book;
import me.bimmr.bimmcore.gui.chat.ChatMenu;
import me.bimmr.bimmcore.gui.inventory.ClickEvent;
import me.bimmr.bimmcore.gui.inventory.Menu;
import me.bimmr.bimmcore.gui.inventory.MenuManager;
import me.bimmr.bimmcore.hologram.Hologram;
import me.bimmr.bimmcore.hologram.HologramLine;
import me.bimmr.bimmcore.items.Items;
import me.bimmr.bimmcore.items.attributes.AttributeType;
import me.bimmr.bimmcore.items.attributes.Operation;
import me.bimmr.bimmcore.items.attributes.Slot;
import me.bimmr.bimmcore.messages.ActionBar;
import me.bimmr.bimmcore.messages.BossBar;
import me.bimmr.bimmcore.messages.MessageDisplay;
import me.bimmr.bimmcore.messages.Title;
import me.bimmr.bimmcore.messages.fancymessage.FancyMessage;
import me.bimmr.bimmcore.messages.fancymessage.FancyMessageListener;
import me.bimmr.bimmcore.misc.Scroller;
import me.bimmr.bimmcore.npc.NPCBase;
import me.bimmr.bimmcore.npc.NPCClickEvent;
import me.bimmr.bimmcore.npc.NPCManager;
import me.bimmr.bimmcore.npc.player.NPCPlayer;
import me.bimmr.bimmcore.reflection.Reflection;
import me.bimmr.bimmcore.utils.MetricsLite;
import me.bimmr.bimmcore.utils.TimeUtil;
import me.bimmr.bimmcore.utils.timed.TimedEvent;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.EntityType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;

public class BimmCore extends JavaPlugin implements Listener {

    /**
     * @deprecated use {@link #supports(int)} instead
     */
    @Deprecated
    public static boolean oldAPI = !supports(13);

    private static BimmCore instance;
    private NPCManager npcManager;

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
        npcManager = new NPCManager();

        loadTimeUtil();
        Bukkit.getPluginManager().registerEvents(this, this);
        Bukkit.getPluginManager().registerEvents(new FancyMessageListener(), this);
        Bukkit.getPluginManager().registerEvents(new MenuManager(), this);
        Bukkit.getPluginManager().registerEvents(npcManager, this);

        if (Reflection.getOnlinePlayers().length == 0)
            npcManager.getNPCPlayerListener().start();
        else
            System.out.println("Unable to start NPCPlayer Listener on server reloads");

        MetricsLite metrics = new MetricsLite(this, 7671);
    }

    public void onDisable() {
        ArrayList<NPCBase> npcBaseList = new ArrayList<>();
        npcBaseList.addAll(NPCManager.getAllNPCs());
        for (NPCBase npcBase : npcBaseList)
            npcBase.destroy();
        Bukkit.getScheduler().cancelTasks(this);
    }

    @EventHandler
    public void command(PlayerCommandPreprocessEvent e) {
        if (false && e.getMessage().startsWith("/BTest")) {
            if (e.getMessage().contains("Menu")) {
                Menu menu = new Menu("Test");
                menu.addItem(new Items(Material.GOLD_BLOCK).setDisplayName("Testing Add"));
                menu.setItem(1, 3, new Items(Material.BOOK).setDisplayName("Testing Set"));
                menu.addItem(2, new Items(Material.DIAMOND_BLOCK).setDisplayName("Testing Add"), new ClickEvent() {
                    @Override
                    public void click() {
                        e.getPlayer().sendMessage("test");
                    }
                });
                menu.build();
                menu.open(e.getPlayer());
            }
            if (e.getMessage().contains("Book")) {
                Book book = new Book();
                book.addLine("" + ChatColor.DARK_AQUA + ChatColor.BOLD + "Book Header");
                book.addBlankLine();
                book.setLine(3, "Line 1");
                book.addBlankLine();

                book.addLine(new FancyMessage("Another line - Line 2").tooltip("Hover Message").onClick((fce) -> fce.getPlayer().sendMessage("test")));
                book.show(e.getPlayer());
            }
            if (e.getMessage().contains("Chat")) {
                System.out.println(FancyMessageListener.chats.size());

                new ChatMenu("[ McInfected Kit " + ChatColor.GREEN + "Private " + ChatColor.WHITE + "]", ChatColor.DARK_GREEN, ChatMenu.HeightControl.AUTO_EXTERNAL)
                        .setSpacedFormat(true)
                        .addLine(new FancyMessage(ChatColor.DARK_GRAY + "[" + ChatColor.GREEN + "✎" + ChatColor.DARK_GRAY + "]").tooltip("Click to set to item in hand").then("  ").then(ChatColor.GREEN + "Leggings: ").then(ChatColor.GRAY + "Diamond_Leggings").showItem(new Items(new ItemStack(Material.DIAMOND_LEGGINGS)).addEnchantment(Enchantment.DAMAGE_ALL, 1).getItem()))
                        .addLine(new FancyMessage(ChatColor.DARK_GRAY + "[" + ChatColor.GREEN + "✎" + ChatColor.DARK_GRAY + "]").tooltip("Click to set to item in hand").then("  ").then(ChatColor.GREEN + "Boots: ").then(ChatColor.GRAY + "Diamond_Boots").showItem(new Items(new ItemStack(Material.DIAMOND_BOOTS)).addEnchantment(Enchantment.DAMAGE_ALL, 1).getItem()))
                        .addLine(new FancyMessage(ChatColor.DARK_GRAY + "[" + ChatColor.GREEN + "⧉" + ChatColor.DARK_GRAY + "]").tooltip("Click to set to item in hand").then("  ").then(ChatColor.GREEN + "Inventory"))
                        .addLine(new FancyMessage(ChatColor.DARK_GRAY + "[" + ChatColor.GREEN + "⧉" + ChatColor.DARK_GRAY + "]").then("  ").then(ChatColor.YELLOW + "Effects"))
                        .addLine(new FancyMessage(ChatColor.DARK_GRAY + "[" + ChatColor.GREEN + "⧉" + ChatColor.DARK_GRAY + "]").then("  ").then(ChatColor.BLUE + "KillStreaks"))

                        .addLine(new FancyMessage(ChatColor.DARK_GRAY + "[" + ChatColor.GREEN + "✎" + ChatColor.DARK_GRAY + "]").then("  ").then(ChatColor.LIGHT_PURPLE + "Rename Kit "))
                        .addLine(new FancyMessage(ChatColor.DARK_GRAY + "[" + ChatColor.GREEN + "✎" + ChatColor.DARK_GRAY + "]").then("  ").then(ChatColor.LIGHT_PURPLE + "Rename Kit "))
                        .addLine(new FancyMessage(ChatColor.DARK_GRAY + "[" + ChatColor.GREEN + "✕" + ChatColor.DARK_GRAY + "]").then("  ").then(ChatColor.RED + "Unload Kit"))
                        .addLine(new FancyMessage(ChatColor.DARK_GRAY + "[" + ChatColor.GREEN + "✖" + ChatColor.DARK_GRAY + "]").then("  ").then(ChatColor.DARK_RED + "Delete Kit"))

                        .show(e.getPlayer());
            }
            if (e.getMessage().contains("Attribute")) {
                ItemStack sword = new ItemStack(Material.DIAMOND_SWORD);
//                Pre 1.13
//                sword = ItemAttributes.addAttribute(sword, new Attribute(AttributeType.GENERIC_MAX_HEALTH, Slot.HAND, 100, Operation.ADD_NUMBER));
//                sword = ItemAttributes.addAttribute(sword, new Attribute(AttributeType.GENERIC_ARMOR, Slot.HAND, 100, Operation.ADD_NUMBER));
//                sword = ItemAttributes.addAttribute(sword, new Attribute(AttributeType.GENERIC_MOVEMENT_SPEED, Slot.ALL, 3, Operation.MULTIPLY_SCALAR_1));

//                Post 1.13
                Items item = new Items(sword);
                item.addAttribute(AttributeType.GENERIC_MAX_HEALTH, Slot.HAND, 100, Operation.ADD_NUMBER);
                item.addAttribute(AttributeType.GENERIC_ARMOR, Slot.HAND, 100, Operation.ADD_NUMBER);
                item.addAttribute(AttributeType.GENERIC_MOVEMENT_SPEED, Slot.ALL, 3, Operation.MULTIPLY_SCALAR_1);


                e.getPlayer().getInventory().setItemInHand(sword);
            }
            if (e.getMessage().contains("Item")) {
                Items items = new Items(e.getPlayer().getInventory().getItemInHand());
                e.getPlayer().sendMessage(items.toString());
            }
            if (e.getMessage().contains("Hologram")) {
                Scroller scroller = new Scroller("&aThis &bIs &ca &dTest &fMessage", 12, 3);
                Hologram hologram = new Hologram(e.getPlayer().getLocation(), "Scrolling Holograms:");
                hologram.addBlankLine();
                hologram.addText(scroller.current(), new TimedEvent(2, this) {
                    @Override
                    public void run() {
                        HologramLine line = (HologramLine) getAttachedObject();
                        line.setText(scroller.next());
                    }
                }, true);
            }
            if (e.getMessage().contains("Action")) {
                Scroller scroller = new Scroller("&aThis &bIs &ca &dTest &fMessage", 12, 3);
                new ActionBar(scroller.current(), 10, new TimedEvent(5) {
                    @Override
                    public void run() {
                        ((MessageDisplay) getAttachedObject()).setText(scroller.next());
                    }
                }).send(e.getPlayer());
            }
            if (e.getMessage().contains("Boss")) {
                Scroller scroller = new Scroller("test message", 7, 2);
                new BossBar(scroller.current(), 10, BarColor.GREEN, BarStyle.SEGMENTED_10, new TimedEvent(5) {
                    @Override
                    public void run() {
                        ((MessageDisplay) getAttachedObject()).setText(scroller.next());
                    }
                }).send(e.getPlayer());
            }
            if (e.getMessage().contains("Title")) {
                Scroller scroller = new Scroller("test message", 7, 2);
                new Title(scroller.current(), 10, new TimedEvent(5) {
                    @Override
                    public void run() {
                        ((MessageDisplay) getAttachedObject()).setText(scroller.next());
                    }
                }).send(e.getPlayer());
            }
            if (e.getMessage().contains("NPCPlayer")) {
                NPCPlayer npc = new NPCPlayer("Bimmr", e.getPlayer().getLocation());
                npc.setNPCClickEvent(new NPCClickEvent() {
                    @Override
                    public void onRightClick() {
                        if (getPlayer().isSneaking()) {
                            npc.setSkin("brenden23");
                        } else {
                            npc.setSkin("eyJ0aW1lc3RhbXAiOjE1ODgwOTU3NjI0MTcsInByb2ZpbGVJZCI6IjkxZjA0ZmU5MGYzNjQzYjU4ZjIwZTMzNzVmODZkMzllIiwicHJvZmlsZU5hbWUiOiJTdG9ybVN0b3JteSIsInNpZ25hdHVyZVJlcXVpcmVkIjp0cnVlLCJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYTlkNTAwMGQ1OGQ3NzczYTViOTYwNjZlMDVlNTdjMmY3ZDc1MWM3YWNkYWFjOTdhYWFlZmIxODEwYzQ5MzMxZiJ9fX0=",
                                    "kxjU/PtVywX6Xs36RLIwH9j18ROeANCULxy3gLTid3YSgqGzGOKYDkWaSCL+8QtYFSNd6DTtfvDxswf9DF3+NXEe0ud9qjaYT+r9QHPvl3GvdTHiqavlYmo/QduiR7r0azck3MnbAbRDhUIGxmnMhltOHraY51HEydkWLtNzFiwJt5CcWBN1X5Q8H52fzG2yhPr+rgQ3+zPedDNT6bA8b5++JKsqS8Lf+e1qkBRWSncQByIfEz+X0/1xO4mcm/Xik/88suzfyWq87RM0fbu7PKzgeXTb6JfMReA2v5SjsNGWAUqLQmtqgW8rf63/QHmiacDLl8X0Rzrtu+gpM3dRqubXzvTRz87dxfhVybFPd5OOoiK6jGQL2NGmcLhcZMzJYV3I/ELWfcfsqcLL3hKEBg2Tu6cVimFTVhx7n0lbQvD+de5lNN4xe1lpD+4v2n+gdTauKQyqZxoTpOqIZ8bXaokZUP9UldzLhFYuiV+KTgaBGgLqWaFvckz7GRUKexUkU07y/12QoCVp9XHmC1G8wzW+LS8i1P3ka4wOArn5HE2L9SZWhN4ah31eQDh9ITtpFjqgJj4y5ydZdBanMVnqHlXRz2jsgDFmgysg5njdzpyhRbkMVe+jzxnJSfngKxQkKgi/Q2ReEWCk3TXwGBseA1Y/0z2CTMo8W3UP64sWOgk=");
                        }

                    }

                    @Override
                    public void onLeftClick() {
                        if (getPlayer().isSneaking())
                            npc.setName("Notch");
                        else
                            npc.setName("Bimmr");
                    }
                });
            }
            if (e.getMessage().contains("NPCMob")) {
                NPCBase npcBase = NPCManager.createNPC(NPCBase.NPCType.MOB, "Bimmr", e.getPlayer().getLocation());
                npcBase.setNPCClickEvent(new NPCClickEvent() {
                    @Override
                    public void onRightClick() {
                        if (getPlayer().isSneaking())
                            npcBase.asMob().setType(EntityType.SKELETON);
                        else
                            npcBase.asMob().setType(EntityType.IRON_GOLEM);

                    }

                    @Override
                    public void onLeftClick() {
                        if (getPlayer().isSneaking())
                            npcBase.setName("Notch");
                        else
                            npcBase.setName("Bimmr");
                    }
                });
            }
            if (e.getMessage().contains("AnvilTest")) {
                Anvil a = new Anvil("Test", "Default", null);
                a.open(e.getPlayer());
            }

            e.setCancelled(true);
        }
    }
}
