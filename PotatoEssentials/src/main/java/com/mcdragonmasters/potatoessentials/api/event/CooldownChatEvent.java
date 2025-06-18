package com.mcdragonmasters.potatoessentials.api.event;

import net.kyori.adventure.text.Component;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.event.player.PlayerEvent;
import org.jetbrains.annotations.NotNull;

/**
 *
 * Called when a player tries to send a message and is on cooldown.
 *
 */
// TODO: finish this
public class CooldownChatEvent extends PlayerEvent {
    private static final HandlerList handlers = new HandlerList();
    private final Component message;

    public CooldownChatEvent(@NotNull Player player, Component message) {
        super(player);
        this.message = message;
    }
    public Component message() {
        return this.message;
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
