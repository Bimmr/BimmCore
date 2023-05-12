package me.bimmr.bimmcore.files;

import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

/**
 * The type File manager.
 */
public class FileManager {
    private final JavaPlugin plugin;
    private HashMap<String, Config> configs = new HashMap<>();

    /**
     * Instantiates a new File manager.
     *
     * @param plugin the plugin
     */
    public FileManager(JavaPlugin plugin) {
        this.plugin = plugin;
    }

    /**
     * Gets plugin.
     *
     * @return the plugin
     */
    public JavaPlugin getPlugin() {
        return plugin;
    }

    /**
     * Gets config.
     *
     * @param name the name
     * @return the config
     */
    public Config getConfig(String name) {
        if (!this.configs.containsKey(name))
            this.configs.put(name, new Config(this, name));
        return this.configs.get(name);
    }

    /**
     * File exists boolean.
     *
     * @param name the name
     * @return the boolean
     */
    public boolean fileExists(String name) {
        return (new File(this.plugin.getDataFolder(), name)).exists();
    }

    /**
     * Remove file.
     *
     * @param name the name
     */
    public void removeFile(String name) {
        (new File(this.plugin.getDataFolder(), name)).delete();
    }

    /**
     * Gets configs.
     *
     * @return the configs
     */
    public HashMap<String, Config> getConfigs() {
        return this.configs;
    }

    /**
     * Is config boolean.
     *
     * @param name the name
     * @return the boolean
     */
    public boolean isConfig(String name) {
        return this.configs.containsKey(name);
    }

    /**
     * Reload all.
     */
    public void reloadAll() {
        for (Map.Entry<String, Config> entry : this.configs.entrySet())
            entry.getValue().reload();
    }

    /**
     * Reload config config.
     *
     * @param name the name
     * @return the config
     */
    public Config reloadConfig(String name) {
        return getConfig(name).reload();
    }

    /**
     * Save config config.
     *
     * @param name the name
     * @return the config
     */
    public Config saveConfig(String name) {
        return getConfig(name).save();
    }

    /**
     * Load all configs.
     *
     * @param path the path
     */
    public void loadAllConfigs(String path) {
        for (File file : (new File(this.plugin.getDataFolder(), path)).listFiles()) {
            if (file.isFile()) {
                String name = path + file.getName();
                if (!isConfig(name)) {
                    getConfig(name);
                } else {
                    getConfig(name).reload();
                }
            }
        }
    }

    /**
     * Load all configs.
     */
    public void loadAllConfigs() {
        loadAllConfigs("");
    }

    /**
     * Sets default file from resources.
     *
     * @param file the file
     */
    public void setDefaultFileFromResources(String file) {
        getConfig(file).copyDefaults(true).save();
    }

}
