package com.mcdragonmasters.potatoessentials.listeners;

import com.mcdragonmasters.potatoessentials.PotatoEssentials;
import com.mcdragonmasters.potatoessentials.commands.messaging.MuteChatCommand;
import com.mcdragonmasters.potatoessentials.utils.Config;
import com.mcdragonmasters.potatoessentials.utils.CustomChat;
import com.mcdragonmasters.potatoessentials.utils.Replacer;
import com.mcdragonmasters.potatoessentials.utils.Utils;

import io.papermc.paper.event.player.AsyncChatEvent;

import net.kyori.adventure.text.Component;

import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import java.util.Collection;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PlayerChatListener implements Listener {

    private static final Pattern DOMAIN_PATTERN = Pattern.compile(
            "(https?://)?([a-zA-Z0-9.-]+\\.[a-zA-Z]{2,})(/[a-zA-Z0-9@:%_+.~#?&/=\\-]*)?");

    @EventHandler
    public void onPlayerChat(AsyncChatEvent e) {
        if (!Config.chatEnabled()) return;
        if (e.isCancelled()) return;
        Player player = e.getPlayer();
        boolean miniMessage = player.hasPermission(PotatoEssentials.NAMESPACE+".chat.minimessage");
        boolean canUseEmojis = player.hasPermission(PotatoEssentials.NAMESPACE+".chat.emojis");
        boolean canUseURLs = player.hasPermission( PotatoEssentials.NAMESPACE+".chat.urls");
        boolean canBypassChatFilter = player.hasPermission(PotatoEssentials.NAMESPACE+".chat.bypassFilter");
        String message = PlainTextComponentSerializer.plainText().serialize(e.message());

        if (!miniMessage) message = Utils.escapeTags(message);

        if (Config.chatFilterWords() && !canBypassChatFilter) {
            Collection<String> filteredWords = Config.chatFilteredWords();
            String regex = "\\b("+String.join("|", filteredWords)+")s?\\b";
            Pattern pattern = Pattern.compile(regex, Pattern.CASE_INSENSITIVE);

            message = pattern.matcher(message).replaceAll(matchResult ->
                    "*".repeat(matchResult.group().length()));
        }


        if (Config.emojisEnabled() && canUseEmojis) {
            Map<String, String> emojis = Config.getEmojis();
            for (Map.Entry<String, String> entry : emojis.entrySet()) {
                String escapedKey = Pattern.quote(entry.getKey());
                Pattern pattern = Pattern.compile(escapedKey, Pattern.CASE_INSENSITIVE);
                message = pattern.matcher(message).replaceAll(entry.getValue());
            }
        }

        if (Config.formatURLs() && canUseURLs) {

            Matcher matcher = DOMAIN_PATTERN.matcher(message);

            StringBuilder formattedMessage = new StringBuilder();

            while (matcher.find()) {
                String fullMatch = matcher.group();
                String clickableUrl = (fullMatch.startsWith("https://")||fullMatch.startsWith("http://"))?fullMatch:"https://"+fullMatch;


                String miniMessageFormattedUrl =
                        "<#00b1fc><hover:show_text:'"+clickableUrl+"'><u><click:open_url:'"+clickableUrl
                        +"'>"+fullMatch+"</click></u></hover></#00b1fc>";

                matcher.appendReplacement(formattedMessage, miniMessageFormattedUrl);
            }
            matcher.appendTail(formattedMessage);

            message = formattedMessage.toString();
        }

        Component finalMessage = PotatoEssentials.isVaultInstalled()
            ? Config.replaceFormat(Config.chatFormat(),
                new Replacer("prefix", Utils.getPrefix(player)),
                new Replacer("name", player.getName()),
                new Replacer("suffix", Utils.getSuffix(player)),
                new Replacer("message", message, true, false))
            : Config.replaceFormat(Config.chatFormat(),
                new Replacer("name", player.getName()),
                new Replacer("message", message, true, false));

        e.setCancelled(true);
        Map<Player, CustomChat> playerChatMap = CustomChat.getPlayerChat();
        if (Config.customChatsEnabled() && playerChatMap.containsKey(player)) {
            CustomChat.sendMessage(player, message, miniMessage, playerChatMap.get(player));
            return;
        }
        boolean canBypassMuteChat = player.hasPermission(PotatoEssentials.NAMESPACE+".chat.bypassMutedChat");
        if (MuteChatCommand.chatMuted && !canBypassMuteChat) {
            player.sendRichMessage("<red>The chat is currently muted!");
            return;
        }
        Bukkit.broadcast(finalMessage);
    }

}
