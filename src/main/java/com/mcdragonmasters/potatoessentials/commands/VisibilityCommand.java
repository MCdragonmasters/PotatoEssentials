package com.mcdragonmasters.potatoessentials.commands;

import com.mcdragonmasters.potatoessentials.PotatoEssentials;
import dev.jorel.commandapi.CommandAPICommand;
import dev.jorel.commandapi.CommandPermission;
import dev.jorel.commandapi.arguments.Argument;
import dev.jorel.commandapi.arguments.MultiLiteralArgument;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
//TODO: add messages
public class VisibilityCommand {
    private final static Map<UUID, String> playerVisibility = new HashMap<>();
    public static void register() {
        Argument<String> visArg = new MultiLiteralArgument("visArg",
                "all", "none", "staff");
        String nameSpace = PotatoEssentials.getNameSpace();
        new CommandAPICommand("visibility")
                .withPermission(CommandPermission.fromString(nameSpace + ".visibility"))
                .withArguments(visArg)
                .executesPlayer((p, args) -> {
                    Plugin instance = PotatoEssentials.getInstance();
                    switch (args.getByArgument(visArg)) {
                        case "all":
                            for (Player loopedPlayer : Bukkit.getOnlinePlayers()) {
                                p.showPlayer(instance, loopedPlayer);
                            }
                            playerVisibility.put(p.getUniqueId(), "all");
                        case "none":
                            for (Player loopedPlayer : Bukkit.getOnlinePlayers()) {
                                p.hidePlayer(instance, loopedPlayer);
                            }
                            playerVisibility.put(p.getUniqueId(), "none");
                        case "staff":
                            for (Player loopedPlayer : Bukkit.getOnlinePlayers()) {
                                if (loopedPlayer.isOp() || loopedPlayer.hasPermission(nameSpace + ".staff")) {
                                    p.showPlayer(instance, loopedPlayer);
                                } else {
                                    p.hidePlayer(instance, loopedPlayer);
                                }
                            }
                            playerVisibility.put(p.getUniqueId(), "staff");

                        case null, default:
                            Bukkit.broadcast(Component.text("Visibility command error"));
                    }
                })
                .register();
    }
    public static Map<UUID, String> getPlayerVisibility() {
        return playerVisibility;
    }
}
