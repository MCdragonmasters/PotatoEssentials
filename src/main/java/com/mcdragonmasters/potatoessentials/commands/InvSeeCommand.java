package com.mcdragonmasters.potatoessentials.commands;

import com.mcdragonmasters.potatoessentials.PotatoEssentials;
import dev.jorel.commandapi.CommandAPICommand;
import dev.jorel.commandapi.arguments.Argument;
import dev.jorel.commandapi.arguments.EntitySelectorArgument;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

import java.util.Objects;

public class InvSeeCommand {
    public static void register() {

        Argument<Player> arg = new EntitySelectorArgument.OnePlayer("target");

        new CommandAPICommand("invsee")
                .withPermission(PotatoEssentials.NAMESPACE+".invsee")
                .withArguments(arg)
                .executesPlayer((sender, args) -> {
                    Player player = Objects.requireNonNull(args.getByArgument(arg));
                    if (sender==player) {
                        sender.sendRichMessage("<red>You cannot /invsee yourself!");
                        return;
                    }
                    Inventory inv = player.getInventory();
                    sender.openInventory(inv);
                }).register();
    }
}
