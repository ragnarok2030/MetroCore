package net.metroCore.Modules.metroedit.command.general;

import net.metroCore.Core.command.AbstractSubCommand;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

public class LimitSubCommand extends AbstractSubCommand {
    private static final int DEFAULT_LIMIT = 10000;
    @Override
    public boolean onExecute(CommandSender sender, Command cmd, String[] args) {
        // simply report the built‐in max block limit
        sender.sendMessage(ChatColor.YELLOW + "Max selection size is " +
                ChatColor.WHITE + DEFAULT_LIMIT +
                ChatColor.YELLOW + " blocks.");
        return true;
    }

    @Override public String getDescription() { return "Show max selection size"; }
    @Override public String getUsage()       { return "limit"; }
    @Override public String getPermission()  { return null; }
}
