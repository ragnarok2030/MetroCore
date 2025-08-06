package net.metroCore.Modules.metroedit.command.selection;

import net.metroCore.Core.command.AbstractSubCommand;
import net.metroCore.MetroCore;
import net.metroCore.Modules.metroedit.MetroEditModule;
import net.metroCore.Modules.metroedit.region.CuboidRegion;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class ChunkSubCommand extends AbstractSubCommand {
    @Override
    public boolean onExecute(CommandSender sender, Command cmd, String[] args) {
        if (!(sender instanceof Player p)) {
            sender.sendMessage("§cOnly players can select chunks.");
            return true;
        }

        var chunk = p.getLocation().getChunk();
        Location min = chunk.getBlock(0, 0, 0).getLocation();
        Location max = chunk.getBlock(15, p.getWorld().getMaxHeight()-1, 15).getLocation();

        var handler = MetroCore.getInstance()
                .getModuleRegistry()
                .<MetroEditModule>get(MetroEditModule.class)
                .getSelectionHandler();

        handler.setPos1(p, min);
        handler.setPos2(p, max);

        p.sendMessage("§aChunk selected from " +
                format(min) + " to " + format(max));
        return true;
    }

    private String format(Location l) {
        return "(" + l.getBlockX() + "," + l.getBlockY() + "," + l.getBlockZ() + ")";
    }

    @Override public String getDescription() { return "Select entire chunk you stand in"; }
    @Override public String getUsage()       { return "chunk"; }
    @Override public String getPermission()  { return "metrocore.metroedit.chunk"; }
}
