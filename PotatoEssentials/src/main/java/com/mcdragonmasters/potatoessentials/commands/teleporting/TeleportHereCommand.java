package com.mcdragonmasters.potatoessentials.commands.teleporting;
import com.mcdragonmasters.potatoessentials.utils.Config;
import com.mcdragonmasters.potatoessentials.utils.PotatoCommand;
import com.mcdragonmasters.potatoessentials.utils.Replacer;
import com.mcdragonmasters.potatoessentials.utils.Utils;
import dev.jorel.commandapi.CommandAPICommand;
import dev.jorel.commandapi.arguments.EntitySelectorArgument;
import dev.jorel.commandapi.executors.CommandArguments;
import net.kyori.adventure.text.Component;
import org.bukkit.entity.Player;

import java.util.Collection;
import java.util.List;

@SuppressWarnings("unchecked")
public class TeleportHereCommand extends PotatoCommand {

    public TeleportHereCommand() {
        super("tphere", "teleporthere");
        setPermission(NAMESPACE+".tphere");
    }
    private final EntitySelectorArgument.ManyPlayers playerArg = new EntitySelectorArgument
            .ManyPlayers("target", false);

    @Override
    public void register() {
        new CommandAPICommand(name)
                .withAliases(aliases)
                .withPermission(permission)
                .withArguments(playerArg)
                .executesPlayer(this::execute)
                .register();
    }
    private void execute(Player sender, CommandArguments args) {
        Collection<Player> players = args.getByArgumentOrDefault(playerArg, List.of(sender));
        Component tpedMsg = Config.replaceFormat(Config.teleportedMsg(),
                new Replacer("teleporter", sender.getName()));

        for (Player player : players) {
            player.teleport(sender);
            player.sendMessage(tpedMsg);
        }
        String msg = Utils.playerNameFormat(players);
        Component tperMsg = Config.replaceFormat(Config.teleporterMsg(),
                new Replacer("teleported", msg));
        sender.sendMessage(tperMsg);
    }
}
