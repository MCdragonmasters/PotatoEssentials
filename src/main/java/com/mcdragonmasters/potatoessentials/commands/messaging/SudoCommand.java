package com.mcdragonmasters.potatoessentials.commands.messaging;

import com.mcdragonmasters.potatoessentials.PotatoEssentials;
import com.mcdragonmasters.potatoessentials.utils.Config;
import com.mcdragonmasters.potatoessentials.utils.Replacer;
import dev.jorel.commandapi.CommandAPICommand;
import dev.jorel.commandapi.arguments.Argument;
import dev.jorel.commandapi.arguments.EntitySelectorArgument;
import dev.jorel.commandapi.arguments.GreedyStringArgument;
import net.kyori.adventure.text.Component;
import org.bukkit.entity.Player;

import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.Set;

@SuppressWarnings("unchecked")
public class SudoCommand {
    public static void register() {

        EntitySelectorArgument.ManyPlayers playerArg = new EntitySelectorArgument
                .ManyPlayers("target");
        Argument<String> stringArg = new GreedyStringArgument("input");
        new CommandAPICommand("sudo")
                .withPermission(PotatoEssentials.NAMESPACE+".sudo")
                .withArguments(playerArg)
                .withArguments(stringArg)
                .executes((sender, args) -> {
                    Collection<Player> players = args.getByArgument(playerArg) != null ?
                            Objects.requireNonNull(args.getByArgument(playerArg)) : (sender instanceof Player p?List.of(p):Set.of());
                    String text = Objects.requireNonNull(args.getByArgument(stringArg));
                    boolean isChat = (text.toLowerCase().startsWith("c:"));
                    String otherMsg = players.size()==1?((Player) players.toArray()[0]).getName()
                            : players.size()+" Players";
                    Component msg = Config.replaceFormat(isChat?Config.sudoSayMsg():Config.sudoCmdMsg(),
                            new Replacer("sudoed-players", otherMsg), new Replacer("input", text));
                    sender.sendMessage(msg);
                    if (isChat) {
                        text = text.substring(2);
                        for (Player player : players) player.chat(text);
                    } else for (Player player : players) player.performCommand(text);

                }).register();
    }
}
