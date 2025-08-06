// src/main/java/net/metroCore/Modules/metroedit/util/SchematicUtil.java
package net.metroCore.Modules.metroedit.util;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Block;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Very simple schematic IO: capture and apply rectangular cuboid snapshots.
 */
public final class SchematicUtil {
    private SchematicUtil() {}

    /**
     * Capture a cuboid region between p1 and p2 into a lightweight schematic map.
     * Returns a map from relative (dx,dy,dz) to Material name.
     */
    public static Map<String, String> captureCuboid(Location p1, Location p2) {
        Map<String, String> snap = new HashMap<>();
        int x1 = Math.min(p1.getBlockX(), p2.getBlockX());
        int y1 = Math.min(p1.getBlockY(), p2.getBlockY());
        int z1 = Math.min(p1.getBlockZ(), p2.getBlockZ());
        int x2 = Math.max(p1.getBlockX(), p2.getBlockX());
        int y2 = Math.max(p1.getBlockY(), p2.getBlockY());
        int z2 = Math.max(p1.getBlockZ(), p2.getBlockZ());
        World w = p1.getWorld();
        for (int x = x1; x <= x2; x++) {
            for (int y = y1; y <= y2; y++) {
                for (int z = z1; z <= z2; z++) {
                    Block b = w.getBlockAt(x, y, z);
                    String key = (x - x1) + "," + (y - y1) + "," + (z - z1);
                    snap.put(key, b.getType().name());
                }
            }
        }
        return snap;
    }

    /**
     * Apply a captured snapshot at a target origin location.
     */
    public static void applyCuboid(Location origin, Map<String, String> snapshot) {
        World w = origin.getWorld();
        String[] parts;
        for (Map.Entry<String, String> e : snapshot.entrySet()) {
            parts = e.getKey().split(",");
            int dx = Integer.parseInt(parts[0]);
            int dy = Integer.parseInt(parts[1]);
            int dz = Integer.parseInt(parts[2]);
            Block b = w.getBlockAt(
                    origin.getBlockX() + dx,
                    origin.getBlockY() + dy,
                    origin.getBlockZ() + dz
            );
            b.setType(org.bukkit.Material.valueOf(e.getValue()));
        }
    }

    /**
     * Save schematic map to file (basic key=value per line).
     */
    public static void saveToFile(Map<String, String> snapshot, File file) throws IOException {
        try (java.io.PrintWriter out = new java.io.PrintWriter(file)) {
            for (Map.Entry<String, String> e : snapshot.entrySet()) {
                out.println(e.getKey() + "=" + e.getValue());
            }
        }
    }

    /**
     * Load schematic map from file (key=value per line).
     */
    public static Map<String, String> loadFromFile(File file) throws IOException {
        Map<String, String> snap = new HashMap<>();
        try (java.io.BufferedReader in = new java.io.BufferedReader(new java.io.FileReader(file))) {
            String line;
            while ((line = in.readLine()) != null) {
                int idx = line.indexOf('=');
                if (idx > 0) {
                    String key = line.substring(0, idx);
                    String val = line.substring(idx+1);
                    snap.put(key, val);
                }
            }
        }
        return snap;
    }
}
