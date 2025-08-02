package net.metroCore.Core.command;

import org.bukkit.command.*;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Base for any multi-sub-command command.
 * Automatically registers as both executor and tab-completer.
 */
public abstract class AbstractCommand implements CommandExecutor, TabCompleter {

    protected final Map<String, AbstractSubCommand> subCommands = new LinkedHashMap<>();
    protected String command = "";

    /** Expose the base command label for registration. */
    public String getCommand() {
        return command;
    }

    /**
     * Register a sub-command by name (lowercased).
     */
    public void registerSubCommand(String name, AbstractSubCommand subCommand) {
        subCommands.put(name.toLowerCase(), subCommand);
    }

    //–––––––––– onCommand (execution) ––––––––––
    @Override
    public boolean onCommand(@NotNull CommandSender sender,
                             @NotNull Command cmd,
                             @NotNull String label,
                             @NotNull String[] args) {
        if (args.length == 0) {
            sendHelpMessage(sender);
            return true;
        }

        String sub = args[0].toLowerCase();
        AbstractSubCommand sc = subCommands.get(sub);
        if (sc == null) {
            sendHelpMessage(sender);
            return true;
        }

        // check perms
        if (sc.getPermission() != null && !sender.hasPermission(sc.getPermission())) {
            sender.sendMessage("§cYou lack permission: " + sc.getPermission());
            return true;
        }

        String[] subArgs = Arrays.copyOfRange(args, 1, args.length);
        return sc.onExecute(sender, cmd, subArgs);
    }

    //–––––––––– onTabComplete (delegates to subcommands) ––––––––––
    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender,
                                                @NotNull Command cmd,
                                                @NotNull String label,
                                                @NotNull String[] args) {
        // no args yet: suggest all sub-command names
        if (args.length == 0 || args.length == 1) {
            String prefix = args.length == 0 ? "" : args[0].toLowerCase();
            return subCommands.keySet().stream()
                    .filter(name -> name.startsWith(prefix))
                    .collect(Collectors.toList());
        }

        // args.length >= 2: find matching sub-command and delegate
        String sub = args[0].toLowerCase();
        AbstractSubCommand sc = subCommands.get(sub);
        if (sc == null) return Collections.emptyList();

        String[] subArgs = Arrays.copyOfRange(args, 1, args.length);
        List<String> suggestions = sc.tabComplete(sender, cmd, subArgs);
        // filter by what they’ve already typed:
        String last = subArgs[subArgs.length - 1].toLowerCase();
        return suggestions.stream()
                .filter(s -> s.toLowerCase().startsWith(last))
                .collect(Collectors.toList());
    }

    /** Prints help header + per-sub-command usage. */
    public void sendHelpMessage(CommandSender sender) {
        sender.sendMessage("§6/" + this.command + " <subcommand> <args>…");
        for (Map.Entry<String, AbstractSubCommand> e : subCommands.entrySet()) {
            AbstractSubCommand sc = e.getValue();
            if (sc.getPermission() == null || sender.hasPermission(sc.getPermission())) {
                sender.sendMessage(
                        "§a/" + command + " §2" + e.getKey()
                                + " §a" + sc.getUsage()
                                + " §f– §a" + sc.getDescription()
                );
            }
        }
    }
}
