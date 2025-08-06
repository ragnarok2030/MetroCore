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

import java.util.ArrayList;
import java.util.List;

public class StackSubCommand extends AbstractSubCommand {
    @Override
    public boolean onExecute(CommandSender sender, Command cmd, String[] args) {
        if (!(sender instanceof Player p)) {
            sender.sendMessage("§cOnly players can use stack.");
            return true;
        }

        // Parse number of repetitions once and make it effectively final
        int parsedTimes = 1;
        if (args.length >= 1) {
            try {
                parsedTimes = Integer.parseInt(args[0]);
            } catch (NumberFormatException ignored) {}
        }
        final int times = parsedTimes;

        MetroEditModule mod = MetroCore.getInstance()
                .getModuleRegistry()
                .get(MetroEditModule.class);

        SelectionHandler sel = mod.getSelectionHandler();
        UndoRedoHandler undo = mod.getUndoRedoHandler();

        sel.getPos1(p).flatMap(a ->
                sel.getPos2(p).map(b -> new CuboidRegion(a, b))
        ).ifPresentOrElse(region -> {
            // Copy region blocks into a final list
            List<Location> blocks = new ArrayList<>();
            for (Location loc : region) {
                blocks.add(loc.clone());
            }
            final List<Location> finalBlocks = blocks;

            // Compute vertical span
            int minY = Integer.MAX_VALUE, maxY = Integer.MIN_VALUE;
            for (Location loc : finalBlocks) {
                int y = loc.getBlockY();
                if (y < minY) minY = y;
                if (y > maxY) maxY = y;
            }
            final int span = maxY - minY + 1;

            // Perform the stacking loop
            for (int t = 1; t <= times; t++) {
                for (Location orig : finalBlocks) {
                    Location target = orig.clone().add(0, span * t, 0);
                    // record for undo
                    undo.record(p,
                            target,
                            target.getBlock().getBlockData(),
                            orig.getBlock().getBlockData()
                    );
                    target.getBlock().setBlockData(orig.getBlock().getBlockData());
                }
            }

            p.sendMessage("§aStacked selection " + times + " time(s).");
        }, () -> {
            p.sendMessage("§cYou must set both positions first.");
        });

        return true;
    }

    @Override public String getDescription() { return "Stack the selection upward"; }
    @Override public String getUsage()       { return "stack [times]"; }
    @Override public String getPermission()  { return "metrocore.metroedit.stack"; }
}
