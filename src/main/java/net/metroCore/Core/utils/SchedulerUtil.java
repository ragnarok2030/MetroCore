package net.metroCore.Core.utils;

import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitTask;

import java.util.concurrent.CompletableFuture;

public final class SchedulerUtil {
    private SchedulerUtil() {}

    /** Run on the main thread after `delay` ticks. */
    public static BukkitTask runLater(Plugin plugin, Runnable action, long delay) {
        return Bukkit.getScheduler().runTaskLater(plugin, action, delay);
    }

    /** Run repeatedly on main thread. */
    public static BukkitTask runRepeating(Plugin plugin, Runnable action, long delay, long period) {
        return Bukkit.getScheduler().runTaskTimer(plugin, action, delay, period);
    }

    /** Run asynchronously. */
    public static BukkitTask runAsync(Plugin plugin, Runnable action) {
        return Bukkit.getScheduler().runTaskAsynchronously(plugin, action);
    }

    /** Convenience: async + return a result via CompletableFuture. */
    public static <T> CompletableFuture<T> supplyAsync(Plugin plugin, java.util.function.Supplier<T> sup) {
        CompletableFuture<T> cf = new CompletableFuture<>();
        Bukkit.getScheduler().runTaskAsynchronously(plugin, () -> {
            try { cf.complete(sup.get()); }
            catch (Throwable t) { cf.completeExceptionally(t); }
        });
        return cf;
    }
}
