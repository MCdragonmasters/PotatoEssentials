package com.mcdragonmasters.potatoessentials.commands.teleporting;
import com.mcdragonmasters.potatoessentials.utils.Config;
import com.mcdragonmasters.potatoessentials.objects.PotatoCommand;
import com.mcdragonmasters.potatoessentials.objects.Replacer;
import com.mcdragonmasters.potatoessentials.utils.Utils;
import dev.jorel.commandapi.CommandAPICommand;
import dev.jorel.commandapi.arguments.EntitySelectorArgument.ManyEntities;
import dev.jorel.commandapi.executors.CommandArguments;
import io.papermc.paper.entity.TeleportFlag.EntityState;
import net.kyori.adventure.text.Component;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerTeleportEvent.TeleportCause;

import java.util.Collection;
import java.util.List;

@SuppressWarnings("unchecked")
public class TeleportHereCommand extends PotatoCommand {

    public TeleportHereCommand() {
        super("tphere",NAMESPACE+".tphere", "teleporthere");
    }
    private final ManyEntities targetsArg = new ManyEntities("target", false);

    @Override
    public void register() {
        new CommandAPICommand(name)
                .withAliases(aliases)
                .withPermission(permission)
                .withArguments(targetsArg)
                .executesPlayer(this::execute)
                .register();
    }
    private void execute(Player sender, CommandArguments args) {
        Collection<Entity> entities = args.getByArgumentOrDefault(targetsArg, List.of(sender));
        Component tpedMsg = Config.replaceFormat(Config.teleportedMsg(),
                new Replacer("teleporter", sender.getName()));

        for (Entity player : entities) {
            player.teleport(sender.getLocation(), TeleportCause.COMMAND, EntityState.RETAIN_PASSENGERS);
            player.sendMessage(tpedMsg);
        }
        Component tperMsg = Config.replaceFormat(Config.teleporterMsg(),
                new Replacer("teleported", Utils.nameFormat(entities, true)));
        sender.sendMessage(tperMsg);
    }
}
