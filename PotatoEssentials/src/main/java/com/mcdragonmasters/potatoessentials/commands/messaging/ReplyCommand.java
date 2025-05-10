package com.mcdragonmasters.potatoessentials.commands.messaging;

import com.mcdragonmasters.potatoessentials.PotatoEssentials;
import com.mcdragonmasters.potatoessentials.utils.Config;
import com.mcdragonmasters.potatoessentials.utils.PotatoCommand;
import com.mcdragonmasters.potatoessentials.utils.Replacer;
import dev.jorel.commandapi.CommandAPICommand;
import dev.jorel.commandapi.arguments.Argument;
import dev.jorel.commandapi.arguments.GreedyStringArgument;
import dev.jorel.commandapi.executors.CommandArguments;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Objects;

import static com.mcdragonmasters.potatoessentials.commands.messaging.MessageToggleCommand.messagesDisabled;

public class ReplyCommand extends PotatoCommand {
    public ReplyCommand() {
        super("reply",PotatoEssentials.NAMESPACE+".message", "r");
    }

    private final Argument<String> stringArgument = new GreedyStringArgument("message");

    @Override
    public void register() {
        new CommandAPICommand(name)
                .withAliases(aliases)
                .withPermission(permission)
                .withArguments(stringArgument)
                .executes(this::execute)
                .register();
    }
    private void execute(CommandSender sender, CommandArguments args) {
        var messageMap = MessageCommand.getMessages();
        if (!messageMap.containsKey(sender)) {
            var msg = Config.replaceFormat(getMsg("noReply"));
            sender.sendMessage(msg);
            return;
        }
        String message = Objects.requireNonNull(args.getByArgument(stringArgument));
        Player lastMessagedPlayer = messageMap.get(sender);
        if (messagesDisabled.contains(lastMessagedPlayer.getUniqueId())&&
                !sender.hasPermission(NAMESPACE+".messagetoggle.bypass")) {
            var msg = Config.replaceFormat(Config.getCmdMsg("message", "messagesDisabled"),
                    new Replacer("receiver", lastMessagedPlayer.getName()));
            sender.sendMessage(msg);
            return;
        }
        MessageCommand.message(sender, message, lastMessagedPlayer);
    }
}