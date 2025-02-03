package com.mcdragonmasters.potatoessentials.commands.teleporting;
import com.mcdragonmasters.potatoessentials.PotatoEssentials;
import dev.jorel.commandapi.CommandAPICommand;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.Collection;

public class TeleportAllCommand {
    public static void register() {

        new CommandAPICommand("tpall")
                .withAliases("tpall")
                .withPermission(PotatoEssentials.getNameSpace()+".tpall")
                .executesPlayer((sender, args) -> {
                    Collection<? extends Player> players = Bukkit.getOnlinePlayers();
                    for (Player player : players) {
                        player.teleport(sender);
                        player.sendRichMessage("<white>You have been Teleported by <gold>"+sender.getName());
                    }
                    String msg = players.size()<2?((Player) players.toArray()[0]).getName()+"</gold>'s"
                            : players.size()+" Players</gold>'";
                    sender.sendRichMessage
                            ("<white>You have Teleported <gold>"+msg+" to <gold>Yourself");

                })
                .register();
    }
}
