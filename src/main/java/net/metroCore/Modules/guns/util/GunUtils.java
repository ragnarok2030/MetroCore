package net.metroCore.Modules.guns.util;

import net.metroCore.Core.utils.nbt.NbtCompound;
import net.metroCore.Core.utils.nbt.NbtConstants;
import net.metroCore.Modules.items.CustomItems;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.entity.Snowball;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.util.Vector;

import java.util.List;
import java.util.Optional;

import static net.metroCore.MetroCore.getInstance;

public final class GunUtils {
    private GunUtils() {}

    // NBT keys
    private static final String GUN_TAG       = "gun";
    private static final String GUN_TYPE_TAG  = "gunType";
    private static final String AMMO_TAG      = "currentAmmo";
    private static final String DAMAGE_TAG    = "damage";

    /** Returns true if this ItemStack is marked as a gun. */
    public static boolean isGun(ItemStack item) {
        if (item == null) return false;
        var n = new NbtCompound(item, "");
        return "true".equals(n.getString(GUN_TAG));
    }

    /** Reads the GunType from the item’s NBT, if present. */
    public static Optional<CustomItems.GunType> getGunType(ItemStack item) {
        var n = new NbtCompound(item, "");
        String name = n.getString(GUN_TYPE_TAG);
        if (name == null) return Optional.empty();
        try {
            return Optional.of(CustomItems.GunType.valueOf(name));
        } catch (IllegalArgumentException e) {
            return Optional.empty();
        }
    }

    /** Reads current ammo (or zero if missing). */
    public static int getAmmo(ItemStack item) {
        var n = new NbtCompound(item, "");
        String s = n.getString(AMMO_TAG);
        try { return Integer.parseInt(s); }
        catch (Exception e) { return 0; }
    }

    /** Sets current ammo in the item’s NBT. */
    public static void setAmmo(ItemStack item, int amount) {
        new NbtCompound(item, "").setString(AMMO_TAG, String.valueOf(amount));
    }

    /** Finds one ammo stack in the player’s inventory for this ammo type. */
    public static ItemStack findAndConsumeAmmo(Player p, CustomItems.GunType type) {
        for (ItemStack stack : p.getInventory().getContents()) {
            if (stack == null) continue;
            if (stack.getType() != type.getAmmoType().getMaterial()) continue;
            var n = new NbtCompound(stack, "");
            if ("true".equals(n.getString("ammo"))
                    && type.getAmmoType().name().equals(n.getString("ammoType"))
                    && stack.getAmount() > 0) {
                stack.setAmount(stack.getAmount() - 1);
                return stack;
            }
        }
        return null;
    }

    /** Updates the lore line that shows “Ammo: X/Y”. */
    public static void updateAmmoLore(ItemStack gun) {
        Optional<CustomItems.GunType> ot = getGunType(gun);
        if (ot.isEmpty()) return;
        CustomItems.GunType type = ot.get();
        int current = getAmmo(gun);
        ItemMeta meta = gun.getItemMeta();
        if (meta == null) return;
        List<String> lore = meta.getLore();
        if (lore == null || lore.size() < 2) return;
        lore.set(1,
                ChatColor.GREEN + "Ammo: " +
                        ChatColor.WHITE + current +
                        ChatColor.GRAY  + "/" + type.getMagazineSize()
        );
        meta.setLore(lore);
        gun.setItemMeta(meta);
    }

    /** Applies NBT and physics to a snowball projectile. */
    public static Snowball shootSnowball(Player shooter, Vector direction, CustomItems.GunType type) {
        Snowball sb = shooter.launchProjectile(Snowball.class, direction);
        sb.setGravity(false);
        sb.setInvisible(true);
        sb.getPersistentDataContainer().set(NbtConstants.key(GUN_TYPE_TAG),
                PersistentDataType.STRING, type.name());
        sb.getPersistentDataContainer().set(NbtConstants.key(DAMAGE_TAG),
                PersistentDataType.INTEGER, type.getAmmoType().getDamage());

        // simple gravity task
        new org.bukkit.scheduler.BukkitRunnable() {
            @Override public void run() {
                if (!sb.isValid()) { cancel(); return; }
                Vector v = sb.getVelocity();
                sb.setVelocity(v.setY(v.getY() - 0.0392));
            }
        }.runTaskTimer(getInstance(), 1, 1);

        return sb;
    }
}
