package com.mcdragonmasters.potatoessentials.commands.warping;

import com.mcdragonmasters.potatoessentials.JSONDatabase.WarpsManager;
import com.mcdragonmasters.potatoessentials.PotatoEssentials;
import com.mcdragonmasters.potatoessentials.utils.Utils;
import dev.jorel.commandapi.CommandAPICommand;
import dev.jorel.commandapi.arguments.Argument;
import dev.jorel.commandapi.arguments.StringArgument;
import org.bukkit.Location;

public class SetWarpCommand {
    public static void register() {

        Argument<String> warpNameArg = new StringArgument("warp-name");

        new CommandAPICommand("setwarp")
                .withPermission(PotatoEssentials.getNameSpace()+".setwarp")
                .withArguments(warpNameArg)
                .executesPlayer((sender, args) -> {
                    String warpName = args.getByArgument(warpNameArg);
                    Location loc = sender.getLocation();
                    WarpsManager.removeWarp(warpName);
                    WarpsManager.saveWarp(warpName,loc);
                    sender.sendRichMessage(
                            "<gray>Set Warp <yellow>'"+warpName+"'</yellow> to <yellow>"+
                                    Utils.formatCoords(loc.getX(),loc.getZ(),loc.getZ()));
                })
                .register();
    }
}
