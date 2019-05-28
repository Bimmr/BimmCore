package me.bimmr.bimmcore.menus.anvil;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

/**
 * Created by Randy on 03/24/17.
 */
//TODO: Make anvil GUI
class AnvilExample {
    public AnvilExample() {
        Anvil anvil = new Anvil("Test", new AnvilFinishEvent() {
            @Override
            public void onFinish() {
                Anvil anvil = getAnvil();
                String input = anvil.getInput();

            }
        }).open(null);
    }
}

public class Anvil {

    private String           message;
    private AnvilFinishEvent anvilFinishEvent;

    public Anvil(String message, AnvilFinishEvent finishEvent) {
        this.message = message;
        this.anvilFinishEvent = finishEvent;
        this.anvilFinishEvent.setup(this);

        ItemStack paper = new ItemStack(Material.PAPER);
        ItemMeta paperMeta = paper.getItemMeta();
        paperMeta.setDisplayName(message);
        paper.setItemMeta(paperMeta);



    }

    public Anvil open(Player player) {

        return this;
    }

    public String getMessage() {
        return this.message;
    }

    public String getInput() {
        return "";
    }


    public static class AnvilAPI{

    }

}
