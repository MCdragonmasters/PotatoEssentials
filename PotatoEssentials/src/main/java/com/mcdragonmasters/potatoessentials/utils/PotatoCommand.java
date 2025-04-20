package com.mcdragonmasters.potatoessentials.utils;

import com.mcdragonmasters.potatoessentials.PotatoEssentials;
import lombok.Getter;
import lombok.Setter;

public abstract class PotatoCommand {
    protected final String NAMESPACE = PotatoEssentials.NAMESPACE;
    protected final PotatoEssentials INSTANCE = PotatoEssentials.INSTANCE;
    @Getter
    protected String name;
    @Getter
    protected String[] aliases;
    @Getter
    @Setter
    protected String permission;

    protected boolean hasAliases;

    protected PotatoCommand(String name, String... aliases) {
        this.name = name;
        this.aliases = aliases;
        this.hasAliases = aliases.length > 0;
    }
    public abstract void register();

    public boolean hasAliases() {
        return hasAliases;
    }
    protected String getMsg(String s) {
        return Config.getCmdMsg(name, s);
    }
}
