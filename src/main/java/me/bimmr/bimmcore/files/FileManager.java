package me.bimmr.bimmcore.files;

import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
/**
 * The Manager for the Config files
 */
public class FileManager {
    private final JavaPlugin plugin;
    private HashMap<String, Config> configs = new HashMap<>();

    public FileManager(JavaPlugin plugin) {
        this.plugin = plugin;
    }

    public JavaPlugin getPlugin() {
        return plugin;
    }

    public Config getConfig(String name) {
        if (!this.configs.containsKey(name))
            this.configs.put(name, new Config(this, name));
        return this.configs.get(name);
    }

    public boolean fileExists(String name) {
        return (new File(this.plugin.getDataFolder(), name)).exists();
    }

    public void removeFile(String name) {
        (new File(this.plugin.getDataFolder(), name)).delete();
    }

    public HashMap<String, Config> getConfigs() {
        return this.configs;
    }

    public boolean isConfig(String name) {
        return this.configs.containsKey(name);
    }

    public void reloadAll() {
        for (Map.Entry<String, Config> entry : this.configs.entrySet())
            entry.getValue().reload();
    }

    public Config reloadConfig(String name) {
        return getConfig(name).reload();
    }

    public Config saveConfig(String name) {
        return getConfig(name).save();
    }

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

    public void loadAllConfigs() {
        loadAllConfigs("");
    }

    public void setDefaultFileFromResources(String file) {
        getConfig(file).copyDefaults(true).save();
    }

}
