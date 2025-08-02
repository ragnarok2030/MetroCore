package net.metroCore.Core.logging;

import net.metroCore.Core.config.FileRegistry;
import org.bukkit.configuration.file.FileConfiguration;

public class DebugLogger {

    private static boolean debug = false;

    public static void loadDebugSetting() {
        FileConfiguration config = FileRegistry.CONFIG.get();
        debug = config.getBoolean("debug", false);
    }

    public static boolean isDebugMode() {
        return debug;
    }

    public static void setDebugMode(boolean state) {
        debug = state;
    }
}
