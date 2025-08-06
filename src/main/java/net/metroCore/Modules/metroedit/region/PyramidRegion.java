// PyramidRegion.java
package net.metroCore.Modules.metroedit.region;

import org.bukkit.Location;
import org.bukkit.World;
import java.util.Iterator;

public class PyramidRegion implements Region {
    private final Location apex;
    private final int baseRadius, height;

    public PyramidRegion(Location apex, int baseRadius, int height) {
        this.apex = apex;
        this.baseRadius = baseRadius;
        this.height = height;
    }

    @Override
    public boolean contains(Location loc) {
        if (!loc.getWorld().equals(apex.getWorld())) return false;
        int dy = loc.getBlockY() - apex.getBlockY();
        if (dy < 0 || dy > height) return false;
        int layerRadius = baseRadius * (height - dy) / height;
        int dx = loc.getBlockX() - apex.getBlockX();
        int dz = loc.getBlockZ() - apex.getBlockZ();
        return dx*dx + dz*dz <= layerRadius*layerRadius;
    }

    @Override
    public Iterator<Location> iterator() {
        return new Iterator<>() {
            private int dy = 0, x = -baseRadius, z = -baseRadius;
            @Override public boolean hasNext() {
                return dy <= height;
            }
            @Override public Location next() {
                int layerRadius = baseRadius * (height - dy) / height;
                Location loc = new Location(
                        apex.getWorld(),
                        apex.getBlockX() + x,
                        apex.getBlockY() + dy,
                        apex.getBlockZ() + z
                );
                if (++x > layerRadius) {
                    x = -layerRadius;
                    if (++z > layerRadius) {
                        z = -layerRadius;
                        dy++;
                    }
                }
                return loc;
            }
        };
    }
}
