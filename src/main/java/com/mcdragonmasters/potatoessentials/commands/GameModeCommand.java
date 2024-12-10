package com.mcdragonmasters.potatoessentials.commands;
import dev.jorel.commandapi.CommandAPICommand;
import dev.jorel.commandapi.CommandPermission;
import dev.jorel.commandapi.arguments.Argument;
import dev.jorel.commandapi.arguments.MultiLiteralArgument;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;

import static com.mcdragonmasters.potatoessentials.PotatoEssentials.prefixMini;

public class GameModeCommand {
    public static void register() {
        Argument<String> gameModeArg = new MultiLiteralArgument("gameModeArg",
                "adventure", "creative", "spectator", "survival");
        Argument<String> gmArg = new MultiLiteralArgument("gmArg",
                "a", "c", "sp", "s", "0", "1", "2", "3");
        CommandPermission permission = CommandPermission.fromString("potatoessentials.gamemode");
        new CommandAPICommand("gamemode")
                .withPermission(permission)
                .withArguments(gameModeArg)
                .executesPlayer((p, args) -> {
                    switch (args.getByArgument(gameModeArg)) {
                        case "adventure":
                            p.setGameMode(GameMode.ADVENTURE);
                            break;
                        case "creative":
                            p.setGameMode(GameMode.CREATIVE);
                            break;
                        case "spectator":
                            p.setGameMode(GameMode.SPECTATOR);
                            break;
                        case "survival":
                            p.setGameMode(GameMode.SURVIVAL);
                            break;
                        case null, default:
                            Bukkit.broadcast(Component.text("Err: idk"));
                            break;
                    }
                    sendGameModeMessage(p, p.getGameMode().translationKey());
                })
                .register();
        new CommandAPICommand("gm")
                .withPermission(permission)
                .withArguments(gmArg)
                .executesPlayer((p, args) -> {
                    switch (args.getByArgument(gmArg)) {
                        case "a", "2":
                            p.setGameMode(GameMode.ADVENTURE);
                            break;
                        case "c", "1":
                            p.setGameMode(GameMode.CREATIVE);
                            break;
                        case "sp", "3":
                            p.setGameMode(GameMode.SPECTATOR);
                            break;
                        case "s", "0":
                            p.setGameMode(GameMode.SURVIVAL);
                            break;
                        case null, default:
                            Bukkit.broadcast(Component.text("Err: idk"));
                            break;
                    }
                    sendGameModeMessage(p, p.getGameMode().translationKey());
                })
                .register();
        new CommandAPICommand("gma")
                .withPermission(permission)
                .executesPlayer((p, args) -> {
                    p.setGameMode(GameMode.ADVENTURE);
                    sendGameModeMessage(p, p.getGameMode().translationKey());
                })
                .register();
        new CommandAPICommand("gmc")
                .withPermission(permission)
                .executesPlayer((p, args) -> {
                    p.setGameMode(GameMode.CREATIVE);
                    sendGameModeMessage(p, p.getGameMode().translationKey());
                })
                .register();
        new CommandAPICommand("gmsp")
                .withPermission(permission)
                .executesPlayer((p, args) -> {
                    p.setGameMode(GameMode.SPECTATOR);
                    sendGameModeMessage(p, p.getGameMode().translationKey());
                })
                .register();
        new CommandAPICommand("gms")
                .withPermission(permission)
                .executesPlayer((p, args) -> {
                    p.setGameMode(GameMode.SURVIVAL);
                    sendGameModeMessage(p, p.getGameMode().translationKey());
                })
                .register();
    }
    private static void sendGameModeMessage(Player p, String translationKey) {
        p.sendRichMessage(prefixMini +"<gray> Set Your Gamemode to<yellow> <translate:"+translationKey+">");
    }
}
