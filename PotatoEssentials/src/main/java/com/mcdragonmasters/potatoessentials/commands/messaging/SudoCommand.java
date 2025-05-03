package com.mcdragonmasters.potatoessentials.commands.messaging;

import com.mcdragonmasters.potatoessentials.utils.Config;
import com.mcdragonmasters.potatoessentials.utils.PotatoCommand;
import com.mcdragonmasters.potatoessentials.utils.Replacer;
import com.mcdragonmasters.potatoessentials.utils.Utils;
import dev.jorel.commandapi.CommandAPICommand;
import dev.jorel.commandapi.arguments.Argument;
import dev.jorel.commandapi.arguments.EntitySelectorArgument;
import dev.jorel.commandapi.arguments.GreedyStringArgument;
import dev.jorel.commandapi.executors.CommandArguments;
import net.kyori.adventure.text.Component;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Collection;
import java.util.Objects;

@SuppressWarnings({"unchecked", "DataFlowIssue"})
public class SudoCommand extends PotatoCommand {

    public SudoCommand() {
        super("sudo");
        setPermission(NAMESPACE+".sudo");
    }

    private final EntitySelectorArgument.ManyPlayers playerArg = new EntitySelectorArgument
            .ManyPlayers("target", false);

    private final Argument<String> stringArg = new GreedyStringArgument("input");

    @Override
    public void register() {
        new CommandAPICommand(name)
                .withPermission(permission)
                .withArguments(playerArg)
                .withArguments(stringArg)
                .executes(this::execute)
                .register();
    }
    private void execute(CommandSender sender, CommandArguments args) {
        Collection<Player> players = args.getByArgument(playerArg);
        String text = Objects.requireNonNull(args.getByArgument(stringArg));
        boolean isChat = (text.toLowerCase().startsWith("c:"));
        String otherMsg = Utils.playerNameFormat(players);
        Component msg = Config.replaceFormat(isChat?Config.sudoSayMsg():Config.sudoCmdMsg(),
                new Replacer("sudoed-players", otherMsg), new Replacer("input", text));
        sender.sendMessage(msg);
        if (isChat) {
            text = text.substring(2);
            for (Player player : players) player.chat(text);
        } else for (Player player : players) player.performCommand(text);

    }
}
