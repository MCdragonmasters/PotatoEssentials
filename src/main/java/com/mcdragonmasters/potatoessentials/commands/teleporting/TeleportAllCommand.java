package com.mcdragonmasters.potatoessentials.commands.teleporting;
import com.mcdragonmasters.potatoessentials.PotatoEssentials;
import com.mcdragonmasters.potatoessentials.utils.Config;
import com.mcdragonmasters.potatoessentials.utils.Replacer;
import dev.jorel.commandapi.CommandAPICommand;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.Collection;

public class TeleportAllCommand {
    public static void register() {

        new CommandAPICommand("tpall")
                .withAliases("tpall")
                .withPermission(PotatoEssentials.NAMESPACE+".tpall")
                .executesPlayer((sender, args) -> {
                    Collection<? extends Player> players = Bukkit.getOnlinePlayers();
                    Component tpedMsg = Config.replaceFormat(Config.teleportedMsg(),
                            new Replacer("teleporter", sender.getName()));

                    for (Player player : players) {
                        player.teleport(sender);
                        player.sendMessage(tpedMsg);
                    }
                    String msg = players.size()<2?((Player) players.toArray()[0]).getName()
                            : players.size()+" Players";
                    Component tperMsg = Config.replaceFormat(Config.teleporterMsg(),
                            new Replacer("teleported", msg));
                    sender.sendMessage(tperMsg);

                })
                .register();
    }
}
