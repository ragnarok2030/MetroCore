// PasteSubCommand.java
package net.metroCore.Modules.metroedit.command.region;

import net.metroCore.Core.command.AbstractSubCommand;
import net.metroCore.MetroCore;
import net.metroCore.Modules.metroedit.MetroEditModule;
import net.metroCore.Modules.metroedit.handler.ClipboardHandler;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class PasteSubCommand extends AbstractSubCommand {
    @Override
    public boolean onExecute(CommandSender sender, Command cmd, String[] args) {
        if (!(sender instanceof Player p)) {
            sender.sendMessage("§cOnly players can paste.");
            return true;
        }

        MetroEditModule mod = MetroCore.getInstance()
                .getModuleRegistry()
                .get(MetroEditModule.class);

        ClipboardHandler clip = mod.getClipboardHandler();
        Location dest = p.getLocation();

        clip.paste(p, dest);
        // paste method already sends success or error message

        return true;
    }

    @Override public String getDescription() { return "Paste your clipboard at your location"; }
    @Override public String getUsage()       { return "paste"; }
    @Override public String getPermission()  { return "metrocore.metroedit.paste"; }
}
