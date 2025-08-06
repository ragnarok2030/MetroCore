package net.metroCore.Modules.metroedit.command.general;

import net.metroCore.Core.command.AbstractSubCommand;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class SearchItemSubCommand extends AbstractSubCommand {
    @Override
    public boolean onExecute(CommandSender sender, Command cmd, String[] args) {
        if (args.length == 0) {
            sender.sendMessage("§cUsage: /metroedit searchitem <term>");
            return true;
        }
        String term = args[0].toUpperCase();
        List<String> matches = Arrays.stream(Material.values())
                .map(Material::name)
                .filter(n -> n.contains(term))
                .limit(20)
                .collect(Collectors.toList());
        if (matches.isEmpty()) {
            sender.sendMessage("§cNo items match “" + term + "”");
        } else {
            sender.sendMessage("§aMatches: §f" + String.join(", ", matches));
        }
        return true;
    }

    @Override public String getDescription() { return "Search for item names"; }
    @Override public String getUsage()       { return "searchitem <term>"; }
    @Override public String getPermission()  { return null; }
}
