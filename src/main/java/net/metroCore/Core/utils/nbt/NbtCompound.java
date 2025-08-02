package net.metroCore.Core.utils.nbt;

import org.bukkit.NamespacedKey;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Locale;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Represents a nested NBT compound tag for an ItemStack or other container.
 * Keys are lowercased to satisfy Bukkit constraints.
 */
public class NbtCompound {

    private final ItemStack item;
    private final ItemMeta meta;
    private final PersistentDataContainer container;
    private final String basePath;

    public NbtCompound(ItemStack item, String basePath) {
        this.item = item;
        this.meta = item.getItemMeta();
        if (meta == null) {
            throw new IllegalArgumentException("ItemMeta is null");
        }
        this.container = meta.getPersistentDataContainer();
        if (basePath == null || basePath.isEmpty()) {
            this.basePath = "";
        } else {
            this.basePath = basePath.endsWith(".") ? basePath : basePath + ".";
        }
    }

    private NamespacedKey makeKey(String key) {
        String full = (basePath + key).toLowerCase(Locale.ROOT);
        String namespace = item.getType().getKey().getNamespace();
        return new NamespacedKey(namespace, full);
    }

    public void setString(String key, String value) {
        NamespacedKey ns = makeKey(key);
        container.set(ns, PersistentDataType.STRING, value);
        item.setItemMeta(meta);
    }

    public String getString(String key) {
        NamespacedKey ns = makeKey(key);
        return container.get(ns, PersistentDataType.STRING);
    }

    public void remove(String key) {
        String full = (basePath + key).toLowerCase(Locale.ROOT);
        Set<NamespacedKey> toRemove = container.getKeys().stream()
                .filter(ns -> ns.getKey().startsWith(full))
                .collect(Collectors.toSet());
        toRemove.forEach(container::remove);
        item.setItemMeta(meta);
    }

    public boolean contains(String key) {
        NamespacedKey ns = makeKey(key);
        return container.has(ns, PersistentDataType.STRING);
    }

    public Set<String> getKeys() {
        String prefix = basePath.toLowerCase(Locale.ROOT);
        return container.getKeys().stream()
                .map(NamespacedKey::getKey)
                .filter(k -> k.startsWith(prefix))
                .map(k -> k.substring(prefix.length()))
                .collect(Collectors.toSet());
    }
}
