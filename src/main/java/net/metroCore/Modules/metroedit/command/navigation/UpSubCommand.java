package net.metroCore.Modules.metroedit.command.navigation;

import net.metroCore.Core.command.AbstractSubCommand;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class UpSubCommand extends AbstractSubCommand {
    @Override
    public boolean onExecute(CommandSender sender, Command cmd, String[] args) {
        if (!(sender instanceof Player p)) {
            sender.sendMessage("§cOnly players can use /metroedit up.");
            return true;
        }

        int amount = 1;
        if (args.length >= 1) {
            try {
                amount = Integer.parseInt(args[0]);
                if (amount < 1) throw new NumberFormatException();
            } catch (NumberFormatException ex) {
                p.sendMessage("§cInvalid height: " + args[0]);
                return true;
            }
        }

        Location from = p.getLocation();
        World world  = from.getWorld();
        int targetY  = from.getBlockY() + amount;

        // place a glass block at the new position
        Location glassLoc = new Location(
                world,
                from.getBlockX(),
                targetY,
                from.getBlockZ()
        );
        glassLoc.getBlock().setType(Material.GLASS);

        // teleport player atop that block
        glassLoc.add(0, 1, 0);
        p.teleport(glassLoc);
        p.sendMessage("§aYou’ve gone up §f" + amount + "§a blocks to Y=" + targetY + ".");

        return true;
    }

    @Override public String getDescription() { return "Go up and place a glass block under you"; }
    @Override public String getUsage()       { return "up [height]"; }
    @Override public String getPermission()  { return "metrocore.metroedit.up"; }
}
