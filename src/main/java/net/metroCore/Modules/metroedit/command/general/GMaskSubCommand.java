package net.metroCore.Modules.metroedit.command.general;

import net.metroCore.Core.command.AbstractSubCommand;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

public class GMaskSubCommand extends AbstractSubCommand {
    private static boolean globalMask = false;

    @Override
    public boolean onExecute(CommandSender sender, Command cmd, String[] args) {
        globalMask = !globalMask;
        sender.sendMessage(ChatColor.YELLOW + "Global mask is now " +
                (globalMask ? "§aON" : "§cOFF"));
        return true;
    }

    @Override public String getDescription() { return "Toggle global edit mask"; }
    @Override public String getUsage()       { return "gmask"; }
    @Override public String getPermission()  { return "metrocore.metroedit.gmask"; }
}
