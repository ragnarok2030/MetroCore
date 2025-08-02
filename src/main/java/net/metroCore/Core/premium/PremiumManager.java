package net.metroCore.Core.premium;

import net.metroCore.Core.config.FileRegistry;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.UUID;

public class PremiumManager {

    private static final FileConfiguration premiumConfig = FileRegistry.PREMIUM.get();

    public static boolean isPremium(UUID uuid) {
        return premiumConfig.getBoolean("premium-users." + uuid.toString(), false);
    }

    public static void setPremium(UUID uuid, boolean value) {
        premiumConfig.set("premium-users." + uuid.toString(), value);
        FileRegistry.PREMIUM.save();
    }

    public static boolean hasFeature(UUID uuid, PremiumFeature feature) {
        if (!isPremium(uuid)) return false;

        return premiumConfig.getBoolean("feature-access." + uuid.toString() + "." + feature.name().toLowerCase(), true);
    }

    public static void reload() {
        FileRegistry.PREMIUM.reload();
    }
}
