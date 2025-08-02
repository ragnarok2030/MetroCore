package net.metroCore;

import net.metroCore.Core.command.CommandManager;
import net.metroCore.Core.config.FileRegistry;
import net.metroCore.Core.config.ConfigUtil;
import net.metroCore.Core.module.ModuleRegistry;
import net.metroCore.Core.logging.LoggerUtil;
import net.metroCore.Core.utils.nbt.NbtConstants;
import net.metroCore.Core.utils.nbt.adapter.NbtAdapter;
import net.metroCore.Core.utils.nbt.adapter.v1_21_R5.NbtAdapterImpl;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.ServicePriority;
import org.bukkit.plugin.java.JavaPlugin;

public final class MetroCore extends JavaPlugin {

    private static MetroCore instance;
    private ModuleRegistry moduleRegistry;
    private CommandManager commandManager;

    @Override
    public void onEnable() {
        instance = this;
        LoggerUtil.info("MetroCore is starting...");

        // Initialize dynamic NBT key factory
        NbtConstants.init(this);

        // Register our NbtAdapter implementation as a Bukkit service
        getServer().getServicesManager()
                .register(NbtAdapter.class, new NbtAdapterImpl(), this, ServicePriority.Normal);

        // 1. Setup all config files from FileRegistry
        for (FileRegistry file : FileRegistry.values()) {
            file.setup(this);
        }

        // 2. Load default config values if needed
        loadDefaultConfigs();

        // 3. Initialize core systems
        this.commandManager = new CommandManager(this);
        this.moduleRegistry = new ModuleRegistry(this);

        // 4. Register modules (add your module instances here)
        moduleRegistry.register(new net.metroCore.Modules.guns.GunsModule(this));
        moduleRegistry.register(new net.metroCore.Modules.items.ItemsModule(this));
        // e.g. moduleRegistry.register(new RobberyModule(this));

        // 5. Enable all active modules
        this.moduleRegistry.onEnable();

        LoggerUtil.success("MetroCore is fully enabled.");
    }

    @Override
    public void onDisable() {
        LoggerUtil.warn("Shutting down MetroCore...");
        if (this.moduleRegistry != null) {
            this.moduleRegistry.onDisable();
        }
        LoggerUtil.info("All modules have been safely disabled.");
    }

    private void loadDefaultConfigs() {
        FileConfiguration modulesConfig = FileRegistry.MODULES.get();

        if (!modulesConfig.contains("modules")) {
            modulesConfig.set("modules.ExampleModule", true); // Placeholder
            FileRegistry.MODULES.save();
            LoggerUtil.info("Generated default modules.yml");
        }

        FileConfiguration premiumConfig = FileRegistry.PREMIUM.get();

        if (!premiumConfig.contains("premium.enabled")) {
            premiumConfig.set("premium.enabled", true);
            premiumConfig.set("premium.maxRobberiesFree", 3);
            premiumConfig.set("premium.maxRobberiesPremium", -1); // -1 = unlimited
            FileRegistry.PREMIUM.save();
            LoggerUtil.info("Generated default premium.yml");
        }

        // Add more config defaults here if needed
    }

    public static MetroCore getInstance() {
        return instance;
    }

    public CommandManager getCommandManager() {
        return commandManager;
    }

    public ModuleRegistry getModuleRegistry() {
        return moduleRegistry;
    }
}
