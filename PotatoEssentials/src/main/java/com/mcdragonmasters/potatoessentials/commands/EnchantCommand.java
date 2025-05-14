package com.mcdragonmasters.potatoessentials.commands;

import com.mcdragonmasters.potatoessentials.utils.Config;
import com.mcdragonmasters.potatoessentials.utils.PotatoCommand;
import com.mcdragonmasters.potatoessentials.utils.Replacer;
import com.mcdragonmasters.potatoessentials.utils.Utils;
import dev.jorel.commandapi.CommandAPICommand;
import dev.jorel.commandapi.arguments.Argument;
import dev.jorel.commandapi.arguments.EnchantmentArgument;
import dev.jorel.commandapi.arguments.EntitySelectorArgument.ManyPlayers;
import dev.jorel.commandapi.arguments.IntegerArgument;
import dev.jorel.commandapi.executors.CommandArguments;
import net.kyori.adventure.text.Component;
import org.bukkit.command.CommandSender;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;

import java.util.Collection;

public class EnchantCommand extends PotatoCommand {

    public EnchantCommand() {
        super("enchant",NAMESPACE+".enchant");
    }

    private final ManyPlayers playerArg = new ManyPlayers("players", false);
    private final Argument<Enchantment> enchantmentArgument = new EnchantmentArgument("enchantment");
    private final Argument<Integer> integerArgument = new IntegerArgument("level", 0, 255);

    @Override
    public void register() {
        new CommandAPICommand(name)
                .withPermission(permission)
                .withArguments(playerArg)
                .withArguments(enchantmentArgument)
                .withOptionalArguments(integerArgument)
                .executes(this::execute)
                .register();
    }
    @SuppressWarnings({"unchecked", "DataFlowIssue"})
    private void execute(CommandSender sender, CommandArguments args) {
        Collection<Player> players = args.getByArgument(playerArg);
        int level = args.getByArgumentOrDefault(integerArgument, 1);
        Enchantment ench = args.getByArgument(enchantmentArgument);
        for (Player player : players) {
            player.getInventory().getItemInMainHand()
                    .addUnsafeEnchantment(ench, level);
        }
        String target = Utils.possessivePlayerNameFormat(players);
        Component msg = Config.replaceFormat(Config.enchantmentFormat(),
                new Replacer("enchantment",
                        Utils.serialize(ench.displayName(level)).replace("enchantment.level.", "")),
                new Replacer("target", target));
        sender.sendMessage(msg);
    }
}
