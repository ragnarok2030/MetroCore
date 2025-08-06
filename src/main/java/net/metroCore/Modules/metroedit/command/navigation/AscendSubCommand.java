package net.metroCore.Modules.metroedit.command.navigation;

import net.metroCore.Core.command.AbstractSubCommand;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class AscendSubCommand extends AbstractSubCommand {
    @Override
    public boolean onExecute(CommandSender sender, Command cmd, String[] args) {
        if (!(sender instanceof Player p)) {
            sender.sendMessage("§cOnly players can ascend.");
            return true;
        }
        Location loc = p.getLocation();
        loc.setY(p.getWorld().getHighestBlockYAt(loc));
        p.teleport(loc);
        p.sendMessage(ChatColor.GREEN + "Teleported to highest block above you.");
        return true;
    }

    @Override public String getDescription() { return "Teleport to the top of the column you're in"; }
    @Override public String getUsage()       { return "ascend"; }
    @Override public String getPermission()  { return "metrocore.metroedit.ascend"; }
}
