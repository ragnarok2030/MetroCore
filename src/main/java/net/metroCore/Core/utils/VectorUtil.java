package net.metroCore.Core.utils;

import org.bukkit.util.Vector;

import java.util.Random;

public final class VectorUtil {
    private VectorUtil() {}

    /** Convert a unit vector to yaw/pitch (degrees). */
    public static float[] toYawPitch(Vector v) {
        Vector norm = v.clone().normalize();
        float yaw   = (float)Math.toDegrees(Math.atan2(-norm.getX(), norm.getZ()));
        float pitch = (float)Math.toDegrees(Math.asin(-norm.getY()));
        return new float[]{ yaw, pitch };
    }

    /** Random unit vector inside a cone around `axis` (degrees). */
    public static Vector randomInCone(Vector axis, double angleDegrees) {
        Random r = new Random();
        double theta = Math.toRadians(angleDegrees) * r.nextDouble();
        double phi   = 2 * Math.PI * r.nextDouble();
        // Spherical coordinates
        double x = Math.sin(theta) * Math.cos(phi);
        double y = Math.cos(theta);
        double z = Math.sin(theta) * Math.sin(phi);
        Vector v = new Vector(x, y, z);
        return v.rotateAroundX(axis.getX())
                .rotateAroundY(axis.getY())
                .rotateAroundZ(axis.getZ());
    }
}
