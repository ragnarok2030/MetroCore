package net.metroCore.Modules.metroedit.command.region;

import net.metroCore.Core.command.AbstractSubCommand;
import net.metroCore.MetroCore;
import net.metroCore.Modules.metroedit.MetroEditModule;
import net.metroCore.Modules.metroedit.handler.SelectionHandler;
import net.metroCore.Modules.metroedit.handler.UndoRedoHandler;
import net.metroCore.Modules.metroedit.handler.UndoRedoHandler.BlockChange;
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

public class SetSubCommand extends AbstractSubCommand {
    private static final Pattern ENTRY = Pattern.compile("(?:(\\d+)%){0,1}([A-Za-z0-9_]+)");
    private final Random rnd = new Random();

    @Override
    public boolean onExecute(CommandSender sender, Command cmd, String[] args) {
        if (!(sender instanceof Player p)) {
            sender.sendMessage("§cOnly players can use set.");
            return true;
        }
        if (args.length < 1) {
            p.sendMessage("§cUsage: /metroedit set <[weight%]material…>");
            return true;
        }

        // 1) parse weighted materials
        List<Material> mats = new ArrayList<>();
        List<Double>   wts  = new ArrayList<>();
        double grandTotal = 0;

        for (String part : args[0].split(",")) {
            Matcher m = ENTRY.matcher(part.trim());
            if (!m.matches()) {
                p.sendMessage("§cInvalid format: " + part);
                return true;
            }
            String pct  = m.group(1);
            String name = m.group(2).toUpperCase(Locale.ROOT);
            Material mat = Material.matchMaterial(name);
            if (mat == null) {
                p.sendMessage("§cUnknown material: " + name);
                return true;
            }
            double weight = (pct != null) ? Double.parseDouble(pct) : 1.0;
            mats.add(mat);
            wts.add(weight);
            grandTotal += weight;
        }

        // 2) build prefix sums
        double[] prefix = new double[wts.size()];
        double acc = 0;
        for (int i = 0; i < wts.size(); i++) {
            acc += wts.get(i);
            prefix[i] = acc;
        }

        // 3) fetch module & handlers
        MetroEditModule mod = MetroCore.getInstance()
                .getModuleRegistry()
                .get(MetroEditModule.class);
        SelectionHandler sel = mod.getSelectionHandler();
        UndoRedoHandler undo = mod.getUndoRedoHandler();

        // 4) pull out positions into finals
        Optional<Location> opt1 = sel.getPos1(p);
        Optional<Location> opt2 = sel.getPos2(p);

        if (opt1.isEmpty() || opt2.isEmpty()) {
            p.sendMessage("§cBoth positions must be set first.");
            return true;
        }

        Location a = opt1.get();
        Location b = opt2.get();
        CuboidRegion region = new CuboidRegion(a, b);

        // 5) collect a batch of changes
        List<BlockChange> batch = new ArrayList<>();
        for (Location loc : region) {
            double r = rnd.nextDouble() * grandTotal;
            int idx = Arrays.binarySearch(prefix, r);
            if (idx < 0) idx = -idx - 1;
            idx = Math.min(idx, mats.size() - 1);
            Material chosen = mats.get(idx);

            batch.add(new BlockChange(
                    loc.clone(),
                    loc.getBlock().getBlockData(),
                    chosen.createBlockData()
            ));
        }

        // 6) record bulk and apply in one shot
        undo.recordBulk(p, batch);
        batch.forEach(BlockChange::applyRedo);

        p.sendMessage("§aSet " + batch.size() + " blocks.");
        return true;
    }

    @Override public String getDescription() { return "Fill selection with blocks, supports weights"; }
    @Override public String getUsage()       { return "set <[weight%]mat,…>"; }
    @Override public String getPermission()  { return "metrocore.metroedit.set"; }
}
