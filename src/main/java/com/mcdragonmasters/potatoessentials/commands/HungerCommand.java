package com.mcdragonmasters.potatoessentials.commands;

import com.mcdragonmasters.potatoessentials.PotatoEssentials;
import com.mcdragonmasters.potatoessentials.listeners.HungerChangeListener;
import dev.jorel.commandapi.CommandAPICommand;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class HungerCommand {

    public static boolean hungerEnabled=true;

    public static void register() {

        PotatoEssentials.pluginManager
                .registerEvents(new HungerChangeListener(), PotatoEssentials.getInstance());

        new CommandAPICommand("hunger")
                .withPermission(PotatoEssentials.getNameSpace()+".hunger")
                .executes((sender, args) -> {
                    hungerEnabled=!hungerEnabled;
                    for (Player player : Bukkit.getOnlinePlayers()) {
                        if (!player.hasPermission(PotatoEssentials.getNameSpace()+".hunger")) continue;
                        sender.sendRichMessage("<gray>[<gold>Hunger</gold>] " +
                                (hungerEnabled ? "<green>Enabled" : "<red>Disabled"));
                    }
                }).register();
    }
}
