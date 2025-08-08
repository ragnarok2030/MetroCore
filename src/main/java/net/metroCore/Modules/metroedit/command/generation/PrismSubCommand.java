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
 * Extrudes the current selection vertically to form a prism.
 * Usage: /metroedit prism <height> <material>
 */
public class PrismSubCommand extends AbstractSubCommand {
    @Override
    public boolean onExecute(CommandSender sender, Command cmd, String[] args) {
        if (!(sender instanceof Player p)) {
            sender.sendMessage("§cOnly players can use prism.");
            return true;
        }
        if (args.length < 2) {
            p.sendMessage("§cUsage: /metroedit prism <height> <material>");
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
        CuboidRegion region = mod.getSelectionHandler()
                .getPos1(p).flatMap(a -> mod.getSelectionHandler().getPos2(p)
                        .map(b -> new CuboidRegion(a, b))).orElse(null);
        if (region == null) {
            p.sendMessage("§cBoth positions must be set first.");
            return true;
        }
        List<BlockChange> batch = new ArrayList<>();

        // Generate layers upward
        for (Location loc : region) {
            for (int y = 1; y <= height; y++) {
                Location target = loc.clone().add(0, y, 0);
                batch.add(new BlockChange(
                        target,
                        target.getBlock().getBlockData(),
                        mat.createBlockData()
                ));
            }
        }
        UndoRedoHandler undo = mod.getUndoRedoHandler();
        // record and apply
        undo.recordBulk(p, batch);
        batch.forEach(BlockChange::applyRedo);

        p.sendMessage("§aPrism extruded " + height + " blocks tall with " + mat.name() + ".");
        return true;
    }

    @Override public String getDescription() { return "Extrude selection into a prism"; }
    @Override public String getUsage()       { return "prism <height> <material>"; }
    @Override public String getPermission()  { return "metrocore.metroedit.prism"; }
}