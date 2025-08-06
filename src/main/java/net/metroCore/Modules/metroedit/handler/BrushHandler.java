package net.metroCore.Modules.metroedit.handler;

import net.metroCore.Core.utils.VectorUtil;
import net.metroCore.Modules.metroedit.region.Region;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

/**
 * A simple “brush” that applies an operation in a radius around the player’s eye.
 */
public class BrushHandler {

    /**
     * Applies the given block material in a spherical brush around the player.
     */
    public void applyBrush(Player player, double radius, org.bukkit.Material material) {
        Location eye = player.getEyeLocation();
        for (Location loc : Region.fromSphere(eye, (int)Math.ceil(radius))) {
            if (loc.distanceSquared(eye) <= radius * radius) {
                loc.getBlock().setType(material);
            }
        }
    }

    /**
     * Shoots a brush “ray” from the eye, placing blocks along the line.
     */
    public void sprayLine(Player player, double maxDistance, org.bukkit.Material material) {
        Location eye = player.getEyeLocation();
        Vector dir = eye.getDirection().normalize();
        for (int i = 1; i <= maxDistance; i++) {
            Location loc = eye.clone().add(dir.clone().multiply(i));
            loc.getBlock().setType(material);
        }
    }
}
