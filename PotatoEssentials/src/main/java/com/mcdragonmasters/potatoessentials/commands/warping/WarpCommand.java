package com.mcdragonmasters.potatoessentials.commands.warping;

import com.mcdragonmasters.potatoessentials.database.WarpsManager;
import com.mcdragonmasters.potatoessentials.PotatoEssentials;
import com.mcdragonmasters.potatoessentials.utils.Config;
import com.mcdragonmasters.potatoessentials.utils.PotatoCommand;
import com.mcdragonmasters.potatoessentials.utils.Replacer;
import dev.jorel.commandapi.CommandAPICommand;
import dev.jorel.commandapi.arguments.Argument;
import dev.jorel.commandapi.arguments.ArgumentSuggestions;
import dev.jorel.commandapi.arguments.StringArgument;
import dev.jorel.commandapi.executors.CommandArguments;
import org.bukkit.Location;
import org.bukkit.entity.Player;

public class WarpCommand extends PotatoCommand {

    public WarpCommand() {
        super("warp",NAMESPACE+".warp");
    }

    private final Argument<String> warpNameArg = new StringArgument("warp-name")
            .replaceSuggestions(ArgumentSuggestions.strings(info ->
                    WarpsManager.getWarps()
            ));
    @Override
    public void register() {
        new CommandAPICommand("warp")
                .withPermission(PotatoEssentials.NAMESPACE+".warp")
                .withArguments(warpNameArg)
                .executesPlayer(this::execute).register();
    }
    private void execute(Player sender, CommandArguments args) {
        String warpName = args.getByArgument(warpNameArg);
        Location warpLocation = WarpsManager.getWarp(warpName);
        String msg;
        if (warpLocation != null) {
            sender.teleport(warpLocation);
            msg = "warpWarp";
        } else msg = "warpNotSet";
        var message = Config.replaceFormat(getMsg(msg), new Replacer("warp-name", warpName));
        sender.sendMessage(message);
    }

}
