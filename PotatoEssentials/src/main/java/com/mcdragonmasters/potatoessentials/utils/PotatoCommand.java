package com.mcdragonmasters.potatoessentials.utils;

import com.mcdragonmasters.potatoessentials.PotatoEssentials;
import dev.jorel.commandapi.CommandAPICommand;
import dev.jorel.commandapi.executors.CommandExecutor;
import lombok.Getter;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;

public abstract class PotatoCommand {
    protected static final String NAMESPACE = PotatoEssentials.NAMESPACE;
    protected final PotatoEssentials INSTANCE = PotatoEssentials.INSTANCE;
    @Getter
    protected String name;
    @Getter
    protected String[] aliases;
    @Getter
    protected String permission;
    protected boolean hasAliases;
    protected CommandAPICommand command;

    protected PotatoCommand(@NotNull String name, @NotNull String permission, String... aliases) {
        this.name = name;
        this.permission = permission;
        this.aliases = aliases;
        this.hasAliases = aliases.length > 0;
    }
    public abstract void register();

    protected CommandAPICommand createCommand(@Nullable CommandExecutor executor, CommandAPICommand... subCommands) {
        this.command = new CommandAPICommand(name)
                .withAliases(aliases)
                .withPermission(permission)
                .withSubcommands(subCommands);
        if (executor != null) this.command.executes(executor);
        return this.command;
    }
    protected CommandAPICommand createCommand(CommandAPICommand... subCommands) {
        return createCommand(null, subCommands);
    }
    protected CommandAPICommand createSubcommand(String name, @Nullable String permission) {

        CommandAPICommand command = new CommandAPICommand(name);
        if (permission != null) command.withPermission(permission);
        return command;
    }
    protected CommandAPICommand createSubcommand(String name) {
        return createSubcommand(name, null);
    }
    public boolean hasAliases() {
        return hasAliases;
    }
    protected String getMsg(String s) {
        return Config.getCmdMsg(name, s);
    }
}
