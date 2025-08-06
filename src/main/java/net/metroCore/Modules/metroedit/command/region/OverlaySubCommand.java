// OverlaySubCommand.java
package net.metroCore.Modules.metroedit.command.region;

import net.metroCore.Core.command.AbstractSubCommand;
import net.metroCore.MetroCore;
import net.metroCore.Modules.metroedit.MetroEditModule;
import net.metroCore.Modules.metroedit.handler.SelectionHandler;
import net.metroCore.Modules.metroedit.handler.UndoRedoHandler;
import net.metroCore.Modules.metroedit.region.CuboidRegion;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class OverlaySubCommand extends AbstractSubCommand {
    @Override
    public boolean onExecute(CommandSender sender, Command cmd, String[] args) {
        if (!(sender instanceof Player p)) {
            sender.sendMessage("§cOnly players can overlay selections.");
            return true;
        }
        if (args.length < 1) {
            p.sendMessage("§cUsage: /metroedit overlay <material>");
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
            for (Location loc : region) {
                // only replace air blocks
                if (loc.getBlock().isPassable()) {
                    undo.record(p, loc, loc.getBlock().getBlockData(), mat.createBlockData());
                    loc.getBlock().setType(mat);
                }
            }
            p.sendMessage("§aOverlayed " + mat.name() + " over air.");
        }, () -> p.sendMessage("§cBoth positions must be set first."));

        return true;
    }

    @Override public String getDescription() { return "Overlay material over air in the selection"; }
    @Override public String getUsage()       { return "overlay <material>"; }
    @Override public String getPermission()  { return "metrocore.metroedit.overlay"; }
}
