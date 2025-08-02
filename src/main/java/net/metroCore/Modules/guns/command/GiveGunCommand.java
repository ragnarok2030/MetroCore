package net.metroCore.Modules.guns.command;

import net.metroCore.Core.command.AbstractSubCommand;
import net.metroCore.Modules.items.CustomItems;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * /gun give <type> [amount]
 */
public class GiveGunCommand extends AbstractSubCommand {

    @Override
    public boolean onExecute(CommandSender sender, Command command, String[] args) {
        if (!(sender instanceof Player p)) {
            sender.sendMessage("§cOnly players can run this.");
            return true;
        }
        if (args.length == 0) {
            p.sendMessage("§cUsage: /gun give <GUN_TYPE> [amount]");
            return true;
        }

        String typeName = args[0].toUpperCase();
        CustomItems.GunType type;
        try {
            type = CustomItems.GunType.valueOf(typeName);
        } catch (IllegalArgumentException e) {
            p.sendMessage("§cUnknown gun type: §f" + typeName);
            // list available
            String available = Arrays.stream(CustomItems.GunType.values())
                    .map(Enum::name)
                    .collect(Collectors.joining(", "));
            p.sendMessage("§6Available: §f" + available);
            return true;
        }

        int amount = 1;
        if (args.length >= 2) {
            try {
                amount = Integer.parseInt(args[1]);
            } catch (NumberFormatException ignore) {}
        }

        ItemStack gunItem = type.getItemStack();
        gunItem.setAmount(amount);
        p.getInventory().addItem(gunItem);
        p.sendMessage("§aGiven §f" + amount + "× " + type.getName());
        return true;
    }

    @Override
    public List<String> tabComplete(CommandSender sender, Command command, String[] args) {
        if (args.length == 1) {
            String prefix = args[0].toUpperCase();
            return Arrays.stream(CustomItems.GunType.values())
                    .map(Enum::name)
                    .filter(n -> n.startsWith(prefix))
                    .collect(Collectors.toList());
        }
        if (args.length == 2) {
            return List.of("1", "5", "10", "64");
        }
        return List.of();
    }

    @Override public String getDescription() { return "Give yourself a gun"; }
    @Override public String getUsage()       { return "give <type> [amount]"; }
    @Override public String getPermission()  { return "metrocore.gun.give"; }
}
