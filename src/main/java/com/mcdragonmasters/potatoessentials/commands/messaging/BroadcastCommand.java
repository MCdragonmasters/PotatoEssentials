package com.mcdragonmasters.potatoessentials.commands.messaging;

import com.mcdragonmasters.potatoessentials.PotatoEssentials;
import com.mcdragonmasters.potatoessentials.utils.Config;
import com.mcdragonmasters.potatoessentials.utils.Replacer;
import dev.jorel.commandapi.CommandAPICommand;
import dev.jorel.commandapi.arguments.Argument;
import dev.jorel.commandapi.arguments.GreedyStringArgument;
import net.kyori.adventure.text.Component;
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
                    Component message = Config.replaceFormat(Config.broadcastFormat(),
                            new Replacer("message", text, true));
                    Bukkit.broadcast(message);
                }).register();

    }
}
