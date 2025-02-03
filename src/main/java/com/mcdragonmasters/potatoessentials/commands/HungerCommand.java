package com.mcdragonmasters.potatoessentials.commands;

import com.mcdragonmasters.potatoessentials.PotatoEssentials;
import com.mcdragonmasters.potatoessentials.listeners.HungerChangeListener;
import dev.jorel.commandapi.CommandAPICommand;

public class HungerCommand {

    public static boolean hungerEnabled=true;

    public static void register() {

        PotatoEssentials.pluginManager
                .registerEvents(new HungerChangeListener(), PotatoEssentials.getInstance());

        new CommandAPICommand("hunger")
                .withPermission(PotatoEssentials.getNameSpace()+".hunger")
                .executes((sender, args) -> {
                    hungerEnabled=!hungerEnabled;
                    sender.sendRichMessage("<gray>[<gold>Hunger</gold>] "+
                            (hungerEnabled?"<green>Enabled":"<red>Disabled"));
                }).register();
    }
}
