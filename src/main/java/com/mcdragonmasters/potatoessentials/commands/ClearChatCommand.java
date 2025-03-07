package com.mcdragonmasters.potatoessentials.commands;

import com.mcdragonmasters.potatoessentials.PotatoEssentials;
import com.mcdragonmasters.potatoessentials.utils.Config;
import com.mcdragonmasters.potatoessentials.utils.PotatoCommand;
import com.mcdragonmasters.potatoessentials.utils.Replacer;
import dev.jorel.commandapi.annotations.Command;
import dev.jorel.commandapi.annotations.Default;
import dev.jorel.commandapi.annotations.Permission;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

@Command("clearchat")
@Permission(PotatoEssentials.NAMESPACE+".clearchat")
public class ClearChatCommand extends PotatoCommand {
    @Override
    public String getName() { return "clearchat"; }

    @Default
    public static void clearChat(CommandSender sender) {
        String lineBreaks = "\n ".repeat(256);
        for (Player player : Bukkit.getOnlinePlayers()) {
            player.sendMessage(lineBreaks);
        }
        var msg = Config.replaceFormat(Config.getCmdMsg("clearchat", "message"),
                new Replacer("name", sender.getName()));
        Bukkit.broadcast(msg);
    }
}
