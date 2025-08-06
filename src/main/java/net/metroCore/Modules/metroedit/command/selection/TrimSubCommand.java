package net.metroCore.Modules.metroedit.command.selection;

import net.metroCore.Core.command.AbstractSubCommand;
import net.metroCore.MetroCore;
import net.metroCore.Modules.metroedit.MetroEditModule;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class TrimSubCommand extends AbstractSubCommand {
    @Override
    public boolean onExecute(CommandSender sender, Command cmd, String[] args) {
        if (!(sender instanceof Player p)) {
            sender.sendMessage("§cOnly players can trim.");
            return true;
        }
        MetroCore.getInstance()
                .getModuleRegistry()
                .<MetroEditModule>get(MetroEditModule.class)
                .getSelectionHandler()
                .trim(p);

        p.sendMessage("§aTrimmed selection to minimal bounds.");
        return true;
    }

    @Override public String getDescription() { return "Trim selection to minimal cuboid"; }
    @Override public String getUsage()       { return "trim"; }
    @Override public String getPermission()  { return "metrocore.metroedit.trim"; }
}
