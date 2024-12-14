package com.mcdragonmasters.potatoessentials.listeners;

import com.mcdragonmasters.potatoessentials.PotatoEssentials;
import com.mcdragonmasters.potatoessentials.commands.VisibilityCommand;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.Plugin;

import java.util.Map;
import java.util.UUID;
//TODO: add messages
public class PlayerJoinQuitListener implements Listener {
    @EventHandler
    public void onJoin(PlayerJoinEvent e) {
        Map<UUID, String> playerVisibility = VisibilityCommand.getPlayerVisibility();
        Plugin instance = PotatoEssentials.getInstance();

        for (UUID uuid : playerVisibility.keySet()) {
            if (!playerVisibility.containsKey(uuid)) return;
            Player p = Bukkit.getPlayer(uuid);
            if (p == null) return;
            Player idkPlayer = e.getPlayer();
            String visibility = playerVisibility.get(uuid);
            if (visibility.equals("staff")) {
                if (idkPlayer.isOp() || idkPlayer.hasPermission(PotatoEssentials.getNameSpace()+".staff")) {
                    p.showPlayer(instance, idkPlayer);
                } else {
                    p.hidePlayer(instance, idkPlayer);
                }
            } else if (visibility.equals("none")) {
                p.hidePlayer(instance, e.getPlayer());
            }
        }
    }
    @EventHandler
    public void onQuit(PlayerQuitEvent e) {
        VisibilityCommand.getPlayerVisibility().remove(e.getPlayer().getUniqueId());
    }
}
