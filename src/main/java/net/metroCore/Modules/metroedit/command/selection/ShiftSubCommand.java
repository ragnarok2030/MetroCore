package net.metroCore.Modules.metroedit.command.selection;

import net.metroCore.Core.command.AbstractSubCommand;
import net.metroCore.MetroCore;
import net.metroCore.Modules.metroedit.MetroEditModule;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class ShiftSubCommand extends AbstractSubCommand {
    @Override
    public boolean onExecute(CommandSender sender, Command cmd, String[] args) {
        if (!(sender instanceof Player p)) {
            sender.sendMessage("§cUsage: /metroedit shift <dx> <dy> <dz>");
            return true;
        }
        if (args.length != 3) {
            p.sendMessage("§cUsage: /metroedit shift <dx> <dy> <dz>");
            return true;
        }
        try {
            int dx = Integer.parseInt(args[0]);
            int dy = Integer.parseInt(args[1]);
            int dz = Integer.parseInt(args[2]);

            MetroCore.getInstance()
                    .getModuleRegistry()
                    .<MetroEditModule>get(MetroEditModule.class)
                    .getSelectionHandler()
                    .shift(p, dx, dy, dz);

            p.sendMessage(String.format("§aShifted selection by (%d, %d, %d).", dx, dy, dz));
        } catch (NumberFormatException ex) {
            p.sendMessage("§cCoordinates must be integers.");
        }
        return true;
    }

    @Override public String getDescription() { return "Shift selection by an offset"; }
    @Override public String getUsage()       { return "shift <dx> <dy> <dz>"; }
    @Override public String getPermission()  { return "metrocore.metroedit.shift"; }
}
