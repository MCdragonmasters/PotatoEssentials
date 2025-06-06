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

        //TODO: make these messages configurable
        if (channel.getKey().equals("global")) {
            player.sendMessage(Config.replaceFormat(
                    "<red>Cannot toggle <global> channel",
                    new Replacer("global", channel.getName())
            ));
            return;
        }
        if (channel.equals(CustomChat.getPlayerChat().get(player))) {
            player.sendMessage(Config.replaceFormat(
                    "<gray>Toggled current channel, moved to <global>",
                    new Replacer("global", CustomChat.getChatMap().get("global").getName())
            ));
            CustomChat.getPlayerChat().remove(player);
        }
        boolean ignored = ignoredChannels.add(channel);
        if (!ignored) ignoredChannels.remove(channel);
        player.sendMessage(Config.replaceFormat(
                getMsg(ignored ? "toggledOff" : "toggledOn"),
                new Replacer("channel", channel.getName())
        ));
    }

}
