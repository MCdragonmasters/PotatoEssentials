package com.mcdragonmasters.potatoessentials.commands.messaging;

import com.mcdragonmasters.potatoessentials.PotatoEssentials;
import com.mcdragonmasters.potatoessentials.utils.Config;
import com.mcdragonmasters.potatoessentials.utils.Replacer;
import dev.jorel.commandapi.CommandAPI;
import dev.jorel.commandapi.CommandAPICommand;
import dev.jorel.commandapi.arguments.Argument;
import dev.jorel.commandapi.arguments.EntitySelectorArgument;
import dev.jorel.commandapi.arguments.GreedyStringArgument;
import lombok.Getter;
import net.kyori.adventure.text.Component;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import static com.mcdragonmasters.potatoessentials.commands.messaging.MessageToggleCommand.messagesDisabled;
import static com.mcdragonmasters.potatoessentials.commands.messaging.SocialSpyCommand.socialSpyPlayers;

public class MessageCommand {

    @Getter
    private static final Map<Player, Player> messageMap = new HashMap<>();

    public static void register() {
        CommandAPI.unregister("message");
        CommandAPI.unregister("msg");
        CommandAPI.unregister("whisper");
        CommandAPI.unregister("w");

        Argument<Player> playerArgument = new EntitySelectorArgument.OnePlayer("player");
        Argument<String> stringArgument = new GreedyStringArgument("message");
        new CommandAPICommand("message")
                .withAliases("msg")
                .withPermission(PotatoEssentials.getNameSpace()+".message")
                .withArguments(playerArgument)
                .withArguments(stringArgument)
                .executesPlayer((player, args) -> {
                    Player receiver = Objects.requireNonNull(args.getByArgument(playerArgument));
                    if (messagesDisabled.contains(receiver.getUniqueId())&&
                            !player.hasPermission(PotatoEssentials.getNameSpace()+".messagetoggle.bypass")) {
                        player.sendRichMessage("<red>"+receiver.getName()+"'s messages are disabled");
                        return;
                    }
                    String message = Objects.requireNonNull(args.getByArgument(stringArgument));
                    message(player, message, receiver);
                }).register();
    }

    public static void message(Player sender, String message, Player receiver) {

        Component senderMsg = Config.replaceFormat(Config.messageSender(),
                new Replacer("message", message),
                new Replacer("sender", sender.getName()),
                new Replacer("receiver", receiver.getName()));

        Component receiverMsg = Config.replaceFormat(Config.messageReceiver(),
                new Replacer("message", message),
                new Replacer("sender", sender.getName()),
                new Replacer("receiver", receiver.getName()));

        Component socialSpyMsg = Config.replaceFormat(Config.messageSocialSpy(),
                new Replacer("message", message),
                new Replacer("sender", sender.getName()),
                new Replacer("receiver", receiver.getName()));

        for (CommandSender socialSpyReceiver : socialSpyPlayers) {
            if(!socialSpyPlayers.contains(socialSpyReceiver)) continue;
            socialSpyReceiver.sendMessage(socialSpyMsg);
        }
        messageMap.put(receiver, sender);

        if (!socialSpyPlayers.contains(sender)) sender.sendMessage(senderMsg);
        if (!socialSpyPlayers.contains(receiver)) receiver.sendMessage(receiverMsg);
    }

}
