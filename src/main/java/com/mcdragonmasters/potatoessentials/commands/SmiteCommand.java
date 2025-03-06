package com.mcdragonmasters.potatoessentials.commands;

import com.mcdragonmasters.potatoessentials.PotatoEssentials;
import com.mcdragonmasters.potatoessentials.utils.PotatoCommand;
import com.mcdragonmasters.potatoessentials.utils.Utils;
import dev.jorel.commandapi.annotations.Command;
import dev.jorel.commandapi.annotations.Default;
import dev.jorel.commandapi.annotations.Permission;
import dev.jorel.commandapi.annotations.arguments.ADoubleArgument;
import dev.jorel.commandapi.annotations.arguments.AEntitySelectorArgument.ManyPlayers;
import net.kyori.adventure.text.Component;
import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Collection;

@Command("smite")
public class SmiteCommand extends PotatoCommand {
    @Override
    public String getName() { return "smite"; }

    @Default
    @Permission(PotatoEssentials.NAMESPACE+".smite")
    public static void smite(CommandSender sender, @ManyPlayers Collection<Player> players, @ADoubleArgument double damage) {
        players.forEach((player -> {
            Location loc = player.getLocation();
            loc.getWorld().strikeLightningEffect(loc);
            player.damage(damage);
        }));
        String playerMsg = Utils.playerNameFormat(players);
        Component msg = Utils.miniMessage("Smiting <yellow>"+playerMsg);
        sender.sendMessage(msg);
    }
    @Default
    @Permission(PotatoEssentials.NAMESPACE+".smite")
    public static void smite(CommandSender sender, @ManyPlayers Collection<Player> players) {
        smite(sender, players, 1);
    }
}