package net.metroCore.Modules.metroedit.command.selection;

import net.metroCore.Core.command.AbstractSubCommand;
import net.metroCore.MetroCore;
import net.metroCore.Modules.metroedit.MetroEditModule;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class ContractSubCommand extends AbstractSubCommand {
    @Override
    public boolean onExecute(CommandSender sender, Command cmd, String[] args) {
        if (!(sender instanceof Player p)) {
            sender.sendMessage("§cOnly players can contract.");
            return true;
        }
        var handler = MetroCore.getInstance()
                .getModuleRegistry()
                .<MetroEditModule>get(MetroEditModule.class)
                .getSelectionHandler();

        handler.contract(p);
        p.sendMessage("§aSelection contracted by 1 block.");
        return true;
    }

    @Override public String getDescription() { return "Shrink selection by 1 on each face"; }
    @Override public String getUsage()       { return "contract"; }
    @Override public String getPermission()  { return "metrocore.metroedit.contract"; }
}
