package net.metroCore.Modules.metroedit.command.navigation;

import net.metroCore.Core.command.AbstractSubCommand;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class JumpToSubCommand extends AbstractSubCommand {
    @Override
    public boolean onExecute(CommandSender sender, Command cmd, String[] args) {
        if (!(sender instanceof Player p)) {
            sender.sendMessage("§cOnly players can use /metroedit jumpto.");
            return true;
        }
        if (args.length < 1) {
            p.sendMessage("§cUsage: /metroedit jumpto <y>");
            return true;
        }

        int targetY;
        try {
            targetY = Integer.parseInt(args[0]);
        } catch (NumberFormatException ex) {
            p.sendMessage("§cInvalid number: " + args[0]);
            return true;
        }

        World world = p.getWorld();
        // clamp between 0 and server max build height
        int max = world.getMaxHeight();
        targetY = Math.max(0, Math.min(targetY, max));

        // place glass block at that Y
        Location glassLoc = new Location(
                world,
                p.getLocation().getBlockX(),
                targetY,
                p.getLocation().getBlockZ()
        );
        glassLoc.getBlock().setType(Material.GLASS);

        // teleport player on top of it
        Location tp = glassLoc.clone().add(0, 1, 0);
        p.teleport(tp);
        p.sendMessage("§aTeleported to Y=" + targetY + " with glass block placed beneath you.");

        return true;
    }

    @Override public String getDescription() { return "Teleport to a specific Y-level and place glass under you"; }
    @Override public String getUsage()       { return "jumpto <y>"; }
    @Override public String getPermission()  { return "metrocore.metroedit.jumpto"; }
}
