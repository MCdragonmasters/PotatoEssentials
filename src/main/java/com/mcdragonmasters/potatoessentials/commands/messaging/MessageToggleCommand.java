package com.mcdragonmasters.potatoessentials.commands.messaging;

import com.mcdragonmasters.potatoessentials.PotatoEssentials;
import com.mcdragonmasters.potatoessentials.utils.Config;
import dev.jorel.commandapi.CommandAPICommand;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public class MessageToggleCommand {

    public static Set<UUID> messagesDisabled = new HashSet<>();

    public static void register() {
        new CommandAPICommand("messagetoggle")
                .withAliases("msgtoggle", "tpm")
                .withPermission(PotatoEssentials.NAMESPACE+".messagetoggle")
                .executesPlayer((player, args) -> {
                    UUID uuid = player.getUniqueId();
                    String toggle;
                    if (messagesDisabled.contains(uuid)) {
                        messagesDisabled.remove(uuid);
                        toggle = "toggledOn";
                    } else {
                        messagesDisabled.add(uuid);
                        toggle = "toggledOff";
                    }
                    var msg = Config.replaceFormat(Config.getCmdMsg("messagetoggle", toggle));
                    player.sendMessage(msg);
                }).register();

    }

}
