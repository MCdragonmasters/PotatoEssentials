package com.mcdragonmasters.potatoessentials.commands;

import com.mcdragonmasters.potatoessentials.PotatoEssentials;
import dev.jorel.commandapi.CommandAPICommand;
import dev.jorel.commandapi.arguments.Argument;
import dev.jorel.commandapi.arguments.IntegerArgument;

import java.util.Objects;

public class FlySpeedCommand {
    public static void register() {
        Argument<Integer> intArg = new IntegerArgument("speed", 1, 10);

        new CommandAPICommand("flyspeed")
                .withPermission(PotatoEssentials.NAMESPACE+".flyspeed")
                .withOptionalArguments(intArg)
                .executesPlayer((player, args) -> {
                    int speed = args.getByArgument(intArg)!=null?Objects.requireNonNull(args.getByArgument(intArg)):2;
                    float actualSpeed = speed/10.0f;
                    player.setFlySpeed(actualSpeed);
                    player.sendRichMessage("<gray>Set your fly speed to<yellow> "+speed);
                }).register();
    }
}
