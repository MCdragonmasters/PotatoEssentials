package com.mcdragonmasters.potatoessentials.commands;

import com.mcdragonmasters.potatoessentials.PotatoEssentials;
import com.mcdragonmasters.potatoessentials.listeners.PlayerTeleportListener;
import com.mcdragonmasters.potatoessentials.utils.Config;
import dev.jorel.commandapi.CommandAPICommand;

import static com.mcdragonmasters.potatoessentials.PotatoEssentials.pluginManager;
import static com.mcdragonmasters.potatoessentials.listeners.PlayerTeleportListener.lastLocation;

public class BackCommand {
    public static void register() {
        pluginManager.registerEvents(new PlayerTeleportListener(), PotatoEssentials.INSTANCE);

        new CommandAPICommand("back")
                .withPermission(PotatoEssentials.NAMESPACE+".back")
                .executesPlayer((player, args) -> {
                    if (lastLocation.get(player)!=null) {
                        var msg = Config.replaceFormat(Config.getCmdMsg("back", "message"));
                        player.sendMessage(msg);
                        player.teleport(lastLocation.get(player));
                    } else player.sendRichMessage("<red>No last location found!");
                }).register();
    }
}
