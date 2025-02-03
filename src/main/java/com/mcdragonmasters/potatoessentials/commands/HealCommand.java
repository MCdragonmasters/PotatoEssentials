package com.mcdragonmasters.potatoessentials.commands;

import com.mcdragonmasters.potatoessentials.PotatoEssentials;
import dev.jorel.commandapi.CommandAPICommand;
import dev.jorel.commandapi.arguments.Argument;
import dev.jorel.commandapi.arguments.DoubleArgument;
import dev.jorel.commandapi.arguments.EntitySelectorArgument;
import org.bukkit.entity.Player;

import java.util.Collection;
import java.util.List;
import java.util.Objects;

@SuppressWarnings("unchecked")
public class HealCommand {
    public static void register() {
        EntitySelectorArgument.ManyPlayers playerArg = new EntitySelectorArgument
                .ManyPlayers("target");
        Argument<Double> doubleArg = new DoubleArgument("amount");
        new CommandAPICommand("heal")
                .withPermission(PotatoEssentials.getNameSpace() + ".heal")
                .withOptionalArguments(playerArg)
                .withOptionalArguments(doubleArg)
                .executesPlayer((sender, args) -> {
                    Collection<Player> players = args.getByArgument(playerArg) != null ?
                            Objects.requireNonNull(args.getByArgument(playerArg)) : List.of(sender);

                    double amount = args.getByArgument(doubleArg) != null ?
                            Objects.requireNonNull(args.getByArgument(doubleArg)) : 20;

                    for (Player player : players) {
                        player.heal(amount);
                    }
                    String msg = players.size()==1?((Player) players.toArray()[0]).getName()
                            : players.size()+" Players";

                    sender.sendRichMessage("<green>Successfully<gray> healed <yellow>"+msg);

                }).register();
    }
}
