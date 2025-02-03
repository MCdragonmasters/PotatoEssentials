package com.mcdragonmasters.potatoessentials.commands.messaging;

import com.mcdragonmasters.potatoessentials.PotatoEssentials;
import com.mcdragonmasters.potatoessentials.utils.Config;
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
import static com.mcdragonmasters.potatoessentials.commands.messaging.SocialSpyCommand.socialSpy;

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

        Component senderMsg = Config.replaceFormat(Config.messageSender(),message, sender,receiver);
        Component receiverMsg = Config.replaceFormat(Config.messageReceiver(),message,sender, receiver);
        Component socialSpyMsg = Config.replaceFormat(Config.messageSocialSpy(), message, sender, receiver);
        for (CommandSender socialSpyReceiver : socialSpy.keySet()) {
            if(!socialSpy.get(socialSpyReceiver)) continue;
            socialSpyReceiver.sendMessage(socialSpyMsg);
        }
        messageMap.put(sender,receiver);

        if (!socialSpy.get(sender)) sender.sendMessage(senderMsg);
        if (!socialSpy.get(receiver)) receiver.sendMessage(receiverMsg);
    }

}
