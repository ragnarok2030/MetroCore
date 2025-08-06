package net.metroCore.Modules.metroedit.command.region;

import net.metroCore.Core.command.AbstractSubCommand;
import net.metroCore.MetroCore;
import net.metroCore.Modules.metroedit.MetroEditModule;
import net.metroCore.Modules.metroedit.handler.SelectionHandler;
import net.metroCore.Modules.metroedit.handler.UndoRedoHandler;
import net.metroCore.Modules.metroedit.region.CuboidRegion;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class RotateSubCommand extends AbstractSubCommand {
    @Override
    public boolean onExecute(CommandSender sender, Command cmd, String[] args) {
        if (!(sender instanceof Player p)) {
            sender.sendMessage("§cOnly players can use rotate.");
            return true;
        }
        // optional direction arg: left or right
        boolean clockwise = args.length>0 && args[0].equalsIgnoreCase("left")==false;

        MetroEditModule mod = MetroCore.getInstance()
                .getModuleRegistry().get(MetroEditModule.class);
        SelectionHandler sel = mod.getSelectionHandler();
        UndoRedoHandler undo = mod.getUndoRedoHandler();

        sel.getPos1(p).flatMap(a ->
                sel.getPos2(p).map(b -> new CuboidRegion(a, b))
        ).ifPresentOrElse(region -> {
            // compute center X/Z
            int minX=Integer.MAX_VALUE,maxX=Integer.MIN_VALUE,minZ=Integer.MAX_VALUE,maxZ=Integer.MIN_VALUE;
            for (Location loc: region) {
                minX = Math.min(minX, loc.getBlockX());
                maxX = Math.max(maxX, loc.getBlockX());
                minZ = Math.min(minZ, loc.getBlockZ());
                maxZ = Math.max(maxZ, loc.getBlockZ());
            }
            double cx = (minX+maxX)/2.0, cz = (minZ+maxZ)/2.0;
            for (Location loc: region) {
                int x = loc.getBlockX(), z = loc.getBlockZ(), y = loc.getBlockY();
                // translate to origin, rotate, translate back
                double dx = x - cx, dz = z - cz;
                double nx = clockwise ? cz - z + cx : z - cz + cx;
                double nz = clockwise ? x - cx + cz : cx - x + cz;
                Location dest = new Location(loc.getWorld(), nx, y, nz);
                undo.record(p, dest, dest.getBlock().getBlockData(), loc.getBlock().getBlockData());
                dest.getBlock().setBlockData(loc.getBlock().getBlockData());
            }
            p.sendMessage("§aRotated selection " + (clockwise?"clockwise":"counter-clockwise") + ".");
        }, () -> p.sendMessage("§cYou must set both positions first."));

        return true;
    }

    @Override public String getDescription() { return "Rotate the selection 90° around Y"; }
    @Override public String getUsage()       { return "rotate [left|right]"; }
    @Override public String getPermission()  { return "metrocore.metroedit.rotate"; }
}
