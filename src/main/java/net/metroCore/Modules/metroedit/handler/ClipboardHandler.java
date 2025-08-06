// ClipboardHandler.java
package net.metroCore.Modules.metroedit.handler;

import net.metroCore.Modules.metroedit.event.SelectionChangedEvent;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import java.util.*;

/**
 * Captures copy/cut into a per-player clipboard.
 */
public class ClipboardHandler implements Listener {
    private final Map<UUID, Clipboard> clipboards = new HashMap<>();

    @EventHandler
    public void onSelectionChanged(SelectionChangedEvent ev) {
        // clear clipboard when selection changes
        clipboards.remove(ev.getPlayer().getUniqueId());
    }

    /**
     * Copy the currently selected region into the player’s clipboard.
     * We use the region’s minimum corner as the origin.
     */
    public void setClipboard(Player player, Iterable<Location> region) {
        // determine origin (min x,y,z)
        int minX = Integer.MAX_VALUE, minY = Integer.MAX_VALUE, minZ = Integer.MAX_VALUE;
        List<Location> all = new ArrayList<>();
        for (Location loc : region) {
            all.add(loc);
            minX = Math.min(minX, loc.getBlockX());
            minY = Math.min(minY, loc.getBlockY());
            minZ = Math.min(minZ, loc.getBlockZ());
        }
        Location origin = new Location(player.getWorld(), minX, minY, minZ);

        Clipboard cb = new Clipboard(origin);
        for (Location loc : all) {
            cb.put(loc, loc.getBlock().getBlockData());
        }
        clipboards.put(player.getUniqueId(), cb);
        player.sendMessage("§aCopied " + cb.size() + " blocks to clipboard.");
    }

    /**
     * Paste the stored clipboard at the given destination.
     */
    public void paste(Player player, Location dest) {
        Clipboard cb = clipboards.get(player.getUniqueId());
        if (cb == null || cb.isEmpty()) {
            player.sendMessage("§cClipboard is empty.");
            return;
        }
        cb.paste(dest);
        player.sendMessage("§aPasted clipboard at " +
                dest.getBlockX() + "," + dest.getBlockY() + "," + dest.getBlockZ());
    }

    /** Clear this player’s clipboard. */
    public void clearClipboard(Player player) {
        clipboards.remove(player.getUniqueId());
        player.sendMessage("§aClipboard cleared.");
    }

    /** Inner clipboard representation with origin. */
    private static class Clipboard {
        private final Location origin;
        private final Map<Location, org.bukkit.block.data.BlockData> data = new HashMap<>();

        public Clipboard(Location origin) {
            this.origin = origin;
        }

        void put(Location loc, org.bukkit.block.data.BlockData bd) {
            data.put(loc.clone(), bd);
        }

        int size() {
            return data.size();
        }

        boolean isEmpty() {
            return data.isEmpty();
        }

        void paste(Location dest) {
            for (Map.Entry<Location, org.bukkit.block.data.BlockData> e : data.entrySet()) {
                Location from = e.getKey();
                // compute offset from origin
                int dx = from.getBlockX() - origin.getBlockX();
                int dy = from.getBlockY() - origin.getBlockY();
                int dz = from.getBlockZ() - origin.getBlockZ();
                Location to = dest.clone().add(dx, dy, dz);
                to.getBlock().setBlockData(e.getValue());
            }
        }
    }
}
