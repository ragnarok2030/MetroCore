package net.metroCore.Core.utils.nbt;

import net.metroCore.Core.utils.nbt.types.NbtTag;
import net.metroCore.Core.utils.nbt.types.NbtType;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.persistence.PersistentDataHolder;
import org.bukkit.persistence.PersistentDataAdapterContext;
import org.bukkit.Bukkit;
import org.bukkit.NamespacedKey;

import java.util.HashMap;
import java.util.Map;

public class NbtItem {

    private final ItemStack item;
    private final PersistentDataContainer container;
    private final NamespacedKey baseKey;

    public NbtItem(ItemStack item, String pluginNamespace) {
        this.item = item.clone();
        if (!(item.getItemMeta() instanceof PersistentDataHolder meta)) {
            throw new IllegalArgumentException("Item does not support metadata");
        }

        this.container = meta.getPersistentDataContainer();
        this.baseKey = new NamespacedKey(pluginNamespace, "nbt");
    }

    public ItemStack getItem() {
        return item;
    }

    public boolean hasTag(String key) {
        return container.has(new NamespacedKey(baseKey.getNamespace(), key), PersistentDataType.STRING);
    }

    public void setString(String key, String value) {
        container.set(new NamespacedKey(baseKey.getNamespace(), key), PersistentDataType.STRING, value);
    }

    public void setInteger(String key, int value) {
        container.set(new NamespacedKey(baseKey.getNamespace(), key), PersistentDataType.INTEGER, value);
    }

    public void setBoolean(String key, boolean value) {
        container.set(new NamespacedKey(baseKey.getNamespace(), key), PersistentDataType.BYTE, (byte)(value ? 1 : 0));
    }

    public void setDouble(String key, double value) {
        container.set(new NamespacedKey(baseKey.getNamespace(), key), PersistentDataType.DOUBLE, value);
    }

    public String getString(String key) {
        return container.get(new NamespacedKey(baseKey.getNamespace(), key), PersistentDataType.STRING);
    }

    public int getInteger(String key) {
        return container.getOrDefault(new NamespacedKey(baseKey.getNamespace(), key), PersistentDataType.INTEGER, 0);
    }

    public boolean getBoolean(String key) {
        Byte b = container.get(new NamespacedKey(baseKey.getNamespace(), key), PersistentDataType.BYTE);
        return b != null && b == 1;
    }

    public double getDouble(String key) {
        return container.getOrDefault(new NamespacedKey(baseKey.getNamespace(), key), PersistentDataType.DOUBLE, 0.0);
    }

    public void removeTag(String key) {
        container.remove(new NamespacedKey(baseKey.getNamespace(), key));
    }
}
