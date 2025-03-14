package com.mcdragonmasters.potatoessentials.utils;

import com.mcdragonmasters.potatoessentials.PotatoEssentials;
import dev.jorel.commandapi.CommandAPI;
import dev.jorel.commandapi.CommandAPICommand;
import dev.jorel.commandapi.RegisteredCommand;
import dev.jorel.commandapi.arguments.GreedyStringArgument;
import lombok.Getter;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import javax.annotation.Nullable;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;


@Getter
public class CustomChat {
    @Getter
    private static final Set<CustomChat> customChats = new HashSet<>();
    @Getter
    private static final Map<String, CustomChat> chatMap = new HashMap<>();
    @Getter
    private static final Map<Player, CustomChat> playerChat = new HashMap<>();
    private final String key;
    private final String name;
    private final String permission;
    private final @Nullable String command;

    public CustomChat(String key, String name, String permission, @Nullable String command) {
        this.key = key;
        this.name = name;
        this.permission = permission;
        this.command = command;
        chatMap.put(key, this);
        customChats.add(this);
        if (command==null) return;
        for (RegisteredCommand registeredCmd : CommandAPI.getRegisteredCommands()) {
            if (registeredCmd.commandName().equals(command)) {
                PotatoEssentials.LOGGER.severe(
                        "Command '%s' for custom chat '%s' was already taken, not registering chat".formatted(command, key));
                chatMap.remove(key, this);
                customChats.remove(this);
                return;

            }
        }
        var messageArg = new GreedyStringArgument("message");
        new CommandAPICommand(command)
                .withPermission(permission)
                .withArguments(messageArg)
                .executes((sender, args) -> {
                    String message = args.getByArgument(messageArg);
                    sendMessage(sender,message,this);
                }).register();
    }
    public static void sendMessage(CommandSender sender, String message, CustomChat chat) {
        boolean miniMsg = sender.hasPermission(PotatoEssentials.NAMESPACE+".chat.minimessage");
        if (!miniMsg) message = Utils.escapeTags(message);
        message = Utils.formatChatMessage(sender, message);
        Component msg = Config.replaceFormat(Config.customChatFormat(),
                new Replacer("chat-name", chat.getName()),
                new Replacer("prefix", Utils.getPrefix(sender)),
                new Replacer("name", sender.getName()),
                new Replacer("message", message, false));

        Bukkit.broadcast(msg, chat.getPermission());
    }
    public static void clearAll() {
        customChats.clear();
        chatMap.clear();
        playerChat.clear();
    }
}
