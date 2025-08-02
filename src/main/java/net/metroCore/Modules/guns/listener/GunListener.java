package net.metroCore.Modules.guns.listener;

import net.metroCore.Core.utils.nbt.NbtCompound;
import net.metroCore.Modules.items.CustomItems;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.entity.Snowball;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import java.util.HashMap;
import java.util.Map;

import static net.metroCore.Core.utils.nbt.NbtConstants.key;
import static net.metroCore.MetroCore.getInstance;

public class GunListener implements Listener {
    private static final double SNOWBALL_SPEED   = 3.5;
    private static final int    SHOTGUN_PELLETS = 5;
    private static final double SHOTGUN_SPREAD  = 0.3;

    private final Map<Player, Long> lastShot = new HashMap<>();

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent ev) {
        if (ev.getHand() != EquipmentSlot.HAND || !ev.getAction().isRightClick()) return;
        Player p = ev.getPlayer();
        ItemStack in = p.getInventory().getItemInMainHand();
        if (in == null) return;

        var nbt = new NbtCompound(in, "");
        if (!Boolean.parseBoolean(nbt.getString("gun"))) return;

        CustomItems.GunType type = CustomItems.GunType.valueOf(nbt.getString("gunType"));
        int ammo = Integer.parseInt(nbt.getString("currentAmmo"));
        if (ammo <= 0) {
            p.sendMessage(ChatColor.RED + "Out of ammo! /gun reload");
            return;
        }

        long now = System.currentTimeMillis();
        long last = lastShot.getOrDefault(p, 0L);
        long delay = (long)(60000.0 / type.getRateOfFire());
        if (now - last < delay) {
            ev.setCancelled(true);
            return;
        }
        ev.setCancelled(true);
        lastShot.put(p, now);

        if (type.getAmmoType() == CustomItems.AmmoType.CALIBER_12_GAUGE) {
            shootShotgun(p, type);
        } else {
            shootSingle(p, type);
        }

        // decrement ammo
        nbt.setString("currentAmmo", String.valueOf(ammo - 1));
        p.getInventory().setItemInMainHand(in);
        p.updateInventory();
        p.sendMessage(ChatColor.YELLOW + "Ammo: " + ChatColor.WHITE + (ammo - 1) + ChatColor.GRAY + "/" + type.getMagazineSize());
    }

    private void shootSingle(Player shooter, CustomItems.GunType type) {
        Vector dir = shooter.getEyeLocation().getDirection().normalize();
        Snowball sb = shooter.launchProjectile(Snowball.class, dir.multiply(SNOWBALL_SPEED));
        applySnowball(sb, type);
        gravity(sb);
    }

    private void shootShotgun(Player shooter, CustomItems.GunType type) {
        Vector base = shooter.getEyeLocation().getDirection().normalize();
        for (int i = 0; i < SHOTGUN_PELLETS; i++) {
            Vector spread = base.clone().add(new Vector(
                    (Math.random()-0.5)*SHOTGUN_SPREAD,
                    (Math.random()-0.5)*SHOTGUN_SPREAD,
                    (Math.random()-0.5)*SHOTGUN_SPREAD
            )).normalize();
            Snowball sb = shooter.launchProjectile(Snowball.class, spread.multiply(SNOWBALL_SPEED));
            applySnowball(sb, type);
            gravity(sb);
        }
    }

    private void applySnowball(Snowball sb, CustomItems.GunType type) {
        sb.setGravity(false);
        sb.setInvisible(true);
        sb.getPersistentDataContainer()
                .set(key("gunType"), org.bukkit.persistence.PersistentDataType.STRING, type.name());
        sb.getPersistentDataContainer()
                .set(key("damage"),  org.bukkit.persistence.PersistentDataType.INTEGER, type.getAmmoType().getDamage());
    }

    private void gravity(Snowball sb) {
        new BukkitRunnable() {
            @Override public void run() {
                if (!sb.isValid()) { cancel(); return; }
                Vector v = sb.getVelocity();
                sb.setVelocity(v.setY(v.getY() - 0.0392));
            }
        }.runTaskTimer(getInstance(), 1, 1);
    }
}
