package com.mcdragonmasters.potatoessentials.commands;

import com.mcdragonmasters.potatoessentials.PotatoEssentials;
import com.mcdragonmasters.potatoessentials.utils.Config;
import dev.jorel.commandapi.CommandAPICommand;
import dev.jorel.commandapi.arguments.Argument;
import dev.jorel.commandapi.arguments.MultiLiteralArgument;

public class MainCommand {
    public static void register() {

        Argument<String> stringArg = new MultiLiteralArgument("argument","reload");

        new CommandAPICommand("potatoessentials")
                .withPermission(PotatoEssentials.getNameSpace()+".potatoessentials")
                .withArguments(stringArg)
                .executes((sender, args) -> {
                    switch (args.getByArgument(stringArg)) {
                        case "reload":
                            Config.reload();
                            sender.sendRichMessage(
                                    "<gold>PotatoEssentials<gray> ><green> Successfully reloaded config!<newline>"+
                                    "<gold>Note: If you have <bold>disabled any commands</bold> in <bold>config<bold> a <bold>restart</bold> is required to actually disable them.");
                            break;
                        case null, default:
                            break;
                    }
                }).register();
    }
}
