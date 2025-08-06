package net.metroCore.Modules.metroedit.command.selection;

import net.metroCore.Core.command.AbstractSubCommand;
import net.metroCore.MetroCore;
import net.metroCore.Modules.metroedit.MetroEditModule;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CountSubCommand extends AbstractSubCommand {
    @Override
    public boolean onExecute(CommandSender sender, Command cmd, String[] args) {
        if (!(sender instanceof Player p)) {
            sender.sendMessage("§cOnly players can count blocks.");
            return true;
        }
        var handler = MetroCore.getInstance()
                .getModuleRegistry()
                .<MetroEditModule>get(MetroEditModule.class)
                .getSelectionHandler();

        int total = handler.count(p);
        p.sendMessage(ChatColor.GOLD + "Blocks in selection (non-air): " +
                ChatColor.WHITE + total);
        return true;
    }

    @Override public String getDescription() { return "Count blocks in selection"; }
    @Override public String getUsage()       { return "count"; }
    @Override public String getPermission()  { return "metrocore.metroedit.count"; }
}
