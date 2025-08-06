// src/main/java/net/metroCore/Modules/metroedit/util/WorldEditUtil.java
package net.metroCore.Modules.metroedit.util;

import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import net.metroCore.Modules.metroedit.region.Region;

/**
 * Low-level block operations: fill regions, replace blocks, selection iterators, etc.
 */
public final class WorldEditUtil {
    private WorldEditUtil() {}

    /** Fill every block in the region with the given material. */
    public static void fillRegion(Region region, Material mat) {
        for (Location loc : region) {
            loc.getBlock().setType(mat);
        }
    }

    /** Replace all instances of fromMat → toMat in the region. */
    public static void replaceRegion(Region region, Material fromMat, Material toMat) {
        for (Location loc : region) {
            Block b = loc.getBlock();
            if (b.getType() == fromMat) {
                b.setType(toMat);
            }
        }
    }

    /** Clear (set to air) everything in the region. */
    public static void clearRegion(Region region) {
        fillRegion(region, Material.AIR);
    }

    /** Load all chunks intersecting the region’s bounding box. */
    public static void loadRegionChunks(Region region) {
        World world = null;
        int minCX = Integer.MAX_VALUE, maxCX = Integer.MIN_VALUE;
        int minCZ = Integer.MAX_VALUE, maxCZ = Integer.MIN_VALUE;
        for (Location loc : region) {
            if (world == null) world = loc.getWorld();
            int cx = loc.getBlockX() >> 4, cz = loc.getBlockZ() >> 4;
            minCX = Math.min(minCX, cx);
            minCZ = Math.min(minCZ, cz);
            maxCX = Math.max(maxCX, cx);
            maxCZ = Math.max(maxCZ, cz);
        }
        if (world != null) {
            for (int x = minCX; x <= maxCX; x++) {
                for (int z = minCZ; z <= maxCZ; z++) {
                    world.loadChunk(x, z, true);
                }
            }
        }
    }
}
