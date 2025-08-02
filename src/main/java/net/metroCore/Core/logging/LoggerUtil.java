package net.metroCore.Core.logging;

import org.bukkit.Bukkit;

public class LoggerUtil {

    private static final String PREFIX = "§7[§dMetroCore§7] §r";

    public static void info(String message) {
        sendConsole("§f[INFO] " + message);
    }

    public static void warn(String message) {
        sendConsole("§e[WARNING] " + message);
    }

    public static void error(String message) {
        sendConsole("§c[ERROR] " + message);
    }

    public static void success(String message) {
        sendConsole("§a[SUCCESS] " + message);
    }

    private static void sendConsole(String message) {
        Bukkit.getConsoleSender().sendMessage(PREFIX + message);
    }
}
