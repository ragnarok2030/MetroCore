package net.metroCore.Modules.guns;

import net.metroCore.Core.module.AbstractModule;
import net.metroCore.Modules.guns.command.GunCommand;
import net.metroCore.Modules.guns.handler.GunShootHandler;
import net.metroCore.Modules.guns.handler.SnowballHandler;
import net.metroCore.Modules.guns.listener.MeleeListener;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.event.HandlerList;

public class GunsModule extends AbstractModule {
    private GunShootHandler shootHandler;
    private MeleeListener   meleeListener;
    private SnowballHandler snowballHandler;

    public GunsModule(JavaPlugin plugin) {
        super(plugin);
    }

    @Override
    public void onEnable() {
        // instantiate our handlers/listeners
        this.shootHandler    = new GunShootHandler();
        this.meleeListener   = new MeleeListener();
        this.snowballHandler = new SnowballHandler();

        // register /gun command
        registerCommand(new GunCommand());

        // register events
        Bukkit.getPluginManager().registerEvents(shootHandler,    getPlugin());
        Bukkit.getPluginManager().registerEvents(meleeListener,   getPlugin());
        Bukkit.getPluginManager().registerEvents(snowballHandler, getPlugin());
    }

    @Override
    public void onDisable() {
        HandlerList.unregisterAll(shootHandler);
        HandlerList.unregisterAll(meleeListener);
        HandlerList.unregisterAll(snowballHandler);
    }
}
