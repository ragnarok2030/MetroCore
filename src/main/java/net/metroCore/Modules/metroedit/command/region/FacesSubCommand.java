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
import java.util.Optional;
import java.util.function.ToIntFunction;

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
        UndoRedoHandler undo = mod.getUndoRedoHandler();

        mod.getSelectionHandler().getPos1(p).flatMap(a ->
                mod.getSelectionHandler().getPos2(p).map(b -> new CuboidRegion(a, b))
        ).ifPresentOrElse(region -> {
            // determine coordinate extractor and target value
            ToIntFunction<Location> coordFunc;
            switch (face) {
                case "north": coordFunc = Location::getBlockZ; break;
                case "south": coordFunc = Location::getBlockZ; break;
                case "west":  coordFunc = Location::getBlockX; break;
                case "east":  coordFunc = Location::getBlockX; break;
                case "down":  coordFunc = Location::getBlockY; break;
                case "up":    coordFunc = Location::getBlockY; break;
                default:
                    p.sendMessage("§cInvalid face: " + face);
                    return;
            }
            // compute target coordinate
            int targetVal;
            if (face.equals("north") || face.equals("west") || face.equals("down")) {
                targetVal = coordFunc.applyAsInt(region.iterator().next());
            } else {
                targetVal = getMax(region, coordFunc);
            }

            // collect changes
            List<BlockChange> batch = new ArrayList<>();
            for (Location loc : region) {
                if (coordFunc.applyAsInt(loc) == targetVal) {
                    batch.add(new BlockChange(
                            loc.clone(),
                            loc.getBlock().getBlockData(),
                            mat.createBlockData()
                    ));
                }
            }

            // record and apply in one undo step
            undo.recordBulk(p, batch);
            batch.forEach(BlockChange::applyRedo);
            p.sendMessage("§aFilled face " + face + " with " + mat.name() + ".");
        }, () -> p.sendMessage("§cBoth positions must be set first."));

        return true;
    }

    @Override public String getDescription() { return "Fill a single face of the selection"; }
    @Override public String getUsage()       { return "faces <face> <material>"; }
    @Override public String getPermission()  { return "metrocore.metroedit.faces"; }

    private int getMax(CuboidRegion region, ToIntFunction<Location> func) {
        int m = Integer.MIN_VALUE;
        for (Location loc : region) {
            m = Math.max(m, func.applyAsInt(loc));
        }
        return m;
    }
}
