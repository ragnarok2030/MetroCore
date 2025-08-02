package net.metroCore.Core.command;

import org.bukkit.command.CommandExecutor;
import org.bukkit.command.PluginCommand;
import org.bukkit.command.TabCompleter;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashMap;
import java.util.Map;

/**
 * Central registry for commands.  On register, wires both executor
 * and, if available, tab-completer.
 */
public class CommandManager {

    private final JavaPlugin plugin;
    private final Map<String, CommandExecutor> commands = new HashMap<>();

    public CommandManager(JavaPlugin plugin) {
        this.plugin = plugin;
    }

    /**
     * Register a command by its label.
     * @param label    the literal command (must be in plugin.yml)
     * @param executor the executor (often an AbstractCommand)
     */
    public void register(String label, CommandExecutor executor) {
        PluginCommand pc = plugin.getCommand(label);
        if (pc == null) {
            plugin.getLogger().severe("Command '" + label + "' not defined in plugin.yml!");
            return;
        }
        pc.setExecutor(executor);
        if (executor instanceof TabCompleter tc) {
            pc.setTabCompleter(tc);
        }
        commands.put(label.toLowerCase(), executor);
    }

    /**
     * Retrieve a previously-registered executor by label.
     */
    public CommandExecutor getExecutor(String label) {
        return commands.get(label.toLowerCase());
    }
}
