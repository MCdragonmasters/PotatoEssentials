package com.mcdragonmasters.potatoessentials.commands;

import com.mcdragonmasters.potatoessentials.utils.PotatoCommand;
import com.mcdragonmasters.potatoessentials.utils.Utils;
import dev.jorel.commandapi.CommandAPICommand;
import dev.jorel.commandapi.arguments.EntitySelectorArgument.ManyPlayers;
import dev.jorel.commandapi.arguments.IntegerArgument;
import dev.jorel.commandapi.executors.CommandArguments;
import net.kyori.adventure.text.Component;
import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Collection;

public class SmiteCommand extends PotatoCommand {

    public SmiteCommand() {
        super("smite");
        setPermission(NAMESPACE+".smite");
    }

    private final ManyPlayers manyPlayersArg = new ManyPlayers("targets");
    private final IntegerArgument integerArg = new IntegerArgument("damage",1,1000);

    @Override
    public void register() {
        new CommandAPICommand(name)
                .withPermission(permission)
                .withArguments(manyPlayersArg)
                .withOptionalArguments(integerArg)
                .executes(this::execute)
                .register();
    }
    @SuppressWarnings("unchecked")
    public void execute(CommandSender sender, CommandArguments args) {
        Collection<Player> players = args.getByArgument(manyPlayersArg);
        int damage = args.getByArgumentOrDefault(integerArg, 1);
        if (players==null) return;
        players.forEach((player -> {
            Location loc = player.getLocation();
            loc.getWorld().strikeLightningEffect(loc);
            player.damage(damage);
        }));
        String playerMsg = Utils.playerNameFormat(players);
        Component msg = Utils.miniMessage("Smiting <yellow>"+playerMsg);
        sender.sendMessage(msg);
    }
}