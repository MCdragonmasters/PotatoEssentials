package com.mcdragonmasters.potatoessentials.listeners;

import com.mcdragonmasters.potatoessentials.PotatoEssentials;
import com.mcdragonmasters.potatoessentials.commands.messaging.MuteChatCommand;
import com.mcdragonmasters.potatoessentials.utils.Config;
import com.mcdragonmasters.potatoessentials.utils.Replacer;
import com.mcdragonmasters.potatoessentials.utils.Utils;
import io.papermc.paper.event.player.AsyncChatEvent;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextReplacementConfig;
import net.kyori.adventure.text.event.ClickEvent;
import net.kyori.adventure.text.event.HoverEvent;
import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.text.format.TextDecoration;
import net.milkbowl.vault.chat.Chat;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import java.util.Map;
import java.util.regex.Pattern;

public class PlayerChatListener implements Listener {

    private static final Pattern DOMAIN_PATTERN = Pattern.compile(
            "(https?://)?([a-zA-Z0-9-]{2,}\\.[a-zA-Z]{2,})");
    private static final Chat vaultChat = PotatoEssentials.getVaultChat();

    @EventHandler
    public void onPlayerChat(AsyncChatEvent e) {
        if (!Config.chatEnabled()) return;
        if (e.isCancelled()) return;
        Player player = e.getPlayer();
        boolean canBypassMuteChat = player.hasPermission(PotatoEssentials.getNameSpace()+".chat.mute.bypass");
        if (MuteChatCommand.chatMuted && !canBypassMuteChat) {
            player.sendRichMessage("<red>The chat is currently muted!");
            e.setCancelled(true);
            return;
        }
        boolean miniMessage = player.hasPermission(PotatoEssentials.getNameSpace()+".chat.minimessage");
        boolean canUseEmojis = player.hasPermission(PotatoEssentials.getNameSpace()+".chat.emojis");
        boolean canUseURLs = player.hasPermission( PotatoEssentials.getNameSpace()+".chat.urls");
        Component message = e.message();

        if (Config.emojisEnabled() && canUseEmojis) {
            Map<String, String> emojis = Config.getEmojis();
            String emojiChar = Config.emojiToken();
            for (Map.Entry<String, String> entry : emojis.entrySet()) {
                TextReplacementConfig replacementConfig = TextReplacementConfig.builder()
                        .match("(?i)"+emojiChar+entry.getKey()+emojiChar)
                        .replacement(Utils.miniMessage(entry.getValue()))
                        .build();
                message = message.replaceText(replacementConfig);
            }
        }
        if (Config.formatURLs() && canUseURLs) {
            TextReplacementConfig URLReplacements = TextReplacementConfig.builder()
                    .match(DOMAIN_PATTERN)
                    .replacement((matchResult, builder) -> {
                        String fullMatch = matchResult.group();
                        String clickableUrl = (fullMatch.startsWith("https://") ||
                                fullMatch.startsWith("http://"))?fullMatch:"https://"+fullMatch;
                        return Component.text(fullMatch)
                                .color(TextColor.color(0, 177, 252))
                                .decorate(TextDecoration.UNDERLINED)
                                .hoverEvent(HoverEvent.showText(Component.text(clickableUrl)))
                                .clickEvent(ClickEvent.openUrl(clickableUrl));
                    }).build();
            message = message.replaceText(URLReplacements);
        }
        Component finalMessage = PotatoEssentials.hasVault()
            ? Config.replaceFormat(Config.chatFormat(),
                new Replacer("name", player.getName()),
                new Replacer("prefix", vaultChat.getPlayerPrefix(player)),
                new Replacer("suffix", vaultChat.getPlayerSuffix(player)),
                new Replacer("message", message, miniMessage))
            : Config.replaceFormat(Config.chatFormat(),
                new Replacer("name", player.getName()),
                new Replacer("message", message, miniMessage));

        e.setCancelled(true);
        Bukkit.broadcast(finalMessage);
    }

}
