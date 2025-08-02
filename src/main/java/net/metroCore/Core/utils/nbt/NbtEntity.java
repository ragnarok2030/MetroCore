package net.metroCore.Core.utils.nbt;

import net.metroCore.Core.utils.nbt.adapter.NbtAdapter;
import org.bukkit.Bukkit;
import org.bukkit.entity.Entity;

import java.util.Map;

/**
 * Wrapper for NBT data on a Bukkit Entity.
 * Uses adapter to access native NMS NBT data.
 */
public class NbtEntity {

    private final Entity entity;
    private final NbtAdapter adapter;

    public NbtEntity(Entity entity) {
        this.entity = entity;
        this.adapter = Bukkit.getServicesManager().getRegistration(NbtAdapter.class).getProvider();
    }

    /**
     * Returns the full NBT compound for this entity.
     */
    public Map<String, Object> getCompound() {
        return adapter.getEntityNBT(entity);
    }

    /**
     * Sets a key-value pair in the entity NBT.
     * Note: value type must be supported by adapter.
     */
    public void setTag(String key, Object value) {
        adapter.setEntityNBTTag(entity, key, value);
    }

    /**
     * Checks if entity has the given NBT key.
     */
    public boolean hasTag(String key) {
        return adapter.hasEntityNBTTag(entity, key);
    }

    /**
     * Removes an NBT tag from the entity.
     */
    public void removeTag(String key) {
        adapter.removeEntityNBTTag(entity, key);
    }
}
