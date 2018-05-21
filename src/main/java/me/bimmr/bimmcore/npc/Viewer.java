package me.bimmr.bimmcore.npc;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import java.util.ArrayList;
import java.util.List;

public class Viewer implements Listener {

    public enum ViewerType {
        ALL, LIST
    }
    public Viewer(ViewerType viewerType){
        this.viewerType = viewerType;
        this.viewers = new ArrayList<>();
    }

    private ViewerType viewerType;
    private List<String> viewers;

    @EventHandler
    public void onJoin(PlayerJoinEvent e){

    }
}
