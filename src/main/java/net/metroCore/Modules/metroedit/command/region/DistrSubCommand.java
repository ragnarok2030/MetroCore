// DistrSubCommand.java
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

import java.util.List;
import java.util.Random;

public class DistrSubCommand extends AbstractSubCommand {
    private final Random rnd = new Random();

    @Override
    public boolean onExecute(CommandSender sender, Command cmd, String[] args) {
        if (!(sender instanceof Player p)) {
            sender.sendMessage("§cOnly players can distribute blocks.");
            return true;
        }
        if (args.length < 2) {
            p.sendMessage("§cUsage: /metroedit distr <material1,material2,…> <chance%>");
            return true;
        }
        List<String> mats = List.of(args[0].split(","));
        int chance;
        try {
            chance = Integer.parseInt(args[1]);
        } catch (NumberFormatException e) {
            p.sendMessage("§cInvalid chance: " + args[1]);
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
                if (rnd.nextInt(100) < chance) {
                    String matName = mats.get(rnd.nextInt(mats.size()));
                    Material mat = Material.matchMaterial(matName);
                    if (mat == null) continue;
                    undo.record(p, loc, loc.getBlock().getBlockData(), mat.createBlockData());
                    loc.getBlock().setType(mat);
                }
            }
            p.sendMessage("§aDistributed blocks with " + chance + "% chance.");
        }, () -> p.sendMessage("§cBoth positions must be set first."));

        return true;
    }

    @Override public String getDescription() { return "Randomly distribute materials in the selection"; }
    @Override public String getUsage()       { return "distr <mat1,mat2,…> <chance%>"; }
    @Override public String getPermission()  { return "metrocore.metroedit.distr"; }
}
