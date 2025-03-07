package com.mcdragonmasters.potatoessentials.commands;

import com.mcdragonmasters.potatoessentials.PotatoEssentials;
import com.mcdragonmasters.potatoessentials.utils.Config;
import dev.jorel.commandapi.annotations.Command;
import dev.jorel.commandapi.annotations.Permission;
import dev.jorel.commandapi.annotations.Subcommand;
import org.bukkit.command.CommandSender;

@SuppressWarnings("UnstableApiUsage")
@Command("potatoessentials")
public class MainCommand {

    private final static String prefix = "<gold>PotatoEssentials<gray> >";

    @Subcommand("reload")
    @Permission(PotatoEssentials.NAMESPACE+".potatoessentials")
    public static void reload(CommandSender sender) {
        if (!sender.hasPermission(PotatoEssentials.NAMESPACE+".potatoessentials")) {
            sender.sendRichMessage("<red>You don't have permission!");
            return;
        }
        String reload = Config.reload(false);
        if (!reload.equals("passed")) {
            sender.sendRichMessage(
                    prefix+" <red>Invalid config! Error:<newline>" +
                            reload);
            return;
        }
        sender.sendRichMessage(
                prefix+" <green>Successfully reloaded config!<newline>"+
                        "<gold>Note: If you have enabled/disabled any <b>commands</b> in the " +
                        "<bold>config</bold> a <bold>restart</bold> is required to actually enable/disable them.");

    }
    @Subcommand("info")
    public static void info(CommandSender sender) {
        String ver = PotatoEssentials.INSTANCE.getPluginMeta().getVersion();
        sender.sendRichMessage(prefix+"<gold> Version:<green> "+ver);
        sender.sendRichMessage(prefix+"<gold>Author:<yellow> MCdragonmasters");
        sender.sendRichMessage(prefix+
                "<gold> Github: <#00b1fc><click:open_url:'https://github.com/MCdragonmasters/PotatoEssentials'>" +
                "https://github.com/MCdragonmasters/PotatoEssentials");
    }
}
