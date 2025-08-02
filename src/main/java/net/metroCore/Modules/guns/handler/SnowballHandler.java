package net.metroCore.Modules.guns.handler;

import org.bukkit.entity.Snowball;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

import static net.metroCore.Core.utils.nbt.NbtConstants.key;
import org.bukkit.persistence.PersistentDataType;

public class SnowballHandler implements Listener {

    @EventHandler
    public void onSnowballHit(EntityDamageByEntityEvent ev) {
        if (!(ev.getDamager() instanceof Snowball sb)) return;
        Integer dmg = sb.getPersistentDataContainer()
                .get(key("damage"), PersistentDataType.INTEGER);
        if (dmg != null && dmg > 0) {
            ev.setDamage(dmg);
        }
    }
}
