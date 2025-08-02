package net.metroCore.Core.premium;

public enum PremiumFeature {
    ROBBERY_LIMIT,
    WEAPON_PACK,
    EXTRA_PERMISSIONS,
    CUSTOM_TAGS,
    EXCLUSIVE_COMMANDS;

    public String getConfigPath() {
        return "premium.features." + this.name().toLowerCase();
    }
}
