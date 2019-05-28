package me.bimmr.bimmcore.npc;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Viewer implements Listener {

    public Viewer( NPC npc){
        this.npc = npc;
        this.viewers = new ArrayList<>();
        for(Player p : Bukkit.getOnlinePlayers())
            viewers.add(p.getName());
    }

    private List<String> viewers;
    private NPC npc;

    @EventHandler
    public void onJoin(PlayerJoinEvent e){
        if(!viewers.contains(e.getPlayer().getName()))
            viewers.add(e.getPlayer().getName());
        updateView(e.getPlayer());
    }

    public void updateView(){
        for(String p : viewers)
            updateView(Bukkit.getPlayer(p));
    }
    public void updateView(Player player){
        npc.show(player);
        for(Map.Entry<NPC.ItemSlots, ItemStack> e : npc.getEquipment().entrySet())
            npc.equip(e.getKey(), player, e.getValue());
    }
    public List<String> getViewers(){return this.viewers;}
}
