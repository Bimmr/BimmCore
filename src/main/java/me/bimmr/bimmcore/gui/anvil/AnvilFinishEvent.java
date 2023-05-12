package me.bimmr.bimmcore.gui.anvil;

/**
 * The type Anvil finish event.
 */
@Deprecated
public abstract class AnvilFinishEvent {

    private Anvil anvil;

    /**
     * On finish.
     */
    public abstract void onFinish();

    /**
     * Sets .
     *
     * @param anvil the anvil
     */
    public void setup(Anvil anvil) {
        this.anvil = anvil;
    }

    /**
     * Gets anvil.
     *
     * @return the anvil
     */
    public Anvil getAnvil() {
        return this.anvil;
    }

}
