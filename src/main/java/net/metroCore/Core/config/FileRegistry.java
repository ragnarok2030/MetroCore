package net.metroCore.Core.config;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;

public enum FileRegistry {
    CONFIG("config.yml"),
    MODULES("modules.yml"),
    PREMIUM("premium.yml");

    private final String fileName;
    private File file;
    private FileConfiguration config;

    FileRegistry(String fileName) {
        this.fileName = fileName;
    }

    public void setup(JavaPlugin plugin) {
        file = new File(plugin.getDataFolder(), fileName);
        if (!file.exists()) {
            plugin.saveResource(fileName, false);
        }
        config = YamlConfiguration.loadConfiguration(file);
    }

    public FileConfiguration get() {
        return config;
    }

    public void save() {
        try {
            if (config != null && file != null) {
                config.save(file);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void reload() {
        if (file != null) {
            config = YamlConfiguration.loadConfiguration(file);
        }
    }
}
