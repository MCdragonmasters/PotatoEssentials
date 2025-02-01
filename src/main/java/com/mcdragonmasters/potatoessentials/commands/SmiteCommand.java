package com.mcdragonmasters.potatoessentials.commands;

import com.mcdragonmasters.potatoessentials.PotatoEssentials;
import dev.jorel.commandapi.CommandAPICommand;
import dev.jorel.commandapi.CommandPermission;
import dev.jorel.commandapi.arguments.Argument;
import dev.jorel.commandapi.arguments.DoubleArgument;
import dev.jorel.commandapi.arguments.EntitySelectorArgument;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.Collection;
import java.util.Objects;

public class SmiteCommand {
    @SuppressWarnings("unchecked")
    public static void register() {
        EntitySelectorArgument.ManyPlayers playerArg = new EntitySelectorArgument.
                ManyPlayers("target");
        Argument<Double> doubleArgument = new DoubleArgument("damage");
        new CommandAPICommand("smite")
                .withPermission(CommandPermission.fromString(
                        PotatoEssentials.getNameSpace()+".smite"))
                .withArguments(playerArg)
                .withOptionalArguments(doubleArgument)
                .executesPlayer((commandSender, args) -> {
                    Collection<Player> players = Objects.requireNonNull(args.getByArgument(playerArg));
                    double damage = args.getByArgument(doubleArgument)!=null?
                            Objects.requireNonNull(args.getByArgument(doubleArgument)):2;
                    for (Player player : players) {
                        Location loc = player.getLocation();
                        loc.getWorld().strikeLightningEffect(loc);
                        player.damage(damage);
                    }
                })
                .register();
    }
}
