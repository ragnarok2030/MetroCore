package net.metroCore.Modules.metroedit.command.general;

import net.metroCore.Core.command.AbstractSubCommand;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

public class TogglePlaceSubCommand extends AbstractSubCommand {
    private static boolean allowPlace = true;

    @Override
    public boolean onExecute(CommandSender sender, Command cmd, String[] args) {
        allowPlace = !allowPlace;
        sender.sendMessage(ChatColor.YELLOW + "Block placement is now " +
                (allowPlace ? "§aENABLED" : "§cDISABLED"));
        return true;
    }

    @Override public String getDescription() { return "Toggle block placement"; }
    @Override public String getUsage()       { return "toggleplace"; }
    @Override public String getPermission()  { return "metrocore.metroedit.toggleplace"; }
}
