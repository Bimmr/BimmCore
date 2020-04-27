package me.bimmr.bimmcore.misc;

import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;
import org.bukkit.configuration.Configuration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

public class FileManager {
    private final JavaPlugin plugin;

    private HashMap<String, Config> configs = new HashMap<>();

    public FileManager(JavaPlugin plugin) {
        this.plugin = plugin;
    }

    public Config getConfig(String name) {
        if (!this.configs.containsKey(name))
            this.configs.put(name, new Config(name));
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
            ((Config)entry.getValue()).reload();
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

    public class Config {
        private String name;

        private File file;

        private YamlConfiguration config;

        private Plugin resourcePlugin;

        public Config(String name) {
            this.name = name;
            this.resourcePlugin = (Plugin)FileManager.this.plugin;
        }

        public boolean contains(String path) {
            return get().contains(path);
        }

        public Config copyDefaults(boolean force) {
            get().options().copyDefaults(force);
            return this;
        }

        public boolean fileExists() {
            return (new File(FileManager.this.plugin.getDataFolder(), this.name)).exists();
        }

        public void removeFile() {
            this.file.delete();
        }

        public YamlConfiguration get() {
            if (this.config == null)
                reload();
            return this.config;
        }

        public Object get(String key) {
            return get().get(key);
        }

        public Config reload() {
            if (this.file == null)
                this.file = new File(FileManager.this.plugin.getDataFolder(), this.name);
            this.config = YamlConfiguration.loadConfiguration(this.file);
            try {
                Reader defConfigStream = new InputStreamReader(this.resourcePlugin.getResource(this.name), "UTF8");
                if (defConfigStream != null) {
                    YamlConfiguration defConfig = YamlConfiguration.loadConfiguration(defConfigStream);
                    this.config.setDefaults((Configuration)defConfig);
                }
            } catch (UnsupportedEncodingException|NullPointerException unsupportedEncodingException) {}
            return this;
        }

        public Config reset() {
            this.file = new File(FileManager.this.plugin.getDataFolder(), this.name);
            this.resourcePlugin.saveResource(this.name, false);
            reload();
            return this;
        }

        public Config save() {
            if (this.config == null || this.file == null)
                return this;
            try {
                if (this.config.getConfigurationSection("").getKeys(true).size() != 0)
                    this.config.save(this.file);
            } catch (IOException ex) {
                ex.printStackTrace();
            }
            return this;
        }

        public Config saveDefaultConfig() {
            if (this.file == null)
                this.file = new File(FileManager.this.plugin.getDataFolder(), this.name);
            if (!this.file.exists() || this.file.length() == 0L)
                this.resourcePlugin.saveResource(this.name, false);
            return this;
        }

        public Config set(String key, Object value) {
            get().set(key, value);
            return this;
        }

        public Config setResourcePlugin(Plugin plugin) {
            this.resourcePlugin = plugin;
            return this;
        }

        public String getName() {
            return this.name;
        }

        public Config setup() {
            copyDefaults(true).save();
            return this;
        }
    }
}
