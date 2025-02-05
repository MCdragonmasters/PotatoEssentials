package com.mcdragonmasters.potatoessentials.commands.teleporting;
import com.mcdragonmasters.potatoessentials.PotatoEssentials;
import dev.jorel.commandapi.CommandAPICommand;
import dev.jorel.commandapi.arguments.EntitySelectorArgument;
import org.bukkit.entity.Player;

import java.util.Collection;
import java.util.List;
import java.util.Objects;

@SuppressWarnings("unchecked")
public class TeleportHereCommand {
    public static void register() {

        EntitySelectorArgument.ManyPlayers playerArg = new EntitySelectorArgument
                .ManyPlayers("target");

        new CommandAPICommand("tphere")
                .withAliases("tphere")
                .withPermission(PotatoEssentials.getNameSpace()+".tphere")
                .withArguments(playerArg)
                .executesPlayer((sender, args) -> {
                    Collection<Player> players = args.getByArgument(playerArg) != null ? Objects.requireNonNull(args.getByArgument(playerArg)) : List.of(sender);
                    for (Player player : players) {
                        player.teleport(sender);
                        player.sendRichMessage("<gray>You have been Teleported to <yellow>"+sender.getName());
                    }
                    String msg = players.size()<2?((Player) players.toArray()[0]).getName()+"</yellow>"
                            : players.size()+" Players</yellow>";
                    sender.sendRichMessage
                            ("<gray>You have Teleported <yellow>"+msg+" to <yellow>Yourself");

                })
                .register();
    }
}
