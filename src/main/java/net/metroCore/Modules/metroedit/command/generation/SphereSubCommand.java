package net.metroCore.Modules.metroedit.command.generation;

import net.metroCore.Core.command.AbstractSubCommand;
import net.metroCore.MetroCore;
import net.metroCore.Modules.metroedit.MetroEditModule;
import net.metroCore.Modules.metroedit.handler.GenerationHandler;
import net.metroCore.Modules.metroedit.region.*;
import net.metroCore.Modules.metroedit.handler.UndoRedoHandler;
import net.metroCore.Modules.metroedit.handler.UndoRedoHandler.BlockChange;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.*;

public class SphereSubCommand extends AbstractSubCommand {
    @Override public boolean onExecute(CommandSender sender, Command cmd, String[] args) {
        if (!(sender instanceof Player p)) return usage(sender);
        int radius = args.length > 0 ? parseInt(sender, args[0], -1) : -1;
        if (radius < 0) return true;
        MetroEditModule mod = MetroCore.getInstance().getModuleRegistry().get(MetroEditModule.class);
        GenerationHandler gen = new GenerationHandler();
        UndoRedoHandler undo = mod.getUndoRedoHandler();
        Location center = p.getLocation();
        List<BlockChange> batch = new ArrayList<>();
        for (Location loc : new SphereRegion(center, radius)) {
            batch.add(new BlockChange(loc.clone(), loc.getBlock().getBlockData(), p.getInventory().getItemInMainHand().getType().createBlockData()));
        }
        undo.recordBulk(p, batch);
        batch.forEach(BlockChange::applyRedo);
        p.sendMessage(ChatColor.GREEN + "Generated sphere radius=" + radius);
        return true;
    }
    private boolean usage(CommandSender s) { s.sendMessage("§cUsage: /metroedit sphere <radius>"); return true; }
    private int parseInt(CommandSender s, String v, int def) { try { return Integer.parseInt(v);} catch (Exception e){s.sendMessage("§cInvalid number: " + v); return def;} }
    @Override public String getDescription() { return "Create a filled sphere"; }
    @Override public String getUsage() { return "sphere <radius>"; }
    @Override public String getPermission() { return "metrocore.metroedit.sphere"; }
}