package com.mcdragonmasters.potatoessentials.commands;

import com.mcdragonmasters.potatoessentials.PotatoEssentials;
import com.mcdragonmasters.potatoessentials.listeners.HungerChangeListener;
import com.mcdragonmasters.potatoessentials.utils.PotatoCommand;
import dev.jorel.commandapi.CommandAPICommand;
import dev.jorel.commandapi.executors.CommandArguments;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class HungerCommand extends PotatoCommand {

    public HungerCommand() {
        super("hunger",NAMESPACE+".hunger");
    }

    public static boolean hungerEnabled = true;

    @Override
    public void register() {

        PotatoEssentials.pluginManager
                .registerEvents(new HungerChangeListener(), PotatoEssentials.INSTANCE);

        new CommandAPICommand(name)
                .withPermission(permission)
                .executes(this::execute)
                .register();
    }
    private void execute(CommandSender sender, CommandArguments args) {
        hungerEnabled=!hungerEnabled;
        for (Player player : Bukkit.getOnlinePlayers()) {
            if (!player.hasPermission(PotatoEssentials.NAMESPACE+".hunger")) continue;
            sender.sendRichMessage("<gray>[<gold>Hunger</gold>] " +
                    (hungerEnabled ? "<green>Enabled" : "<red>Disabled"));
        }
    }
}
