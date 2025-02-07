package com.mcdragonmasters.potatoessentials.listeners;

import com.mcdragonmasters.potatoessentials.PotatoEssentials;
import com.mcdragonmasters.potatoessentials.utils.Config;
import com.mcdragonmasters.potatoessentials.utils.Replacer;
import com.mcdragonmasters.potatoessentials.utils.Utils;
import io.papermc.paper.event.player.AsyncChatEvent;
import net.kyori.adventure.text.Component;
import net.milkbowl.vault.chat.Chat;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class PlayerChatListener implements Listener {

    private static final Chat vaultChat = PotatoEssentials.getVaultChat();

    @EventHandler
    public void onPlayerChat(AsyncChatEvent e) {
        if (!Config.chatEnabled()) return;
        if (e.isCancelled()) return;
        Player player = e.getPlayer();
        boolean miniMessage = player.hasPermission(PotatoEssentials.getNameSpace()+".chat.minimessage");
        Component message;
        if (!PotatoEssentials.hasVault()) {
            message = Config.replaceFormat(Config.chatFormat(),
                    new Replacer("name", e.getPlayer().getName()),
                    new Replacer("message", Utils.serialize(e.message()), miniMessage));
        } else {
            message = Config.replaceFormat(Config.chatFormat(),
                    new Replacer("name", e.getPlayer().getName()),
                    new Replacer("prefix", vaultChat.getPlayerPrefix(player)),
                    new Replacer("suffix", vaultChat.getPlayerSuffix(player)),
                    new Replacer("message", Utils.serialize(e.message()), miniMessage));
        }
        e.setCancelled(true);
        Bukkit.broadcast(message);
    }

}
