package net.metroCore.Modules.metroedit.command.selection;

import net.metroCore.Core.command.AbstractSubCommand;
import net.metroCore.MetroCore;
import net.metroCore.Modules.metroedit.MetroEditModule;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class SizeSubCommand extends AbstractSubCommand {
    @Override
    public boolean onExecute(CommandSender sender, Command cmd, String[] args) {
        if (!(sender instanceof Player p)) {
            sender.sendMessage("§cOnly players can view size.");
            return true;
        }
        var handler = MetroCore.getInstance()
                .getModuleRegistry()
                .<MetroEditModule>get(MetroEditModule.class)
                .getSelectionHandler();

        var size = handler.size(p); // returns {dx, dy, dz}
        int volume = size[0] * size[1] * size[2];

        p.sendMessage(ChatColor.GOLD + "Selection size: " +
                size[0] + "×" + size[1] + "×" + size[2]);
        p.sendMessage(ChatColor.GOLD + "Volume: " + volume + " blocks");
        return true;
    }

    @Override public String getDescription() { return "Show selection size and volume"; }
    @Override public String getUsage()       { return "size"; }
    @Override public String getPermission()  { return "metrocore.metroedit.size"; }
}
