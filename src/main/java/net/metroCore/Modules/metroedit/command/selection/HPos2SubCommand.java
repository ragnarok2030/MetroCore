package net.metroCore.Modules.metroedit.command.selection;

import net.metroCore.Core.command.AbstractSubCommand;
import net.metroCore.MetroCore;
import net.metroCore.Modules.metroedit.MetroEditModule;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class HPos2SubCommand extends AbstractSubCommand {
    @Override
    public boolean onExecute(CommandSender sender, Command cmd, String[] args) {
        if (!(sender instanceof Player p)) {
            sender.sendMessage("§cOnly players can use this.");
            return true;
        }
        var handler = MetroCore.getInstance()
                .getModuleRegistry()
                .<MetroEditModule>get(MetroEditModule.class)
                .getSelectionHandler();

        var eye = p.getEyeLocation();
        handler.setPos2(p, eye);
        p.sendMessage(ChatColor.GREEN + "Horizontal Pos 2 set to Y=" + eye.getBlockY());
        return true;
    }

    @Override public String getDescription() { return "Set Y of position 2 to your eye height"; }
    @Override public String getUsage()       { return "hpos2"; }
    @Override public String getPermission()  { return "metrocore.metroedit.hpos2"; }
}
