package net.metroCore.Core.utils.nbt;

import org.bukkit.NamespacedKey;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

import java.util.Set;

public class NbtUtils {

    /**
     * Get all keys stored in a PersistentDataContainer.
     */
    public static Set<NamespacedKey> getAllKeys(PersistentDataContainer container) {
        return container.getKeys();
    }

    /**
     * Clone one container into another.
     */
    public static void cloneContainer(PersistentDataContainer source, PersistentDataContainer target) {
        for (NamespacedKey key : source.getKeys()) {
            Object value = source.get(key, getDataType(source, key));
            if (value != null) {
                target.set(key, getDataType(source, key), value);
            }
        }
    }

    /**
     * Determine the PersistentDataType of a given key.
     * Limited due to lack of dynamic type info — only works for known types.
     */
    @SuppressWarnings("unchecked")
    public static <T, Z> PersistentDataType<T, Z> getDataType(PersistentDataContainer container, NamespacedKey key) {
        if (container.has(key, PersistentDataType.STRING)) {
            return (PersistentDataType<T, Z>) PersistentDataType.STRING;
        }
        if (container.has(key, PersistentDataType.INTEGER)) {
            return (PersistentDataType<T, Z>) PersistentDataType.INTEGER;
        }
        if (container.has(key, PersistentDataType.BYTE)) {
            return (PersistentDataType<T, Z>) PersistentDataType.BYTE;
        }
        if (container.has(key, PersistentDataType.DOUBLE)) {
            return (PersistentDataType<T, Z>) PersistentDataType.DOUBLE;
        }
        if (container.has(key, PersistentDataType.LONG)) {
            return (PersistentDataType<T, Z>) PersistentDataType.LONG;
        }
        if (container.has(key, PersistentDataType.FLOAT)) {
            return (PersistentDataType<T, Z>) PersistentDataType.FLOAT;
        }

        return null;
    }

    /**
     * Remove all keys from the PersistentDataContainer.
     */
    public static void clearAll(PersistentDataContainer container) {
        for (NamespacedKey key : container.getKeys()) {
            container.remove(key);
        }
    }
}
