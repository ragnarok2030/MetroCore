package net.metroCore.Core.module;

import net.metroCore.Core.command.AbstractCommand;
import net.metroCore.MetroCore;
import org.bukkit.command.PluginCommand;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

/**
 * Base class for all modules.  Provides convenient command registration,
 * wiring both executor and tab-completer.
 */
public abstract class AbstractModule {

    private final JavaPlugin plugin;

    protected AbstractModule(@NotNull JavaPlugin plugin) {
        this.plugin = plugin;
    }

    /** Called once when the module is enabled. */
    public abstract void onEnable();

    /** Called once when the module is disabled. */
    public abstract void onDisable();

    /**
     * Register an AbstractCommand under its own label.
     * Automatically sets both executor and tab-completer.
     */
    protected void registerCommand(AbstractCommand cmd) {
        PluginCommand pc = plugin.getCommand(cmd.getCommand());
        if (pc == null) {
            plugin.getLogger().severe("Command '" + cmd.getCommand() + "' not defined in plugin.yml!");
            return;
        }
        pc.setExecutor(cmd);
        pc.setTabCompleter(cmd);
    }

    protected JavaPlugin getPlugin() {
        return plugin;
    }
}
