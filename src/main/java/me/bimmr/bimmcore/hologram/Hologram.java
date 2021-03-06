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
 * A Utilities class to create a Hologram
 */
public class Hologram extends TimedObject {
    private final double LINE_HEIGHT = 0.27D;

    private List<HologramLine> hologramLines;
    private Viewer viewer;
    private Location location;
    private UUID uuid;

    /**
     * Create a Hologram
     * Shows to all, TimedEvent is null
     *
     * @param location The location for the hologram line
     */
    public Hologram(Location location) {
        this(true, location, "", null, false);
    }

    /**
     * Create a Hologram
     * Shows to all, TimedEvent is null
     *
     * @param location The location for the hologram line
     * @param text     The text for the hologram line as a String
     */
    public Hologram(Location location, String text) {
        this(true, location, Arrays.asList(text), null, false);
    }

    /**
     * Create a Hologram
     * Shows to all, TimedEvent is null
     *
     * @param location The location for the hologram line
     * @param text     The text for the hologram line as a List
     */
    public Hologram(Location location, List<String> text) {
        this(true, location, text, null, false);
    }

    /**
     * Create a Hologram
     * TimedEvent is null
     *
     * @param showToAll If the hologram is shown to all
     * @param location  The location for the hologram line
     * @param text      The text for the hologram line as a String
     */
    public Hologram(boolean showToAll, Location location, String text) {
        this(showToAll, location, Arrays.asList(text), null, false);
    }

    /**
     * Create a Hologram
     * TimedEvent is null
     *
     * @param showToAll If the hologram is shown to all
     * @param location  The location for the hologram line
     * @param text      The text for the hologram line as a List
     */
    public Hologram(boolean showToAll, Location location, List<String> text) {
        this(showToAll, location, text, null, false);
    }

    /**
     * Create a Hologram
     * Doesn't start TimedEvent
     *
     * @param showToAll  If the hologram is shown to all
     * @param location   The location for the hologram line
     * @param text       The text for the hologram line as a String
     * @param timed The timed event to run
     * @param time the time
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
     * Create a Hologram
     * Doesn't start TimedEvent
     *
     * @param showToAll  If the hologram is shown to all
     * @param location   The location for the hologram line
     * @param text       The text for the hologram line as a String
     * @param timedEvent The timed event to run
     */
    public Hologram(boolean showToAll, Location location, String text, TimedEvent timedEvent) {
        this(showToAll, location, Arrays.asList(text), timedEvent, false);
    }

    /**
     * Create a Hologram
     * Doesn't start TimedEvent
     *
     * @param showToAll  If the hologram is shown to all
     * @param location   The location for the hologram line
     * @param text       The text for the hologram line as a List
     * @param timed The timed event to run
     * @param time  the time
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
     * Create a Hologram
     * Doesn't start TimedEvent
     *
     * @param showToAll  If the hologram is shown to all
     * @param location   The location for the hologram line
     * @param text       The text for the hologram line as a List
     * @param timedEvent The timed event to run
     */
    public Hologram(boolean showToAll, Location location, List<String> text, TimedEvent timedEvent) {
        this(showToAll, location, text, timedEvent, false);
    }

    /**
     * Create a Hologram
     * Shows to all, Doesn't start TimedEvent
     *
     * @param location   The location for the hologram line
     * @param text       The text for the hologram line as a List
     * @param timed The timed event to run
     * @param time
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
     * Create a Hologram
     * Shows to all, Doesn't start TimedEvent
     *
     * @param location   The location for the hologram line
     * @param text       The text for the hologram line as a List
     * @param timedEvent The timed event to run
     */
    public Hologram(Location location, List<String> text, TimedEvent timedEvent) {
        this(true, location, text, timedEvent, false);
    }

    /**
     * Create a Hologram
     * Shows to all, Doesn't start Timed Event
     *
     * @param location   The location for the hologram line
     * @param text       The text for the hologram line as a String
     * @param timed The timed event to run
     * @param time the time
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
     * Create a Hologram
     * Shows to all, Doesn't start Timed Event
     *
     * @param location   The location for the hologram line
     * @param text       The text for the hologram line as a String
     * @param timedEvent The timed event to run
     */
    public Hologram(Location location, String text, TimedEvent timedEvent) {
        this(true, location, Arrays.asList(text), timedEvent, false);
    }

