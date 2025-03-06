package com.mcdragonmasters.potatoessentials.commands.warping;

import com.mcdragonmasters.potatoessentials.database.WarpsManager;
import com.mcdragonmasters.potatoessentials.PotatoEssentials;
import dev.jorel.commandapi.CommandAPICommand;
import dev.jorel.commandapi.arguments.Argument;
import dev.jorel.commandapi.arguments.StringArgument;

public class DelWarpCommand {
    public static void register() {

        Argument<String> warpNameArg = new StringArgument("warp-name");

        new CommandAPICommand("deletewarp")
                .withAliases("delwarp")
                .withPermission(PotatoEssentials.NAMESPACE+".deletewarp")
                .withArguments(warpNameArg)
                .executesPlayer((sender, args) -> {
                    String warpName = args.getByArgument(warpNameArg);
                    WarpsManager.removeWarp(warpName);
                    sender.sendRichMessage("<gray>Successfully <red>deleted</red> Warp <yellow>'"+warpName+"'");
                })
                .register();
    }
}
