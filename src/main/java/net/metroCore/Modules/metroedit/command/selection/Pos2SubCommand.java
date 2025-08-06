package net.metroCore.Modules.metroedit.command.selection;

import net.metroCore.Core.command.AbstractSubCommand;
import net.metroCore.MetroCore;
import net.metroCore.Modules.metroedit.MetroEditModule;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class Pos2SubCommand extends AbstractSubCommand {
    @Override
    public boolean onExecute(CommandSender sender, Command cmd, String[] args) {
        if (!(sender instanceof Player p)) {
            sender.sendMessage("§cOnly players can use this.");
            return true;
        }
        MetroEditModule mod = MetroCore.getInstance()
                .getModuleRegistry()
                .get(MetroEditModule.class);

        mod.getSelectionHandler().setPos2(p, p.getTargetBlockExact(100).getLocation());
        p.sendMessage(ChatColor.GREEN + "Position 2 set to " +
                format(p.getTargetBlockExact(100).getLocation()));
        return true;
    }

    private String format(org.bukkit.Location l) {
        return "(" + l.getBlockX() + "," + l.getBlockY() + "," + l.getBlockZ() + ")";
    }

    @Override public String getDescription() { return "Set selection position 2"; }
    @Override public String getUsage()       { return "pos2"; }
    @Override public String getPermission()  { return "metrocore.metroedit.pos2"; }
}
