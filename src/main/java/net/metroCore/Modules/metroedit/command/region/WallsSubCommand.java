package net.metroCore.Modules.metroedit.command.region;

import net.metroCore.Core.command.AbstractSubCommand;
import net.metroCore.MetroCore;
import net.metroCore.Modules.metroedit.MetroEditModule;
import net.metroCore.Modules.metroedit.handler.SelectionHandler;
import net.metroCore.Modules.metroedit.handler.UndoRedoHandler;
import net.metroCore.Modules.metroedit.handler.UndoRedoHandler.BlockChange;
import net.metroCore.Modules.metroedit.region.CuboidRegion;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class WallsSubCommand extends AbstractSubCommand {
    @Override
    public boolean onExecute(CommandSender sender, Command cmd, String[] args) {
        if (!(sender instanceof Player p)) {
            sender.sendMessage("§cOnly players can build walls.");
            return true;
        }
        if (args.length < 1) {
            p.sendMessage("§cUsage: /metroedit walls <material>");
            return true;
        }
        Material mat = Material.matchMaterial(args[0]);
        if (mat == null) {
            p.sendMessage("§cUnknown material: " + args[0]);
            return true;
        }

        MetroEditModule mod = MetroCore.getInstance()
                .getModuleRegistry()
                .get(MetroEditModule.class);
        SelectionHandler sel = mod.getSelectionHandler();
        UndoRedoHandler undo = mod.getUndoRedoHandler();

        sel.getPos1(p).flatMap(a ->
                sel.getPos2(p).map(b -> new CuboidRegion(a, b))
        ).ifPresentOrElse(region -> {
            int minX = Integer.MAX_VALUE, maxX = Integer.MIN_VALUE;
            int minY = Integer.MAX_VALUE, maxY = Integer.MIN_VALUE;
            int minZ = Integer.MAX_VALUE, maxZ = Integer.MIN_VALUE;
            for (Location loc : region) {
                int x = loc.getBlockX(), y = loc.getBlockY(), z = loc.getBlockZ();
                minX = Math.min(minX, x); maxX = Math.max(maxX, x);
                minY = Math.min(minY, y); maxY = Math.max(maxY, y);
                minZ = Math.min(minZ, z); maxZ = Math.max(maxZ, z);
            }

            // Prepare batch
            List<BlockChange> batch = new ArrayList<>();
            for (Location loc : region) {
                int x = loc.getBlockX(), z = loc.getBlockZ();
                boolean edgeX = (x == minX || x == maxX);
                boolean edgeZ = (z == minZ || z == maxZ);
                if (edgeX || edgeZ) {
                    batch.add(new BlockChange(
                            loc.clone(),
                            loc.getBlock().getBlockData(),
                            mat.createBlockData()
                    ));
                }
            }

            // Record and apply
            undo.recordBulk(p, batch);
            batch.forEach(BlockChange::applyRedo);

            p.sendMessage("§aBuilt walls around selection with " + mat.name() + ".");
        }, () -> p.sendMessage("§cBoth positions must be set first."));

        return true;
    }

    @Override public String getDescription() { return "Build walls around the selection"; }
    @Override public String getUsage()       { return "walls <material>"; }
    @Override public String getPermission()  { return "metrocore.metroedit.walls"; }
}
