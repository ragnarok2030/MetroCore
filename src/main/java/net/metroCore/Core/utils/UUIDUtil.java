package net.metroCore.Core.utils;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;

import java.util.UUID;

public class UUIDUtil {

    public static UUID getUUID(String playerName) {
        OfflinePlayer player = Bukkit.getOfflinePlayer(playerName);
        return player != null ? player.getUniqueId() : null;
    }

    public static String getName(UUID uuid) {
        OfflinePlayer player = Bukkit.getOfflinePlayer(uuid);
        return player != null ? player.getName() : null;
    }
}
