package net.metroCore.Core.utils.nbt;

import net.metroCore.Core.utils.nbt.adapter.NbtAdapter;
import net.metroCore.MetroCore;
import org.bukkit.block.BlockState;
import org.bukkit.plugin.Plugin;

import java.util.Map;

/**
 * Wrapper class for NBT data on tile entity blocks (chests, signs, etc).
 * Uses adapter to access native NMS NBT data.
 */
public class NbtBlock {

    private final BlockState blockState;
    private final NbtAdapter adapter;

    public NbtBlock(BlockState blockState) {
        this.blockState = blockState;
        Plugin plugin = MetroCore.getInstance();
        this.adapter = plugin.getServer().getServicesManager().getRegistration(NbtAdapter.class).getProvider();
    }

    /**
     * Returns the full NBT compound for this block’s tile entity.
     */
    public Map<String, Object> getCompound() {
        return adapter.getBlockNBT(blockState);
    }

    /**
     * Sets a key-value pair in the block’s NBT.
     * Note: value type must be supported by adapter.
     */
    public void setTag(String key, Object value) {
        adapter.setBlockNBTTag(blockState, key, value);
    }

    /**
     * Checks if block has the given NBT key.
     */
    public boolean hasTag(String key) {
        return adapter.hasBlockNBTTag(blockState, key);
    }

    /**
     * Removes an NBT tag from the block.
     */
    public void removeTag(String key) {
        adapter.removeBlockNBTTag(blockState, key);
    }
}
