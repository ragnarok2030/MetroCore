package net.metroCore.Core.command;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

import java.util.Collections;
import java.util.List;

/**
 * A single sub-command in your AbstractCommand.  By default it
 * only handles execution, but you can override `tabComplete` too.
 */
public abstract class AbstractSubCommand {
    /** Execute when someone types: /<base> <sub> ... */
    public abstract boolean onExecute(CommandSender sender, Command command, String[] args);

    /** One–line description for help */
    public abstract String getDescription();
    /** Usage hint */
    public abstract String getUsage();
    /** Permission node (or null) */
    public abstract String getPermission();

    /**
     * Override this to provide your own completions for this subcommand.
     * By default: no further suggestions.
     *
     * @param sender the executor
     * @param args   the args they’ve already typed AFTER the sub-command
     * @return list of completions (unfiltered)
     */
    public List<String> tabComplete(CommandSender sender, Command command, String[] args) {
        return Collections.emptyList();
    }
}
