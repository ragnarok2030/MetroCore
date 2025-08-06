package net.metroCore.Modules.metroedit.command.selection;

import net.metroCore.Core.command.AbstractSubCommand;
import net.metroCore.MetroCore;
import net.metroCore.Modules.metroedit.MetroEditModule;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class InsetSubCommand extends AbstractSubCommand {
    @Override
    public boolean onExecute(CommandSender sender, Command cmd, String[] args) {
        if (!(sender instanceof Player p)) {
            sender.sendMessage("§cOnly players can inset.");
            return true;
        }
        MetroCore.getInstance()
                .getModuleRegistry()
                .<MetroEditModule>get(MetroEditModule.class)
                .getSelectionHandler()
                .inset(p);

        p.sendMessage("§aSelection inset by 1 block.");
        return true;
    }

    @Override public String getDescription() { return "Inset selection by 1"; }
    @Override public String getUsage()       { return "inset"; }
    @Override public String getPermission()  { return "metrocore.metroedit.inset"; }
}
