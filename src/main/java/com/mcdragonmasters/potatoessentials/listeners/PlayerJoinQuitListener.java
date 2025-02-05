package com.mcdragonmasters.potatoessentials.listeners;

import com.mcdragonmasters.potatoessentials.PotatoEssentials;
import com.mcdragonmasters.potatoessentials.commands.messaging.MessageCommand;
import com.mcdragonmasters.potatoessentials.commands.VanishCommand;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class PlayerJoinQuitListener implements Listener {

    @EventHandler
    public void onJoin(PlayerJoinEvent e) {
        Player joiner = e.getPlayer();
        if (joiner.hasPermission(PotatoEssentials.getNameSpace()+".vanish.bypass")) return;
        for (Player player : VanishCommand.getVanishedPlayers()) {
            joiner.hidePlayer(PotatoEssentials.getInstance(), player);
        }

    }
    @EventHandler
    public void onQuit(PlayerQuitEvent e) {
        Player player = e.getPlayer();
        VanishCommand.getVanishedPlayers().remove(player);
        MessageCommand.getMessageMap().remove(player);
    }
}