    /**
     * Create a Hologram
     * Shows to all
     *
     * @param location        The location for the hologram line
     * @param text            The text for the hologram line as a String
     * @param timed      The timed event to run
     * @param time  the time
     * @param startTimedEvent If timed even is starting right away
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
     * Create a Hologram
     * Shows to all
     *
     * @param location        The location for the hologram line
     * @param text            The text for the hologram line as a String
     * @param timedEvent      The timed event to run
     * @param startTimedEvent If timed even is starting right away
     */
    public Hologram(Location location, String text, TimedEvent timedEvent, boolean startTimedEvent) {
        this(true, location, text, timedEvent, startTimedEvent);
    }

    /**
     * Create a Hologram
     * Shows to all
     *
     * @param location        The location for the hologram line
     * @param text            The text for the hologram line as a List
     * @param timed      The timed event to run
     * @param time  the time
     * @param startTimedEvent If timed even is starting right away
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
     * Create a Hologram
     * Shows to all
     *
     * @param location        The location for the hologram line
     * @param text            The text for the hologram line as a List
     * @param timedEvent      The timed event to run
     * @param startTimedEvent If timed even is starting right away
     */
    public Hologram(Location location, List<String> text, TimedEvent timedEvent, boolean startTimedEvent) {
        this(true, location, text, timedEvent, startTimedEvent);
    }

    /**
     * Create a Hologram
     *
     * @param showToAll       If the hologram is shown to all
     * @param location        The location for the hologram line
     * @param text            The text for the hologram line as a String
     * @param timed      The timed event to run
     * @param time  the time
     * @param startTimedEvent If timed even is starting right away
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
     * Create a Hologram
     *
     * @param showToAll       If the hologram is shown to all
     * @param location        The location for the hologram line
     * @param text            The text for the hologram line as a String
     * @param timedEvent      The timed event to run
     * @param startTimedEvent If timed even is starting right away
     */
    public Hologram(boolean showToAll, Location location, String text, TimedEvent timedEvent, boolean startTimedEvent) {
        this(showToAll, location, Arrays.asList(text), timedEvent, startTimedEvent);
    }


    /**
     * Create a Hologram
     *
     * @param showToAll       If the hologram is shown to all
     * @param location        The location for the hologram line
     * @param text            The text for the hologram line as a List
     * @param timed           The timed event to run
     * @param time            the time
     * @param startTimedEvent If timed even is starting right away
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
     * Create a Hologram
     *
     * @param showToAll       If the hologram is shown to all
     * @param location        The location for the hologram line
     * @param text            The text for the hologram line as a List
     * @param timedEvent      The timed event to run
     * @param startTimedEvent If timed even is starting right away
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
     * @return Get the Viewer
     */
    public Viewer getViewer() {
        return this.viewer;
    }

    /**
     * Gets hologram lines.
     *
     * @return Get the list of HologramLines
     */
    public List<HologramLine> getHologramLines() {
        return this.hologramLines;
    }

    /**
     * Gets location.
     *
     * @return Get the Location
     */
    public Location getLocation() {
        return this.location;
    }

    /**
     * Set the Text of the first line
     * TimedEvent is null
     * Calls {@link #setText(int, String, TimedEvent, boolean)}
     *
     * @param text The text
     * @return The Hologram
     */
    public Hologram setText(String text) {
        return setText(0, text, null, false);
    }

    /**
     * Set the text of line x
     * TimedEvent is null
     * Calls {@link #setText(int, String, TimedEvent, boolean)}
     *
     * @param line The line number
     * @param text The text
     * @return The Hologram
     */
    public Hologram setText(int line, String text) {
        return setText(line, text, null, false);
    }

