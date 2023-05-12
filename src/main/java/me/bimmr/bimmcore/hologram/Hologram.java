package me.bimmr.bimmcore.hologram;

import me.bimmr.bimmcore.reflection.Viewer;
import me.bimmr.bimmcore.utils.timed.Timed;
import me.bimmr.bimmcore.utils.timed.TimedEvent;
import me.bimmr.bimmcore.utils.timed.TimedObject;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

/**
 * The type Hologram.
 */
public class Hologram extends TimedObject {
    private final double LINE_HEIGHT = 0.27D;

    private List<HologramLine> hologramLines;
    private Viewer viewer;
    private Location location;
    private UUID uuid;

    /**
     * Instantiates a new Hologram.
     *
     * @param location the location
     */
    public Hologram(Location location) {
        this(true, location, "", null, false);
    }

    /**
     * Instantiates a new Hologram.
     *
     * @param location the location
     * @param text     the text
     */
    public Hologram(Location location, String text) {
        this(true, location, Arrays.asList(text), null, false);
    }

    /**
     * Instantiates a new Hologram.
     *
     * @param location the location
     * @param text     the text
     */
    public Hologram(Location location, List<String> text) {
        this(true, location, text, null, false);
    }

    /**
     * Instantiates a new Hologram.
     *
     * @param showToAll the show to all
     * @param location  the location
     * @param text      the text
     */
    public Hologram(boolean showToAll, Location location, String text) {
        this(showToAll, location, Arrays.asList(text), null, false);
    }

    /**
     * Instantiates a new Hologram.
     *
     * @param showToAll the show to all
     * @param location  the location
     * @param text      the text
     */
    public Hologram(boolean showToAll, Location location, List<String> text) {
        this(showToAll, location, text, null, false);
    }

    /**
     * Instantiates a new Hologram.
     *
     * @param showToAll the show to all
     * @param location  the location
     * @param text      the text
     * @param timed     the timed
     * @param time      the time
     */
    public Hologram(boolean showToAll, Location location, String text, Timed timed, int time ) {
        this(showToAll, location, Arrays.asList(text), new TimedEvent(time) {
            @Override
            public void run() {
                timed.onRun(this);
            }
        }, false);
    }

    /**
     * Instantiates a new Hologram.
     *
     * @param showToAll  the show to all
     * @param location   the location
     * @param text       the text
     * @param timedEvent the timed event
     */
    public Hologram(boolean showToAll, Location location, String text, TimedEvent timedEvent) {
        this(showToAll, location, Arrays.asList(text), timedEvent, false);
    }

    /**
     * Instantiates a new Hologram.
     *
     * @param showToAll the show to all
     * @param location  the location
     * @param text      the text
     * @param timed     the timed
     * @param time      the time
     */
    public Hologram(boolean showToAll, Location location, List<String> text, Timed timed, int time) {
        this(showToAll, location, text, new TimedEvent(time) {
            @Override
            public void run() {
                timed.onRun(this);
            }
        }, false);
    }

    /**
     * Instantiates a new Hologram.
     *
     * @param showToAll  the show to all
     * @param location   the location
     * @param text       the text
     * @param timedEvent the timed event
     */
    public Hologram(boolean showToAll, Location location, List<String> text, TimedEvent timedEvent) {
        this(showToAll, location, text, timedEvent, false);
    }

    /**
     * Instantiates a new Hologram.
     *
     * @param location the location
     * @param text     the text
     * @param timed    the timed
     * @param time     the time
     */
    public Hologram(Location location, List<String> text, Timed timed, int time) {
        this(location, text, new TimedEvent(time) {
            @Override
            public void run() {
                timed.onRun(this);
            }
        }, false);
    }

    /**
     * Instantiates a new Hologram.
     *
     * @param location   the location
     * @param text       the text
     * @param timedEvent the timed event
     */
    public Hologram(Location location, List<String> text, TimedEvent timedEvent) {
        this(true, location, text, timedEvent, false);
    }

