package net.metroCore.Modules.metroedit.command.general;

import net.metroCore.Core.command.AbstractSubCommand;
import net.metroCore.MetroCore;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

public class InfoSubCommand extends AbstractSubCommand {
    @Override
    public boolean onExecute(CommandSender sender, Command cmd, String[] args) {
        sender.sendMessage("§6=== MetroEdit ===");
        sender.sendMessage("§fVersion: §e" + MetroCore.getInstance().getDescription().getVersion());
        sender.sendMessage("§fEnabled modules: §e" +
                String.join(", ", MetroCore.getInstance().getModuleRegistry().getEnabledModuleNames())
        );
        return true;
    }

    @Override public String getDescription() { return "Show plugin info and enabled modules"; }
    @Override public String getUsage()       { return ""; }
    @Override public String getPermission()  { return null; }
}
