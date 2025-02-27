package com.mcdragonmasters.potatoessentials.commands;

import com.mcdragonmasters.potatoessentials.PotatoEssentials;
import com.mcdragonmasters.potatoessentials.utils.Config;
import com.mcdragonmasters.potatoessentials.utils.Replacer;
import com.mcdragonmasters.potatoessentials.utils.Report;
import dev.jorel.commandapi.CommandAPICommand;
import dev.jorel.commandapi.arguments.*;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.HashSet;
import java.util.Set;


public class ReportCommand {

    public static Set<Report> reports= new HashSet<>();

    @SuppressWarnings("DataFlowIssue")
    public static void register() {
        CommandAPICommand subCmd = new CommandAPICommand("log")
                .withPermission(PotatoEssentials.getNameSpace()+".report.log")
                .executes((sender, args) -> {
                    if (reports.isEmpty()) {
                        sender.sendRichMessage("<red>No reports to list.");
                        return;
                    }
                    sender.sendMessage(Config.getConfig().getRichMessage("commands.report.report-log-title"));
                    int i = 0;
                    for (Report rpt : reports) {
                        i++;
                        sender.sendMessage(Config.replaceFormat(Config.reportLogFormat(),
                                new Replacer("reported", rpt.getReported().getName()),
                                new Replacer("reporter", rpt.getReporter().getName()),
                                new Replacer("reason", rpt.getReason())));
                        if (i==15) break;
                    }
                });

        Argument<Player> playerArg = new PlayerArgument("target")
                .replaceSafeSuggestions(SafeSuggestions.suggest(info ->
                        Bukkit.getOnlinePlayers().toArray(new Player[0])
                ));

        Argument<String> reasonArg = new GreedyStringArgument("reason");
        new CommandAPICommand("report")
                .withPermission(PotatoEssentials.getNameSpace()+".report")
                .withArguments(playerArg)
                .withArguments(reasonArg)
                .executes((sender, args) -> {
                    Player reported = args.getByArgument(playerArg);
                    String reason = args.getByArgument(reasonArg);
                    reports.add(new Report(reported, sender, reason));
                    if (reported == sender) {
                        sender.sendRichMessage("<red>You cannot report Yourself!");
                        return;
                    }
                    for (Player player : Bukkit.getOnlinePlayers()) {
                        if (!player.hasPermission(PotatoEssentials.getNameSpace()+".report.log")) continue;
                        Component msg = Config.replaceFormat(Config.reportFormat(),
                                new Replacer("reported", reported.getName()),
                                new Replacer("reporter", sender.getName()),
                                new Replacer("reason", reason));
                        player.sendMessage(msg);
                    }
                }).withSubcommand(subCmd).register();
    }
}
