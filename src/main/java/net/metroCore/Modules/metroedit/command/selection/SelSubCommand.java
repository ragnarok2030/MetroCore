package net.metroCore.Modules.metroedit.command.selection;

import net.metroCore.Core.command.AbstractSubCommand;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

// Simply forwards to PosSubCommand
public class SelSubCommand extends AbstractSubCommand {
    @Override
    public boolean onExecute(CommandSender sender, Command cmd, String[] args) {
        return new PosSubCommand().onExecute(sender, cmd, args);
    }

    @Override public String getDescription() { return "Show selection coords"; }
    @Override public String getUsage()       { return "sel"; }
    @Override public String getPermission()  { return "metrocore.metroedit.sel"; }
}
