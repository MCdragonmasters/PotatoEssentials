package com.mcdragonmasters.potatoessentials.commands.messaging;

import com.mcdragonmasters.potatoessentials.PotatoEssentials;
import dev.jorel.commandapi.CommandAPICommand;
import org.bukkit.command.CommandSender;

import java.util.HashSet;
import java.util.Set;

public class SocialSpyCommand {

    public static Set<CommandSender> socialSpyList = new HashSet<>();

    public static void register() {
        new CommandAPICommand("socialspy")
                .withPermission(PotatoEssentials.NAMESPACE+".socialspy")
                .executes((sender, args) -> {
                    if (!socialSpyList.contains(sender)){
                        socialSpyList.add(sender);
                    } else {
                        socialSpyList.remove(sender);
                    }
                    boolean enabled = socialSpyList.contains(sender);
                    sender.sendRichMessage("<gray>[<gold>SocialSpy<gray>] "
                        +(enabled?"<green>Enabled":"<red>Disabled"));
                }).register();
    }
}
