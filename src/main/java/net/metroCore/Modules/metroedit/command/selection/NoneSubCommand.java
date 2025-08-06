package net.metroCore.Modules.metroedit.command.selection;

import net.metroCore.Core.command.AbstractSubCommand;
import net.metroCore.MetroCore;
import net.metroCore.Modules.metroedit.MetroEditModule;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class NoneSubCommand extends AbstractSubCommand {
    @Override
    public boolean onExecute(CommandSender sender, Command cmd, String[] args) {
        if (!(sender instanceof Player p)) {
            sender.sendMessage("§cOnly players can clear selection.");
            return true;
        }
        var handler = MetroCore.getInstance()
                .getModuleRegistry()
                .<MetroEditModule>get(MetroEditModule.class)
                .getSelectionHandler();

        handler.clearSelection(p);
        p.sendMessage(ChatColor.YELLOW + "Selection cleared.");
        return true;
    }

    @Override public String getDescription() { return "Clear current selection"; }
    @Override public String getUsage()       { return "none"; }
    @Override public String getPermission()  { return "metrocore.metroedit.none"; }
}
