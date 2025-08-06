package net.metroCore.Modules.metroedit.command.general;

import net.metroCore.Core.command.AbstractSubCommand;
import net.metroCore.MetroCore;
import net.metroCore.Modules.metroedit.MetroEditModule;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class UndoSubCommand extends AbstractSubCommand {
    @Override
    public boolean onExecute(CommandSender sender, Command cmd, String[] args) {
        if (!(sender instanceof Player p)) {
            sender.sendMessage("§cOnly players can undo.");
            return true;
        }
        MetroEditModule mod = MetroCore.getInstance()
                .getModuleRegistry().get(MetroEditModule.class);
        mod.getUndoRedoHandler().undo(p);
        return true;
    }

    @Override public String getDescription() { return "Undo last change"; }
    @Override public String getUsage()       { return "undo"; }
    @Override public String getPermission()  { return "metrocore.metroedit.undo"; }
}
