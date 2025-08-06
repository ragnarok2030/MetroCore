package net.metroCore.Modules.metroedit.command.navigation;

import net.metroCore.Core.command.AbstractSubCommand;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class UnstuckSubCommand extends AbstractSubCommand {
    @Override
    public boolean onExecute(CommandSender sender, Command cmd, String[] args) {
        if (!(sender instanceof Player p)) {
            sender.sendMessage("§cOnly players can unstuck themselves.");
            return true;
        }
        Location loc = p.getLocation();
        // nudge up until not in solid
        while (loc.getBlock().getType().isSolid()) {
            loc = loc.add(0, 1, 0);
            if (loc.getY() > p.getWorld().getMaxHeight()) break;
        }
        p.teleport(loc);
        p.sendMessage(ChatColor.GREEN + "You have been unstuck.");
        return true;
    }

    @Override public String getDescription() { return "Unstuck you if you're trapped in blocks"; }
    @Override public String getUsage()       { return "unstuck"; }
    @Override public String getPermission()  { return "metrocore.metroedit.unstuck"; }
}
