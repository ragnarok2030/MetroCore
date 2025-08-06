package net.metroCore.Modules.metroedit.command.selection;

import net.metroCore.Core.command.AbstractSubCommand;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class WandSubCommand extends AbstractSubCommand {
    @Override
    public boolean onExecute(CommandSender sender, Command cmd, String[] args) {
        if (!(sender instanceof Player p)) {
            sender.sendMessage("§cOnly players can use this.");
            return true;
        }
        p.getInventory().addItem(new org.bukkit.inventory.ItemStack(Material.WOODEN_AXE));
        p.sendMessage("§aGave you the WorldEdit wand (wooden axe).");
        return true;
    }

    @Override public String getDescription() { return "Give the edit wand"; }
    @Override public String getUsage()       { return "wand"; }
    @Override public String getPermission()  { return "metrocore.metroedit.wand"; }
}
