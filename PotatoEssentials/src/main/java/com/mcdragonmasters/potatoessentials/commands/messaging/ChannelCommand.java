package com.mcdragonmasters.potatoessentials.commands.messaging;

import com.mcdragonmasters.potatoessentials.utils.Config;
import com.mcdragonmasters.potatoessentials.utils.CustomChat;
import com.mcdragonmasters.potatoessentials.utils.PotatoCommand;
import com.mcdragonmasters.potatoessentials.utils.Replacer;
import dev.jorel.commandapi.CommandAPICommand;
import dev.jorel.commandapi.arguments.Argument;
import dev.jorel.commandapi.arguments.ArgumentSuggestions;
import dev.jorel.commandapi.arguments.StringArgument;
import dev.jorel.commandapi.executors.CommandArguments;
import net.kyori.adventure.text.Component;
import org.bukkit.entity.Player;

import java.util.HashSet;
import java.util.Set;

public class ChannelCommand extends PotatoCommand {
    public ChannelCommand() {
        super("channel",NAMESPACE+".channel", "chat");
    }

    private final Argument<String> chatArgument = new StringArgument("chat").replaceSuggestions(
            ArgumentSuggestions.strings(info -> {
                Set<String> chats = new HashSet<>();
                for (CustomChat chat : CustomChat.getCustomChats()) {
                    if (info.sender().hasPermission(chat.getPermission())) chats.add(chat.getKey());
                }
                chats.add("all");
                chats.add("global");
                return chats.toArray(new String[0]);
            }));

    @Override
    public void register() {
        new CommandAPICommand(name)
                .withAliases(aliases)
                .withPermission(permission)
                .withArguments(chatArgument)
                .executesPlayer(this::execute)
                .register();
    }
    private void execute(Player player, CommandArguments args) {
        String chatStr = args.getByArgument(chatArgument);
        if ("global".equals(chatStr)||"all".equals(chatStr)) {
            CustomChat.getPlayerChat().remove(player);
            Component msg = Config.replaceFormat(Config.getCmdMsg("channel", "chatChannelChange"),
                    new Replacer("chat-name", "<yellow>Global"));
            player.sendMessage(msg);
            return;
        }
        var chatMap = CustomChat.getChatMap();
        if (!chatMap.containsKey(chatStr)) {
            var msg = Config.replaceFormat(Config.getCmdMsg("channel", "chatNotFound"),
                    new Replacer("chat-name", chatStr));
            player.sendMessage(msg);
            return;
        }
        CustomChat chat = chatMap.get(chatStr);
        if (!player.hasPermission(chat.getPermission())) {
            player.sendRichMessage("<red>You do not have permission!");
            return;
        }
        CustomChat.getPlayerChat().put(player, chat);
        Component msg = Config.replaceFormat(Config.getCmdMsg("channel", "chatChannelChange"),
                new Replacer("chat-name", chat.getName()));
        player.sendMessage(msg);
    }
}
