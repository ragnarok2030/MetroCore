package net.metroCore.Core.utils;

import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.World;

public final class WorldUtil {
    private WorldUtil() {}

    /** Synchronously load (or generate) a chunk, then return it. */
    public static Chunk loadChunk(World world, int cx, int cz) {
        world.loadChunk(cx, cz, true);
        return world.getChunkAt(cx, cz);
    }

    /** Unload a chunk if it’s loaded; returns true if already unloaded or on successful unload. */
    public static boolean unloadChunk(World world, int cx, int cz) {
        Chunk chunk = world.getChunkAt(cx, cz);
        if (!chunk.isLoaded()) return true;
        return world.unloadChunkRequest(cx, cz);
    }
}
