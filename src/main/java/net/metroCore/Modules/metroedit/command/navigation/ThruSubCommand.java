package net.metroCore.Modules.metroedit.command.navigation;

import net.metroCore.Core.command.AbstractSubCommand;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

public class ThruSubCommand extends AbstractSubCommand {
    private static final double MAX_DISTANCE = 50.0;
    private static final double STEP = 0.5;

    @Override
    public boolean onExecute(CommandSender sender, Command cmd, String[] args) {
        if (!(sender instanceof Player p)) {
            sender.sendMessage("§cOnly players can use thru.");
            return true;
        }

        Location eye = p.getEyeLocation();
        Vector dir = eye.getDirection().normalize();

        // Step forward in small increments until we hit a non-passable block
        for (double d = 0; d <= MAX_DISTANCE; d += STEP) {
            Location probe = eye.clone().add(dir.clone().multiply(d));
            Block block = probe.getBlock();
            if (!block.isPassable()) {
                // Found the wall—now teleport just on the far side of it
                Location target = block.getLocation()
                        .add(0.5, 1.0, 0.5)       // center of block + one up
                        .add(dir.multiply(0.5));   // half a block forward
                p.teleport(target);
                p.sendMessage(ChatColor.GREEN + "Phased through the wall!");
                return true;
            }
        }

        p.sendMessage(ChatColor.RED + "No wall found within " + (int)MAX_DISTANCE + " blocks.");
        return true;
    }

    @Override public String getDescription() { return "Phase through the nearest wall in front of you"; }
    @Override public String getUsage()       { return "thru"; }
    @Override public String getPermission()  { return "metrocore.metroedit.thru"; }
}
