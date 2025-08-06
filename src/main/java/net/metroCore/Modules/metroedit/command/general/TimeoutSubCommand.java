package net.metroCore.Modules.metroedit.command.general;

import net.metroCore.Core.command.AbstractSubCommand;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

public class TimeoutSubCommand extends AbstractSubCommand {
    @Override
    public boolean onExecute(CommandSender sender, Command cmd, String[] args) {
        // just demonstrate timing a no-op
        long start = System.currentTimeMillis();
        // ... you could run some test logic here ...
        long end = System.currentTimeMillis();
        sender.sendMessage(ChatColor.GREEN + "Command executed in " +
                ChatColor.WHITE + (end - start) + "ms");
        return true;
    }

    @Override public String getDescription() { return "Measure command execution time"; }
    @Override public String getUsage()       { return "timeout"; }
    @Override public String getPermission()  { return null; }
}
