package net.metroCore.Modules.metroedit.command.generation;

import net.metroCore.Core.command.AbstractSubCommand;
import net.metroCore.MetroCore;
import net.metroCore.Modules.metroedit.MetroEditModule;
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

/**
 * Creates a hollow cylinder (tube) from the current selection.
 * Usage: /metroedit hcyl <height> <material>
 */
public class HCylSubCommand extends AbstractSubCommand {
    @Override
    public boolean onExecute(CommandSender sender, Command cmd, String[] args) {
        if (!(sender instanceof Player p)) {
            sender.sendMessage("§cOnly players can use hcyl.");
            return true;
        }
        if (args.length < 2) {
            p.sendMessage("§cUsage: /metroedit hcyl <height> <material>");
            return true;
        }
        int height;
        try {
            height = Integer.parseInt(args[0]);
        } catch (NumberFormatException ex) {
            p.sendMessage("§cInvalid height: " + args[0]);
            return true;
        }
        Material mat = Material.matchMaterial(args[1]);
        if (mat == null) {
            p.sendMessage("§cUnknown material: " + args[1]);
            return true;
        }

        MetroEditModule mod = MetroCore.getInstance()
                .getModuleRegistry().get(MetroEditModule.class);
        CuboidRegion selRegion = mod.getSelectionHandler()
                .getPos1(p).flatMap(a -> mod.getSelectionHandler().getPos2(p)
                        .map(b -> new CuboidRegion(a, b))).orElse(null);
        if (selRegion == null) {
            p.sendMessage("§cBoth positions must be set first.");
            return true;
        }
        List<BlockChange> batch = new ArrayList<>();

        // For each block in the base region, extrude only the perimeter up
        for (Location loc : selRegion) {
            int x = loc.getBlockX(), z = loc.getBlockZ();
            int minX= Integer.MAX_VALUE, maxX=Integer.MIN_VALUE, minZ=Integer.MAX_VALUE, maxZ=Integer.MIN_VALUE;
            // compute bounds of base layer
            for (Location l : selRegion) {
                minX = Math.min(minX, l.getBlockX()); maxX = Math.max(maxX, l.getBlockX());
                minZ = Math.min(minZ, l.getBlockZ()); maxZ = Math.max(maxZ, l.getBlockZ());
            }
            boolean edge = (x==minX || x==maxX || z==minZ || z==maxZ);
            if (!edge) continue;

            for (int dy = 1; dy <= height; dy++) {
                Location target = loc.clone().add(0, dy, 0);
                batch.add(new BlockChange(
                        target,
                        target.getBlock().getBlockData(),
                        mat.createBlockData()
                ));
            }
        }
        UndoRedoHandler undo = mod.getUndoRedoHandler();
        undo.recordBulk(p, batch);
        batch.forEach(BlockChange::applyRedo);

        p.sendMessage("§aHollow cylinder built " + height + " blocks tall with " + mat.name() + ".");
        return true;
    }

    @Override public String getDescription() { return "Create a hollow cylinder (perimeter) upward"; }
    @Override public String getUsage()       { return "hcyl <height> <material>"; }
    @Override public String getPermission()  { return "metrocore.metroedit.hcyl"; }
}
