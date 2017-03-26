package me.bimmr.bimmcore.menus.anvil;

/**
 * Created by Randy on 03/24/17.
 */
public abstract class AnvilFinishEvent {

    private Anvil anvil;


    public abstract void onFinish();

    public void setup(Anvil anvil) {
        this.anvil = anvil;
    }

    public Anvil getAnvil() {
        return this.anvil;
    }

}
