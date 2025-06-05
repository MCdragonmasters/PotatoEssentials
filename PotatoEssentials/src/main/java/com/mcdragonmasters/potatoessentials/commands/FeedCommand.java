package com.mcdragonmasters.potatoessentials.commands;

import com.mcdragonmasters.potatoessentials.utils.Config;
import com.mcdragonmasters.potatoessentials.objects.PotatoCommand;
import com.mcdragonmasters.potatoessentials.objects.Replacer;
import com.mcdragonmasters.potatoessentials.utils.Utils;
import dev.jorel.commandapi.CommandAPICommand;
import dev.jorel.commandapi.arguments.Argument;
import dev.jorel.commandapi.arguments.EntitySelectorArgument.ManyPlayers;
import dev.jorel.commandapi.arguments.IntegerArgument;
import dev.jorel.commandapi.executors.CommandArguments;
import net.kyori.adventure.text.Component;
import org.bukkit.entity.Player;

import java.util.Collection;
import java.util.List;

@SuppressWarnings("unchecked")
public class FeedCommand extends PotatoCommand {

    public FeedCommand() {
        super("feed",NAMESPACE+".feed");
    }

    private final ManyPlayers playerArg = new ManyPlayers("target", false);
    private final Argument<Integer> intArg = new IntegerArgument("amount");

    @Override
    public void register() {
        new CommandAPICommand(name)
                .withPermission(permission)
                .withOptionalArguments(playerArg)
                .withOptionalArguments(intArg)
                .executesPlayer(this::execute)
                .register();
    }

    private void execute(Player sender, CommandArguments args) {
        Collection<Player> players = args.getByArgumentOrDefault(playerArg, List.of(sender));

        int amount = args.getByArgumentOrDefault(intArg, 20);
        for (Player player : players) {
            player.setFoodLevel(player.getFoodLevel()+amount);
        }
        Component msg = Config.replaceFormat(Config.getCmdMsg("feed", "message"),
                new Replacer("target", Utils.nameFormat(players)));

        sender.sendMessage(msg);

    }
}
