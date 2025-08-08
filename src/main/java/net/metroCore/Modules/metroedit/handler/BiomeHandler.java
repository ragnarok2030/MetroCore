package net.metroCore.Modules.metroedit.handler;

import net.metroCore.Modules.metroedit.region.Region;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;

import java.util.HashMap;
import java.util.Map;

/**
 * Provides biome-related utilities: info, list, set, and smoothing.
 */
public class BiomeHandler implements Listener {

    /** Print biome at each selection pos. */
    public void showBiomeInfo(Player player, Location loc) {
        org.bukkit.block.Biome biome = loc.getBlock().getBiome();
        player.sendMessage("§6Biome at " +
                loc.getBlockX()+","+
                loc.getBlockY()+","+
                loc.getBlockZ()+
                ": §f" + biome);
    }

    /** Fill entire region with a single biome. */
    public void fill(Region region, org.bukkit.block.Biome biome) {
        for (Location loc : region) {
            World w = loc.getWorld();
            w.setBiome(loc.getBlockX(), loc.getBlockZ(), biome);
        }
    }

    /** Smooth biomes within the region by majority vote of neighbors. */
    public void smooth(Region region) {
        Map<String, org.bukkit.block.Biome> orig = new HashMap<>();
        // capture original biomes by X,Z
        for (Location loc : region) {
            String key = loc.getBlockX() + "," + loc.getBlockZ();
            orig.put(key, loc.getBlock().getBiome());
        }
        // apply smoothing
        for (Location loc : region) {
            World w = loc.getWorld();
            Map<org.bukkit.block.Biome, Integer> counts = new HashMap<>();
            for (int dx = -1; dx <= 1; dx++) {
                for (int dz = -1; dz <= 1; dz++) {
                    String key = (loc.getBlockX() + dx) + "," + (loc.getBlockZ() + dz);
                    org.bukkit.block.Biome b = orig.getOrDefault(key, w.getBiome(loc.getBlockX()+dx, loc.getBlockZ()+dz));
                    counts.put(b, counts.getOrDefault(b, 0) + 1);
                }
            }
            // find majority
            org.bukkit.block.Biome best = loc.getBlock().getBiome();
            int max = 0;
            for (Map.Entry<org.bukkit.block.Biome, Integer> e : counts.entrySet()) {
                if (e.getValue() > max) {
                    max = e.getValue();
                    best = e.getKey();
                }
            }
            w.setBiome(loc.getBlockX(), loc.getBlockZ(), best);
        }
    }
}
