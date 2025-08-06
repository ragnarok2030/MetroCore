// SphereRegion.java
package net.metroCore.Modules.metroedit.region;

import org.bukkit.Location;
import org.bukkit.World;
import java.util.Iterator;

public class SphereRegion implements Region {
    private final World world;
    private final int cx, cy, cz, radius, r2;

    public SphereRegion(Location center, int radius) {
        this.world = center.getWorld();
        this.cx = center.getBlockX();
        this.cy = center.getBlockY();
        this.cz = center.getBlockZ();
        this.radius = radius;
        this.r2 = radius * radius;
    }

    @Override
    public boolean contains(Location loc) {
        if (!loc.getWorld().equals(world)) return false;
        int dx = loc.getBlockX() - cx;
        int dy = loc.getBlockY() - cy;
        int dz = loc.getBlockZ() - cz;
        return dx*dx + dy*dy + dz*dz <= r2;
    }

    @Override
    public Iterator<Location> iterator() {
        return new Iterator<>() {
            private int x = cx - radius, y = cy - radius, z = cz - radius;
            @Override public boolean hasNext() {
                return y <= cy + radius;
            }
            @Override public Location next() {
                Location loc = new Location(world, x, y, z);
                // advance
                if (++x > cx + radius) {
                    x = cx - radius;
                    if (++z > cz + radius) {
                        z = cz - radius;
                        y++;
                    }
                }
                return loc;
            }
        };
    }
}
