package net.metroCore.Modules.metroedit.command.region;

import net.metroCore.Core.command.AbstractSubCommand;
import net.metroCore.MetroCore;
import net.metroCore.Modules.metroedit.MetroEditModule;
import net.metroCore.Modules.metroedit.handler.ClipboardHandler;
import net.metroCore.Modules.metroedit.handler.UndoRedoHandler;
import net.metroCore.Modules.metroedit.handler.ClipboardHandler.Clipboard;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class PasteSubCommand extends AbstractSubCommand {
    @Override
    public boolean onExecute(CommandSender sender, Command cmd, String[] args) {
        if (!(sender instanceof Player p)) {
            sender.sendMessage("§cOnly players can paste.");
            return true;
        }

        MetroEditModule mod = MetroCore.getInstance()
                .getModuleRegistry()
                .get(MetroEditModule.class);

        ClipboardHandler clipHandler = mod.getClipboardHandler();
        ClipboardHandler.Clipboard cb = clipHandler.getClipboard(p).orElse(null);
        if (cb == null || cb.isEmpty()) {
            p.sendMessage("§cClipboard is empty.");
            return true;
        }

        // Prepare batch of changes for undo
        List<UndoRedoHandler.BlockChange> batch = new ArrayList<>();
        Location dest = p.getLocation().getBlock().getLocation();
        for (Map.Entry<Location, org.bukkit.block.data.BlockData> e : cb.getEntries()) {
            Location from = e.getKey();
            int dx = from.getBlockX() - cb.getOrigin().getBlockX();
            int dy = from.getBlockY() - cb.getOrigin().getBlockY();
            int dz = from.getBlockZ() - cb.getOrigin().getBlockZ();
            Location to = dest.clone().add(dx, dy, dz);
            batch.add(new UndoRedoHandler.BlockChange(
                    to.clone(),
                    to.getBlock().getBlockData(),
                    e.getValue()
            ));
        }

        // Record and apply
        UndoRedoHandler undo = mod.getUndoRedoHandler();
        undo.recordBulk(p, batch);
        batch.forEach(UndoRedoHandler.BlockChange::applyRedo);

        p.sendMessage("§aPasted " + batch.size() + " blocks at " +
                dest.getBlockX() + "," + dest.getBlockY() + "," + dest.getBlockZ());
        return true;
    }

    @Override public String getDescription() { return "Paste your clipboard at your location with undo support"; }
    @Override public String getUsage()       { return "paste"; }
    @Override public String getPermission()  { return "metrocore.metroedit.paste"; }
}