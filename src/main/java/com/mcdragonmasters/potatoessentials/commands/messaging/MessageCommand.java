package com.mcdragonmasters.potatoessentials.commands.messaging;

import com.mcdragonmasters.potatoessentials.PotatoEssentials;
import com.mcdragonmasters.potatoessentials.utils.Config;
import com.mcdragonmasters.potatoessentials.utils.Replacer;

import com.mcdragonmasters.potatoessentials.utils.Utils;
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
    private static final Map<Player, Player> messages = new HashMap<>();

    public static void register() {
        CommandAPI.unregister("message");
        CommandAPI.unregister("msg");
        CommandAPI.unregister("whisper");
        CommandAPI.unregister("w");

        Argument<Player> playerArgument = new EntitySelectorArgument.OnePlayer("player");
        Argument<String> stringArgument = new GreedyStringArgument("message");
        new CommandAPICommand("message")
                .withAliases("msg","whisper", "w")
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
        Replacer[] replacers;
        if (PotatoEssentials.hasVault()) {
            replacers = new Replacer[]{
                    new Replacer("sender", sender.getName()),
                    new Replacer("sender-prefix", Utils.getPrefix(sender)),
                    new Replacer("sender-suffix", Utils.getSuffix(sender)),
                    new Replacer("receiver", receiver.getName()),
                    new Replacer("receiver-prefix", Utils.getPrefix(receiver)),
                    new Replacer("receiver-suffix", Utils.getSuffix(receiver)),
                    new Replacer("message", message, false) };
              } else { replacers = new Replacer[]{
                    new Replacer("sender", sender.getName()),
                    new Replacer("receiver", receiver.getName()),
                    new Replacer("message", message, false) };
        }

        Component senderMsg = Config.replaceFormat(Config.messageSender(), replacers);

        Component receiverMsg = Config.replaceFormat(Config.messageReceiver(), replacers);

        Component socialSpyMsg = Config.replaceFormat(Config.messageSocialSpy(), replacers);

        for (CommandSender socialSpyReceiver : socialSpyPlayers) {
            if(!socialSpyPlayers.contains(socialSpyReceiver)) continue;
            socialSpyReceiver.sendMessage(socialSpyMsg);
        }
        messages.put(receiver, sender);
        messages.put(sender, receiver);

        if (!socialSpyPlayers.contains(sender)) sender.sendMessage(senderMsg);
        if (!socialSpyPlayers.contains(receiver)) receiver.sendMessage(receiverMsg);
    }

}
