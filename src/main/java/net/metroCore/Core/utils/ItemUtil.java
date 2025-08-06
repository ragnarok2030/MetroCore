package net.metroCore.Core.utils;

import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

public final class ItemUtil {
    private ItemUtil() {}

    /** Deep clone an ItemStack (including meta). */
    public static ItemStack cloneItem(ItemStack original) {
        if (original == null) return null;
        ItemStack clone = original.clone();
        ItemMeta meta = original.getItemMeta();
        if (meta != null) {
            clone.setItemMeta(meta.clone());
        }
        return clone;
    }

    /** Safely add a lore line (even if none existed). */
    public static void addLore(ItemStack item, String line) {
        ItemMeta meta = item.getItemMeta();
        if (meta == null) return;
        List<String> lore = meta.hasLore() ? new ArrayList<>(meta.getLore()) : new ArrayList<>();
        lore.add(line);
        meta.setLore(lore);
        item.setItemMeta(meta);
    }

    /** Remove all occurrences of this lore text. */
    public static void removeLore(ItemStack item, String line) {
        ItemMeta meta = item.getItemMeta();
        if (meta == null || !meta.hasLore()) return;
        List<String> lore = new ArrayList<>(meta.getLore());
        lore.removeIf(l -> l.equals(line));
        meta.setLore(lore);
        item.setItemMeta(meta);
    }

    /** Merge contents of src into dest (stacks where possible). */
    public static void mergeInventories(Inventory dest, Inventory src) {
        for (ItemStack stack : src.getContents()) {
            if (stack != null) dest.addItem(stack);
        }
    }
}
