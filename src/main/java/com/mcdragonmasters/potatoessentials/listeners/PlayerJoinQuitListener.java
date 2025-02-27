package com.mcdragonmasters.potatoessentials.listeners;

import com.mcdragonmasters.potatoessentials.PotatoEssentials;
import com.mcdragonmasters.potatoessentials.commands.messaging.MessageCommand;
import com.mcdragonmasters.potatoessentials.commands.VanishCommand;
import com.mcdragonmasters.potatoessentials.utils.Config;
import com.mcdragonmasters.potatoessentials.utils.Replacer;
import com.mcdragonmasters.potatoessentials.utils.Utils;
import net.kyori.adventure.text.Component;
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
    public void alsoOnJoin(PlayerJoinEvent e) {
        Player joiner = e.getPlayer();
        Component msg = PotatoEssentials.hasVault()
                ? Config.replaceFormat(Config.joinMessageFormat(),
                new Replacer("name", joiner.getName()),
                new Replacer("prefix", Utils.getPrefix(joiner)),
                new Replacer("suffix", Utils.getSuffix(joiner)))
                : Config.replaceFormat(Config.joinMessageFormat(),
                new Replacer("name", joiner.getName()));
        e.joinMessage(msg);
    }
    @EventHandler
    public void onQuit(PlayerQuitEvent e) {
        Player quiter = e.getPlayer();
        VanishCommand.getVanishedPlayers().remove(quiter);
        MessageCommand.getMessages().remove(quiter);

        Component msg = PotatoEssentials.hasVault()
                ? Config.replaceFormat(Config.quitMessageFormat(),
                new Replacer("name", quiter.getName()),
                new Replacer("prefix", Utils.getPrefix(quiter)),
                new Replacer("suffix", Utils.getSuffix(quiter)))
                : Config.replaceFormat(Config.joinMessageFormat(),
                new Replacer("name", quiter.getName()));
        e.quitMessage(msg);
    }
}