    /**
     * Instantiates a new Hologram.
     *
     * @param location the location
     * @param text     the text
     * @param timed    the timed
     * @param time     the time
     */
    public Hologram(Location location, String text, Timed timed, int time) {
        this(location, text, new TimedEvent(time) {
            @Override
            public void run() {
                timed.onRun(this);
            }
        }, false);
    }

    /**
     * Instantiates a new Hologram.
     *
     * @param location   the location
     * @param text       the text
     * @param timedEvent the timed event
     */
    public Hologram(Location location, String text, TimedEvent timedEvent) {
        this(true, location, Arrays.asList(text), timedEvent, false);
    }

    /**
     * Instantiates a new Hologram.
     *
     * @param location        the location
     * @param text            the text
     * @param timed           the timed
     * @param time            the time
     * @param startTimedEvent the start timed event
     */
    public Hologram(Location location, String text, Timed timed, int time, boolean startTimedEvent) {
        this(location, text, new TimedEvent(time) {
            @Override
            public void run() {
                timed.onRun(this);
            }
        }, startTimedEvent);
    }

    /**
     * Instantiates a new Hologram.
     *
     * @param location        the location
     * @param text            the text
     * @param timedEvent      the timed event
     * @param startTimedEvent the start timed event
     */
    public Hologram(Location location, String text, TimedEvent timedEvent, boolean startTimedEvent) {
        this(true, location, text, timedEvent, startTimedEvent);
    }

    /**
     * Instantiates a new Hologram.
     *
     * @param location        the location
     * @param text            the text
     * @param timed           the timed
     * @param time            the time
     * @param startTimedEvent the start timed event
     */
    public Hologram(Location location, List<String> text, Timed timed, int time, boolean startTimedEvent) {
        this(location, text, new TimedEvent(time) {
            @Override
            public void run() {
                timed.onRun(this);
            }
        }, startTimedEvent);
    }

    /**
     * Instantiates a new Hologram.
     *
     * @param location        the location
     * @param text            the text
     * @param timedEvent      the timed event
     * @param startTimedEvent the start timed event
     */
    public Hologram(Location location, List<String> text, TimedEvent timedEvent, boolean startTimedEvent) {
        this(true, location, text, timedEvent, startTimedEvent);
    }

    /**
     * Instantiates a new Hologram.
     *
     * @param showToAll       the show to all
     * @param location        the location
     * @param text            the text
     * @param timed           the timed
     * @param time            the time
     * @param startTimedEvent the start timed event
     */
    public Hologram(boolean showToAll, Location location, String text, Timed timed, int time, boolean startTimedEvent) {
        this(showToAll, location, text, new TimedEvent(time) {
            @Override
            public void run() {
                timed.onRun(this);
            }
        }, startTimedEvent);
    }

    /**
     * Instantiates a new Hologram.
     *
     * @param showToAll       the show to all
     * @param location        the location
     * @param text            the text
     * @param timedEvent      the timed event
     * @param startTimedEvent the start timed event
     */
    public Hologram(boolean showToAll, Location location, String text, TimedEvent timedEvent, boolean startTimedEvent) {
        this(showToAll, location, Arrays.asList(text), timedEvent, startTimedEvent);
    }


    /**
     * Instantiates a new Hologram.
     *
     * @param showToAll       the show to all
     * @param location        the location
     * @param text            the text
     * @param timed           the timed
     * @param time            the time
     * @param startTimedEvent the start timed event
     */
    public Hologram(boolean showToAll, Location location, List<String> text, Timed timed, int time, boolean startTimedEvent) {
        this(showToAll, location, text, new TimedEvent(time) {
            @Override
            public void run() {
                timed.onRun(this);
            }
        }, startTimedEvent);
    }

