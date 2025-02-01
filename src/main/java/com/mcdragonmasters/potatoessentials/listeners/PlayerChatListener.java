package com.mcdragonmasters.potatoessentials.listeners;

import com.mcdragonmasters.potatoessentials.PotatoEssentials;
import com.mcdragonmasters.potatoessentials.utils.Config;
import io.papermc.paper.event.player.AsyncChatEvent;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.milkbowl.vault.chat.Chat;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class PlayerChatListener implements Listener {


    @EventHandler
    public void onPlayerChat(AsyncChatEvent e) {
        if (e.isCancelled()) return;

        Player player = e.getPlayer();
        Chat vaultChat = PotatoEssentials.getVaultChat();

        String format = Config.chatFormat();

        format = format.replace("%name%", player.getName());
        if (PotatoEssentials.hasVault()) {
            format = format
              .replace("%prefix%", vaultChat.getPlayerPrefix(player))
              .replace("%suffix%", vaultChat.getPlayerSuffix(player));
        }

        Component formattedComponent = MiniMessage.miniMessage().deserialize(format);

        Component messageComponent = e.message();
        Component finalMessage = formattedComponent.replaceText(builder ->
                builder.match("%message%").replacement(messageComponent)
        );

        e.renderer((source, sourceDisplayName, message, viewer)
                -> finalMessage);
    }

}
