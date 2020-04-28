package me.bimmr.bimmcore.misc;

import org.apache.commons.lang.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;

import java.util.ArrayList;
import java.util.List;

/**
 * The type Coords.
 */
public class Coords {

    private String world;
    private int x, y, z;
    private float pitch, yaw;

    /**
     * Instantiates a new Coords.
     *
     * @param loc the loc
     */
    public Coords(Location loc) {
        setWorld(loc.getWorld().getName());
        setX(loc.getBlockX());
        setY(loc.getBlockY());
        setZ(loc.getBlockZ());
        setYaw(loc.getYaw());
        setPitch(loc.getPitch());
    }

    /**
     * Instantiates a new Coords.
     *
     * @param string the string
     */
    public Coords(String string) {
        String[] list = string.split(",");
        this.world = list[0];
        this.x = Integer.parseInt(list[1]);
        this.y = Integer.parseInt(list[2]);
        this.z = Integer.parseInt(list[3]);

        if (StringUtils.countMatches(string, ",") == 5) {
            setYaw(Float.parseFloat(list[4]));
            setPitch(Float.parseFloat(list[5]));
        }
    }

    /**
     * Instantiates a new Coords.
     *
     * @param world the world
     * @param x     the x
     * @param y     the y
     * @param z     the z
     */
    public Coords(World world, int x, int y, int z) {
        setWorld(world.getName());
        setX(x);
        setY(y);
        setZ(z);
    }

    /**
     * Instantiates a new Coords.
     *
     * @param world the world
     * @param x     the x
     * @param y     the y
     * @param z     the z
     * @param yaw   the yaw
     * @param pitch the pitch
     */
    public Coords(World world, int x, int y, int z, float yaw, float pitch) {
        setWorld(world.getName());
        setX(x);
        setY(y);
        setZ(z);
    }

    /**
     * List from string array list.
     *
     * @param list the list
     * @return the array list
     */
    public static ArrayList<Coords> listFromString(List<String> list) {
        ArrayList<Coords> coords = new ArrayList<Coords>();
        for (String string : list)
            coords.add(new Coords(string));
        return coords;
    }

    /**
     * List from coords array list.
     *
     * @param list the list
     * @return the array list
     */
    public static ArrayList<String> listFromCoords(List<Coords> list) {
        ArrayList<String> coords = new ArrayList<String>();
        for (Coords coord : list)
            coords.add(coord.toString());
        return coords;
    }

    /**
     * As location location.
     *
     * @return the location
     */
    public Location asLocation() {
        return new Location(Bukkit.getWorld(this.world), this.x, this.y, this.z, this.yaw, this.pitch);
    }

    /**
     * As location ignore yaw and pitch location.
     *
     * @return the location
     */
    public Location asLocationIgnoreYawAndPitch() {
        return new Location(Bukkit.getWorld(this.world), this.x, this.y, this.z);
    }

    @Override
    public String toString() {
        return this.world + "," + this.x + "," + this.y + "," + this.z + "," + this.yaw + "," + this.pitch;
    }

    /**
     * To string ignore yaw and pitch string.
     *
     * @return the string
     */
    public String toStringIgnoreYawAndPitch() {
        return this.world + "," + this.x + "," + this.y + "," + this.z;
    }

    /**
     * Gets world.
     *
     * @return the world
     */
    public String getWorld() {
        return this.world;
    }

    /**
     * Sets world.
     *
     * @param world the world
     */
    public void setWorld(String world) {
        this.world = world;
    }

    /**
     * Gets x.
     *
     * @return the x
     */
    public int getX() {
        return this.x;
    }

    /**
     * Sets x.
     *
     * @param x the x
     */
    public void setX(int x) {
        this.x = x;
    }

    /**
     * Gets y.
     *
     * @return the y
     */
    public int getY() {
        return this.y;
    }

    /**
     * Sets y.
     *
     * @param y the y
     */
    public void setY(int y) {
        this.y = y;
    }


    /**
     * Gets z.
     *
     * @return the z
     */
    public int getZ() {
        return this.z;
    }

    /**
     * Sets z.
     *
     * @param z the z
     */
    public void setZ(int z) {
        this.z = z;
    }


    /**
     * Gets pitch.
     *
     * @return the pitch
     */
    public float getPitch() {
        return this.pitch;
    }

    /**
     * Sets pitch.
     *
     * @param pitch the pitch
     */
    public void setPitch(float pitch) {
        this.pitch = pitch;
    }

    /**
     * Gets yaw.
     *
     * @return the yaw
     */
    public float getYaw() {
        return this.yaw;
    }

    /**
     * Sets yaw.
     *
     * @param yaw the yaw
     */
    public void setYaw(float yaw) {
        this.yaw = yaw;
    }
}
