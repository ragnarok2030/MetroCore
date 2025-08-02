package net.metroCore.Modules.items;

import net.metroCore.Core.module.AbstractModule;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * Module to bootstrap your CustomItems enum (guns, ammo, melee),
 * so it appears in modules.yml and can be toggled on/off.
 */
public class ItemsModule extends AbstractModule {

    public ItemsModule(JavaPlugin plugin) {
        super(plugin);
    }

    @Override
    public void onEnable() {
        getPlugin().getLogger().info("ItemsModule enabled.");
    }

    @Override
    public void onDisable() {
        getPlugin().getLogger().info("ItemsModule disabled.");
    }
}
