package me.bimmr.bimmcore.messages;

/**
 * Created by Randy on 05/11/16.
 */
public abstract class SecondEvent {
    private BossBar   bossBar;
    private ActionBar actionBar;

    public void setBossBar(BossBar bossBar){
        this.bossBar = bossBar;
    }
    public void setActionBar(ActionBar actionBar){
        this.actionBar = actionBar;
    }
    public BossBar getBossBar() {
        return this.bossBar;
    }

    public ActionBar getActionBar() {
        return this.actionBar;
    }

    public abstract void run();
}
