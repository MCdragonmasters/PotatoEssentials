package com.mcdragonmasters.potatoessentials.commands.messaging;

import com.mcdragonmasters.potatoessentials.PotatoEssentials;
import com.mcdragonmasters.potatoessentials.utils.Config;
import com.mcdragonmasters.potatoessentials.utils.Replacer;
import dev.jorel.commandapi.CommandAPICommand;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;

public class MuteChatCommand {

    public static boolean chatMuted = false;

    public static void register() {

        new CommandAPICommand("mutechat")
                .withPermission(PotatoEssentials.NAMESPACE+".mutechat")
                .executes((sender, args) -> {
                    chatMuted = !chatMuted;
                    Component msg = Config.replaceFormat(chatMuted?Config.muteChatMutedMsg()
                                    : Config.muteChatUnmutedMsg(),
                            new Replacer("sender", sender.getName()));
                    Bukkit.broadcast(msg);
                }).register();
    }
}
