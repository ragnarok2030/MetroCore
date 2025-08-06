package net.metroCore.Modules.metroedit.command.general;

import net.metroCore.Core.command.AbstractSubCommand;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

import java.lang.reflect.Method;

public class WatchdogSubCommand extends AbstractSubCommand {
    @Override
    public boolean onExecute(CommandSender sender, Command cmd, String[] args) {
        try {
            // 1) Reflectively call Bukkit.getServer().spigot()
            Method spigotMethod = Bukkit.getServer().getClass().getMethod("spigot");
            Object spigotHandle = spigotMethod.invoke(Bukkit.getServer());

            // 2) On that handle, call getTPS()
            Method getTPS = spigotHandle.getClass().getMethod("getTPS");
            double[] tpsArr = (double[]) getTPS.invoke(spigotHandle);

            if (tpsArr != null && tpsArr.length > 0) {
                sender.sendMessage(
                        ChatColor.AQUA + "Current TPS: " +
                                ChatColor.WHITE + String.format("%.2f", tpsArr[0])
                );
            } else {
                sender.sendMessage(ChatColor.RED + "TPS data unavailable.");
            }
        } catch (NoSuchMethodException e) {
            sender.sendMessage(ChatColor.RED + "This server build does not support TPS reporting.");
        } catch (Exception e) {
            sender.sendMessage(ChatColor.RED + "Failed to fetch TPS: " + e.getClass().getSimpleName());
        }
        return true;
    }

    @Override public String getDescription() { return "Show server TPS via watchdog"; }
    @Override public String getUsage()       { return "watchdog"; }
    @Override public String getPermission()  { return null; }
}
