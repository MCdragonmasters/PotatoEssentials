package com.mcdragonmasters.potatoessentials.commands;

import com.mcdragonmasters.potatoessentials.PotatoEssentials;
import dev.jorel.commandapi.CommandAPI;
import dev.jorel.commandapi.CommandAPICommand;
import dev.jorel.commandapi.CommandPermission;
import dev.jorel.commandapi.arguments.*;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;

import java.util.Collection;
import java.util.Objects;

public class EnchantCommand {
    @SuppressWarnings("unchecked")
    public static void register() {

        CommandAPI.unregister("enchant");

        EntitySelectorArgument.ManyPlayers playerArg = new EntitySelectorArgument.
                ManyPlayers("players");
        Argument<Enchantment> enchantmentArgument = new EnchantmentArgument("enchantment");
        Argument<Integer> integerArgument = new IntegerArgument("level", 1, 255);

        new CommandAPICommand("enchant")
                .withPermission(CommandPermission.fromString(
                        PotatoEssentials.getNameSpace()+".enchant"))
                .withArguments(playerArg)
                .withArguments(enchantmentArgument)
                .withOptionalArguments(integerArgument)
                .executes((commandSender, args) -> {
                    Collection<Player> players = Objects.requireNonNull(args.getByArgument(playerArg));
                    int level = args.getByArgument(integerArgument)!=null?
                            Objects.requireNonNull(args.getByArgument(integerArgument)):1;
                    Enchantment ench = Objects.requireNonNull(args.getByArgument(enchantmentArgument));
                    for (Player player : players) {
                        player.getInventory().getItemInMainHand()
                                .addUnsafeEnchantment(ench, level);
                    }
                    String msg = players.size()==1 ? " to <yellow>" +
                            ((Player) players.toArray()[0]).getName()+"</yellow>'s"
                            : " to <yellow>"+players.size()+" players</yellow>'";
                    commandSender.sendRichMessage("Applied enchantment<yellow> <translate:"+ench.translationKey()+
                            "> "+level+"</yellow>"+msg+" held item");
                })
                .register();
    }
}
