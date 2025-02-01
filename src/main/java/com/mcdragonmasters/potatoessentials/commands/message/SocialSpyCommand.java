package com.mcdragonmasters.potatoessentials.commands.message;

import com.mcdragonmasters.potatoessentials.PotatoEssentials;
import dev.jorel.commandapi.CommandAPICommand;
import org.bukkit.command.CommandSender;

import java.util.HashMap;
import java.util.Map;

public class SocialSpyCommand {

    public static Map<CommandSender, Boolean> socialSpyEnabled = new HashMap<>();

    public static void register() {
        new CommandAPICommand("socialspy")
                .withPermission(PotatoEssentials.getNameSpace()+".socialspy")
                .executes((sender, args) -> {
                    socialSpyEnabled.putIfAbsent(sender,false);
                    boolean enabled = socialSpyEnabled.get(sender);
                    socialSpyEnabled.put(sender,!enabled);
                    sender.sendRichMessage("<gray>[<gold>SocialSpy<gray>] "+
                            (!enabled?"<green>Enabled":"<red>Disabled"));
                }).register();
    }
}
