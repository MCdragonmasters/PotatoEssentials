package com.mcdragonmasters.potatoessentials.utils;

import com.mcdragonmasters.potatoessentials.PotatoEssentials;
import dev.jorel.commandapi.CommandAPI;
import dev.jorel.commandapi.InternalConfig;
import lombok.Getter;
import org.bukkit.plugin.Plugin;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.logging.Level;

public class PotatoCommandRegistrar {
    private final Plugin plugin;
    @Getter
    private int registeredCmds = 0;
    private final PotatoLogger LOGGER;

    public PotatoCommandRegistrar(Plugin plugin) {
        this.plugin = plugin;
        this.LOGGER = new PotatoLogger(plugin.getLogger(), plugin.getConfig());
        PotatoEssentials.INSTANCE.getCmdAPIConfig().setNamespace(plugin.getName().toLowerCase());
        try {
            Field field = InternalConfig.class.getDeclaredField("namespace");
            field.setAccessible(true);
            field.set(CommandAPI.getConfiguration(), plugin.getName().toLowerCase());
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "error setting namespace", e);
        }
    }

    public void register(PotatoCommand command, boolean forced) {
        String name = command.getName();
        if (forced || Config.commandEnabled(name, plugin.getConfig())) {
            command.register();
            var msg = "Registering command '%s'";
            if (command.hasAliases()) msg += " with aliases '%s'";
            LOGGER.debug(msg.formatted(name, formatList(command.getAliases())));
            registeredCmds++;
        } else {
            LOGGER.debug("Command '%s' disabled in config, not registering".formatted(name));
        }
    }
    public void register(PotatoCommand command) {
        register(command, false);
    }
    private static String formatList(String[] list) {
        String arrStr = Arrays.toString(list);
        return arrStr.substring(1, arrStr.length() - 1);
    }
}
