package net.metroCore.Modules.metroedit.command.general;

import net.metroCore.Core.command.AbstractSubCommand;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class WorldSubCommand extends AbstractSubCommand {
    @Override
    public boolean onExecute(CommandSender sender, Command cmd, String[] args) {
        if (!(sender instanceof Player p)) {
            sender.sendMessage("§cOnly players can run this.");
            return true;
        }
        World w = p.getWorld();
        Location l = p.getLocation();
        p.sendMessage(ChatColor.GOLD + "World: §e" + w.getName());
        p.sendMessage(ChatColor.GOLD + "Seed: §e" + w.getSeed());
        p.sendMessage(ChatColor.GOLD + "Coordinates: §e" +
                l.getBlockX()+"," +l.getBlockY()+"," +l.getBlockZ());
        return true;
    }

    @Override public String getDescription() { return "Show world info and coords"; }
    @Override public String getUsage()       { return "world"; }
    @Override public String getPermission()  { return null; }
}