    /**
     * Instantiates a new Hologram.
     *
     * @param showToAll       the show to all
     * @param location        the location
     * @param text            the text
     * @param timedEvent      the timed event
     * @param startTimedEvent the start timed event
     */
    public Hologram(boolean showToAll, Location location, List<String> text, TimedEvent timedEvent, boolean startTimedEvent) {
        this.uuid = UUID.randomUUID();
        this.location = location;
        this.hologramLines = new ArrayList<>();
        for (String line : text) {
            Location loc = this.location;
            loc.setY(loc.getY() - LINE_HEIGHT * this.hologramLines.size());
            this.hologramLines.add(new HologramLine(this, loc, line));
        }
        this.viewer = new Viewer(showToAll) {
            public void update(Player p) {
                for (HologramLine line : Hologram.this.hologramLines)
                    if (line != null && line.getText() != null && !line.getText().equals(""))
                        line.showPlayer(p.getName());
            }

            public void onAddToView(Player p) {
            }

            public void onRemoveFromView(Player p) {
            }
        };
        setTimedEvent(timedEvent, startTimedEvent);
    }

    /**
     * Gets viewer.
     *
     * @return the viewer
     */
    public Viewer getViewer() {
        return this.viewer;
    }

    /**
     * Gets hologram lines.
     *
     * @return the hologram lines
     */
    public List<HologramLine> getHologramLines() {
        return this.hologramLines;
    }

    /**
     * Gets location.
     *
     * @return the location
     */
    public Location getLocation() {
        return this.location;
    }

    /**
     * Sets text.
     *
     * @param text the text
     * @return the text
     */
    public Hologram setText(String text) {
        return setText(0, text, null, false);
    }

    /**
     * Sets text.
     *
     * @param line the line
     * @param text the text
     * @return the text
     */
    public Hologram setText(int line, String text) {
        return setText(line, text, null, false);
    }

    /**
     * Sets text.
     *
     * @param line  the line
     * @param text  the text
     * @param timed the timed
     * @param time  the time
     * @return the text
     */
    public Hologram setText(int line, String text, Timed timed, int time) {
        return setText(line, text, new TimedEvent(time) {
            @Override
            public void run() {
                timed.onRun(this);
            }
        });
    }

    /**
     * Sets text.
     *
     * @param line       the line
     * @param text       the text
     * @param timedEvent the timed event
     * @return the text
     */
    public Hologram setText(int line, String text, TimedEvent timedEvent) {
        return setText(line, text, timedEvent, false);
    }

    /**
     * Sets text.
     *
     * @param line                the line
     * @param text                the text
     * @param timed               the timed
     * @param time                the time
     * @param timedEventAutoStart the timed event auto start
     * @return the text
     */
    public Hologram setText(int line, String text, Timed timed, int time, boolean timedEventAutoStart) {
        return setText(line, text, new TimedEvent(time) {
            @Override
            public void run() {
                timed.onRun(this);
            }
        }, timedEventAutoStart);
    }

    /**
     * Sets text.
     *
     * @param line                the line
     * @param text                the text
     * @param timedEvent          the timed event
     * @param timedEventAutoStart the timed event auto start
     * @return the text
     */
    public Hologram setText(int line, String text, TimedEvent timedEvent, boolean timedEventAutoStart) {
        HologramLine holoLine = this.hologramLines.get(line);

        holoLine.setText(text);
        if (timedEvent != null)
            holoLine.setTimedEvent(timedEvent, timedEventAutoStart);
        this.viewer.update();
        return this;
    }

    /**
     * Sets hologram line.
     *
     * @param line the line
     * @param text the text
     * @return the hologram line
     */
    public Hologram setHologramLine(int line, String text) {
        return setHologramLine(line, text, null, false);
    }

    /**
     * Sets hologram line.
     *
     * @param line  the line
     * @param text  the text
     * @param timed the timed
     * @param time  the time
     * @return the hologram line
     */
    public Hologram setHologramLine(int line, String text, Timed timed, int time) {
        return setHologramLine(line, text, new TimedEvent(time) {
            @Override
            public void run() {
                timed.onRun(this);
            }
        });
    }

    /**
     * Sets hologram line.
     *
     * @param line       the line
     * @param text       the text
     * @param timedEvent the timed event
     * @return the hologram line
     */
    public Hologram setHologramLine(int line, String text, TimedEvent timedEvent) {
        return setHologramLine(line, text, timedEvent, false);
    }

