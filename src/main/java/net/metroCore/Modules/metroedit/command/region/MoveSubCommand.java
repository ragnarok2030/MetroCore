package net.metroCore.Modules.metroedit.command.region;

import net.metroCore.Core.command.AbstractSubCommand;
import net.metroCore.MetroCore;
import net.metroCore.Modules.metroedit.MetroEditModule;
import net.metroCore.Modules.metroedit.handler.UndoRedoHandler;
import net.metroCore.Modules.metroedit.handler.UndoRedoHandler.BlockChange;
import net.metroCore.Modules.metroedit.region.CuboidRegion;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class MoveSubCommand extends AbstractSubCommand {
    @Override
    public boolean onExecute(CommandSender sender, Command cmd, String[] args) {
        if (!(sender instanceof Player p)) {
            sender.sendMessage("§cOnly players can use move.");
            return true;
        }
        if (args.length < 3) {
            p.sendMessage("§cUsage: /metroedit move <dx> <dy> <dz>");
            return true;
        }
        int dx, dy, dz;
        try {
            dx = Integer.parseInt(args[0]);
            dy = Integer.parseInt(args[1]);
            dz = Integer.parseInt(args[2]);
        } catch (NumberFormatException e) {
            p.sendMessage("§cAll offsets must be integers.");
            return true;
        }

        MetroEditModule mod = MetroCore.getInstance()
                .getModuleRegistry()
                .get(MetroEditModule.class);
        UndoRedoHandler undo = mod.getUndoRedoHandler();

        mod.getSelectionHandler().getPos1(p).flatMap(a ->
                mod.getSelectionHandler().getPos2(p).map(b -> new CuboidRegion(a, b))
        ).ifPresentOrElse(region -> {
            List<BlockChange> batch = new ArrayList<>();
            // copy blocks to new positions
            for (Location loc : region) {
                Location dest = loc.clone().add(dx, dy, dz);
                batch.add(new BlockChange(
                        dest.clone(),
                        dest.getBlock().getBlockData(),
                        loc.getBlock().getBlockData()
                ));
            }
            // clear original positions
            for (Location loc : region) {
                batch.add(new BlockChange(
                        loc.clone(),
                        loc.getBlock().getBlockData(),
                        org.bukkit.Material.AIR.createBlockData()
                ));
            }
            // record and apply all changes
            undo.recordBulk(p, batch);
            batch.forEach(BlockChange::applyRedo);

            p.sendMessage("§aMoved selection by (" + dx + "," + dy + "," + dz + ").");
        }, () -> p.sendMessage("§cYou must set both positions first."));

        return true;
    }

    @Override public String getDescription() { return "Move the selection by an offset"; }
    @Override public String getUsage()       { return "move <dx> <dy> <dz>"; }
    @Override public String getPermission()  { return "metrocore.metroedit.move"; }
}