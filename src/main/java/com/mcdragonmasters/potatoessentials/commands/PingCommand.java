package com.mcdragonmasters.potatoessentials.commands;

import com.mcdragonmasters.potatoessentials.PotatoEssentials;
import dev.jorel.commandapi.CommandAPICommand;
import dev.jorel.commandapi.arguments.Argument;
import dev.jorel.commandapi.arguments.PlayerArgument;
import org.bukkit.entity.Player;

public class PingCommand {
    public static void register() {
        Argument<Player> playerArgument = new PlayerArgument("player");
        new CommandAPICommand("ping")
                .withPermission(PotatoEssentials.getNameSpace() + ".ping")
                .withOptionalArguments(playerArgument)
                .executesPlayer((player, args) -> {
                    Player arg = args.getByArgument(playerArgument);
                    int ping;
                    boolean isSender;
                    if (arg != null) {
                        isSender = false;
                        ping = arg.getPing();
                    } else {
                        isSender = true;
                        ping = player.getPing();
                    }
                    if (isSender) {
                        player.sendRichMessage("<yellow>Your<gray> ping is " + ping);
                    } else {
                        player.sendRichMessage("<yellow>" + arg.getName() + "'s<gray> ping is " + ping);
                    }
                }).register();
    }
}
