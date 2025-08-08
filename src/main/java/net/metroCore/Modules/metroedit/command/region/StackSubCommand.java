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

        // parse repetition count
        int parsed = 1;
        if (args.length >= 1) {
            try {
                parsed = Integer.parseInt(args[0]);
            } catch (NumberFormatException ignored) {}
        }
        final int times = parsed;

        MetroEditModule mod = MetroCore.getInstance()
                .getModuleRegistry()
                .get(MetroEditModule.class);
        SelectionHandler sel = mod.getSelectionHandler();
        UndoRedoHandler undo = mod.getUndoRedoHandler();

        sel.getPos1(p).flatMap(a ->
                sel.getPos2(p).map(b -> new CuboidRegion(a, b))
        ).ifPresentOrElse(region -> {
            // capture original blocks
            List<Location> originals = new ArrayList<>();
            for (Location loc : region) originals.add(loc.clone());

            // compute vertical span
            int minY = Integer.MAX_VALUE, maxY = Integer.MIN_VALUE;
            for (Location loc : originals) {
                int y = loc.getBlockY();
                minY = Math.min(minY, y);
                maxY = Math.max(maxY, y);
            }
            int span = maxY - minY + 1;

            // build batch of changes
            List<BlockChange> batch = new ArrayList<>();
            for (int t = 1; t <= times; t++) {
                for (Location loc : originals) {
                    Location dest = loc.clone().add(0, span * t, 0);
                    batch.add(new BlockChange(
                            dest.clone(),
                            dest.getBlock().getBlockData(),
                            loc.getBlock().getBlockData()
                    ));
                }
            }

            // record and apply all at once
            undo.recordBulk(p, batch);
            batch.forEach(BlockChange::applyRedo);

            p.sendMessage("§aStacked selection " + times + " time(s).");
        }, () -> p.sendMessage("§cYou must set both positions first."));

        return true;
    }

    @Override public String getDescription() { return "Stack the selection upward"; }
    @Override public String getUsage()       { return "stack [times]"; }
    @Override public String getPermission()  { return "metrocore.metroedit.stack"; }
}
