package com.mcdragonmasters.potatoessentials.commands;

import com.mcdragonmasters.potatoessentials.PotatoEssentials;
import com.mcdragonmasters.potatoessentials.listeners.PlayerJoinQuitListener;
import dev.jorel.commandapi.CommandAPICommand;
import dev.jorel.commandapi.arguments.Argument;
import dev.jorel.commandapi.arguments.EntitySelectorArgument;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

import static com.mcdragonmasters.potatoessentials.PotatoEssentials.pluginManager;

public class VanishCommand {

    @Getter
    private final static Set<Player> vanishedPlayers = new HashSet<>();

    public static void register() {
        pluginManager.registerEvents(new PlayerJoinQuitListener(), PotatoEssentials.INSTANCE);

        Argument<Player> vanishArg = new EntitySelectorArgument.OnePlayer("target");

        String namespace = PotatoEssentials.NAMESPACE;

        new CommandAPICommand("vanish")
                .withPermission(namespace+".vanish")
                .withOptionalArguments(vanishArg)
                .executesPlayer((sender, args) -> {
                    Player vanisher = args.getByArgument(vanishArg)!=null?Objects.requireNonNull(args.getByArgument(vanishArg)):sender;
                    boolean vanished;
                    if (!vanishedPlayers.contains(vanisher)) {
                        vanishedPlayers.add(vanisher);
                        vanished = true;
                    } else {
                        vanishedPlayers.remove(vanisher);
                        vanished = false;
                    }
                    String msg = vanished?"now":"no longer";
                    for (Player player : Bukkit.getOnlinePlayers()) {
                        if (player == vanisher) continue;
                        if (player.hasPermission(namespace+".vanish.bypass")){
                            player.sendRichMessage("<dark_gray>[<gold>Vanish</gold>]<yellow> "+
                                    vanisher.getName()+"<gray> is "+msg+" in vanish.");
                            continue;
                        }
                        if (vanished) player.hidePlayer(PotatoEssentials.INSTANCE, vanisher);
                        if (!vanished) player.showPlayer(PotatoEssentials.INSTANCE, vanisher);
                    }
                    vanisher.sendRichMessage(
                            "<dark_gray>[<gold>Vanish</gold>]<gray> You are "+msg+" in<yellow> Vanish.");
                })
                .register();
    }
}
