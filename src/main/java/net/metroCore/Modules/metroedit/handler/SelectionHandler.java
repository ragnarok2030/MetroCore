package net.metroCore.Modules.metroedit.handler;

import net.metroCore.Modules.metroedit.event.SelectionChangedEvent;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

public class SelectionHandler implements Listener {

    private static final Material WAND_TOOL = Material.WOODEN_AXE;

    // Selection positions per player
    private final Map<UUID, Location> pos1 = new HashMap<>();
    private final Map<UUID, Location> pos2 = new HashMap<>();
    // Edit mode per player
    private final Map<UUID, Boolean> editMode = new HashMap<>();

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent ev) {
        if (ev.getHand() != EquipmentSlot.HAND) return;
        Action action = ev.getAction();
        if (action != Action.LEFT_CLICK_BLOCK && action != Action.RIGHT_CLICK_BLOCK) return;
        if (ev.getItem() == null || ev.getItem().getType() != WAND_TOOL) return;

        Player player = ev.getPlayer();
        Location clicked = ev.getClickedBlock().getLocation().clone();

        // If in edit mode, skip selection
        if (editMode.getOrDefault(player.getUniqueId(), false)) return;

        boolean changed = false;
        if (action == Action.LEFT_CLICK_BLOCK) {
            setPos1(player, clicked);
            player.sendMessage(ChatColor.GREEN + "Position 1 set to " + format(clicked));
            changed = true;
        } else if (action == Action.RIGHT_CLICK_BLOCK) {
            setPos2(player, clicked);
            player.sendMessage(ChatColor.GREEN + "Position 2 set to " + format(clicked));
            changed = true;
        }

        if (changed) {
            ev.setCancelled(true);
            player.getServer().getPluginManager().callEvent(
                    new SelectionChangedEvent(player, getPos1(player), getPos2(player))
            );
        }
    }

    /** Public setter for position 1 */
    public void setPos1(Player player, Location loc) {
        pos1.put(player.getUniqueId(), loc.clone());
    }

    /** Public setter for position 2 */
    public void setPos2(Player player, Location loc) {
        pos2.put(player.getUniqueId(), loc.clone());
    }

    /** Clear both positions for this player. */
    public void clearSelection(Player player) {
        UUID id = player.getUniqueId();
        pos1.remove(id);
        pos2.remove(id);
    }

    /** Toggle wand edit mode; returns new mode state. */
    public boolean toggleEditMode(Player player) {
        UUID id = player.getUniqueId();
        boolean newMode = !editMode.getOrDefault(id, false);
        editMode.put(id, newMode);
        return newMode;
    }

    /** Ensure pos1 <= pos2 on all axes. */
    public void trim(Player player) {
        Optional<Location> a = getPos1(player);
        Optional<Location> b = getPos2(player);
        if (a.isEmpty() || b.isEmpty()) return;
        Location p1 = a.get();
        Location p2 = b.get();
        int minX = Math.min(p1.getBlockX(), p2.getBlockX());
        int minY = Math.min(p1.getBlockY(), p2.getBlockY());
        int minZ = Math.min(p1.getBlockZ(), p2.getBlockZ());
        int maxX = Math.max(p1.getBlockX(), p2.getBlockX());
        int maxY = Math.max(p1.getBlockY(), p2.getBlockY());
        int maxZ = Math.max(p1.getBlockZ(), p2.getBlockZ());
        pos1.put(player.getUniqueId(), new Location(p1.getWorld(), minX, minY, minZ));
        pos2.put(player.getUniqueId(), new Location(p1.getWorld(), maxX, maxY, maxZ));
    }

    /** Shift both positions by dx, dy, dz. */
    public void shift(Player player, int dx, int dy, int dz) {
        getPos1(player).ifPresent(loc -> setPos1(player, loc.clone().add(dx, dy, dz)));
        getPos2(player).ifPresent(loc -> setPos2(player, loc.clone().add(dx, dy, dz)));
    }

    /** Expand selection by 1 block on each face. */
    public void outset(Player player) {
        Optional<Location> a = getPos1(player);
        Optional<Location> b = getPos2(player);
        if (a.isEmpty() || b.isEmpty()) return;
        Location p1 = a.get();
        Location p2 = b.get();
        setPos1(player, new Location(p1.getWorld(), p1.getBlockX()-1, p1.getBlockY()-1, p1.getBlockZ()-1));
        setPos2(player, new Location(p2.getWorld(), p2.getBlockX()+1, p2.getBlockY()+1, p2.getBlockZ()+1));
    }

    /** Shrink selection by 1 block on each face. */
    public void contract(Player player) {
        Optional<Location> a = getPos1(player);
        Optional<Location> b = getPos2(player);
        if (a.isEmpty() || b.isEmpty()) return;
        Location p1 = a.get();
        Location p2 = b.get();
        setPos1(player, new Location(p1.getWorld(), p1.getBlockX()+1, p1.getBlockY()+1, p1.getBlockZ()+1));
        setPos2(player, new Location(p2.getWorld(), p2.getBlockX()-1, p2.getBlockY()-1, p2.getBlockZ()-1));
    }

    /** Alias for shrink (inset). */
    public void inset(Player player) {
        contract(player);
    }

    /** Returns inclusive size [dx, dy, dz] of the selection. */
    public int[] size(Player player) {
        Optional<Location> a = getPos1(player);
        Optional<Location> b = getPos2(player);
        if (a.isEmpty() || b.isEmpty()) return new int[]{0,0,0};
        Location p1 = a.get();
        Location p2 = b.get();
        int dx = Math.abs(p2.getBlockX() - p1.getBlockX()) + 1;
        int dy = Math.abs(p2.getBlockY() - p1.getBlockY()) + 1;
        int dz = Math.abs(p2.getBlockZ() - p1.getBlockZ()) + 1;
        return new int[]{dx, dy, dz};
    }

    /** Count non-air blocks in the selection. */
    public int count(Player player) {
        Optional<Location> a = getPos1(player);
        Optional<Location> b = getPos2(player);
        if (a.isEmpty() || b.isEmpty()) return 0;
        Location p1 = a.get();
        Location p2 = b.get();
        int minX = Math.min(p1.getBlockX(), p2.getBlockX());
        int minY = Math.min(p1.getBlockY(), p2.getBlockY());
        int minZ = Math.min(p1.getBlockZ(), p2.getBlockZ());
        int maxX = Math.max(p1.getBlockX(), p2.getBlockX());
        int maxY = Math.max(p1.getBlockY(), p2.getBlockY());
        int maxZ = Math.max(p1.getBlockZ(), p2.getBlockZ());
        int total = 0;
        for (int x = minX; x <= maxX; x++) {
            for (int y = minY; y <= maxY; y++) {
                for (int z = minZ; z <= maxZ; z++) {
                    if (p1.getWorld().getBlockAt(x,y,z).getType() != Material.AIR) {
                        total++;
                    }
                }
            }
        }
        return total;
    }

    /** Get position 1 for this player, if set. */
    public Optional<Location> getPos1(Player player) {
        return Optional.ofNullable(pos1.get(player.getUniqueId()));
    }

    /** Get position 2 for this player, if set. */
    public Optional<Location> getPos2(Player player) {
        return Optional.ofNullable(pos2.get(player.getUniqueId()));
    }

    private String format(Location loc) {
        return String.format("(%d, %d, %d)", loc.getBlockX(), loc.getBlockY(), loc.getBlockZ());
    }
}
