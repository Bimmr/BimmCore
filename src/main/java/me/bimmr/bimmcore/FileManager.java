package me.bimmr.bimmcore;

import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.*;
import java.util.HashMap;
import java.util.Map.Entry;

/**
 * Easy way to handle config files
 */
public class FileManager {

    private final JavaPlugin plugin;
    private HashMap<String, Config> configs = new HashMap<String, Config>();

    public FileManager(JavaPlugin plugin) {
        this.plugin = plugin;
    }

    /**
     * Get the config by the name(Don't forget the .yml)
     *
     * @param name
     * @return
     */
    public Config getConfig(String name) {
        if (!configs.containsKey(name)) configs.put(name, new Config(name));

        return configs.get(name);
    }

    /**
     * Gets if the file exits in the directory
     *
     * @param name
     * @return
     */
    public boolean fileExists(String name) {
        return (new File(plugin.getDataFolder(), name)).exists();
    }

    /**
     * Deletes the file
     *
     * @param name
     */
    public void removeFile(String name) {
        (new File(plugin.getDataFolder(), name)).delete();
    }

    /**
     * @return the configs
     */
    public HashMap<String, Config> getConfigs() {
        return configs;
    }

    /**
     * Checks if the name is a loaded config
     *
     * @param name
     * @return
     */
    public boolean isConfig(String name) {
        return configs.containsKey(name);
    }

    /**
     * Reload All Configs
     */
    public void reloadAll() {
        for (Entry<String, Config> entry : configs.entrySet())
            entry.getValue().reload();
    }

    /**
     * Reload the config by the name(Don't forget the .yml)
     *
     * @param name
     * @return
     */
    public Config reloadConfig(String name) {
        return getConfig(name).reload();
    }

    /**
     * Save the config by the name(Don't forget the .yml)
     *
     * @param name
     * @return
     */
    public Config saveConfig(String name) {
        return getConfig(name).save();
    }

    /**
     * Load up all the files in a path
     * If a file is already loaded, it will just reload the file
     *
     * @param path
     */
    public void loadAllConfigs(String path) {
        for (File file : (new File(plugin.getDataFolder(), path)).listFiles()) {
            if (file.isFile()) {
                String name = path + file.getName();
                if (!this.isConfig(name)) this.getConfig(name);
                else this.getConfig(name).reload();
            }
        }
    }

    /**
     * Load up all the files in the plugins datafolder
     * If a file is already loaded, it will just reload the file
     */
    public void loadAllConfigs() {
        loadAllConfigs("");
    }

    /**
     * Sets the defaults and saves them from the resource file
     *
     * @param file
     */
    public void setDefaultFileFromResources(String file) {
        getConfig(file).copyDefaults(true).save();
    }

    public class Config {

        private String            name;
        private File              file;
        private YamlConfiguration config;
        private Plugin            resourcePlugin;

        public Config(String name) {
            this.name = name;
            this.resourcePlugin = plugin;
        }

        /**
         * Checks if the path exists in the config file
         *
         * @param path
         * @return
         */
        public boolean contains(String path) {
            return get().contains(path);
        }

        /**
         * Copies the config from the resources to the config's default
         * settings.
         * Force = true = Will add any new values from the default file
         * Force = false = Will NOT add new values from the default file
         *
         * @param force
         * @return
         */
        public Config copyDefaults(boolean force) {
            get().options().copyDefaults(force);
            return this;
        }

        /**
         * Checks if the file exists in the directory
         *
         * @return
         */
        public boolean fileExists() {
            return (new File(plugin.getDataFolder(), this.name)).exists();
        }

        /**
         * Deletes the file
         */
        public void removeFile() {
            this.file.delete();
        }

        /**
         * Gets the config as a YamlConfiguration
         *
         * @return
         */
        public YamlConfiguration get() {
            if (this.config == null) reload();

            return this.config;
        }

        /**
         * Returns a value from the config
         *
         * @param key
         * @return
         */
        public Object get(String key) {
            return get().get(key);
        }

        /**
         * Reloads the config
         *
         * @return
         */
        public Config reload() {
            if (file == null) this.file = new File(plugin.getDataFolder(), this.name);

            this.config = YamlConfiguration.loadConfiguration(file);

            Reader defConfigStream;
            try {
                defConfigStream = new InputStreamReader(resourcePlugin.getResource(this.name), "UTF8");

                if (defConfigStream != null) {
                    YamlConfiguration defConfig = YamlConfiguration.loadConfiguration(defConfigStream);
                    this.config.setDefaults(defConfig);
                }
            } catch (UnsupportedEncodingException | NullPointerException e) {

            }
            return this;
        }

        /**
         * Resets to the default config
         *
         * @return
         */
        public Config reset() {
            file = new File(plugin.getDataFolder(), this.name);

            this.resourcePlugin.saveResource(this.name, false);
            reload();
            return this;
        }

        /**
         * Saves the config as long as the config isn't empty
         *
         * @return
         */
        public Config save() {
            if ((this.config == null) || (this.file == null)) return this;
            try {
                if (config.getConfigurationSection("").getKeys(true).size() != 0) config.save(this.file);
            } catch (IOException ex) {
                ex.printStackTrace();
            }
            return this;
        }

        /**
         * Will put everything from the default file into the file(As long as
         * the file doesn't already exist!)
         */
        public Config saveDefaultConfig() {
            if (file == null) {
                file = new File(plugin.getDataFolder(), this.name);
            }
            if (!file.exists() || file.length() == 0) {
                this.resourcePlugin.saveResource(this.name, false);
            }
            return this;
        }

        /**
         * An easy way to set a value into the config
         *
         * @param key
         * @param value
         * @return
         */
        public Config set(String key, Object value) {
            get().set(key, value);
            return this;
        }

        /**
         * Set the plugin instance this config will try and pull the resource
         * from
         *
         * @param plugin
         */
        public Config setResourcePlugin(Plugin plugin) {
            this.resourcePlugin = plugin;
            return this;
        }

        /**
         * Get the name of the config file
         *
         * @return
         */
        public String getName() {
            return this.name;
        }

        /**
         * Method to copy from the resources and save, use this in onEnable
         */
        public void setup() {
            this.copyDefaults(true).save();
        }
    }
}
