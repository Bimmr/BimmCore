package me.bimmr.bimmcore.hologram;

import java.util.ArrayList;
import java.util.List;

import me.bimmr.bimmcore.BimmCore;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.Plugin;

public abstract class Viewer implements Listener {
    private List<String> players;

    private boolean autoUpdate;

    public abstract void update(Player paramPlayer);

    public abstract void onAddToView(Player paramPlayer);

    public abstract void onRemoveFromView(Player paramPlayer);

    public Viewer() {
        this(true);
    }

    public Viewer(boolean autoUpdate) {
        this(autoUpdate, new ArrayList<>());
    }

    public Viewer(List<String> players) {
        this(true, players);
    }

    public Viewer(boolean autoUpdate, List<String> players) {
        this.autoUpdate = autoUpdate;
        this.players = players;
        Bukkit.getPluginManager().registerEvents(this, (Plugin) BimmCore.getInstance());
        if (autoUpdate)
            for (Player p : Bukkit.getOnlinePlayers()) {
                players.add(p.getName());
                update(p);
            }
    }

    public void destroy() {
        HandlerList.unregisterAll(this);
    }

    public void update() {
        for (String player : this.players)
            update(Bukkit.getPlayer(player));
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent pje) {
        String name = pje.getPlayer().getName();
        if (!this.players.contains(name) && this.autoUpdate) {
            this.players.add(name);
            onAddToView(pje.getPlayer());
            update(pje.getPlayer());
        }
    }

    @EventHandler
    public void onLeave(PlayerQuitEvent pqe) {
        String name = pqe.getPlayer().getName();
        if (this.players.contains(name))
            removePlayer(pqe.getPlayer().getName());
    }

    public List<String> getPlayers() {
        return this.players;
    }

    public boolean isAutoUpdate() {
        return this.autoUpdate;
    }

    public void setAutoUpdate(boolean autoUpdate) {
        this.autoUpdate = autoUpdate;
    }

    public void addPlayer(String player) {
        getPlayers().add(player);
        onAddToView(Bukkit.getPlayer(player));
    }

    public void removePlayer(String player) {
        getPlayers().remove(player);
        onRemoveFromView(Bukkit.getPlayer(player));
    }
}
