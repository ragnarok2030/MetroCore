package net.metroCore.Core.utils;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Block;

import java.util.HashSet;
import java.util.Set;

public final class LocationUtil {
    private LocationUtil() {}

    /** Euclidean distance on X/Z plane. */
    public static double distance2D(Location a, Location b) {
        double dx = a.getX() - b.getX();
        double dz = a.getZ() - b.getZ();
        return Math.sqrt(dx * dx + dz * dz);
    }

    /** Center point between two corners. */
    public static Location getCenter(Location a, Location b) {
        World w = a.getWorld();
        double x = (a.getX() + b.getX()) / 2.0;
        double y = (a.getY() + b.getY()) / 2.0;
        double z = (a.getZ() + b.getZ()) / 2.0;
        return new Location(w, x, y, z);
    }

    /** All blocks within a sphere radius (inclusive). */
    public static Set<Block> blocksInRadius(Location center, double radius) {
        Set<Block> blocks = new HashSet<>();
        World world = center.getWorld();
        int cx = center.getBlockX();
        int cy = center.getBlockY();
        int cz = center.getBlockZ();
        int r = (int)Math.ceil(radius);
        double radiusSq = radius * radius;
        for (int x = cx - r; x <= cx + r; x++) {
            for (int y = cy - r; y <= cy + r; y++) {
                for (int z = cz - r; z <= cz + r; z++) {
                    double dx = cx - x;
                    double dy = cy - y;
                    double dz = cz - z;
                    if (dx*dx + dy*dy + dz*dz <= radiusSq) {
                        blocks.add(world.getBlockAt(x, y, z));
                    }
                }
            }
        }
        return blocks;
    }
}
