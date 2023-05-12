package me.bimmr.bimmcore.reflection;

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

/**
 * The type Viewer.
 */
public abstract class Viewer implements Listener {
    private List<String> players;

    private boolean autoUpdate;

    /**
     * Update.
     *
     * @param paramPlayer the param player
     */
    public abstract void update(Player paramPlayer);

    /**
     * On add to view.
     *
     * @param paramPlayer the param player
     */
    public abstract void onAddToView(Player paramPlayer);

    /**
     * On remove from view.
     *
     * @param paramPlayer the param player
     */
    public abstract void onRemoveFromView(Player paramPlayer);

    /**
     * Instantiates a new Viewer.
     */
    public Viewer() {
        this(true);
    }

    /**
     * Instantiates a new Viewer.
     *
     * @param autoUpdate the auto update
     */
    public Viewer(boolean autoUpdate) {
        this(autoUpdate, new ArrayList<>());
    }

    /**
     * Instantiates a new Viewer.
     *
     * @param players the players
     */
    public Viewer(List<String> players) {
        this(true, players);
    }

    /**
     * Instantiates a new Viewer.
     *
     * @param autoUpdate the auto update
     * @param players    the players
     */
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

    /**
     * Destroy.
     */
    public void destroy() {
        HandlerList.unregisterAll(this);
    }

    /**
     * Update.
     */
    public void update() {
        for (String player : this.players)
            update(Bukkit.getPlayer(player));
    }

    /**
     * On join.
     *
     * @param pje the pje
     */
    @EventHandler
    public void onJoin(PlayerJoinEvent pje) {
        String name = pje.getPlayer().getName();
        if (!this.players.contains(name) && this.autoUpdate) {
            this.players.add(name);
            onAddToView(pje.getPlayer());
            update(pje.getPlayer());
        }
    }

    /**
     * On leave.
     *
     * @param pqe the pqe
     */
    @EventHandler
    public void onLeave(PlayerQuitEvent pqe) {
        String name = pqe.getPlayer().getName();
        if (this.players.contains(name))
            removePlayer(pqe.getPlayer().getName());
    }

    /**
     * Gets players.
     *
     * @return the players
     */
    public List<String> getPlayers() {
        return this.players;
    }

    /**
     * Is auto update boolean.
     *
     * @return the boolean
     */
    public boolean isAutoUpdate() {
        return this.autoUpdate;
    }

    /**
     * Sets auto update.
     *
     * @param autoUpdate the auto update
     */
    public void setAutoUpdate(boolean autoUpdate) {
        this.autoUpdate = autoUpdate;
    }

    /**
     * Add player.
     *
     * @param player the player
     */
    public void addPlayer(String player) {
        getPlayers().add(player);
        onAddToView(Bukkit.getPlayer(player));
    }

    /**
     * Remove player.
     *
     * @param player the player
     */
    public void removePlayer(String player) {
        getPlayers().remove(player);
        onRemoveFromView(Bukkit.getPlayer(player));
    }

    /**
     * Is viewing boolean.
     *
     * @param player the player
     * @return the boolean
     */
    public boolean isViewing(String player){
        return getPlayers().contains(player);
    }
}
