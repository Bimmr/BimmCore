package me.bimmr.bimmcore.hologram;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import me.bimmr.bimmcore.TimedObject;
import me.bimmr.bimmcore.Viewer;
import me.bimmr.bimmcore.events.timing.TimedEvent;
import org.bukkit.Location;
import org.bukkit.entity.Player;

public class Hologram extends TimedObject {
    private final double LINE_HEIGHT = 0.27D;

    private List<HologramLine> hologramLines;

    private Viewer viewer;

    private Location location;

    public Hologram(Location location, String text) {
        this(true, location, Arrays.asList(new String[] { text }), (TimedEvent)null, false);
    }

    public Hologram(Location location, List<String> text) {
        this(true, location, text, (TimedEvent)null, false);
    }

    public Hologram(boolean showToAll, Location location, String text) {
        this(showToAll, location, Arrays.asList(new String[] { text }), (TimedEvent)null, false);
    }

    public Hologram(boolean showToAll, Location location, List<String> text) {
        this(showToAll, location, text, (TimedEvent)null, false);
    }

    public Hologram(boolean showToAll, Location location, String text, TimedEvent timedEvent) {
        this(showToAll, location, Arrays.asList(new String[] { text }), timedEvent, false);
    }

    public Viewer getViewer() {
        return this.viewer;
    }

    public Hologram(boolean showToAll, Location location, List<String> text, TimedEvent timedEvent) {
        this(showToAll, location, text, timedEvent, false);
    }

    public Hologram(Location location, List<String> text, TimedEvent timedEvent) {
        this(true, location, text, timedEvent, false);
    }

    public Hologram(Location location, String text, TimedEvent timedEvent) {
        this(true, location, Arrays.asList(new String[] { text }), timedEvent, false);
    }

    public Hologram(boolean showToAll, Location location, String text, TimedEvent timedEvent, boolean autoStart) {
        this(showToAll, location, Arrays.asList(new String[] { text }), timedEvent, autoStart);
    }

    public Hologram(Location location, String text, TimedEvent timedEvent, boolean autoStart) {
        this(true, location, Arrays.asList(new String[] { text }), timedEvent, autoStart);
    }

    public Hologram(Location location, List<String> text, TimedEvent timedEvent, boolean autoStart) {
        this(true, location, text, timedEvent, autoStart);
    }

    public List<HologramLine> getHologramLines() {
        return this.hologramLines;
    }

    public Location getLocation() {
        return this.location;
    }

    public Hologram(boolean showToAll, Location location, List<String> text, TimedEvent timedEvent, boolean autoStart) {
        this.location = location;
        this.hologramLines = new ArrayList<>();
        for (String line : text) {
            Location loc = this.location;
            loc.setY(loc.getY() - 0.27D * this.hologramLines.size());
            this.hologramLines.add(new HologramLine(loc, line));
        }
        this.viewer = new Viewer(showToAll) {
            public void update(Player p) {
                for (HologramLine line : Hologram.this.hologramLines)
                    line.showPlayer(p.getName());
            }

            public void onAddToView(Player p) {}

            public void onRemoveFromView(Player p) {}
        };
        setTimedEvent(timedEvent, autoStart);
    }

    public Hologram setText(String text) {
        return setText(0, text);
    }

    public Hologram setText(int line, String text) {
        ((HologramLine)this.hologramLines.get(line)).setText(text);
        this.viewer.update();
        return this;
    }

    public Hologram addText(String text) {
        Location loc = this.location.clone();
        loc.setY(loc.getY() - 0.27D * this.hologramLines.size());
        this.hologramLines.add(new HologramLine(loc, text));
        this.viewer.update();
        return this;
    }

    public void showPlayer(String player) {
        this.viewer.addPlayer(player);
        this.viewer.update();
    }

    public void remove() {
        for (HologramLine line : this.hologramLines)
            line.remove();
    }

    public void removePlayer(String player) {
        for (HologramLine line : this.hologramLines)
            line.removePlayer(player);
        this.viewer.removePlayer(player);
    }
}
