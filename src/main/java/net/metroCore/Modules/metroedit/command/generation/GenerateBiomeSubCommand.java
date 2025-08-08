package net.metroCore.Modules.metroedit.command.generation;

import net.metroCore.Core.command.AbstractSubCommand;
import net.metroCore.MetroCore;
import net.metroCore.Modules.metroedit.MetroEditModule;
import net.metroCore.Modules.metroedit.handler.BiomeHandler;
import net.metroCore.Modules.metroedit.handler.SelectionHandler;
import net.metroCore.Modules.metroedit.region.CuboidRegion;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Optional;

public class GenerateBiomeSubCommand extends AbstractSubCommand {

    @Override
    public boolean onExecute(CommandSender sender, Command cmd, String[] args) {
        if (!(sender instanceof Player p)) {
            sender.sendMessage(ChatColor.RED + "Only players can run this command.");
            return true;
        }
        if (args.length < 1) {
            p.sendMessage(ChatColor.RED + "Usage: /metroedit generatebiome <biomeName|smooth>");
            return true;
        }

        String biomeArg = args[0].toUpperCase();
        MetroEditModule mod = MetroCore.getInstance()
                .getModuleRegistry()
                .get(MetroEditModule.class);
        SelectionHandler sel = mod.getSelectionHandler();
        BiomeHandler biomeHandler = mod.getBiomeHandler();

        Optional<Location> o1 = sel.getPos1(p);
        Optional<Location> o2 = sel.getPos2(p);
        if (o1.isEmpty() || o2.isEmpty()) {
            p.sendMessage(ChatColor.RED + "You must set both positions first.");
            return true;
        }

        CuboidRegion region = new CuboidRegion(o1.get(), o2.get());

        if ("SMOOTH".equals(biomeArg)) {
            biomeHandler.smooth(region);
            p.sendMessage(ChatColor.GREEN + "Smoothed biomes in selection.");
        } else {
            try {
                org.bukkit.block.Biome target = org.bukkit.block.Biome.valueOf(biomeArg);
                biomeHandler.fill(region, target);
                p.sendMessage(ChatColor.GREEN + "Set selection to biome " + target + ".");
            } catch (IllegalArgumentException ex) {
                p.sendMessage(ChatColor.RED + "Unknown biome: " + args[0]);
            }
        }

        return true;
    }

    @Override
    public String getDescription() {
        return "Fill or smooth biomes in your selection.";
    }

    @Override
    public String getUsage() {
        return "generatebiome <biomeName|smooth>";
    }

    @Override
    public String getPermission() {
        return "metrocore.metroedit.generatebiome";
    }
}