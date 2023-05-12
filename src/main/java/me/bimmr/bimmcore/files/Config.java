package me.bimmr.bimmcore.files;

import org.bukkit.configuration.Configuration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;

/**
 * The type Config.
 */
public class Config {
    private FileManager fileManager;
    private String name;

    private File file;

    private YamlConfiguration config;

    private Plugin resourcePlugin;

    /**
     * Instantiates a new Config.
     *
     * @param fileManager the file manager
     * @param name        the name
     */
    public Config(FileManager fileManager, String name) {
        this.fileManager = fileManager;
        this.name = name;
        this.resourcePlugin = fileManager.getPlugin();
    }

    /**
     * Contains boolean.
     *
     * @param path the path
     * @return the boolean
     */
    public boolean contains(String path) {
        return get().contains(path);
    }

    /**
     * Copy defaults config.
     *
     * @param force the force
     * @return the config
     */
    public Config copyDefaults(boolean force) {
        get().options().copyDefaults(force);
        return this;
    }

    /**
     * File exists boolean.
     *
     * @return the boolean
     */
    public boolean fileExists() {
        return (new File(fileManager.getPlugin().getDataFolder(), this.name)).exists();
    }

    /**
     * Remove file boolean.
     *
     * @return the boolean
     */
    public boolean removeFile() {
        return this.file.delete();
    }

    /**
     * Get yaml configuration.
     *
     * @return the yaml configuration
     */
    public YamlConfiguration get() {
        if (this.config == null)
            reload();
        return this.config;
    }

    /**
     * Get object.
     *
     * @param key the key
     * @return the object
     */
    public Object get(String key) {
        return get().get(key);
    }

    /**
     * Gets int.
     *
     * @param key the key
     * @return the int
     */
    public int getInt(String key) {
        return get().getInt(key);
    }

    /**
     * Gets boolean.
     *
     * @param key the key
     * @return the boolean
     */
    public boolean getBoolean(String key) {
        return get().getBoolean(key);
    }

    /**
     * Gets string.
     *
     * @param key the key
     * @return the string
     */
    public String getString(String key) {
        return get().getString(key);
    }

    /**
     * Reload config.
     *
     * @return the config
     */
    public Config reload() {
        if (this.file == null)
            this.file = new File(fileManager.getPlugin().getDataFolder(), this.name);
        this.config = YamlConfiguration.loadConfiguration(this.file);
        try {
            Reader defConfigStream = new InputStreamReader(this.resourcePlugin.getResource(this.name), StandardCharsets.UTF_8);
            if (defConfigStream != null) {
                YamlConfiguration defConfig = YamlConfiguration.loadConfiguration(defConfigStream);
                this.config.setDefaults((Configuration) defConfig);
            }
        } catch (NullPointerException ignored) {
        }
        return this;
    }

    /**
     * Reset config.
     *
     * @return the config
     */
    public Config reset() {
        this.file = new File(fileManager.getPlugin().getDataFolder(), this.name);
        this.resourcePlugin.saveResource(this.name, false);
        reload();
        return this;
    }

    /**
     * Save resource config.
     *
     * @param replace the replace
     * @return the config
     */
    public Config saveResource(boolean replace) {
        this.resourcePlugin.saveResource(this.name, replace);
        return this;
    }

    /**
     * Save config.
     *
     * @return the config
     */
    public Config save() {
        if (this.config == null || this.file == null)
            return this;
        try {
            if (getKeys("", true).size() > 0)
                this.config.save(this.file);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return this;
    }

    /**
     * Save default config config.
     *
     * @return the config
     */
    public Config saveDefaultConfig() {
        if (this.file == null)
            this.file = new File(fileManager.getPlugin().getDataFolder(), this.name);
        if (!this.file.exists() || this.file.length() == 0L)
            this.resourcePlugin.saveResource(this.name, false);
        return this;
    }

    /**
     * Set config.
     *
     * @param key   the key
     * @param value the value
     * @return the config
     */
    public Config set(String key, Object value) {
        get().set(key, value);
        return this;
    }

    /**
     * Sets resource plugin.
     *
     * @param plugin the plugin
     * @return the resource plugin
     */
    public Config setResourcePlugin(Plugin plugin) {
        this.resourcePlugin = plugin;
        return this;
    }

    /**
     * Gets name.
     *
     * @return the name
     */
    public String getName() {
        return this.name;
    }

    /**
     * Sets .
     *
     * @return the
     */
    public Config setup() {
        copyDefaults(true).save();
        return this;
    }

    /**
     * Gets keys.
     *
     * @param path the path
     * @param deep the deep
     * @return the keys
     */
    public ArrayList<String> getKeys(String path, boolean deep) {
        ArrayList<String> list = new ArrayList<>();
        if (get(path) != null)
            list.addAll(get().getConfigurationSection(path).getKeys(deep));
        return list;
    }

    /**
     * Gets keys.
     *
     * @param path the path
     * @return the keys
     */
    public ArrayList<String> getKeys(String path) {
        return getKeys(path, false);
    }

    /**
     * Gets keys.
     *
     * @return the keys
     */
    public ArrayList<String> getKeys() {
        return getKeys("", false);
    }
}
