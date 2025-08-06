// CuboidRegion.java
package net.metroCore.Modules.metroedit.region;

import org.bukkit.Location;
import org.bukkit.World;
import java.util.Iterator;

public class CuboidRegion implements Region {
    private final World world;
    private final int x1, y1, z1, x2, y2, z2;

    public CuboidRegion(Location p1, Location p2) {
        this.world = p1.getWorld();
        this.x1 = Math.min(p1.getBlockX(), p2.getBlockX());
        this.y1 = Math.min(p1.getBlockY(), p2.getBlockY());
        this.z1 = Math.min(p1.getBlockZ(), p2.getBlockZ());
        this.x2 = Math.max(p1.getBlockX(), p2.getBlockX());
        this.y2 = Math.max(p1.getBlockY(), p2.getBlockY());
        this.z2 = Math.max(p1.getBlockZ(), p2.getBlockZ());
    }

    @Override
    public boolean contains(Location loc) {
        if (!loc.getWorld().equals(world)) return false;
        int x = loc.getBlockX(), y = loc.getBlockY(), z = loc.getBlockZ();
        return x >= x1 && x <= x2 && y >= y1 && y <= y2 && z >= z1 && z <= z2;
    }

    @Override
    public Iterator<Location> iterator() {
        return new Iterator<>() {
            private int cx = x1, cy = y1, cz = z1;
            @Override public boolean hasNext() {
                return cx <= x2 && cy <= y2 && cz <= z2;
            }
            @Override public Location next() {
                Location loc = new Location(world, cx, cy, cz);
                if (++cz > z2) {
                    cz = z1;
                    if (++cx > x2) {
                        cx = x1;
                        cy++;
                    }
                }
                return loc;
            }
        };
    }
}
