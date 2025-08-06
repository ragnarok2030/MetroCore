// in e.g. net.metroCore.Modules.metroedit.util.EditUtil.java
package net.metroCore.Modules.metroedit.util;

import net.metroCore.Modules.metroedit.handler.UndoRedoHandler;
import org.bukkit.Location;
import org.bukkit.block.data.BlockData;
import org.bukkit.entity.Player;

public final class EditUtil {
    private EditUtil() {}

    /**
     * Record + apply a single block change.
     */
    public static void recordAndSet(UndoRedoHandler undo, Player player,
                                    Location loc, BlockData newData) {
        BlockData oldData = loc.getBlock().getBlockData();
        undo.record(player, loc, oldData, newData);
        loc.getBlock().setBlockData(newData);
    }
}
