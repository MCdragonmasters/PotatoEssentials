package com.mcdragonmasters.potatoessentials.commands.messaging;

import com.mcdragonmasters.potatoessentials.PotatoEssentials;
import dev.jorel.commandapi.CommandAPICommand;
import org.bukkit.command.CommandSender;

import java.util.HashSet;
import java.util.Set;

public class SocialSpyCommand {

    public static Set<CommandSender> socialSpyPlayers = new HashSet<>();

    public static void register() {
        new CommandAPICommand("socialspy")
                .withPermission(PotatoEssentials.getNameSpace()+".socialspy")
                .executes((sender, args) -> {
                    if (!socialSpyPlayers.contains(sender)){
                        socialSpyPlayers.add(sender);
                    } else {
                        socialSpyPlayers.remove(sender);
                    }
                    boolean enabled = socialSpyPlayers.contains(sender);
                    sender.sendRichMessage("<gray>[<gold>SocialSpy<gray>] "
                        +(enabled?"<green>Enabled":"<red>Disabled"));
                }).register();
    }
}
