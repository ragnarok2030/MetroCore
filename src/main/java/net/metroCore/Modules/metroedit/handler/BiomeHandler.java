package net.metroCore.Modules.metroedit.handler;

import net.metroCore.Modules.metroedit.region.Region;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;

/**
 * Provides biome-related utilities: info, list, set.
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

    /** Change entire region’s biome (if supported by your server). */
    public void setRegionBiome(Region region, org.bukkit.block.Biome biome) {
        for (Location loc : region) {
            World w = loc.getWorld();
            w.setBiome(loc.getBlockX(), loc.getBlockZ(), biome);
        }
    }
}
