// FacesSubCommand.java
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

import java.util.Optional;

public class FacesSubCommand extends AbstractSubCommand {
    @Override
    public boolean onExecute(CommandSender sender, Command cmd, String[] args) {
        if (!(sender instanceof Player p)) {
            sender.sendMessage("§cOnly players can fill faces.");
            return true;
        }
        if (args.length < 2) {
            p.sendMessage("§cUsage: /metroedit faces <north|south|east|west|up|down> <material>");
            return true;
        }
        String face = args[0].toLowerCase();
        Material mat = Material.matchMaterial(args[1]);
        if (mat == null) {
            p.sendMessage("§cUnknown material: " + args[1]);
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
                Optional<Boolean> match = switch (face) {
                    case "north" -> Optional.of(loc.getBlockZ() == region.iterator().next().getBlockZ());
                    case "south" -> Optional.of(loc.getBlockZ() == getMax(region, r -> r.getBlockZ()));
                    case "west"  -> Optional.of(loc.getBlockX() == region.iterator().next().getBlockX());
                    case "east"  -> Optional.of(loc.getBlockX() == getMax(region, r -> r.getBlockX()));
                    case "down"  -> Optional.of(loc.getBlockY() == region.iterator().next().getBlockY());
                    case "up"    -> Optional.of(loc.getBlockY() == getMax(region, r -> r.getBlockY()));
                    default      -> Optional.empty();
                };
                if (match.orElse(false)) {
                    undo.record(p, loc, loc.getBlock().getBlockData(), mat.createBlockData());
                    loc.getBlock().setType(mat);
                }
            }
            p.sendMessage("§aFilled face " + face + " with " + mat.name() + ".");
        }, () -> p.sendMessage("§cBoth positions must be set first."));

        return true;
    }

    private interface Coord { int get(Location l); }

    private int getMax(CuboidRegion region, Coord c) {
        int m = Integer.MIN_VALUE;
        for (Location loc : region) {
            m = Math.max(m, c.get(loc));
        }
        return m;
    }

    @Override public String getDescription() { return "Fill a single face of the selection"; }
    @Override public String getUsage()       { return "faces <face> <material>"; }
    @Override public String getPermission()  { return "metrocore.metroedit.faces"; }
}
