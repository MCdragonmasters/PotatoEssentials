package com.mcdragonmasters.potatoessentials.commands;

import com.mcdragonmasters.potatoessentials.PotatoEssentials;
import com.mcdragonmasters.potatoessentials.listeners.PlayerTeleportListener;
import dev.jorel.commandapi.CommandAPICommand;

import static com.mcdragonmasters.potatoessentials.PotatoEssentials.pluginManager;
import static com.mcdragonmasters.potatoessentials.listeners.PlayerTeleportListener.lastLocation;

public class BackCommand {
    public static void register() {
        pluginManager.registerEvents(new PlayerTeleportListener(), PotatoEssentials.getInstance());

        new CommandAPICommand("back")
                .withPermission(PotatoEssentials.getNameSpace()+".back")
                .executesPlayer((player, args) -> {
                    if (lastLocation.get(player)!=null) {
                        //player.sendRichMessage("");
                        player.teleport(lastLocation.get(player));
                    } else player.sendRichMessage("<red>No last location found!");
                }).register();
    }
}
