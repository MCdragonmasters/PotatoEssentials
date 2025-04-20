package com.mcdragonmasters.potatoessentials.events;

import lombok.Getter;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.event.player.PlayerEvent;
import org.jetbrains.annotations.NotNull;

public class VanishToggleEvent extends PlayerEvent {
    private static final HandlerList handlers = new HandlerList();
    @Getter
    private final boolean vanished;
    public VanishToggleEvent(Player vanished, boolean isVanished) {
        super(vanished);
        this.vanished = isVanished;
    }

    @Override
    public @NotNull HandlerList getHandlers() {
        return handlers;
    }
    @SuppressWarnings("unused")
    public static HandlerList getHandlerList() {
        return handlers;
    }
}
