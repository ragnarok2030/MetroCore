package net.metroCore.Modules.metroedit.command.general;

import net.metroCore.Core.command.AbstractSubCommand;
import net.metroCore.MetroCore;
import net.metroCore.Modules.metroedit.MetroEditModule;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class RedoSubCommand extends AbstractSubCommand {
    @Override
    public boolean onExecute(CommandSender sender, Command cmd, String[] args) {
        if (!(sender instanceof Player p)) {
            sender.sendMessage("§cOnly players can redo.");
            return true;
        }
        MetroEditModule mod = MetroCore.getInstance()
                .getModuleRegistry().get(MetroEditModule.class);
        mod.getUndoRedoHandler().redo(p);
        return true;
    }

    @Override public String getDescription() { return "Redo last undone change"; }
    @Override public String getUsage()       { return "redo"; }
    @Override public String getPermission()  { return "metrocore.metroedit.redo"; }
}
