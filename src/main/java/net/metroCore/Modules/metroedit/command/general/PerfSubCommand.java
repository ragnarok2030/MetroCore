package net.metroCore.Modules.metroedit.command.general;

import net.metroCore.Core.command.AbstractSubCommand;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

import java.lang.reflect.Method;
import java.util.Optional;

public class PerfSubCommand extends AbstractSubCommand {
    @Override
    public boolean onExecute(CommandSender sender, Command cmd, String[] args) {
        try {
            // call Bukkit.getServer().spigot().getTPS() reflectively
            Object server = Bukkit.getServer();
            Method spigotMethod = server.getClass().getMethod("spigot");
            Object spigotHandle = spigotMethod.invoke(server);

            Method getTPSMethod = spigotHandle.getClass().getMethod("getTPS");
            double[] tpsArr = (double[]) getTPSMethod.invoke(spigotHandle);

            if (tpsArr != null && tpsArr.length > 0) {
                sender.sendMessage(
                        ChatColor.AQUA + "Server TPS: " +
                                ChatColor.WHITE + String.format("%.2f", tpsArr[0])
                );
            } else {
                sender.sendMessage(ChatColor.RED + "TPS data unavailable.");
            }
        } catch (NoSuchMethodException e) {
            sender.sendMessage(ChatColor.RED + "TPS reporting not supported on this build.");
        } catch (Exception e) {
            sender.sendMessage(ChatColor.RED + "Failed to fetch TPS.");
        }
        return true;
    }

    @Override public String getDescription() { return "Show server performance (TPS)"; }
    @Override public String getUsage()       { return "perf"; }
    @Override public String getPermission()  { return null; }
}
