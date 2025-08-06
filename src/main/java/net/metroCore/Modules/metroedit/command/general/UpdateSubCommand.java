package net.metroCore.Modules.metroedit.command.general;

import net.metroCore.Core.command.AbstractSubCommand;
import net.metroCore.MetroCore;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

public class UpdateSubCommand extends AbstractSubCommand {
    @Override
    public boolean onExecute(CommandSender sender, Command cmd, String[] args) {
        MetroCore.getInstance().getModuleRegistry().reload();
        sender.sendMessage(ChatColor.GREEN + "MetroCore modules reloaded.");
        return true;
    }

    @Override public String getDescription() { return "Reload all modules"; }
    @Override public String getUsage()       { return "update"; }
    @Override public String getPermission()  { return "metrocore.command.reload"; }
}
