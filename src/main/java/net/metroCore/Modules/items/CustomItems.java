package net.metroCore.Modules.items;

import net.metroCore.Core.utils.nbt.NbtCompound;
import net.metroCore.Core.utils.nbt.NbtConstants;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.List;

/**
 * All custom “gun”, “ammo” and “melee” items for your module,
 * using NbtCompound for NBT storage.
 */
public class CustomItems {

    public enum GunType {
        GLOCK_17("Glock 17", Material.GOLDEN_HOE, AmmoType.CALIBER_9MM, 600, 17, 0),
        FN_FIVE_SEVEN("FN Five-seven", Material.GOLDEN_HOE, AmmoType.CALIBER_5_7MM, 600, 20, 0),
        // ... (other guns) ...
        PUMP_ACTION_SHOTGUN("Pump-action Shotgun", Material.GOLDEN_HOE, AmmoType.CALIBER_12_GAUGE, 100, 8, 0);

        private final String   name;
        private final Material material;
        private final AmmoType ammoType;
        private final int      rateOfFire;
        private final int      magazineSize;
        private final int      customModelData;

        GunType(String name, Material material, AmmoType ammoType,
                int rateOfFire, int magazineSize, int customModelData)
        {
            this.name            = name;
            this.material        = material;
            this.ammoType        = ammoType;
            this.rateOfFire      = rateOfFire;
            this.magazineSize    = magazineSize;
            this.customModelData = customModelData;
        }

        public String   getName()         { return name; }
        public Material getMaterial()     { return material; }
        public AmmoType getAmmoType()     { return ammoType; }
        public int      getRateOfFire()   { return rateOfFire; }
        public int      getMagazineSize() { return magazineSize; }

        public ItemStack getItemStack() {
            ItemStack item = new ItemStack(material);
            ItemMeta meta = item.getItemMeta();
            if (meta != null) {
                meta.setCustomModelData(customModelData);
                meta.setDisplayName(ChatColor.GRAY + name);
                meta.setLore(List.of(
                        ChatColor.YELLOW + "Ammo Type: " + ChatColor.WHITE + ammoType.getDescription()
                ));
                item.setItemMeta(meta);

                // Write initial NBT
                NbtCompound nbt = new NbtCompound(item, "");
                nbt.setString("gunType", name());
                nbt.setString("gun", "true");
                nbt.setString("currentAmmo", String.valueOf(magazineSize));
            }
            return item;
        }
    }

    public enum AmmoType {
        CALIBER_9MM(9.0, "9mm", Material.IRON_NUGGET, 6, 0),
        CALIBER_5_7MM(5.7, "5.7mm", Material.IRON_NUGGET, 10, 0),
        // ... (other ammo types) ...
        CALIBER_12_GAUGE(12.0, "12 gauge", Material.IRON_NUGGET, 10, 0);

        private final double   caliber;
        private final String   description;
        private final Material material;
        private final int      damage;
        private final int      customModelData;

        AmmoType(double caliber, String description, Material material,
                 int damage, int customModelData)
        {
            this.caliber         = caliber;
            this.description     = description;
            this.material        = material;
            this.damage          = damage;
            this.customModelData = customModelData;
        }

        public double   getCaliber()     { return caliber; }
        public String   getDescription() { return description; }
        public Material getMaterial()    { return material; }
        public int      getDamage()      { return damage; }

        public ItemStack getItemStack() {
            ItemStack item = new ItemStack(material);
            ItemMeta meta = item.getItemMeta();
            if (meta != null) {
                meta.setCustomModelData(customModelData);
                meta.setDisplayName(ChatColor.GRAY + description);
                item.setItemMeta(meta);

                NbtCompound nbt = new NbtCompound(item, "");
                nbt.setString("ammoType", name());
                nbt.setString("ammo", "true");
            }
            return item;
        }
    }

    public enum MeleeWeapon {
        BASEBALL_BAT("Baseball Bat", Material.IRON_HOE, 16, 100),
        CROWBAR("Crowbar", Material.IRON_HOE, 15, 0),
        // ... (other melee) ...
        KATANA("Katana", Material.IRON_HOE, 19, 0);

        private final String   name;
        private final Material material;
        private final int      damage;
        private final int      customModelData;

        MeleeWeapon(String name, Material material, int damage, int customModelData) {
            this.name            = name;
            this.material        = material;
            this.damage          = damage;
            this.customModelData = customModelData;
        }

        public String   getName()     { return name; }
        public Material getMaterial() { return material; }
        public int      getDamage()   { return damage; }

        public ItemStack getItemStack() {
            ItemStack item = new ItemStack(material);
            ItemMeta meta = item.getItemMeta();
            if (meta != null) {
                meta.setCustomModelData(customModelData);
                meta.setDisplayName(ChatColor.GRAY + name);
                item.setItemMeta(meta);

                NbtCompound nbt = new NbtCompound(item, "");
                nbt.setString("weapon", "true");
                nbt.setString("weaponType", name());
            }
            return item;
        }
    }
}
