package com.mcdragonmasters.potatoessentials.commands.messaging;

import com.mcdragonmasters.potatoessentials.utils.Config;
import com.mcdragonmasters.potatoessentials.utils.PotatoCommand;
import dev.jorel.commandapi.CommandAPICommand;
import dev.jorel.commandapi.executors.CommandArguments;
import org.bukkit.command.CommandSender;

import java.util.HashSet;
import java.util.Set;

public class SocialSpyCommand extends PotatoCommand {
    public SocialSpyCommand() {
        super("socialspy");
        setPermission(NAMESPACE+".socialspy");
    }

    public static Set<CommandSender> socialSpyList = new HashSet<>();

    @Override
    public void register() {
        new CommandAPICommand(name)
                .withPermission(permission)
                .executes(this::execute)
                .register();
    }
    private void execute(CommandSender sender, CommandArguments args) {
        if (!socialSpyList.contains(sender)){
            socialSpyList.add(sender);
        } else {
            socialSpyList.remove(sender);
        }
        boolean enabled = socialSpyList.contains(sender);
        var msg = Config.replaceFormat(getMsg(enabled?"socialSpyEnabled":"socialSpyDisabled"));
        sender.sendMessage(msg);
    }
}
