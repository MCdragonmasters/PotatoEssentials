package com.mcdragonmasters.potatoessentials.commands;

import com.mcdragonmasters.potatoessentials.PotatoEssentials;
import dev.jorel.commandapi.CommandAPICommand;
import dev.jorel.commandapi.arguments.Argument;
import dev.jorel.commandapi.arguments.EntitySelectorArgument;
import dev.jorel.commandapi.arguments.IntegerArgument;
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
                .withPermission(PotatoEssentials.getNameSpace() + ".feed")
                .withOptionalArguments(playerArg)
                .withOptionalArguments(intArg)
                .executesPlayer((sender, args) -> {
                    Collection<Player> players = args.getByArgument(playerArg) != null ?
                            Objects.requireNonNull(args.getByArgument(playerArg)) : List.of(sender);

                    int amount = args.getByArgument(intArg) != null ?
                            Objects.requireNonNull(args.getByArgument(intArg)) : 20;

                    for (Player player : players) {
                        player.setFoodLevel(player.getFoodLevel()+amount);
                    }
                    String msg = players.size()==1?((Player) players.toArray()[0]).getName()
                            : players.size()+" Players";

                    sender.sendRichMessage("<green>Successfully<gray> fed<yellow> "+msg);

                }).register();
    }
}
