package net.metroCore.Modules.metroedit.command.general;

import net.metroCore.Core.command.AbstractSubCommand;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class PlacementSubCommand extends AbstractSubCommand {
    @Override
    public boolean onExecute(CommandSender sender, Command cmd, String[] args) {
        if (!(sender instanceof Player p)) {
            sender.sendMessage("§cOnly players can run this.");
            return true;
        }
        Location l = p.getLocation();
        p.sendMessage(ChatColor.GREEN + "Placement point: " +
                ChatColor.WHITE + l.getBlockX()+"," +l.getBlockY()+"," +l.getBlockZ());
        return true;
    }

    @Override public String getDescription() { return "Show current placement coordinates"; }
    @Override public String getUsage()       { return "placement"; }
    @Override public String getPermission()  { return null; }
}
