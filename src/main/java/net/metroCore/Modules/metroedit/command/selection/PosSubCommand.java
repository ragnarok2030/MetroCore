package net.metroCore.Modules.metroedit.command.selection;

import net.metroCore.Core.command.AbstractSubCommand;
import net.metroCore.MetroCore;
import net.metroCore.Modules.metroedit.MetroEditModule;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class PosSubCommand extends AbstractSubCommand {
    @Override
    public boolean onExecute(CommandSender sender, Command cmd, String[] args) {
        if (!(sender instanceof Player p)) {
            sender.sendMessage("§cOnly players can use this.");
            return true;
        }
        var sel = MetroCore.getInstance()
                .getModuleRegistry()
                .<MetroEditModule>get(MetroEditModule.class)
                .getSelectionHandler();

        var p1 = sel.getPos1(p).map(loc -> loc.getBlockX()+","+
                        loc.getBlockY()+","+
                        loc.getBlockZ())
                .orElse("unset");
        var p2 = sel.getPos2(p).map(loc -> loc.getBlockX()+","+
                        loc.getBlockY()+","+
                        loc.getBlockZ())
                .orElse("unset");

        p.sendMessage(ChatColor.GOLD + "Position 1: " + ChatColor.WHITE + p1);
        p.sendMessage(ChatColor.GOLD + "Position 2: " + ChatColor.WHITE + p2);
        return true;
    }

    @Override public String getDescription() { return "Show current selection positions"; }
    @Override public String getUsage()       { return "pos"; }
    @Override public String getPermission()  { return "metrocore.metroedit.pos"; }
}
