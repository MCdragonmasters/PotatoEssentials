package com.mcdragonmasters.potatoessentials.commands.messaging;

import com.mcdragonmasters.potatoessentials.PotatoEssentials;
import com.mcdragonmasters.potatoessentials.utils.Config;
import com.mcdragonmasters.potatoessentials.utils.PotatoCommand;
import com.mcdragonmasters.potatoessentials.utils.Replacer;

import com.mcdragonmasters.potatoessentials.utils.Utils;
import dev.jorel.commandapi.CommandAPI;
import dev.jorel.commandapi.CommandAPICommand;
import dev.jorel.commandapi.arguments.Argument;
import dev.jorel.commandapi.arguments.EntitySelectorArgument;
import dev.jorel.commandapi.arguments.GreedyStringArgument;
import dev.jorel.commandapi.executors.CommandArguments;
import lombok.Getter;
import net.kyori.adventure.text.Component;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import static com.mcdragonmasters.potatoessentials.commands.messaging.MessageToggleCommand.messagesDisabled;
import static com.mcdragonmasters.potatoessentials.commands.messaging.SocialSpyCommand.socialSpyList;

public class MessageCommand extends PotatoCommand {
    public MessageCommand() {
        super("message","msg","whisper", "w");
        setPermission(NAMESPACE+".message");
    }

    @Getter
    private static final Map<CommandSender, Player> messages = new HashMap<>();

    private final Argument<Player> playerArgument = new EntitySelectorArgument.OnePlayer("player");
    private final Argument<String> stringArgument = new GreedyStringArgument("message");

    @Override
    public void register() {
        CommandAPI.unregister("message");
        CommandAPI.unregister("msg");
        CommandAPI.unregister("whisper");
        CommandAPI.unregister("w");

        new CommandAPICommand(name)
                .withAliases(aliases)
                .withPermission(permission)
                .withArguments(playerArgument)
                .withArguments(stringArgument)
                .executes(this::execute)
                .register();
    }

    private void execute(CommandSender sender, CommandArguments args) {
        Player receiver = Objects.requireNonNull(args.getByArgument(playerArgument));
        if (messagesDisabled.contains(receiver.getUniqueId())&&
                !sender.hasPermission(NAMESPACE+".messagetoggle.bypass")) {
            sender.sendRichMessage("<red>"+receiver.getName()+"'s messages are disabled");
            return;
        }
        String message = Objects.requireNonNull(args.getByArgument(stringArgument));
        message(sender, message, receiver);
    }

    public static void message(CommandSender sender, String message, Player receiver) {
        message = Utils.formatChatMessage(sender, message);
        Replacer[] replacers = new Replacer[]{
                new Replacer("sender", sender.getName()),
                new Replacer("sender-prefix", Utils.getPrefix(sender)),
                new Replacer("sender-suffix", Utils.getSuffix(sender)),
                new Replacer("receiver", receiver.getName()),
                new Replacer("receiver-prefix", Utils.getPrefix(receiver)),
                new Replacer("receiver-suffix", Utils.getSuffix(receiver)),
                new Replacer("message", message, false) };

        Component senderMsg = Config.replaceFormat(Config.messageSender(), replacers);

        Component receiverMsg = Config.replaceFormat(Config.messageReceiver(), replacers);

        Component socialSpyMsg = Config.replaceFormat(Config.messageSocialSpy(), replacers);

        for (CommandSender potentialSocialSpyReceiver : socialSpyList) {
            if(!socialSpyList.contains(potentialSocialSpyReceiver)) continue;
            if(receiver==potentialSocialSpyReceiver) continue;
            if(sender==potentialSocialSpyReceiver) continue;
            potentialSocialSpyReceiver.sendMessage(socialSpyMsg);
        }
        if (sender instanceof Player senderP) {
            messages.put(receiver, senderP);
        }
        messages.put(sender, receiver);

        sender.sendMessage(senderMsg);
        receiver.sendMessage(receiverMsg);
    }

}
