package net.metroCore.Modules.metroedit.command.general;

import net.metroCore.Core.command.AbstractSubCommand;
import net.metroCore.MetroCore;
import net.metroCore.Modules.metroedit.MetroEditModule;
import net.metroCore.Modules.metroedit.region.CuboidRegion;
import org.bukkit.Particle;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class DrawSelSubCommand extends AbstractSubCommand {
    @Override
    public boolean onExecute(CommandSender sender, Command cmd, String[] args) {
        if (!(sender instanceof Player p)) {
            sender.sendMessage("§cOnly players can draw selection.");
            return true;
        }

        MetroEditModule mod = MetroCore.getInstance()
                .getModuleRegistry()
                .get(MetroEditModule.class);

        var sel = mod.getSelectionHandler();
        var optA = sel.getPos1(p);
        var optB = sel.getPos2(p);

        if (optA.isEmpty() || optB.isEmpty()) {
            p.sendMessage("§cBoth positions must be set first.");
            return true;
        }

        var a = optA.get();
        var b = optB.get();
        CuboidRegion region = new CuboidRegion(a, b);

        for (var loc : region) {
            // outline if any coordinate equals its min or max
            int x = loc.getBlockX(), y = loc.getBlockY(), z = loc.getBlockZ();
            if (x == a.getBlockX() || x == b.getBlockX()
                    || y == a.getBlockY() || y == b.getBlockY()
                    || z == a.getBlockZ() || z == b.getBlockZ()) {
                p.spawnParticle(Particle.DUST, loc.add(0.5, 0.5, 0.5), 1,
                        new Particle.DustOptions(org.bukkit.Color.RED, 1.0f));
            }
        }

        p.sendMessage("§aDrew selection outline with particles.");
        return true;
    }

    @Override public String getDescription() { return "Draw selection outline with particles"; }
    @Override public String getUsage()       { return "drawsel"; }
    @Override public String getPermission()  { return "metrocore.metroedit.drawsel"; }
}
