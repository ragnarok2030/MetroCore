package net.metroCore.Core.logging;

import org.bukkit.Bukkit;
import java.util.logging.Level;
import java.util.logging.Logger;

public class CoreLogger {

    private static final Logger logger = Bukkit.getLogger();
    private static final String PREFIX = "[MetroCore] ";

    public static void info(String message) {
        logger.log(Level.INFO, PREFIX + message);
    }

    public static void warn(String message) {
        logger.log(Level.WARNING, PREFIX + message);
    }

    public static void error(String message) {
        logger.log(Level.SEVERE, PREFIX + message);
    }

    public static void debug(String message) {
        if (DebugLogger.isDebugMode()) {
            logger.log(Level.INFO, PREFIX + "[Debug] " + message);
        }
    }
}
