package com.mcdragonmasters.potatoessentials.commands;

import com.mcdragonmasters.potatoessentials.PotatoEssentials;
import com.mcdragonmasters.potatoessentials.utils.Config;
import com.mcdragonmasters.potatoessentials.utils.Replacer;
import com.mcdragonmasters.potatoessentials.utils.Utils;
import dev.jorel.commandapi.CommandAPICommand;
import dev.jorel.commandapi.arguments.Argument;
import dev.jorel.commandapi.arguments.EntitySelectorArgument;
import dev.jorel.commandapi.arguments.IntegerArgument;
import net.kyori.adventure.text.Component;
import org.bukkit.entity.Player;

import java.util.Collection;
import java.util.List;
import java.util.Objects;

@SuppressWarnings("unchecked")
public class FeedCommand {
    public static void register() {
        EntitySelectorArgument.ManyPlayers playerArg = new EntitySelectorArgument
                .ManyPlayers("target");
        Argument<Integer> intArg = new IntegerArgument("amount");
        new CommandAPICommand("feed")
                .withPermission(PotatoEssentials.NAMESPACE + ".feed")
                .withOptionalArguments(playerArg)
                .withOptionalArguments(intArg)
                .executesPlayer((sender, args) -> {
                    Collection<Player> players = args.getByArgument(playerArg) != null ?
                            Objects.requireNonNull(args.getByArgument(playerArg)) : List.of(sender);

                    int amount = args.getByArgumentOrDefault(intArg, 20);
                    for (Player player : players) {
                        player.setFoodLevel(player.getFoodLevel()+amount);
                    }
                    Component msg = Config.replaceFormat(Config.getCmdMsg("feed", "message"),
                            new Replacer("target", Utils.playerNameFormat(players)));

                    sender.sendMessage(msg);

                }).register();
    }
}
