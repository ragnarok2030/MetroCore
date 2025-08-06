package net.metroCore.Modules.metroedit.command.selection;

import net.metroCore.Core.command.AbstractSubCommand;
import net.metroCore.MetroCore;
import net.metroCore.Modules.metroedit.MetroEditModule;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class OutsetSubCommand extends AbstractSubCommand {
    @Override
    public boolean onExecute(CommandSender sender, Command cmd, String[] args) {
        if (!(sender instanceof Player p)) {
            sender.sendMessage("§cOnly players can outset.");
            return true;
        }
        MetroCore.getInstance()
                .getModuleRegistry()
                .<MetroEditModule>get(MetroEditModule.class)
                .getSelectionHandler()
                .outset(p);

        p.sendMessage("§aSelection expanded by 1 block.");
        return true;
    }

    @Override public String getDescription() { return "Expand selection by 1"; }
    @Override public String getUsage()       { return "outset"; }
    @Override public String getPermission()  { return "metrocore.metroedit.outset"; }
}