    /**
     * Set the text of line x and attach a TimedEvent
     * Doesn't auto-start the TimedEvent
     * Calls {@link #setText(int, String, TimedEvent, boolean)}
     *
     * @param line  The line number
     * @param text  The text
     * @param timed The TimedEvent
     * @param time  The time
     * @return The Hologram
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
     * Set the text of line x and attach a TimedEvent
     * Doesn't auto-start the TimedEvent
     * Calls {@link #setText(int, String, TimedEvent, boolean)}
     *
     * @param line       The line number
     * @param text       The text
     * @param timedEvent The TimedEvent
     * @return The Hologram
     */
    public Hologram setText(int line, String text, TimedEvent timedEvent) {
        return setText(line, text, timedEvent, false);
    }

    /**
     * Set the text of line x and attach a TimedEvent and say if it auto-starts
     *
     * @param line                The line number
     * @param text                The text
     * @param timed               The TimedEvent
     * @param time                the time
     * @param timedEventAutoStart If the TimedEvent auto-starts
     * @return The Hologram
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
     * Set the text of line x and attach a TimedEvent and say if it auto-starts
     *
     * @param line                The line number
     * @param text                The text
     * @param timedEvent          The TimedEvent
     * @param timedEventAutoStart If the TimedEvent auto-starts
     * @return The Hologram
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
     * @param timed the timed event
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
     * @param timed           the timed event
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
     * Add text to the hologram
     * TimedEvent is null
     * Calls {@link #addText(String, TimedEvent, boolean)}
     *
     * @param text The text
     * @return The Hologram
     */
    public Hologram addText(String text) {
        return addText(text, null, false);
    }

    /**
     * Add text to the hologram with an attached TimedEvent
     * TimedEvent doesn't auto-start
     * Calls {@link #addText(String, TimedEvent, boolean)}
     *
     * @param text  The text
     * @param timed The TimedEvent
     * @param time  the time
     * @return The hologram
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
     * Add text to the hologram with an attached TimedEvent
     * TimedEvent doesn't auto-start
     * Calls {@link #addText(String, TimedEvent, boolean)}
     *
     * @param text       The text
     * @param timedEvent The TimedEvent
     * @return The hologram
     */
    public Hologram addText(String text, TimedEvent timedEvent) {
        return addText(text, timedEvent, false);
    }

    /**
     * Add text to the hologram with an attached TimedEvent and saying if it auto-starts
     *
     * @param text            The text
     * @param timed           The TimedEvent
     * @param time            the time
     * @param startTimedEvent If the TimedEvent auto-starts
     * @return The Hologram
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
     * Add text to the hologram with an attached TimedEvent and saying if it auto-starts
     *
     * @param text            The text
     * @param timedEvent      The TimedEvent
     * @param startTimedEvent If the TimedEvent auto-starts
     * @return The Hologram
     */
    public Hologram addText(String text, TimedEvent timedEvent, boolean startTimedEvent) {
        Location loc = this.location.clone();
        loc.setY(loc.getY() - 0.27D * this.hologramLines.size());
        this.hologramLines.add(new HologramLine(this, loc, text, timedEvent, startTimedEvent));
        this.viewer.update();
        return this;
    }

    /**
     * Add a blank line to the Hologram
     *
     * @return The Hologram
     */
    public Hologram addBlankLine() {
        return addText("");
    }

    /**
     * Show the hologram to a player
     * Calls {@link #showPlayer(String)}
     *
     * @param player The player
     */
    public void showPlayer(Player player) {
        showPlayer(player.getName());
    }

    /**
     * Show the hologram to a player
     *
     * @param player The player's name
     */
    public void showPlayer(String player) {
        this.viewer.addPlayer(player);
        this.viewer.update();
    }


    /**
     * Remove the hologram and all the lines
     */
    public void remove() {
        for (HologramLine line : this.hologramLines)
            line.remove();
    }

    /**
     * Remove the hologram from a player
     *
     * @param player The player
     */
    public void remove(String player) {
        for (HologramLine line : this.hologramLines)
            line.removePlayer(player);
        this.viewer.removePlayer(player);
    }

    /**
     * Remove the hologram from a player
     *
     * @param player The player's name
     */
    public void remove(Player player) {
        remove(player.getName());
    }

}
