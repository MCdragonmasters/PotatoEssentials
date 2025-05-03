package com.mcdragonmasters.potatoessentials.commands;

import com.mcdragonmasters.potatoessentials.PotatoEssentials;
import com.mcdragonmasters.potatoessentials.utils.Config;
import com.mcdragonmasters.potatoessentials.utils.PotatoCommand;
import com.mcdragonmasters.potatoessentials.utils.Replacer;
import com.mcdragonmasters.potatoessentials.utils.Utils;
import dev.jorel.commandapi.CommandAPICommand;
import dev.jorel.commandapi.arguments.EntitySelectorArgument;
import dev.jorel.commandapi.executors.CommandArguments;
import org.bukkit.entity.Player;

import java.util.Collection;
import java.util.List;

@SuppressWarnings("unchecked")
public class ClearInventoryCommand extends PotatoCommand {
    public ClearInventoryCommand() {
        super("clearinventory", "ci");
        setPermission(PotatoEssentials.NAMESPACE+".clearinventory");
    }

    private final EntitySelectorArgument.ManyPlayers playerArg = new EntitySelectorArgument
            .ManyPlayers("target", false);

    @Override
    public void register() {
        new CommandAPICommand(name)
                .withAliases(aliases)
                .withPermission(permission)
                .withOptionalArguments(playerArg)
                .executesPlayer(this::execute)
                .register();

    }
    private void execute(Player sender, CommandArguments args) {
        Collection<Player> players = args.getByArgumentOrDefault(playerArg, List.of(sender));
        for (Player p : players) {
            p.getInventory().clear();
        }
        var msg = Config.replaceFormat(getMsg("clearInventory"),
                new Replacer("target", Utils.playerNameFormat(players)));
        sender.sendMessage(msg);
    }
}
