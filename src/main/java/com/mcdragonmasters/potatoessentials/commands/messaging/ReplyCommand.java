package com.mcdragonmasters.potatoessentials.commands.messaging;

import com.mcdragonmasters.potatoessentials.PotatoEssentials;
import dev.jorel.commandapi.CommandAPICommand;
import dev.jorel.commandapi.arguments.Argument;
import dev.jorel.commandapi.arguments.GreedyStringArgument;
import org.bukkit.entity.Player;

import java.util.Objects;

import static com.mcdragonmasters.potatoessentials.commands.messaging.MessageToggleCommand.messagesDisabled;

public class ReplyCommand {

    public static void register() {
        Argument<String> stringArgument = new GreedyStringArgument("message");
        new CommandAPICommand("reply")
                .withAliases("r")
                .withPermission(PotatoEssentials.NAMESPACE+".message")
                .withArguments(stringArgument)
                .executes((sender, args) -> {
                    var messageMap = MessageCommand.getMessages();
                    if (!messageMap.containsKey(sender)) {
                        sender.sendRichMessage("<red>You don't have anyone to reply to!");
                        return;
                    }

                    String message = Objects.requireNonNull(args.getByArgument(stringArgument));
                    Player lastMessagedPlayer = messageMap.get(sender);
                    if (messagesDisabled.contains(lastMessagedPlayer.getUniqueId())&&
                            !sender.hasPermission(PotatoEssentials.NAMESPACE+".messagetoggle.bypass")) {
                        sender.sendRichMessage("<red>"+lastMessagedPlayer.getName()+"'s messages are disabled");
                        return;
                    }
                    MessageCommand.message(sender, message, lastMessagedPlayer);
                }).register();
    }

}