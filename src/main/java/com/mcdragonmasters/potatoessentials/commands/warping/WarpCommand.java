package com.mcdragonmasters.potatoessentials.commands.warping;

import com.mcdragonmasters.potatoessentials.JSONDatabase.WarpsManager;
import com.mcdragonmasters.potatoessentials.PotatoEssentials;
import dev.jorel.commandapi.CommandAPICommand;
import dev.jorel.commandapi.arguments.Argument;
import dev.jorel.commandapi.arguments.ArgumentSuggestions;
import dev.jorel.commandapi.arguments.StringArgument;
import org.bukkit.Location;

public class WarpCommand {
    public static void register() {

        Argument<String> warpNameArg = new StringArgument("warp-name")
                .replaceSuggestions(ArgumentSuggestions.strings(info ->
                    WarpsManager.getWarps()
                ));

        new CommandAPICommand("warp")
                .withPermission(PotatoEssentials.getNameSpace()+".warp")
                .withArguments(warpNameArg)
                .executesPlayer((sender, args) -> {
                    String warpName = args.getByArgument(warpNameArg);
                    Location warpLocation = WarpsManager.getWarp(warpName);
                    if (warpLocation != null) {
                        sender.teleport(warpLocation);
                        sender.sendRichMessage("<gold>Warping to <yellow>"+warpName);
                    } else {
                        sender.sendRichMessage("<red>Warp<yellow> "+warpName+"<red> is not set!");
                    }
                }).register();
    }

}
