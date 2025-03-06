package com.mcdragonmasters.potatoessentials.commands.teleporting;
import com.mcdragonmasters.potatoessentials.PotatoEssentials;
import com.mcdragonmasters.potatoessentials.utils.Config;
import com.mcdragonmasters.potatoessentials.utils.Replacer;
import dev.jorel.commandapi.CommandAPICommand;
import dev.jorel.commandapi.arguments.EntitySelectorArgument;
import net.kyori.adventure.text.Component;
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
                .withPermission(PotatoEssentials.NAMESPACE+".tphere")
                .withArguments(playerArg)
                .executesPlayer((sender, args) -> {
                    Collection<Player> players = args.getByArgument(playerArg) != null ? Objects.requireNonNull(args.getByArgument(playerArg)) : List.of(sender);
                    Component tpedMsg = Config.replaceFormat(Config.teleportedMsg(),
                            new Replacer("teleporter", sender.getName()));

                    for (Player player : players) {
                        player.teleport(sender);
                        player.sendMessage(tpedMsg);
                    }
                    String msg = players.size()<2?((Player) players.toArray()[0]).getName()
                            : players.size()+" Players";
                    Component tperMsg = Config.replaceFormat(Config.teleporterMsg(),
                            new Replacer("teleported", msg));
                    sender.sendMessage(tperMsg);
                })
                .register();
    }
}
