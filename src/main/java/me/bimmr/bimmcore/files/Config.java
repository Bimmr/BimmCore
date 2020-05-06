package me.bimmr.bimmcore.files;

import org.bukkit.configuration.Configuration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;

import java.io.*;
import java.util.ArrayList;

public class Config {
    private FileManager fileManager;
    private String name;

    private File file;

    private YamlConfiguration config;

    private Plugin resourcePlugin;

    public Config(FileManager fileManager, String name) {
        this.fileManager = fileManager;
        this.name = name;
        this.resourcePlugin = fileManager.getPlugin();
    }

    public boolean contains(String path) {
        return get().contains(path);
    }

    public Config copyDefaults(boolean force) {
        get().options().copyDefaults(force);
        return this;
    }

    public boolean fileExists() {
        return (new File(fileManager.getPlugin().getDataFolder(), this.name)).exists();
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
            this.file = new File(fileManager.getPlugin().getDataFolder(), this.name);
        this.config = YamlConfiguration.loadConfiguration(this.file);
        try {
            Reader defConfigStream = new InputStreamReader(this.resourcePlugin.getResource(this.name), "UTF8");
            if (defConfigStream != null) {
                YamlConfiguration defConfig = YamlConfiguration.loadConfiguration(defConfigStream);
                this.config.setDefaults((Configuration) defConfig);
            }
        } catch (UnsupportedEncodingException | NullPointerException unsupportedEncodingException) {
        }
        return this;
    }

    public Config reset() {
        this.file = new File(fileManager.getPlugin().getDataFolder(), this.name);
        this.resourcePlugin.saveResource(this.name, false);
        reload();
        return this;
    }

    public Config saveResource(boolean replace) {
        this.resourcePlugin.saveResource(this.name, replace);
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
            this.file = new File(fileManager.getPlugin().getDataFolder(), this.name);
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

    public ArrayList<String> getKeys(String path) {
        ArrayList<String> list = new ArrayList<>();
        for (String name : get().getConfigurationSection(path).getKeys(true))
            if (!name.contains("."))
                list.add(name);
        return list;
    }
}
