package com.mcdragonmasters.potatoessentials.commands;
import dev.jorel.commandapi.CommandAPICommand;
import dev.jorel.commandapi.CommandPermission;
import dev.jorel.commandapi.arguments.Argument;
import dev.jorel.commandapi.arguments.MultiLiteralArgument;
import org.bukkit.GameMode;

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
                        case "creative":
                            p.setGameMode(GameMode.CREATIVE);
                        case "spectator":
                            p.setGameMode(GameMode.SPECTATOR);
                        case "survival":
                            p.setGameMode(GameMode.SURVIVAL);
                        case null, default:
                            break;
                    }
                    p.sendRichMessage("<gray>Set Your Gamemode to<yellow> " + p.getGameMode().name());
                })
                .register();
        new CommandAPICommand("gm")
                .withPermission(permission)
                .withArguments(gmArg)
                .executesPlayer((p, args) -> {
                    switch (args.getByArgument(gmArg)) {
                        case "a", "2":
                            p.setGameMode(GameMode.ADVENTURE);
                        case "c", "1":
                            p.setGameMode(GameMode.CREATIVE);
                        case "sp", "3":
                            p.setGameMode(GameMode.SPECTATOR);
                        case "s", "0":
                            p.setGameMode(GameMode.SURVIVAL);
                        case null, default:
                            break;
                    }
                    p.sendRichMessage("<gray>Set Your Gamemode to<yellow> " + p.getGameMode().name());
                })
                .register();
    }
}
