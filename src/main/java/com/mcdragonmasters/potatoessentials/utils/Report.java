package com.mcdragonmasters.potatoessentials.utils;

import lombok.Getter;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

@Getter
public class Report {
    private final Player reported;
    private final CommandSender reporter;
    private final String reason;

    public Report(Player reported, CommandSender reporter, String reason) {
        this.reported = reported;
        this.reporter = reporter;
        this.reason = reason;
    }
}
