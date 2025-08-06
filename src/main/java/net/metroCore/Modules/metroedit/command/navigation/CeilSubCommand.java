package net.metroCore.Modules.metroedit.command.navigation;

import net.metroCore.Core.command.AbstractSubCommand;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CeilSubCommand extends AbstractSubCommand {
    @Override
    public boolean onExecute(CommandSender sender, Command cmd, String[] args) {
        if (!(sender instanceof Player p)) {
            sender.sendMessage("§cOnly players can use ceil.");
            return true;
        }
        Location loc = p.getLocation().clone().add(0, 1, 0);
        // climb up through air until you hit a block, then step above it
        while (loc.getBlock().getType().isAir()) {
            loc.add(0, 1, 0);
            if (loc.getY() > p.getWorld().getMaxHeight()) break;
        }
        p.teleport(loc);
        p.sendMessage(ChatColor.GREEN + "Teleported to ceiling above you.");
        return true;
    }

    @Override public String getDescription() { return "Teleport to the first block ceiling above you"; }
    @Override public String getUsage()       { return "ceil"; }
    @Override public String getPermission()  { return "metrocore.metroedit.ceil"; }
}
