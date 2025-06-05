package com.mcdragonmasters.potatoessentials.commands;

import com.mcdragonmasters.potatoessentials.PotatoEssentials;
import com.mcdragonmasters.potatoessentials.utils.Config;
import com.mcdragonmasters.potatoessentials.objects.PotatoCommand;
import com.mcdragonmasters.potatoessentials.objects.Replacer;
import com.mcdragonmasters.potatoessentials.utils.Report;
import dev.jorel.commandapi.arguments.Argument;
import dev.jorel.commandapi.arguments.GreedyStringArgument;
import dev.jorel.commandapi.arguments.PlayerArgument;
import dev.jorel.commandapi.arguments.SafeSuggestions;
import dev.jorel.commandapi.executors.CommandArguments;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.HashSet;
import java.util.Set;


public class ReportCommand extends PotatoCommand {

    public ReportCommand() {
        super("report", NAMESPACE+".report");
    }

    public static Set<Report> reports = new HashSet<>();

    private final Argument<Player> playerArg = new PlayerArgument("target")
            .replaceSafeSuggestions(SafeSuggestions.suggest(info ->
                    Bukkit.getOnlinePlayers().toArray(new Player[0])
            ));
    private final Argument<String> reasonArg = new GreedyStringArgument("reason");

    @Override
    public void register() {
        createCommand(
                createSubcommand("log", permission+".log")
                        .executes(this::executeLog)
        ).withArguments(playerArg)
                .withArguments(reasonArg)
                .executes(this::execute)
                .register();
    }

    @SuppressWarnings("DataFlowIssue")
    private void execute(CommandSender sender, CommandArguments args) {
        Player reported = args.getByArgument(playerArg);
        String reason = args.getByArgument(reasonArg);
        reports.add(new Report(reported, sender, reason));
        if (reported == sender) {
            sender.sendRichMessage("<red>You cannot report Yourself!");
            return;
        }
        for (Player player : Bukkit.getOnlinePlayers()) {
            if (!player.hasPermission(PotatoEssentials.NAMESPACE+".report.log")) continue;
            Component msg = Config.replaceFormat(Config.reportFormat(),
                    new Replacer("reported", reported.getName()),
                    new Replacer("reporter", sender.getName()),
                    new Replacer("reason", reason));
            player.sendMessage(msg);
        }
    }
    private void executeLog(CommandSender sender, CommandArguments args) {
        if (reports.isEmpty()) {
            sender.sendRichMessage("<red>No reports to list.");
            return;
        }
        sender.sendMessage(Config.replaceFormat(Config.getConfig().getString("commands.report.report-log-title")));
        int i = 0;
        for (Report rpt : reports) {
            i++;
            sender.sendMessage(Config.replaceFormat(Config.reportLogFormat(),
                    new Replacer("reported", rpt.getReported().getName()),
                    new Replacer("reporter", rpt.getReporter().getName()),
                    new Replacer("reason", rpt.getReason())));
            if (i==15) break;
        }
    }
}
