// Region.java
package net.metroCore.Modules.metroedit.region;

import org.bukkit.Location;
import java.util.Iterator;

/**
 * Represents a selectable region in the world.
 * Provides factories for all built-in shapes.
 */
public interface Region extends Iterable<Location> {
    boolean contains(Location loc);
    Iterator<Location> iterator();

    /** Cuboid between two corners. */
    static Region fromCuboid(Location p1, Location p2) {
        return new CuboidRegion(p1, p2);
    }

    /** Sphere centered at `center` with integer radius. */
    static Region fromSphere(Location center, int radius) {
        return new SphereRegion(center, radius);
    }

    /** Cylinder between p1/p2 along Y. */
    static Region fromCylinder(Location p1, Location p2) {
        return new CylinderRegion(p1, p2);
    }

    /** Pyramid with apex at `apex`, base radius and height. */
    static Region fromPyramid(Location apex, int baseRadius, int height) {
        return new PyramidRegion(apex, baseRadius, height);
    }
}
