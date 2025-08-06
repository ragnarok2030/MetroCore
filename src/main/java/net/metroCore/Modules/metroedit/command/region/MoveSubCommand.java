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
                .getModuleRegistry().get(MetroEditModule.class);
        SelectionHandler sel = mod.getSelectionHandler();
        UndoRedoHandler undo = mod.getUndoRedoHandler();

        sel.getPos1(p).flatMap(a ->
                sel.getPos2(p).map(b -> new CuboidRegion(a, b))
        ).ifPresentOrElse(region -> {
            // First copy to new locations
            for (Location loc : region) {
                Location dest = loc.clone().add(dx, dy, dz);
                undo.record(p, dest, dest.getBlock().getBlockData(), loc.getBlock().getBlockData());
                dest.getBlock().setBlockData(loc.getBlock().getBlockData());
            }
            // Then clear original
            for (Location loc : region) {
                undo.record(p, loc, loc.getBlock().getBlockData(), org.bukkit.Material.AIR.createBlockData());
                loc.getBlock().setType(org.bukkit.Material.AIR);
            }
            p.sendMessage("§aMoved selection by (" + dx + "," + dy + "," + dz + ").");
        }, () -> p.sendMessage("§cYou must set both positions first."));

        return true;
    }

    @Override public String getDescription() { return "Move the selection by an offset"; }
    @Override public String getUsage()       { return "move <dx> <dy> <dz>"; }
    @Override public String getPermission()  { return "metrocore.metroedit.move"; }
}
