package com.mcdragonmasters.potatoessentials.commands.messaging;

import com.mcdragonmasters.potatoessentials.utils.Config;
import com.mcdragonmasters.potatoessentials.utils.PotatoCommand;
import com.mcdragonmasters.potatoessentials.utils.Replacer;
import dev.jorel.commandapi.CommandAPICommand;
import dev.jorel.commandapi.arguments.Argument;
import dev.jorel.commandapi.arguments.GreedyStringArgument;
import dev.jorel.commandapi.executors.CommandArguments;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;

import java.util.Objects;

public class BroadcastCommand extends PotatoCommand {
    public BroadcastCommand() {
        super("broadcast", "bc");
        setPermission(NAMESPACE+".broadcast");
    }

    private final Argument<String> arg = new GreedyStringArgument("message");

    @Override
    public void register() {
        new CommandAPICommand(name)
                .withAliases(aliases)
                .withPermission(permission)
                .withArguments(arg)
                .executes(this::execute)
                .register();

    }
    private void execute(CommandSender sender, CommandArguments args) {
        String text = Objects.requireNonNull(args.getByArgument(arg));
        Component message = Config.replaceFormat(Config.broadcastFormat(),
                new Replacer("message", text, true));
        Bukkit.broadcast(message);
    }
}
