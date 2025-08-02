package net.metroCore.Core.config;

import org.bukkit.configuration.file.FileConfiguration;

public class ConfigUtil {

    private final FileConfiguration config;

    public ConfigUtil(FileConfiguration config) {
        this.config = config;
    }

    public String getString(String path) {
        return config.getString(path, "");
    }

    public boolean getBoolean(String path) {
        return config.getBoolean(path, false);
    }

    public int getInt(String path) {
        return config.getInt(path, 0);
    }

    public double getDouble(String path) {
        return config.getDouble(path, 0.0);
    }

    public void set(String path, Object value) {
        config.set(path, value);
    }

    public boolean contains(String path) {
        return config.contains(path);
    }
}
