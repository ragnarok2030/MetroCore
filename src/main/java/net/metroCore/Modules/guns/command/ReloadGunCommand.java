package net.metroCore.Modules.guns.command;

import net.metroCore.Core.command.AbstractSubCommand;
import net.metroCore.Core.utils.nbt.NbtCompound;
import net.metroCore.Core.utils.nbt.NbtConstants;
import net.metroCore.Modules.items.CustomItems;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.List;

public class ReloadGunCommand extends AbstractSubCommand {

    @Override
    public boolean onExecute(CommandSender sender, Command cmd, String[] args) {
        if (!(sender instanceof Player p)) {
            sender.sendMessage("§cOnly players can reload guns.");
            return true;
        }

        ItemStack gun = p.getInventory().getItemInMainHand();
        if (gun == null) {
            p.sendMessage("§cHold a gun to reload.");
            return true;
        }

        var wrapper = new NbtCompound(gun, "");
        // check “gun” boolean tag
        String gunTag = wrapper.getString("gun");
        if (gunTag == null || !Boolean.parseBoolean(gunTag)) {
            p.sendMessage("§cThat’s not a gun.");
            return true;
        }

        // read type + ammo
        String typeName = wrapper.getString("gunType");
        var type = CustomItems.GunType.valueOf(typeName);
        int currentAmmo = Integer.parseInt(wrapper.getString("currentAmmo"));
        int maxAmmo     = type.getMagazineSize();

        if (currentAmmo > 0) {
            p.sendMessage("§eYou still have ammo: §f" + currentAmmo + "§e/§f" + maxAmmo);
            return true;
        }

        // find and consume one ammo item
        ItemStack found = findAmmo(p, type.getAmmoType());
        if (found == null) {
            p.sendMessage("§cYou have no " + type.getAmmoType().getDescription() + " to reload.");
            return true;
        }
        found.setAmount(found.getAmount() - 1);

        // refill ammo in NBT
        wrapper.setString("currentAmmo", String.valueOf(maxAmmo));

        // update lore line #2
        if (gun.hasItemMeta()) {
            ItemMeta meta = gun.getItemMeta();
            List<String> lore = meta.getLore();
            if (lore != null && lore.size() >= 2) {
                lore.set(1,
                        ChatColor.GREEN + "Ammo: " +
                                ChatColor.WHITE + maxAmmo +
                                ChatColor.GRAY  + "/" + maxAmmo
                );
                meta.setLore(lore);
                gun.setItemMeta(meta);
            }
        }

        // apply NBTCompound changes back to the item
        // (wrapper already updates the ItemMeta internally on setString)
        p.getInventory().setItemInMainHand(gun);
        p.updateInventory();

        p.sendMessage("§aReloaded your " + type.getName() +
                "! Ammo: §f" + maxAmmo + "§a/§f" + maxAmmo);
        return true;
    }

    private ItemStack findAmmo(Player p, CustomItems.AmmoType ammoType) {
        for (ItemStack stack : p.getInventory().getContents()) {
            if (stack == null || stack.getType() != ammoType.getMaterial()) continue;
            var wrapper = new NbtCompound(stack, "");
            String isAmmo = wrapper.getString("ammo");
            String aType  = wrapper.getString("ammoType");
            if (Boolean.parseBoolean(isAmmo)
                    && ammoType.name().equals(aType)
                    && stack.getAmount() > 0) {
                return stack;
            }
        }
        return null;
    }

    @Override public String getDescription() { return "Reload your gun"; }
    @Override public String getUsage()       { return ""; }
    @Override public String getPermission()  { return "metrocore.gun.reload"; }
}
