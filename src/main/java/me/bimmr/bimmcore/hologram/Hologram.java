package me.bimmr.bimmcore.hologram;

import me.bimmr.bimmcore.utils.timed.TimedObject;
import me.bimmr.bimmcore.utils.timed.TimedEvent;
import me.bimmr.bimmcore.reflection.Viewer;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Hologram Class
 */
public class Hologram extends TimedObject {
    private final double LINE_HEIGHT = 0.27D;

    private List<HologramLine> hologramLines;
    private Viewer viewer;
    private Location location;

    /**
     * Create a Hologram
     * Shows to all, TimedEvent is null
     *
     * @param location The location for the hologram line
     * @param text     The text for the hologram line as a String
     **/
    public Hologram(Location location, String text) {
        this(true, location, Arrays.asList(text), null, false);
    }

    /**
     * Create a Hologram
     * Shows to all, TimedEvent is null
     *
     * @param location The location for the hologram line
     * @param text     The text for the hologram line as a List
     **/
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
     **/
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
     **/
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
     * @param timedEvent The timed event to run
     **/
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
     * @param timedEvent The timed event to run
     **/
    public Hologram(boolean showToAll, Location location, List<String> text, TimedEvent timedEvent) {
        this(showToAll, location, text, timedEvent, false);
    }

    /**
     * Create a Hologram
     * Shows to all, Doesn't start TimedEvent
     *
     * @param location   The location for the hologram line
     * @param text       The text for the hologram line as a List
     * @param timedEvent The timed event to run
     **/
    public Hologram(Location location, List<String> text, TimedEvent timedEvent) {
        this(true, location, text, timedEvent, false);
    }

    /**
     * Create a Hologram
     * Shows to all, Doesn't start Timed Event
     *
     * @param location   The location for the hologram line
     * @param text       The text for the hologram line as a String
     * @param timedEvent The timed event to run
     **/
    public Hologram(Location location, String text, TimedEvent timedEvent) {
        this(true, location, Arrays.asList(text), timedEvent, false);
    }

    /**
     * Create a Hologram
     * Shows to all
     *
     * @param location        The location for the hologram line
     * @param text            The text for the hologram line as a String
     * @param timedEvent      The timed event to run
     * @param startTimedEvent If timed even is starting right away
     **/
    public Hologram(Location location, String text, TimedEvent timedEvent, boolean startTimedEvent) {
        this(true, location, Arrays.asList(text), timedEvent, startTimedEvent);
    }

    /**
     * Create a Hologram
     * Shows to all
     *
     * @param location        The location for the hologram line
     * @param text            The text for the hologram line as a List
     * @param timedEvent      The timed event to run
     * @param startTimedEvent If timed even is starting right away
     **/
    public Hologram(Location location, List<String> text, TimedEvent timedEvent, boolean startTimedEvent) {
        this(true, location, text, timedEvent, startTimedEvent);
    }

    /**
     * Create a Hologram
     *
     * @param showToAll       If the hologram is shown to all
     * @param location        The location for the hologram line
     * @param text            The text for the hologram line as a String
     * @param timedEvent      The timed event to run
     * @param startTimedEvent If timed even is starting right away
     **/
    public Hologram(boolean showToAll, Location location, String text, TimedEvent timedEvent, boolean startTimedEvent) {
        this(showToAll, location, Arrays.asList(text), timedEvent, startTimedEvent);
    }


    /**
     * Create a Hologram
     *
     * @param showToAll       If the hologram is shown to all
     * @param location        The location for the hologram line
     * @param text            The text for the hologram line as a List
     * @param timedEvent      The timed event to run
     * @param startTimedEvent If timed even is starting right away
     **/
    public Hologram(boolean showToAll, Location location, List<String> text, TimedEvent timedEvent, boolean startTimedEvent) {
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
     * @return Get the Viewer
     */
    public Viewer getViewer() {
        return this.viewer;
    }

    /**
     * @return Get the list of HologramLines
     */
    public List<HologramLine> getHologramLines() {
        return this.hologramLines;
    }

    /**
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
