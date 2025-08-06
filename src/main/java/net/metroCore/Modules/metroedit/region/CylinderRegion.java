package net.metroCore.Modules.metroedit.region;

import org.bukkit.Location;
import java.util.Iterator;

public class CylinderRegion implements Region {
    private final SphereRegion horizontal;
    private final int y1, y2;

    public CylinderRegion(Location p1, Location p2) {
        this.horizontal = new SphereRegion(
                new Location(p1.getWorld(), p1.getBlockX(), 0, p1.getBlockZ()),
                Math.max(Math.abs(p1.getBlockX() - p2.getBlockX()), Math.abs(p1.getBlockZ() - p2.getBlockZ()))
        );
        this.y1 = Math.min(p1.getBlockY(), p2.getBlockY());
        this.y2 = Math.max(p1.getBlockY(), p2.getBlockY());
    }

    @Override
    public boolean contains(Location loc) {
        return horizontal.contains(new Location(loc.getWorld(), loc.getBlockX(), 0, loc.getBlockZ()))
                && loc.getBlockY() >= y1 && loc.getBlockY() <= y2;
    }

    @Override
    public Iterator<Location> iterator() {
        return new Iterator<>() {
            private Iterator<Location> horiz = horizontal.iterator();
            private int cy = y1;
            private Location base;

            @Override
            public boolean hasNext() {
                return horiz.hasNext() || cy < y2;
            }

            @Override
            public Location next() {
                if (base == null || !horiz.hasNext()) {
                    horiz = horizontal.iterator();
                    base = horiz.next();
                    cy++;
                } else {
                    base = horiz.next();
                }
                return new Location(
                        base.getWorld(),
                        base.getBlockX(), cy, base.getBlockZ()
                );
            }
        };
    }
}
