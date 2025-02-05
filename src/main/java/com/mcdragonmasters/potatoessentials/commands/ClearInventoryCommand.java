package com.mcdragonmasters.potatoessentials.commands;

import com.mcdragonmasters.potatoessentials.PotatoEssentials;
import dev.jorel.commandapi.CommandAPICommand;
import dev.jorel.commandapi.arguments.EntitySelectorArgument;
import org.bukkit.entity.Player;

import java.util.Collection;
import java.util.List;
import java.util.Objects;

@SuppressWarnings("unchecked")
public class ClearInventoryCommand {
    public static void register() {
        EntitySelectorArgument.ManyPlayers playerArg = new EntitySelectorArgument
                .ManyPlayers("target");

        new CommandAPICommand("clearinventory")
                .withAliases("ci")
                .withPermission(PotatoEssentials.getNameSpace()+".clearinventory")
                .withOptionalArguments(playerArg)
                .executesPlayer((sender,args) -> {
                    Collection<Player> players = args.getByArgument(playerArg) != null ?
                            Objects.requireNonNull(args.getByArgument(playerArg)) : List.of(sender);
                    for (Player player : players) {
                        player.getInventory().clear();
                    }
                    String msg = players.size()>1?((Player)players.toArray()[0]).getName()+"</yellow>'s"
                            : players.size()+" Players</yellow>'";
                    sender.sendRichMessage("<gray>Cleared<yellow> "+msg+" Inventory");
                }).register();

    }
}
