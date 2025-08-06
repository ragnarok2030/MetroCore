package net.metroCore.Modules.metroedit.command.selection;

import net.metroCore.Core.command.AbstractSubCommand;
import net.metroCore.MetroCore;
import net.metroCore.Modules.metroedit.MetroEditModule;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class ToggleEditWandSubCommand extends AbstractSubCommand {
    @Override
    public boolean onExecute(CommandSender sender, Command cmd, String[] args) {
        if (!(sender instanceof Player p)) {
            sender.sendMessage("§cOnly players can toggle wand mode.");
            return true;
        }
        var mod = MetroCore.getInstance()
                .getModuleRegistry()
                .<MetroEditModule>get(MetroEditModule.class);
        boolean newMode = mod.getSelectionHandler().toggleEditMode(p);
        p.sendMessage(newMode
                ? "§aWand now edits blocks."
                : "§aWand now only selects positions.");
        return true;
    }

    @Override public String getDescription() { return "Toggle wand edit/selection mode"; }
    @Override public String getUsage()       { return "selwand"; }
    @Override public String getPermission()  { return "metrocore.metroedit.selwand"; }
}
