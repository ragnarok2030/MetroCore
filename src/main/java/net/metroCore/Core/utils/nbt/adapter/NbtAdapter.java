package net.metroCore.Core.utils.nbt.adapter;

import org.bukkit.entity.Entity;
import org.bukkit.inventory.ItemStack;

import java.util.Map;

/**
 * Interface to abstract Minecraft native NBT operations,
 * allowing version-specific implementations.
 */
public interface NbtAdapter {

    // ItemStack NBT

    /**
     * Get the full NBT compound data for an ItemStack.
     * @param item the ItemStack
     * @return a map representing the NBT compound keys and values
     */
    Map<String, Object> getItemNBT(ItemStack item);

    /**
     * Set a key-value pair in the ItemStack's NBT compound.
     * @param item the ItemStack to modify
     * @param key the NBT key
     * @param value the value (supported types)
     */
    void setItemNBTTag(ItemStack item, String key, Object value);

    /**
     * Check if the ItemStack has a specific NBT key.
     */
    boolean hasItemNBTTag(ItemStack item, String key);

    /**
     * Remove an NBT key from the ItemStack.
     */
    void removeItemNBTTag(ItemStack item, String key);

    // Entity NBT

    /**
     * Get the full NBT compound data for an Entity.
     * @param entity the Bukkit Entity
     * @return a map representing the NBT compound keys and values
     */
    Map<String, Object> getEntityNBT(Entity entity);

    /**
     * Set a key-value pair in the Entity's NBT compound.
     */
    void setEntityNBTTag(Entity entity, String key, Object value);

    /**
     * Check if the Entity has a specific NBT key.
     */
    boolean hasEntityNBTTag(Entity entity, String key);

    /**
     * Remove an NBT key from the Entity.
     */
    void removeEntityNBTTag(Entity entity, String key);

    // Tile Entity / Block NBT

    /**
     * Get the full NBT compound data for a tile entity block (chest, sign, etc).
     */
    Map<String, Object> getBlockNBT(Object tileEntity);

    /**
     * Set a key-value pair in the tile entity's NBT compound.
     */
    void setBlockNBTTag(Object tileEntity, String key, Object value);

    /**
     * Check if the tile entity has a specific NBT key.
     */
    boolean hasBlockNBTTag(Object tileEntity, String key);

    /**
     * Remove an NBT key from the tile entity.
     */
    void removeBlockNBTTag(Object tileEntity, String key);
}
