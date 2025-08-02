package net.metroCore.Core.module;

import net.metroCore.Core.config.FileRegistry;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;
import java.util.*;

public class ModuleRegistry {

    private final JavaPlugin plugin;
    private final Map<Class<? extends AbstractModule>, AbstractModule> registry = new HashMap<>();

    public ModuleRegistry(JavaPlugin plugin) {
        this.plugin = plugin;
    }

    public void register(AbstractModule module) {
        registry.put(module.getClass(), module);
    }

    public <T extends AbstractModule> T get(Class<T> clazz) {
        return clazz.cast(registry.get(clazz));
    }

    public AbstractModule getByName(String name) {
        return registry.values().stream()
                .filter(m -> m.getClass().getSimpleName().equalsIgnoreCase(name))
                .findFirst().orElse(null);
    }

    public void onEnable() {
        FileConfiguration config = FileRegistry.MODULES.get();
        for (Map.Entry<Class<? extends AbstractModule>, AbstractModule> entry : registry.entrySet()) {
            String name = entry.getKey().getSimpleName();
            if (config.getBoolean("modules." + name, true)) {
                entry.getValue().onEnable();
                plugin.getLogger().info("§aEnabled module: §f" + name);
            }
        }
    }

    public void onDisable() {
        registry.values().forEach(AbstractModule::onDisable);
    }

    public void reload() {
        registry.values().forEach(AbstractModule::onDisable);
        registry.values().forEach(AbstractModule::onEnable);
    }

    public void enableModule(String name) {
        AbstractModule module = getByName(name);
        if (module != null) {
            module.onEnable();
            FileConfiguration config = FileRegistry.MODULES.get();
            config.set("modules." + name, true);
            FileRegistry.MODULES.save();
        }
    }

    public void disableModule(String name) {
        AbstractModule module = getByName(name);
        if (module != null) {
            module.onDisable();
            FileConfiguration config = FileRegistry.MODULES.get();
            config.set("modules." + name, false);
            FileRegistry.MODULES.save();
        }
    }

    public List<String> getEnabledModuleNames() {
        FileConfiguration config = FileRegistry.MODULES.get();
        List<String> enabled = new ArrayList<>();
        for (Class<? extends AbstractModule> clazz : registry.keySet()) {
            String name = clazz.getSimpleName();
            if (config.getBoolean("modules." + name, true)) {
                enabled.add(name);
            }
        }
        return enabled;
    }
}
