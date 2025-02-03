package com.mcdragonmasters.potatoessentials.listeners;

import com.mcdragonmasters.potatoessentials.utils.Config;
import io.papermc.paper.event.player.AsyncChatEvent;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class PlayerChatListener implements Listener {


    @EventHandler
    public void onPlayerChat(AsyncChatEvent e) {
        if (!Config.chatEnabled()) return;
        if (e.isCancelled()) return;

        Component message = Config.replaceFormat(
                Config.chatFormat(), e.message(), e.getPlayer(), null);

        e.setCancelled(true);
        Bukkit.broadcast(message);
    }

}
