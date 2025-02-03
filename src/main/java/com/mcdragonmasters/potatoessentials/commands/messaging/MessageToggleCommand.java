package com.mcdragonmasters.potatoessentials.commands.messaging;

import com.mcdragonmasters.potatoessentials.PotatoEssentials;
import dev.jorel.commandapi.CommandAPICommand;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public class MessageToggleCommand {

    public static Set<UUID> messagesDisabled = new HashSet<>();

    public static void register() {
        new CommandAPICommand("messagetoggle")
                .withAliases("msgtoggle")
                .withPermission(PotatoEssentials.getNameSpace()+".messagetoggle")
                .executesPlayer((player, args) -> {
                    UUID uuid = player.getUniqueId();
                    String message;
                    if (messagesDisabled.contains(uuid)) {
                        messagesDisabled.remove(uuid);
                        message = "<red>Disabled";
                    } else {
                        messagesDisabled.add(uuid);
                        message = "<green>Enabled";
                    }
                    player.sendRichMessage("<gray>Private Messages have been "+message);
                }).register();

    }

}
