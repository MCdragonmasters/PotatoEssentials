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
import static com.mcdragonmasters.potatoessentials.commands.messaging.SocialSpyCommand.socialSpyList;

public class MessageCommand {

    @Getter
    private static final Map<CommandSender, CommandSender> messages = new HashMap<>();

    public static void register() {
        CommandAPI.unregister("message");
        CommandAPI.unregister("msg");
        CommandAPI.unregister("whisper");
        CommandAPI.unregister("w");

        Argument<Player> playerArgument = new EntitySelectorArgument.OnePlayer("player");
        Argument<String> stringArgument = new GreedyStringArgument("message");
        new CommandAPICommand("message")
                .withAliases("msg","whisper", "w")
                .withPermission(PotatoEssentials.NAMESPACE+".message")
                .withArguments(playerArgument)
                .withArguments(stringArgument)
                .executes((sender, args) -> {
                    Player receiver = Objects.requireNonNull(args.getByArgument(playerArgument));
                    if (messagesDisabled.contains(receiver.getUniqueId())&&
                            !sender.hasPermission(PotatoEssentials.NAMESPACE+".messagetoggle.bypass")) {
                        sender.sendRichMessage("<red>"+receiver.getName()+"'s messages are disabled");
                        return;
                    }
                    String message = Objects.requireNonNull(args.getByArgument(stringArgument));
                    message(sender, message, receiver);
                }).register();
    }

    public static void message(CommandSender sender, String message, CommandSender receiver) {
        Replacer[] replacers;
        if (PotatoEssentials.isVaultInstalled()) {
            replacers = new Replacer[]{
                    new Replacer("sender", sender.getName()),
                    new Replacer("sender-prefix", Utils.getPrefix(sender)),
                    new Replacer("sender-suffix", Utils.getSuffix(sender)),
                    new Replacer("receiver", receiver.getName()),
                    new Replacer("receiver-prefix", Utils.getPrefix(receiver)),
                    new Replacer("receiver-suffix", Utils.getSuffix(receiver)),
                    new Replacer("message", message, false, false) };
        } else {
            replacers = new Replacer[]{
                    new Replacer("sender", sender.getName()),
                    new Replacer("receiver", receiver.getName()),
                    new Replacer("message", message, false, false) };
        }

        Component senderMsg = Config.replaceFormat(Config.messageSender(), replacers);

        Component receiverMsg = Config.replaceFormat(Config.messageReceiver(), replacers);

        Component socialSpyMsg = Config.replaceFormat(Config.messageSocialSpy(), replacers);

        for (CommandSender potentialSocialSpyReceiver : socialSpyList) {
            if(!socialSpyList.contains(potentialSocialSpyReceiver)) continue;
            if(receiver==potentialSocialSpyReceiver) continue;
            if(sender==potentialSocialSpyReceiver) continue;
            potentialSocialSpyReceiver.sendMessage(socialSpyMsg);
        }
        messages.put(receiver, sender);
        messages.put(sender, receiver);

        sender.sendMessage(senderMsg);
        receiver.sendMessage(receiverMsg);
    }

}
