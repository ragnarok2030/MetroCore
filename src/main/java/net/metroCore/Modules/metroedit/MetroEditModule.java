package net.metroCore.Modules.metroedit;

import net.metroCore.Core.module.AbstractModule;
import net.metroCore.Modules.metroedit.command.MetroEditCommand;
import net.metroCore.Modules.metroedit.handler.*;
import org.bukkit.Bukkit;
import org.bukkit.event.HandlerList;
import org.bukkit.plugin.java.JavaPlugin;

public class MetroEditModule extends AbstractModule {

    private MetroEditCommand       mainCommand;
    private SelectionHandler       selectionHandler;
    private ClipboardHandler       clipboardHandler;
    private UndoRedoHandler        undoRedoHandler;
    private BiomeHandler           biomeHandler;
    private DoubleSlashListener    doubleSlashListener;

    public MetroEditModule(JavaPlugin plugin) {
        super(plugin);
    }

    @Override
    public void onEnable() {
        // 1) /metroedit (and /me alias from plugin.yml)
        mainCommand = new MetroEditCommand();
        registerCommand(mainCommand);

        // 2) Instantiate all listeners
        selectionHandler    = new SelectionHandler();
        clipboardHandler    = new ClipboardHandler();
        undoRedoHandler     = new UndoRedoHandler();
        biomeHandler        = new BiomeHandler();
        doubleSlashListener = new DoubleSlashListener();

        // 3) Hook them into Bukkit
        Bukkit.getPluginManager().registerEvents(selectionHandler,    getPlugin());
        Bukkit.getPluginManager().registerEvents(clipboardHandler,    getPlugin());
        Bukkit.getPluginManager().registerEvents(undoRedoHandler,     getPlugin());
        Bukkit.getPluginManager().registerEvents(biomeHandler,        getPlugin());
        Bukkit.getPluginManager().registerEvents(doubleSlashListener, getPlugin());
    }

    @Override
    public void onDisable() {
        HandlerList.unregisterAll(selectionHandler);
        HandlerList.unregisterAll(clipboardHandler);
        HandlerList.unregisterAll(undoRedoHandler);
        HandlerList.unregisterAll(biomeHandler);
        HandlerList.unregisterAll(doubleSlashListener);
    }

    // Expose for your subcommands:
    public SelectionHandler getSelectionHandler() { return selectionHandler; }
    public ClipboardHandler  getClipboardHandler() { return clipboardHandler; }
    public UndoRedoHandler   getUndoRedoHandler()  { return undoRedoHandler;  }
    public BiomeHandler      getBiomeHandler()     { return biomeHandler;     }
}
