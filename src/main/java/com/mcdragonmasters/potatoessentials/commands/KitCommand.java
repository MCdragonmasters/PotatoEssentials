package com.mcdragonmasters.potatoessentials.commands;

import com.mcdragonmasters.potatoessentials.PotatoEssentials;
import com.mcdragonmasters.potatoessentials.database.KitManager;

import com.mcdragonmasters.potatoessentials.utils.Config;
import com.mcdragonmasters.potatoessentials.utils.Replacer;
import com.mcdragonmasters.potatoessentials.utils.Utils;
import dev.jorel.commandapi.CommandAPICommand;
import dev.jorel.commandapi.arguments.ArgumentSuggestions;
import dev.jorel.commandapi.arguments.EntitySelectorArgument;
import dev.jorel.commandapi.arguments.StringArgument;
import org.bukkit.entity.Player;

import java.util.Collection;
import java.util.List;

public class KitCommand {
    private final KitManager kitManager;
    public KitCommand(KitManager kitManager) {
        this.kitManager = kitManager;
    }
    @SuppressWarnings("unchecked")
    public void register() {

        var kitNameArg = new StringArgument("kitName").
                replaceSuggestions(ArgumentSuggestions.strings(kitManager.getKitNames()));
        var kitGivePlayersArg = new EntitySelectorArgument.ManyPlayers("target");
        new CommandAPICommand("kit")
                .withSubcommand(
                        new CommandAPICommand("create")
                                .withPermission(PotatoEssentials.NAMESPACE+".kit.create")
                                .withArguments(kitNameArg)
                                .executesPlayer((player, args) -> {
                                    String kitName = args.getByArgument(kitNameArg);
                                    if (kitManager.getKitNames().contains(kitName)) {
                                        var msg = Config.replaceFormat(Config.getCmdMsg("kit", "kitAlreadyExists"),
                                                new Replacer("kit-name", kitName));
                                        player.sendMessage(msg);
                                        return;
                                    }
                                    if (player.getInventory().isEmpty()) {
                                        var msg = Config.replaceFormat(Config.getCmdMsg("kit", "emptyInventory"));
                                        player.sendMessage(msg);
                                        return;
                                    }
                                    var msg = Config.replaceFormat(Config.getCmdMsg("kit", "kitCreate"),
                                            new Replacer("kit-name", kitName));
                                    player.sendMessage(msg);
                                    kitManager.saveKit(kitName, player);
                                })
                ).withSubcommand(
                        new CommandAPICommand("give")
                                .withPermission(PotatoEssentials.NAMESPACE+".kit.give")
                                .withArguments(kitNameArg)
                                .withOptionalArguments(kitGivePlayersArg)
                                .executesPlayer((player, args) -> {
                                    String kitName = args.getByArgument(kitNameArg);
                                    Collection<Player> players = args.getByArgumentOrDefault(kitGivePlayersArg, List.of(player));
                                    if (!kitManager.getKitNames().contains(kitName)) {
                                        var msg = Config.replaceFormat(Config.getCmdMsg("kit", "kitNotFound"),
                                                new Replacer("kit-name", kitName));
                                        player.sendMessage(msg);
                                        return;
                                    }
                                    var msg = Config.replaceFormat(Config.getCmdMsg("kit","kitGiveTo"),
                                            new Replacer("kit-name", kitName),
                                            new Replacer("target", Utils.playerNameFormat(players)));
                                    player.sendMessage(msg);
                                    players.forEach(p -> kitManager.giveKit(kitName, p));
                                })
                )
                .withSubcommand(
                        new CommandAPICommand("delete")
                                .withPermission(PotatoEssentials.NAMESPACE+".kit.delete")
                                .withArguments(kitNameArg)
                                .executesPlayer((player, args) -> {
                                    String kitName = args.getByArgument(kitNameArg);
                                    if (!kitManager.getKitNames().contains(kitName)) {
                                        var msg = Config.replaceFormat(Config.getCmdMsg("kit", "kitNotFound"),
                                                new Replacer("kit-name", kitName));
                                        player.sendMessage(msg);
                                        return;
                                    }
                                    kitManager.deleteKit(kitName);
                                    var msg = Config.replaceFormat(Config.getCmdMsg("kit", "kitDelete"),
                                            new Replacer("kit-name", kitName));
                                    player.sendMessage(msg);
                                })
                ).register();
    }
}
