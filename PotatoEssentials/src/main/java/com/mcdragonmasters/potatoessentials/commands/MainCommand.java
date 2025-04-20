package com.mcdragonmasters.potatoessentials.commands;

import com.mcdragonmasters.potatoessentials.PotatoEssentials;
import com.mcdragonmasters.potatoessentials.utils.Config;
import com.mcdragonmasters.potatoessentials.utils.PotatoCommand;

import dev.jorel.commandapi.CommandAPICommand;
import dev.jorel.commandapi.executors.CommandArguments;

import org.bukkit.command.CommandSender;

@SuppressWarnings("UnstableApiUsage")
public class MainCommand extends PotatoCommand {

    private final static String prefix = "<gold>PotatoEssentials<gray> >";

    public MainCommand() {
        super("potatoessentials");
    }

    @Override
    public void register() {
        new CommandAPICommand(name)
                .withSubcommand(
                        new CommandAPICommand("info")
                                .executes(this::info)
                )
                .withSubcommand(
                        new CommandAPICommand("reload")
                                .withPermission(NAMESPACE+".potatoessentials")
                                .executes(this::reload)
                ).register();
    }

    public void reload(CommandSender sender, CommandArguments ca) {
        if (!sender.hasPermission(PotatoEssentials.NAMESPACE+".potatoessentials")) {
            sender.sendRichMessage("<red>You don't have permission!");
            return;
        }
        String reload = Config.reload();
        if (!reload.equals("passed")) {
            sender.sendRichMessage(
                    prefix+" <red>Invalid config! Error:<newline>" +
                            reload);
            return;
        }
        sender.sendRichMessage(
                prefix+" <green>Successfully reloaded config!<newline>"+
                        "<gold>Note: If you have enabled/disabled any <b>commands</b> in the " +
                        "<b>config</b> a <b>restart</b> is required to actually enable/disable them.");

    }
    public void info(CommandSender sender, CommandArguments ca) {
        String ver = PotatoEssentials.INSTANCE.getPluginMeta().getVersion();
        sender.sendRichMessage(prefix+"<gold> Version:<green> "+ver);
        sender.sendRichMessage(prefix+"<gold> Author:<yellow> MCdragonmasters");
        sender.sendRichMessage(prefix+
                "<gold> Github: <#00b1fc><click:open_url:'https://github.com/MCdragonmasters/PotatoEssentials'>" +
                "https://github.com/MCdragonmasters/PotatoEssentials");
    }
}
