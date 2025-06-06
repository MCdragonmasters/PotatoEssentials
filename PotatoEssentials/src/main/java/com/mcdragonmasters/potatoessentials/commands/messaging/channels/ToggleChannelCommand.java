package com.mcdragonmasters.potatoessentials.commands.messaging.channels;

import com.mcdragonmasters.potatoessentials.commands.messaging.channels.ChannelCommand.ChannelArgument;
import com.mcdragonmasters.potatoessentials.objects.PotatoCommand;
import com.mcdragonmasters.potatoessentials.objects.Replacer;
import com.mcdragonmasters.potatoessentials.utils.Config;
import com.mcdragonmasters.potatoessentials.objects.CustomChat;
import dev.jorel.commandapi.arguments.Argument;
import dev.jorel.commandapi.executors.CommandArguments;
import org.bukkit.entity.Player;

import java.util.Objects;
import java.util.Set;

public class ToggleChannelCommand extends PotatoCommand {
    public ToggleChannelCommand() {
        super("togglechannel", NAMESPACE+".togglechannel", "togglechat");
    }

//                Set<String> chats = new HashSet<>();
//                for (CustomChat chat : CustomChat.getCustomChats()) {
//                    if (info.sender().hasPermission(chat.getPermission())) chats.add(chat.getKey());
//                }

    private final Argument<CustomChat> channelArgument = new ChannelArgument("channel");

    @Override
    public void register() {
        createCommand()
                .withArguments(channelArgument)
                .executesPlayer(this::execute)
                .register();
    }

    private void execute(Player player, CommandArguments args) {
        Set<CustomChat> ignoredChannels = CustomChat.getPlayerIgnoredChannels(player);
        CustomChat channel = args.getByArgument(channelArgument);
        Objects.requireNonNull(channel);
        boolean ignored = ignoredChannels.add(channel);
        if (!ignored) ignoredChannels.remove(channel);
        player.sendMessage(Config.replaceFormat(
                getMsg(ignored ? "toggledOff" : "toggledOn"),
                new Replacer("channel", channel.getName())
        ));
    }

}
