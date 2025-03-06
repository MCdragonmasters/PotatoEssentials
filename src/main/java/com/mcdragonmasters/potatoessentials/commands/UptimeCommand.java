package com.mcdragonmasters.potatoessentials.commands;

import com.mcdragonmasters.potatoessentials.PotatoEssentials;
import com.mcdragonmasters.potatoessentials.utils.Config;
import com.mcdragonmasters.potatoessentials.utils.PotatoCommand;
import com.mcdragonmasters.potatoessentials.utils.Replacer;
import dev.jorel.commandapi.annotations.Command;
import dev.jorel.commandapi.annotations.Default;
import dev.jorel.commandapi.annotations.Permission;
import net.kyori.adventure.text.Component;
import org.bukkit.command.CommandSender;

import java.lang.management.ManagementFactory;
import java.time.Duration;

import java.time.Instant;

@Command("uptime")
public class UptimeCommand extends PotatoCommand {
    public String getName() { return "uptime"; }
    @Default
    @Permission(PotatoEssentials.NAMESPACE+".uptime")
    public static void uptime(CommandSender sender) {
        Component msg = Config.replaceFormat(Config.uptimeMessageFormat(),
                new Replacer("uptime", timeSinceBoot()));
        sender.sendMessage(msg);
    }
    private static String timeSinceBoot() {
        long uptimeMillis = ManagementFactory.getRuntimeMXBean().getUptime();

        Instant startTime = Instant.now().minusMillis(uptimeMillis);

        Duration dur = Duration.between(startTime, Instant.now());

        long hours = dur.toHours();
        long minutes = dur.toMinutes() % 60;
        long seconds = dur.getSeconds() % 60;

        String hoursMsg = hours==0?"":(hours+(hours==1?" hour, ":" hours, "));
        String minutesMsg = minutes==0?"":(minutes+(minutes==1?" minute, ":" minutes, "));
        String secondsMsg = seconds+(seconds==1?" second ":" seconds");

        return hoursMsg+minutesMsg+secondsMsg;
    }
}
