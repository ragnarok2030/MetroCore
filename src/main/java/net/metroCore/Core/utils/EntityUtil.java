package net.metroCore.Core.utils;

import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.Collection;

public final class EntityUtil {
    private EntityUtil() {}

    /** Spawn a living entity at loc with a speed of 0 (no AI movement). */
    public static LivingEntity spawnStaticEntity(Location loc, EntityType type) {
        LivingEntity e = (LivingEntity)loc.getWorld().spawnEntity(loc, type);
        e.setAI(false);
        return e;
    }

    /** Remove all entities of given types in a radius. */
    public static void removeEntities(Location center, double radius, EntityType... types) {
        Collection<Entity> ents = center.getWorld().getNearbyEntities(center, radius, radius, radius);
        for (Entity e : ents) {
            for (EntityType t : types) {
                if (e.getType() == t) {
                    e.remove();
                    break;
                }
            }
        }
    }

    /** Apply a brief blindness effect to an entity. */
    public static void blind(Entity e, int ticks) {
        if (e instanceof LivingEntity living) {
            living.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, ticks, 1, true, false));
        }
    }
}
