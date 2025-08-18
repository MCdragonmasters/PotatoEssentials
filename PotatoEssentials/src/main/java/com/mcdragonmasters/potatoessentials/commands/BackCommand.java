package com.mcdragonmasters.potatoessentials.commands;

import com.mcdragonmasters.potatoessentials.PotatoEssentials;
import com.mcdragonmasters.potatoessentials.listeners.PlayerTeleportListener;
import com.mcdragonmasters.potatoessentials.utils.Config;
import com.mcdragonmasters.potatoessentials.objects.PotatoCommand;
import dev.jorel.commandapi.CommandAPICommand;
import dev.jorel.commandapi.executors.CommandArguments;
import io.papermc.paper.entity.TeleportFlag.EntityState;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerTeleportEvent.TeleportCause;

import static com.mcdragonmasters.potatoessentials.PotatoEssentials.pluginManager;
import static com.mcdragonmasters.potatoessentials.listeners.PlayerTeleportListener.lastLocation;

public class BackCommand extends PotatoCommand {
    public BackCommand() {
        super("back", NAMESPACE+".back");
    }
    @Override
    public void register() {
        pluginManager.registerEvents(new PlayerTeleportListener(), PotatoEssentials.INSTANCE);

        new CommandAPICommand(name)
                .withPermission(permission)
                .executesPlayer(this::execute)
                .register();
    }
    private void execute(Player player, CommandArguments args) {
        if (lastLocation.get(player)!=null) {
            var msg = Config.replaceFormat(Config.getCmdMsg("back", "message"));
            player.sendMessage(msg);
            player.teleport(lastLocation.get(player), TeleportCause.COMMAND, EntityState.RETAIN_PASSENGERS);
        } else player.sendRichMessage("<red>No last location found!");
    }
}
