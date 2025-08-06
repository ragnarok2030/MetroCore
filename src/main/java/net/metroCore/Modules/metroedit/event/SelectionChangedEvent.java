package net.metroCore.Modules.metroedit.event;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

import java.util.Optional;

public class SelectionChangedEvent extends Event {
    private static final HandlerList HANDLERS = new HandlerList();

    private final Player player;
    private final Optional<Location> pos1, pos2;

    public SelectionChangedEvent(Player player, Optional<Location> pos1, Optional<Location> pos2) {
        this.player = player;
        this.pos1 = pos1;
        this.pos2 = pos2;
    }

    public Player getPlayer() {
        return player;
    }

    public Optional<Location> getPos1() {
        return pos1;
    }

    public Optional<Location> getPos2() {
        return pos2;
    }

    @Override public HandlerList getHandlers() {
        return HANDLERS;
    }
    public static HandlerList getHandlerList() {
        return HANDLERS;
    }
}
