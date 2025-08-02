package net.metroCore.Modules.guns.listener;

import net.metroCore.Core.utils.nbt.NbtCompound;
import net.metroCore.Modules.items.CustomItems;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.inventory.ItemStack;

public class MeleeListener implements Listener {

    @EventHandler
    public void onPlayerAttack(EntityDamageByEntityEvent ev) {
        if (!(ev.getDamager() instanceof Player player)) return;
        ItemStack item = player.getInventory().getItemInMainHand();
        if (item == null || item.getType() != Material.IRON_HOE) return;

        var wrapper = new NbtCompound(item, "");
        if ("true".equals(wrapper.getString("weapon"))) {
            String type = wrapper.getString("weaponType");
            double damage = CustomItems.MeleeWeapon.valueOf(type).getDamage();
            ev.setDamage(damage);
        }
    }

    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent ev) {
        Player killer = ev.getEntity().getKiller();
        if (killer == null) return;
        ItemStack item = killer.getInventory().getItemInMainHand();
        if (item == null || item.getType() != Material.IRON_HOE) return;

        var wrapper = new NbtCompound(item, "");
        if ("true".equals(wrapper.getString("weapon"))) {
            String name = item.getItemMeta().getDisplayName();
            ev.setDeathMessage(
                    ChatColor.RED + ev.getEntity().getName() +
                            " was slain by " + ChatColor.YELLOW + killer.getName() +
                            ChatColor.WHITE + " with " + ChatColor.GOLD + name
            );
        }
    }
}
