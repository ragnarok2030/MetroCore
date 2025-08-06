package net.metroCore.Modules.metroedit.handler;

import net.metroCore.MetroCore;
import org.bukkit.command.CommandSender;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;

/**
 * Redirect any message starting with "//" into "/metroedit …"
 */
public class DoubleSlashListener implements Listener {

    @EventHandler
    public void onCommandPreprocess(PlayerCommandPreprocessEvent ev) {
        String msg = ev.getMessage();   // e.g. "//set stone"
        if (!msg.startsWith("//")) return;

        ev.setCancelled(true);

        // strip the leading slashes, trim any space
        String args = msg.substring(2).trim();

        // dispatch as: metroedit <the rest>
        CommandSender sender = ev.getPlayer();
        String full = "metroedit " + args;
        MetroCore.getInstance().getServer().dispatchCommand(sender, full);
    }
}
