package net.metroCore.Modules.metroedit.command.general;

import net.metroCore.Core.command.AbstractSubCommand;
import net.metroCore.MetroCore;
import net.metroCore.Modules.metroedit.MetroEditModule;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class ClearHistorySubCommand extends AbstractSubCommand {
    @Override
    public boolean onExecute(CommandSender sender, Command cmd, String[] args) {
        if (!(sender instanceof Player p)) {
            sender.sendMessage("§cOnly players can clear history.");
            return true;
        }
        MetroEditModule mod = MetroCore.getInstance()
                .getModuleRegistry().get(MetroEditModule.class);
        mod.getUndoRedoHandler().clearHistory(p);
        p.sendMessage("§aUndo/redo history cleared.");
        return true;
    }

    @Override public String getDescription() { return "Clear your undo/redo history"; }
    @Override public String getUsage()       { return "clearhistory"; }
    @Override public String getPermission()  { return "metrocore.metroedit.clearhistory"; }
}
