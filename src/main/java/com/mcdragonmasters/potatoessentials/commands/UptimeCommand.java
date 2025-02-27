package com.mcdragonmasters.potatoessentials.commands;

import com.mcdragonmasters.potatoessentials.PotatoEssentials;
import dev.jorel.commandapi.CommandAPICommand;

import java.lang.management.ManagementFactory;
import java.time.Duration;

import java.time.Instant;

public class UptimeCommand {
    public static void register() {
        new CommandAPICommand("uptime")
                .withPermission(PotatoEssentials.getNameSpace()+".uptime")
                .executes((sender, args) -> {
                    sender.sendMessage(timeSinceBoot());
                })
                .register();
    }
    public static String timeSinceBoot() {
        var idk = Instant.ofEpochSecond(ManagementFactory.getRuntimeMXBean().getUptime());
        var dur = Duration.between(idk, Instant.now());
        return dur.toString();
    }
}
