package net.metroCore.Modules.metroedit.command.general;

import net.metroCore.Core.command.AbstractSubCommand;
import net.metroCore.MetroCore;
import net.metroCore.Modules.metroedit.MetroEditModule;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class ReorderSubCommand extends AbstractSubCommand {
    @Override
    public boolean onExecute(CommandSender sender, Command cmd, String[] args) {
        if (!(sender instanceof Player p)) {
            sender.sendMessage("§cOnly players can reorder selection.");
            return true;
        }
        MetroEditModule mod = MetroCore.getInstance()
                .getModuleRegistry().get(MetroEditModule.class);
        var sel = mod.getSelectionHandler();
        Location a = sel.getPos1(p).orElse(null);
        Location b = sel.getPos2(p).orElse(null);
        if (a == null || b == null) {
            p.sendMessage("§cBoth positions must be set first.");
            return true;
        }
        sel.setPos1(p, min(a, b));
        sel.setPos2(p, max(a, b));
        p.sendMessage("§aSelection corners reordered.");
        return true;
    }
    private Location min(Location a, Location b) {
        return new Location(a.getWorld(),
                Math.min(a.getBlockX(), b.getBlockX()),
                Math.min(a.getBlockY(), b.getBlockY()),
                Math.min(a.getBlockZ(), b.getBlockZ()));
    }
    private Location max(Location a, Location b) {
        return new Location(a.getWorld(),
                Math.max(a.getBlockX(), b.getBlockX()),
                Math.max(a.getBlockY(), b.getBlockY()),
                Math.max(a.getBlockZ(), b.getBlockZ()));
    }

    @Override public String getDescription() { return "Reorder selection corners"; }
    @Override public String getUsage()       { return "reorder"; }
    @Override public String getPermission()  { return "metrocore.metroedit.reorder"; }
}
