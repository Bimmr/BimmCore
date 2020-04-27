package me.bimmr.bimmcore.gui.anvil;


@Deprecated
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
