package net.metroCore.Modules.metroedit.command.region;

import net.metroCore.Core.command.AbstractSubCommand;
import net.metroCore.MetroCore;
import net.metroCore.Modules.metroedit.MetroEditModule;
import net.metroCore.Modules.metroedit.handler.UndoRedoHandler;
import net.metroCore.Modules.metroedit.handler.UndoRedoHandler.BlockChange;
import net.metroCore.Modules.metroedit.handler.SelectionHandler;
import net.metroCore.Modules.metroedit.region.CuboidRegion;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class RotateSubCommand extends AbstractSubCommand {
    @Override
    public boolean onExecute(CommandSender sender, Command cmd, String[] args) {
        if (!(sender instanceof Player p)) {
            sender.sendMessage("§cOnly players can use rotate.");
            return true;
        }
        // optional direction arg: left or right
        boolean clockwise = args.length > 0 && !args[0].equalsIgnoreCase("left");

        MetroEditModule mod = MetroCore.getInstance()
                .getModuleRegistry()
                .get(MetroEditModule.class);
        SelectionHandler sel = mod.getSelectionHandler();
        UndoRedoHandler undo = mod.getUndoRedoHandler();

        sel.getPos1(p).flatMap(a ->
                sel.getPos2(p).map(b -> new CuboidRegion(a, b))
        ).ifPresentOrElse(region -> {
            // compute center X/Z bounds
            int minX = Integer.MAX_VALUE, maxX = Integer.MIN_VALUE;
            int minZ = Integer.MAX_VALUE, maxZ = Integer.MIN_VALUE;
            List<Location> original = new ArrayList<>();
            for (Location loc : region) {
                original.add(loc.clone());
                int x = loc.getBlockX(), z = loc.getBlockZ();
                minX = Math.min(minX, x);
                maxX = Math.max(maxX, x);
                minZ = Math.min(minZ, z);
                maxZ = Math.max(maxZ, z);
            }
            double cx = (minX + maxX) / 2.0;
            double cz = (minZ + maxZ) / 2.0;

            // batch all changes
            List<BlockChange> batch = new ArrayList<>();
            for (Location loc : original) {
                int x = loc.getBlockX(), y = loc.getBlockY(), z = loc.getBlockZ();
                // rotate around center
                double nx = clockwise ? (cz - z + cx) : (z - cz + cx);
                double nz = clockwise ? (x - cx + cz) : (cx - x + cz);
                Location dest = new Location(loc.getWorld(), nx, y, nz);
                batch.add(new BlockChange(
                        dest.clone(),
                        dest.getBlock().getBlockData(),
                        loc.getBlock().getBlockData()
                ));
            }
            // record and apply
            undo.recordBulk(p, batch);
            batch.forEach(BlockChange::applyRedo);

            p.sendMessage("§aRotated selection " + (clockwise ? "clockwise" : "counter-clockwise") + ".");
        }, () -> p.sendMessage("§cYou must set both positions first."));

        return true;
    }

    @Override public String getDescription() { return "Rotate the selection 90° around Y"; }
    @Override public String getUsage()       { return "rotate [left|right]"; }
    @Override public String getPermission()  { return "metrocore.metroedit.rotate"; }
}
