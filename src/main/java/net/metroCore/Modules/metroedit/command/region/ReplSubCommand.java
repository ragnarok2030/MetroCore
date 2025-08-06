// ReplSubCommand.java
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

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ReplSubCommand extends AbstractSubCommand {
    private static final Pattern ENTRY = Pattern.compile("(?:(\\d+)%){0,1}([A-Za-z0-9_]+)");
    private final Random rnd = new Random();

    @Override
    public boolean onExecute(CommandSender sender, Command cmd, String[] args) {
        if (!(sender instanceof Player p)) {
            sender.sendMessage("§cOnly players can use replace.");
            return true;
        }
        if (args.length < 2) {
            p.sendMessage("§cUsage: /metroedit repl <from> <[weight%]to…>");
            return true;
        }

        Material from = Material.matchMaterial(args[0].toUpperCase(Locale.ROOT));
        if (from == null) {
            p.sendMessage("§cUnknown source material: " + args[0]);
            return true;
        }

        // Parse weighted targets
        List<Material> toMats = new ArrayList<>();
        List<Double>   wts    = new ArrayList<>();
        double         total  = 0;

        for (String part : args[1].split(",")) {
            Matcher m = ENTRY.matcher(part.trim());
            if (!m.matches()) {
                p.sendMessage("§cInvalid entry: " + part);
                return true;
            }
            String pct = m.group(1);
            String name = m.group(2).toUpperCase(Locale.ROOT);
            Material mat = Material.matchMaterial(name);
            if (mat == null) {
                p.sendMessage("§cUnknown material: " + name);
                return true;
            }
            double w = (pct != null) ? Double.parseDouble(pct) : 1.0;
            toMats.add(mat);
            wts.add(w);
            total += w;
        }

        // Build prefix sum
        final double[] prefix = new double[wts.size()];
        double acc = 0;
        for (int i = 0; i < wts.size(); i++) {
            acc += wts.get(i);
            prefix[i] = acc;
        }
        final double grandTotal = acc;

        MetroEditModule mod = MetroCore.getInstance()
                .getModuleRegistry().get(MetroEditModule.class);
        SelectionHandler sel = mod.getSelectionHandler();
        UndoRedoHandler undo = mod.getUndoRedoHandler();

        sel.getPos1(p).flatMap(a ->
                sel.getPos2(p).map(b -> new CuboidRegion(a, b))
        ).ifPresentOrElse(region -> {
            int replaced = 0;
            for (Location loc : region) {
                if (loc.getBlock().getType() != from) continue;
                double r = rnd.nextDouble() * grandTotal;
                int idx = Arrays.binarySearch(prefix, r);
                if (idx < 0) idx = -idx - 1;
                idx = Math.min(idx, toMats.size() - 1);

                Material chosen = toMats.get(idx);
                undo.record(p, loc, loc.getBlock().getBlockData(), chosen.createBlockData());
                loc.getBlock().setBlockData(chosen.createBlockData());
                replaced++;
            }
            p.sendMessage("§aReplaced " + replaced + " of " + from);
        }, () -> p.sendMessage("§cBoth positions must be set first."));

        return true;
    }

    @Override public String getDescription() { return "Replace blocks in selection, supports weights"; }
    @Override public String getUsage()       { return "repl <from> <[weight%]to…>"; }
    @Override public String getPermission()  { return "metrocore.metroedit.repl"; }
}
