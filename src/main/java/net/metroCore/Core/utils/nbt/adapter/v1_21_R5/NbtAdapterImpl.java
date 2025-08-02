package net.metroCore.Core.utils.nbt.adapter.v1_21_R5;

import net.metroCore.Core.utils.nbt.NbtCompound;
import net.metroCore.Core.utils.nbt.adapter.NbtAdapter;
import org.bukkit.NamespacedKey;
import org.bukkit.block.BlockState;
import org.bukkit.block.TileState;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Universal NbtAdapter for Minecraft 1.21.8 (v1_21_R5) using only Bukkit PersistentDataContainer.
 * • Items: via NbtCompound wrapper
 * • Entities: via PersistentDataContainer on LivingEntity
 * • Blocks: via PersistentDataContainer on TileState (passed in as Object)
 */
public class NbtAdapterImpl implements NbtAdapter {

    private static final String NAMESPACE = "metrocore";

    // --- ItemStack NBT via NbtCompound ---
    @Override
    public Map<String, Object> getItemNBT(ItemStack item) {
        NbtCompound wrapper = new NbtCompound(item, "");
        Set<String> keys = wrapper.getKeys();
        return keys.stream()
                .collect(Collectors.toMap(k -> k, wrapper::getString));
    }

    @Override
    public void setItemNBTTag(ItemStack item, String key, Object value) {
        new NbtCompound(item, "").setString(key, String.valueOf(value));
    }

    @Override
    public boolean hasItemNBTTag(ItemStack item, String key) {
        return new NbtCompound(item, "").contains(key);
    }

    @Override
    public void removeItemNBTTag(ItemStack item, String key) {
        new NbtCompound(item, "").remove(key);
    }

    // --- Entity NBT via PersistentDataContainer (only LivingEntity) ---
    @Override
    public Map<String, Object> getEntityNBT(Entity entity) {
        if (!(entity instanceof LivingEntity living)) return Map.of();
        PersistentDataContainer pdc = living.getPersistentDataContainer();
        Map<String,Object> map = new HashMap<>();
        pdc.getKeys().stream()
                .filter(ns -> ns.getNamespace().equals(NAMESPACE))
                .forEach(ns -> map.put(ns.getKey(),
                        pdc.get(ns, PersistentDataType.STRING)));
        return map;
    }

    @Override
    public void setEntityNBTTag(Entity entity, String key, Object value) {
        if (!(entity instanceof LivingEntity living)) return;
        living.getPersistentDataContainer()
                .set(new NamespacedKey(NAMESPACE, key),
                        PersistentDataType.STRING,
                        String.valueOf(value));
    }

    @Override
    public boolean hasEntityNBTTag(Entity entity, String key) {
        if (!(entity instanceof LivingEntity living)) return false;
        return living.getPersistentDataContainer()
                .has(new NamespacedKey(NAMESPACE, key),
                        PersistentDataType.STRING);
    }

    @Override
    public void removeEntityNBTTag(Entity entity, String key) {
        if (!(entity instanceof LivingEntity living)) return;
        living.getPersistentDataContainer()
                .remove(new NamespacedKey(NAMESPACE, key));
    }

    // --- Block (tile-entity) NBT via PersistentDataContainer on TileState ---
    @Override
    public Map<String, Object> getBlockNBT(Object tileEntity) {
        if (!(tileEntity instanceof BlockState state)) return Map.of();
        if (!(state instanceof TileState tile)) return Map.of();
        PersistentDataContainer pdc = tile.getPersistentDataContainer();
        Map<String,Object> map = new HashMap<>();
        pdc.getKeys().stream()
                .filter(ns -> ns.getNamespace().equals(NAMESPACE))
                .forEach(ns -> map.put(ns.getKey(),
                        pdc.get(ns, PersistentDataType.STRING)));
        return map;
    }

    @Override
    public void setBlockNBTTag(Object tileEntity, String key, Object value) {
        if (!(tileEntity instanceof BlockState state)) return;
        if (!(state instanceof TileState tile)) return;
        tile.getPersistentDataContainer()
                .set(new NamespacedKey(NAMESPACE, key),
                        PersistentDataType.STRING,
                        String.valueOf(value));
        tile.update();
    }

    @Override
    public boolean hasBlockNBTTag(Object tileEntity, String key) {
        if (!(tileEntity instanceof BlockState state)) return false;
        if (!(state instanceof TileState tile)) return false;
        return tile.getPersistentDataContainer()
                .has(new NamespacedKey(NAMESPACE, key),
                        PersistentDataType.STRING);
    }

    @Override
    public void removeBlockNBTTag(Object tileEntity, String key) {
        if (!(tileEntity instanceof BlockState state)) return;
        if (!(state instanceof TileState tile)) return;
        tile.getPersistentDataContainer()
                .remove(new NamespacedKey(NAMESPACE, key));
        tile.update();
    }
}