    /**
     * Sets hologram line.
     *
     * @param line            the line
     * @param text            the text
     * @param timed           the timed
     * @param time            the time
     * @param startTimedEvent the start timed event
     * @return the hologram line
     */
    public Hologram setHologramLine(int line, String text, Timed timed, int time, boolean startTimedEvent) {
        return setHologramLine(line, text, new TimedEvent(time) {
            @Override
            public void run() {
                timed.onRun(this);
            }
        }, startTimedEvent);
    }

    /**
     * Sets hologram line.
     *
     * @param line            the line
     * @param text            the text
     * @param timedEvent      the timed event
     * @param startTimedEvent the start timed event
     * @return the hologram line
     */
    public Hologram setHologramLine(int line, String text, TimedEvent timedEvent, boolean startTimedEvent) {
        if (hologramLines.size() < line) {
            for (int i = hologramLines.size(); i < line; i++)
                addBlankLine();

            if (text == null || text.equals(" "))
                return addBlankLine();
            else
                return addText(text);
        } else {
            Location loc = this.location.clone();
            loc.setY(loc.getY() - 0.27D * this.hologramLines.size());
            this.hologramLines.add(line, new HologramLine(this, loc, text, timedEvent, startTimedEvent));
            this.viewer.update();
            return this;
        }
    }

    /**
     * Add text hologram.
     *
     * @param text the text
     * @return the hologram
     */
    public Hologram addText(String text) {
        return addText(text, null, false);
    }

    /**
     * Add text hologram.
     *
     * @param text  the text
     * @param timed the timed
     * @param time  the time
     * @return the hologram
     */
    public Hologram addText(String text, Timed timed, int time) {
        return addText(text, new TimedEvent(time) {
            @Override
            public void run() {
                timed.onRun(this);
            }
        }, false);
    }

    /**
     * Add text hologram.
     *
     * @param text       the text
     * @param timedEvent the timed event
     * @return the hologram
     */
    public Hologram addText(String text, TimedEvent timedEvent) {
        return addText(text, timedEvent, false);
    }

    /**
     * Add text hologram.
     *
     * @param text            the text
     * @param timed           the timed
     * @param time            the time
     * @param startTimedEvent the start timed event
     * @return the hologram
     */
    public Hologram addText(String text, Timed timed, int time, boolean startTimedEvent) {
        return addText(text, new TimedEvent(time) {
            @Override
            public void run() {
                timed.onRun(this);
            }
        }, startTimedEvent);
    }

    /**
     * Add text hologram.
     *
     * @param text            the text
     * @param timedEvent      the timed event
     * @param startTimedEvent the start timed event
     * @return the hologram
     */
    public Hologram addText(String text, TimedEvent timedEvent, boolean startTimedEvent) {
        Location loc = this.location.clone();
        loc.setY(loc.getY() - 0.27D * this.hologramLines.size());
        this.hologramLines.add(new HologramLine(this, loc, text, timedEvent, startTimedEvent));
        this.viewer.update();
        return this;
    }

    /**
     * Add blank line hologram.
     *
     * @return the hologram
     */
    public Hologram addBlankLine() {
        return addText("");
    }

    /**
     * Show player.
     *
     * @param player the player
     */
    public void showPlayer(Player player) {
        showPlayer(player.getName());
    }

    /**
     * Show player.
     *
     * @param player the player
     */
    public void showPlayer(String player) {
        this.viewer.addPlayer(player);
        this.viewer.update();
    }


    /**
     * Remove.
     */
    public void remove() {
        for (HologramLine line : this.hologramLines)
            line.remove();
    }

    /**
     * Remove.
     *
     * @param player the player
     */
    public void remove(String player) {
        for (HologramLine line : this.hologramLines)
            line.removePlayer(player);
        this.viewer.removePlayer(player);
    }

    /**
     * Remove.
     *
     * @param player the player
     */
    public void remove(Player player) {
        remove(player.getName());
    }

}
