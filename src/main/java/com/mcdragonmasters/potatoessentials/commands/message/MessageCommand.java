package com.mcdragonmasters.potatoessentials.commands.message;

import com.mcdragonmasters.potatoessentials.PotatoEssentials;
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

import static com.mcdragonmasters.potatoessentials.commands.message.SocialSpyCommand.socialSpyEnabled;

public class MessageCommand {

    @Getter
    private static final Map<Player, Player> messageMap = new HashMap<>();

    public static void register() {
        CommandAPI.unregister("message");
        CommandAPI.unregister("msg");

        Argument<Player> playerArgument = new EntitySelectorArgument.OnePlayer("player");
        Argument<String> stringArgument = new GreedyStringArgument("message");
        new CommandAPICommand("message")
                .withAliases("msg")
                .withPermission(PotatoEssentials.getNameSpace()+".message")
                .withArguments(playerArgument)
                .withArguments(stringArgument)
                .executesPlayer((player, args) -> {
                    Player receiver = Objects.requireNonNull(args.getByArgument(playerArgument));
                    String message = Objects.requireNonNull(args.getByArgument(stringArgument));
                    message(player, message, receiver);
                }).register();
    }

    public static void message(Player sender, String message, Player receiver) {

        Component msg = Component.text(message.trim());

        Component senderMsg = Utils.miniMessage
                ("<gold>You <gray>» "+receiver.getName()+"<reset>: ").append(msg);
        Component receiverMsg = Utils.miniMessage
                ("<gray>"+sender.getName()+" » <gold>You<reset>: ").append(msg);
        Component socialSpyMessage = Utils.miniMessage
                ("<gray>[<gold>SS</gold>] » <yellow>"+sender.getName()+
                        "</yellow> -> <yellow>"+receiver.getName()+"</yellow>:</gray><newline>").append(msg);
        for (CommandSender socialSpyReceiver : socialSpyEnabled.keySet()) {
            if(!socialSpyEnabled.get(socialSpyReceiver)) continue;
            socialSpyReceiver.sendMessage(socialSpyMessage);
        }
        messageMap.put(sender,receiver);

        sender.sendMessage(senderMsg);
        receiver.sendMessage(receiverMsg);
    }

}
