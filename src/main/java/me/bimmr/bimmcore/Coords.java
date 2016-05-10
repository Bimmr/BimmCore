package me.bimmr.bimmcore;

import org.apache.commons.lang.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Bimmr
 */
public class Coords {

    private String world;
    private int    x, y, z;
    private float pitch, yaw;

    public Coords(Location loc) {
        setWorld(loc.getWorld().getName());
        setX(loc.getBlockX());
        setY(loc.getBlockY());
        setZ(loc.getBlockZ());
        setYaw(loc.getYaw());
        setPitch(loc.getPitch());
    }

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

    public Coords(World world, int x, int y, int z) {
        setWorld(world.getName());
        setX(x);
        setY(y);
        setZ(z);
    }

    public Coords(World world, int x, int y, int z, float yaw, float pitch) {
        setWorld(world.getName());
        setX(x);
        setY(y);
        setZ(z);
    }

    /**
     * Get a list of <u>Coords</u> from a list of <u>Coords#toString</u>
     *
     * @param list
     * @return
     */
    public static ArrayList<Coords> listFromString(List<String> list) {
        ArrayList<Coords> coords = new ArrayList<Coords>();
        for (String string : list)
            coords.add(new Coords(string));
        return coords;
    }

    /**
     * Get a list of <u>Coords#toString</u> from a list of <u>Coords</u>
     *
     * @param list
     * @return
     */
    public static ArrayList<String> listFromCoords(List<Coords> list) {
        ArrayList<String> coords = new ArrayList<String>();
        for (Coords coord : list)
            coords.add(coord.toString());
        return coords;
    }

    /**
     * Get the location
     *
     * @return
     */
    public Location asLocation() {
        return new Location(Bukkit.getWorld(this.world), this.x, this.y, this.z, this.yaw, this.pitch);
    }

    /**
     * Get a location ignoring the Yaw and Pitch
     *
     * @return
     */
    public Location asLocationIgnoreYawAndPitch() {
        return new Location(Bukkit.getWorld(this.world), this.x, this.y, this.z);
    }

    /**
     * Gets the Coord as a nicely formatted String
     * worldName,x,y,z,yaw,pitch
     *
     * @return
     */
    @Override
    public String toString() {
        return this.world + "," + this.x + "," + this.y + "," + this.z + "," + this.yaw + "," + this.pitch;
    }

    /**
     * Gets the Coord as a nicely formatted String
     * worldName,x,y,z
     *
     * @return
     */
    public String toStringIgnoreYawAndPitch() {
        return this.world + "," + this.x + "," + this.y + "," + this.z;
    }

    /**
     * Get the world's name
     *
     * @return
     */
    public String getWorld() {
        return this.world;
    }

    /**
     * Set the world name
     *
     * @param world
     */
    public void setWorld(String world) {
        this.world = world;
    }

    /**
     * Get the x
     *
     * @return
     */
    public int getX() {
        return this.x;
    }

    /**
     * Set the x
     *
     * @param x
     */
    public void setX(int x) {
        this.x = x;
    }

    /**
     * Get the Y
     *
     * @return
     */
    public int getY() {
        return this.y;
    }

    /**
     * Set the Y
     *
     * @param y
     */
    public void setY(int y) {
        this.y = y;
    }


    /**
     * Get the Z
     *
     * @return
     */
    public int getZ() {
        return this.z;
    }

    /**
     * Set the Z
     *
     * @param z
     */
    public void setZ(int z) {
        this.z = z;
    }


    /**
     * Get the pitch
     *
     * @return
     */
    public float getPitch() {
        return this.pitch;
    }

    /**
     * Set the pitch
     *
     * @param pitch
     */
    public void setPitch(float pitch) {
        this.pitch = pitch;
    }

    /**
     * Get the Yaw
     *
     * @return
     */
    public float getYaw() {
        return this.yaw;
    }

    /**
     * Set the Yaw
     *
     * @param yaw
     */
    public void setYaw(float yaw) {
        this.yaw = yaw;
    }
}
