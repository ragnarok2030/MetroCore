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

public class CutSubCommand extends AbstractSubCommand {
    @Override
    public boolean onExecute(CommandSender sender, Command cmd, String[] args) {
        if (!(sender instanceof Player p)) {
            sender.sendMessage("§cOnly players can cut.");
            return true;
        }

        MetroEditModule mod = MetroCore.getInstance()
                .getModuleRegistry()
                .get(MetroEditModule.class);
        UndoRedoHandler undo = mod.getUndoRedoHandler();

        mod.getSelectionHandler().getPos1(p).flatMap(a ->
                mod.getSelectionHandler().getPos2(p).map(b -> new CuboidRegion(a, b))
        ).ifPresentOrElse(region -> {
            // copy to clipboard
            mod.getClipboardHandler().setClipboard(p, region);

            // build a batch of changes (all to AIR)
            List<BlockChange> batch = new ArrayList<>();
            for (Location loc : region) {
                batch.add(new BlockChange(
                        loc.clone(),
                        loc.getBlock().getBlockData(),
                        Material.AIR.createBlockData()
                ));
            }
            // record & execute in a single undo step
            undo.recordBulk(p, batch);
            batch.forEach(BlockChange::applyRedo);

            p.sendMessage("§aCut selection to clipboard.");
        }, () -> p.sendMessage("§cBoth positions must be set first."));
        return true;
    }

    @Override public String getDescription() { return "Cut your selection to clipboard"; }
    @Override public String getUsage()       { return "cut"; }
    @Override public String getPermission()  { return "metrocore.metroedit.cut"; }
}
