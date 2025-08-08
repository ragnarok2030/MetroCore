package net.metroCore.Modules.metroedit.command.region;

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

public class FlipSubCommand extends AbstractSubCommand {
    @Override
    public boolean onExecute(CommandSender sender, Command cmd, String[] args) {
        if (!(sender instanceof Player p)) {
            sender.sendMessage("§cOnly players can use flip.");
            return true;
        }
        if (args.length < 1 || !"xyz".contains(args[0].toLowerCase())) {
            p.sendMessage("§cUsage: /metroedit flip <axis:x|y|z>");
            return true;
        }
        char axis = args[0].toLowerCase().charAt(0);

        MetroEditModule mod = MetroCore.getInstance()
                .getModuleRegistry()
                .get(MetroEditModule.class);
        UndoRedoHandler undo = mod.getUndoRedoHandler();

        mod.getSelectionHandler().getPos1(p).flatMap(a ->
                mod.getSelectionHandler().getPos2(p).map(b -> new CuboidRegion(a, b))
        ).ifPresentOrElse(region -> {
            // determine min/max along axis
            int min = Integer.MAX_VALUE, max = Integer.MIN_VALUE;
            for (Location loc : region) {
                int val = axis == 'x' ? loc.getBlockX() : axis == 'y' ? loc.getBlockY() : loc.getBlockZ();
                min = Math.min(min, val);
                max = Math.max(max, val);
            }
            double pivot = (min + max) / 2.0;

            // collect changes
            List<BlockChange> batch = new ArrayList<>();
            for (Location loc : region) {
                int vx = loc.getBlockX();
                int vy = loc.getBlockY();
                int vz = loc.getBlockZ();
                double nx = axis == 'x' ? (pivot * 2 - vx) : vx;
                double ny = axis == 'y' ? (pivot * 2 - vy) : vy;
                double nz = axis == 'z' ? (pivot * 2 - vz) : vz;
                Location dest = new Location(loc.getWorld(), nx, ny, nz);
                batch.add(new BlockChange(
                        dest.clone(),
                        dest.getBlock().getBlockData(),
                        loc.getBlock().getBlockData()
                ));
            }

            // record and apply all
            undo.recordBulk(p, batch);
            batch.forEach(BlockChange::applyRedo);

            p.sendMessage("§aFlipped selection on axis " + axis + ".");
        }, () -> p.sendMessage("§cYou must set both positions first."));

        return true;
    }

    @Override public String getDescription() { return "Flip the selection across an axis"; }
    @Override public String getUsage()       { return "flip <x|y|z>"; }
    @Override public String getPermission()  { return "metrocore.metroedit.flip"; }
}