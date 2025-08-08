package net.metroCore.Modules.metroedit.handler;

import net.metroCore.Modules.metroedit.event.SelectionChangedEvent;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import java.util.*;

/**
 * Captures copy/cut into a per-player clipboard and exposes it for undo.
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
        int minX = Integer.MAX_VALUE, minY = Integer.MAX_VALUE, minZ = Integer.MAX_VALUE;
        List<Location> all = new ArrayList<>();
        for (Location loc : region) {
            all.add(loc.clone());
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
     * Internal paste without undo; for compatibility.
     */
    public void paste(Player player, Location dest) {
        getClipboard(player).ifPresentOrElse(cb -> cb.paste(dest),
                () -> player.sendMessage("§cClipboard is empty."));
    }

    /**
     * Returns an Optional of the raw Clipboard for undo-handling.
     */
    public Optional<Clipboard> getClipboard(Player player) {
        return Optional.ofNullable(clipboards.get(player.getUniqueId()));
    }

    /** Clear this player’s clipboard. */
    public void clearClipboard(Player player) {
        clipboards.remove(player.getUniqueId());
        player.sendMessage("§aClipboard cleared.");
    }

    /** Inner clipboard representation with origin. */
    public static class Clipboard {
        private final Location origin;
        private final Map<Location, org.bukkit.block.data.BlockData> data = new LinkedHashMap<>();

        public Clipboard(Location origin) {
            this.origin = origin.clone();
        }

        void put(Location loc, org.bukkit.block.data.BlockData bd) {
            data.put(loc.clone(), bd);
        }

        public Location getOrigin() {
            return origin.clone();
        }

        public Set<Map.Entry<Location, org.bukkit.block.data.BlockData>> getEntries() {
            return Collections.unmodifiableSet(data.entrySet());
        }

        public int size() {
            return data.size();
        }

        public boolean isEmpty() {
            return data.isEmpty();
        }

        /**
         * Basic paste without undo.
         */
        public void paste(Location dest) {
            for (Map.Entry<Location, org.bukkit.block.data.BlockData> e : data.entrySet()) {
                Location from = e.getKey();
                int dx = from.getBlockX() - origin.getBlockX();
                int dy = from.getBlockY() - origin.getBlockY();
                int dz = from.getBlockZ() - origin.getBlockZ();
                Location to = dest.clone().add(dx, dy, dz);
                to.getBlock().setBlockData(e.getValue());
            }
        }
    }
}