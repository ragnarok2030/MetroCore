package net.metroCore.Modules.metroedit.handler;

import net.metroCore.Modules.metroedit.region.Region;
import org.bukkit.Location;

/**
 * Takes a Region and fills it with a given block type.
 */
public class GenerationHandler {

    /**
     * Fill the given region with blockMaterial.
     */
    public void fillRegion(Region region, org.bukkit.Material blockMaterial) {
        for (Location loc : region) {
            loc.getBlock().setType(blockMaterial);
        }
    }

    /**
     * Undo/redo integration: you can hook into record changes.
     * e.g. call UndoRedoHandler.record(...) before changing each block.
     */
}
