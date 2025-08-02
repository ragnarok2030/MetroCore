package net.metroCore.Modules.guns.handler;

import net.metroCore.Core.utils.nbt.NbtCompound;
import net.metroCore.Modules.items.CustomItems;
import org.bukkit.ChatColor;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.entity.Snowball;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import java.util.HashMap;
import java.util.Map;

import static net.metroCore.Core.utils.nbt.NbtConstants.key;
import static net.metroCore.MetroCore.getInstance;

public class GunShootHandler implements Listener {
    private final NamespacedKey gunKey       = key("gun");
    private final NamespacedKey gunTypeKey   = key("gunType");
    private final NamespacedKey ammoKey      = key("currentAmmo");
    private final NamespacedKey damageKey    = key("damage");
    private final Map<Player, Long> lastShot = new HashMap<>();

    private static final double SNOWBALL_SPEED   = 3.5;
    private static final int    SHOTGUN_PELLETS = 5;
    private static final double SHOTGUN_SPREAD  = 0.3;

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent ev) {
        if (ev.getHand() != EquipmentSlot.HAND || !ev.getAction().isRightClick()) return;
        Player player = ev.getPlayer();
        ItemStack inHand = player.getInventory().getItemInMainHand();
        if (inHand == null) return;

        var wrapper = new NbtCompound(inHand, "");
        if (!Boolean.parseBoolean(wrapper.getString("gun"))) return;

        var type = CustomItems.GunType.valueOf(wrapper.getString("gunType"));
        int currentAmmo = Integer.parseInt(wrapper.getString("currentAmmo"));
        if (currentAmmo <= 0) {
            player.sendMessage(ChatColor.RED + "Out of ammo! Use /gun reload");
            return;
        }

        long now   = System.currentTimeMillis();
        long last  = lastShot.getOrDefault(player, 0L);
        long delay = (long)(60000.0 / type.getRateOfFire());
        if (now - last < delay) {
            ev.setCancelled(true);
            return;
        }
        ev.setCancelled(true);
        lastShot.put(player, now);

        if (type.getAmmoType() == CustomItems.AmmoType.CALIBER_12_GAUGE) {
            shootShotgun(player, type);
        } else {
            shootSingle(player, type);
        }

        // Decrement ammo in NBT
        wrapper.setString("currentAmmo", String.valueOf(currentAmmo - 1));
        // write back to hand
        player.getInventory().setItemInMainHand(inHand);
        player.updateInventory();
        player.sendMessage(ChatColor.YELLOW + "Ammo remaining: "
                + ChatColor.WHITE + (currentAmmo - 1));
    }

    private void shootSingle(Player shooter, CustomItems.GunType type) {
        Vector dir = shooter.getEyeLocation().getDirection().normalize();
        Snowball sb = shooter.launchProjectile(Snowball.class, dir.multiply(SNOWBALL_SPEED));
        applySnowballData(sb, type);
        gravityTask(sb);
    }

    private void shootShotgun(Player shooter, CustomItems.GunType type) {
        Vector base = shooter.getEyeLocation().getDirection().normalize();
        for (int i = 0; i < SHOTGUN_PELLETS; i++) {
            Vector spread = base.clone().add(new Vector(
                    (Math.random() - 0.5) * SHOTGUN_SPREAD,
                    (Math.random() - 0.5) * SHOTGUN_SPREAD,
                    (Math.random() - 0.5) * SHOTGUN_SPREAD
            )).normalize();
            Snowball sb = shooter.launchProjectile(Snowball.class, spread.multiply(SNOWBALL_SPEED));
            applySnowballData(sb, type);
            gravityTask(sb);
        }
    }

    private void applySnowballData(Snowball sb, CustomItems.GunType type) {
        sb.setGravity(false);
        sb.setInvisible(true);
        sb.getPersistentDataContainer().set(gunTypeKey, PersistentDataType.STRING, type.name());
        sb.getPersistentDataContainer().set(damageKey,  PersistentDataType.INTEGER, type.getAmmoType().getDamage());
    }

    private void gravityTask(Snowball sb) {
        new BukkitRunnable() {
            @Override public void run() {
                if (!sb.isValid()) { cancel(); return; }
                Vector v = sb.getVelocity();
                sb.setVelocity(v.setY(v.getY() - 0.0392));
            }
        }.runTaskTimer(getInstance(), 1, 1);
    }

    @EventHandler
    public void onSnowballHit(EntityDamageByEntityEvent ev) {
        if (!(ev.getDamager() instanceof Snowball sb)) return;
        Integer dmg = sb.getPersistentDataContainer().get(damageKey, PersistentDataType.INTEGER);
        if (dmg != null && dmg > 0) {
            ev.setDamage(dmg);
        }
    }
}
