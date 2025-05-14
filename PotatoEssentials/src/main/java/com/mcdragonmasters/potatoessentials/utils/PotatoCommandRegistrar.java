package com.mcdragonmasters.potatoessentials.utils;

import com.mcdragonmasters.potatoessentials.PotatoPlugin;
import dev.jorel.commandapi.CommandAPI;
import dev.jorel.commandapi.CommandAPIBukkit;
import dev.jorel.commandapi.InternalConfig;
import lombok.Getter;
import lombok.Setter;

import java.lang.reflect.Field;
import java.util.*;
import java.util.function.Function;
import java.util.logging.Level;

public class PotatoCommandRegistrar {
    @Getter
    private final PotatoPlugin plugin;
    @Getter
    private int registeredCmds = 0;
    private final PotatoLogger LOGGER;
    @Getter
    private final List<String> registeredCommandNames = new ArrayList<>();
    private final HashMap<Class<? extends PotatoCommand>, PotatoCommand> commandInstances = new HashMap<>();
    @Setter
    private Function<String, Boolean> commandEnabledCheck;

    public PotatoCommandRegistrar(PotatoPlugin plugin) {
        this.plugin = plugin;
        this.LOGGER = plugin.getLogger();
        setCommandEnabledCheck((name) -> plugin.getConfig().getBoolean("commands."+name+".enabled"));
    }

    public <T extends PotatoCommand> T getCommandInstance(Class<T> clazz) {
        return clazz.cast(commandInstances.get(clazz));
    }

    public void register(PotatoCommand command, boolean forced) {
        if (!CommandAPI.getConfiguration().getNamespace().equals(plugin.getName().toLowerCase())) {
            try {
                Field field = InternalConfig.class.getDeclaredField("namespace");
                field.setAccessible(true);
                field.set(CommandAPI.getConfiguration(), plugin.getName().toLowerCase());
            } catch (Exception e) {
                LOGGER.log(Level.SEVERE, "error setting namespace", e);
            }
        }
        String name = command.getName();
        if (commandInstances.containsKey(command.getClass())) {
            LOGGER.warning("Command '"+command+"' was registered multiple times!");
        } else if (forced || commandEnabledCheck.apply(name)) {
            for (String alias : command.getAliases()) {
                CommandAPIBukkit.unregister(alias, false, true);
                CommandAPIBukkit.unregister(alias, false, false);
            }
            CommandAPIBukkit.unregister(name, false, true);
            CommandAPIBukkit.unregister(name, false, false);
            command.registrar = this;
            command.register();
            var msg = "Registering command '%s'";
            if (command.hasAliases()) msg += " with aliases '%s'";
            LOGGER.debug(msg.formatted(name, formatList(command.getAliases())));
            commandInstances.put(command.getClass(), command);
            registeredCommandNames.add(name);
            registeredCmds++;
        } else {
            LOGGER.debug("Command '%s' disabled in config, not registering".formatted(name));
        }
    }

    public void register(List<PotatoCommand> commands) {
        for (PotatoCommand command : commands) {
            this.register(command, false);
        }
    }
    public void register(PotatoCommand... commands) {
        this.register(Arrays.asList(commands));
    }

    private static String formatList(String[] list) {
        String arrStr = Arrays.toString(list);
        return arrStr.substring(1, arrStr.length() - 1);
    }
}
