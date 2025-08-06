package net.metroCore.Modules.metroedit.handler;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.block.data.BlockData;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;

import java.util.*;

/**
 * Tracks undo/redo stacks per player, grouping multi-block edits
 * into single undo steps.
 */
public class UndoRedoHandler implements Listener {

    // common Change interface
    private interface Change {
        void applyUndo();
        void applyRedo();
    }

    // single-block change
    public static class BlockChange implements Change {
        private final Location loc;
        private final BlockData before, after;

        public BlockChange(Location loc, BlockData before, BlockData after) {
            this.loc    = loc;
            this.before = before;
            this.after  = after;
        }

        @Override
        public void applyUndo() {
            loc.getBlock().setBlockData(before);
        }

        @Override
        public void applyRedo() {
            loc.getBlock().setBlockData(after);
        }
    }

    // bulk change groups many BlockChanges into one undo step
    private static class BulkChange implements Change {
        private final List<BlockChange> changes;

        BulkChange(List<BlockChange> changes) {
            // make a defensive copy
            this.changes = new ArrayList<>(changes);
        }

        @Override
        public void applyUndo() {
            // undo in reverse order
            ListIterator<BlockChange> it = changes.listIterator(changes.size());
            while (it.hasPrevious()) {
                it.previous().applyUndo();
            }
        }

        @Override
        public void applyRedo() {
            // redo in original order
            for (BlockChange c : changes) {
                c.applyRedo();
            }
        }
    }

    private final Map<UUID, Deque<Change>> undoStacks = new HashMap<>();
    private final Map<UUID, Deque<Change>> redoStacks = new HashMap<>();

    /**
     * Record a single‐block change as an undo step.
     */
    public void record(Player player, Location loc, BlockData before, BlockData after) {
        recordBulk(player, Collections.singletonList(new BlockChange(loc.clone(), before, after)));
    }

    /**
     * Record a whole list of block changes as one undo step.
     */
    public void recordBulk(Player player, List<BlockChange> blockChanges) {
        UUID id = player.getUniqueId();
        Deque<Change> stack = undoStacks.computeIfAbsent(id, k -> new ArrayDeque<>());
        stack.push(new BulkChange(blockChanges));
        // clear redo history on new edit
        redoStacks.remove(id);
    }

    /**
     * Undo the last grouped change.
     */
    public void undo(Player player) {
        UUID id = player.getUniqueId();
        Deque<Change> stack = undoStacks.get(id);
        if (stack == null || stack.isEmpty()) {
            player.sendMessage(ChatColor.RED + "Nothing to undo.");
            return;
        }
        Change c = stack.pop();
        c.applyUndo();
        redoStacks
                .computeIfAbsent(id, k -> new ArrayDeque<>())
                .push(c);
        player.sendMessage(ChatColor.GREEN + "Undid last change.");
    }

    /**
     * Redo the last undone change.
     */
    public void redo(Player player) {
        UUID id = player.getUniqueId();
        Deque<Change> stack = redoStacks.get(id);
        if (stack == null || stack.isEmpty()) {
            player.sendMessage(ChatColor.RED + "Nothing to redo.");
            return;
        }
        Change c = stack.pop();
        c.applyRedo();
        undoStacks
                .computeIfAbsent(id, k -> new ArrayDeque<>())
                .push(c);
        player.sendMessage(ChatColor.GREEN + "Redid last change.");
    }

    /**
     * Clear both undo & redo history for this player.
     */
    public void clearHistory(Player player) {
        UUID id = player.getUniqueId();
        undoStacks.remove(id);
        redoStacks.remove(id);
        player.sendMessage(ChatColor.GREEN + "Undo/redo history cleared.");
    }
}
