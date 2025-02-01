package com.mcdragonmasters.potatoessentials.commands.message;

import com.mcdragonmasters.potatoessentials.PotatoEssentials;
import com.mcdragonmasters.potatoessentials.utils.Config;
import com.mcdragonmasters.potatoessentials.utils.Utils;
import dev.jorel.commandapi.CommandAPICommand;
import dev.jorel.commandapi.arguments.Argument;
import dev.jorel.commandapi.arguments.GreedyStringArgument;
import org.bukkit.Bukkit;

import java.util.Objects;

public class BroadcastCommand {
    public static void register() {

        Argument<String> arg = new GreedyStringArgument("message");

        new CommandAPICommand("broadcast")
                .withAliases("bc")
                .withPermission(PotatoEssentials.getNameSpace()+".broadcast")
                .withArguments(arg)
                .executes((sender, args) -> {
                    String text = Objects.requireNonNull(args.getByArgument(arg));
                    String message = Config.broadcastFormat().replace("%message%",text);
                    Bukkit.broadcast(Utils.miniMessage(message));
                }).register();

    }
}
