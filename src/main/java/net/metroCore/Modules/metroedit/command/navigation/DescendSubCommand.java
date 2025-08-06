package net.metroCore.Modules.metroedit.command.navigation;

import net.metroCore.Core.command.AbstractSubCommand;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class DescendSubCommand extends AbstractSubCommand {
    @Override
    public boolean onExecute(CommandSender sender, Command cmd, String[] args) {
        if (!(sender instanceof Player p)) {
            sender.sendMessage("§cOnly players can descend.");
            return true;
        }
        Location loc = p.getLocation();
        // move down until you hit a solid block
        while (!loc.getBlock().getType().isSolid()) {
            loc = loc.subtract(0, 1, 0);
            if (loc.getY() < 0) break;
        }
        p.teleport(loc.add(0, 1, 0));
        p.sendMessage(ChatColor.GREEN + "Teleported to ground below you.");
        return true;
    }

    @Override public String getDescription() { return "Teleport to the ground below you"; }
    @Override public String getUsage()       { return "descend"; }
    @Override public String getPermission()  { return "metrocore.metroedit.descend"; }
}
