package net.metroCore.Core.utils.nbt;

import org.bukkit.NamespacedKey;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * Centralized NBT key factory: always uses your plugin as the namespace,
 * but allows you to request any key at runtime.
 */
public class NbtConstants {
    private static JavaPlugin plugin;

    /** Must be called once on plugin enable. */
    public static void init(JavaPlugin mainPlugin) {
        plugin = mainPlugin;
    }

    /**
     * Return a NamespacedKey in your plugin's namespace for ANY key name.
     * e.g.  NbtConstants.key("gun"), or NbtConstants.key("someOtherFeature").
     */
    public static NamespacedKey key(String key) {
        if (plugin == null) {
            throw new IllegalStateException("NbtConstants not initialized");
        }
        return new NamespacedKey(plugin, key);
    }
}
