package com.mcdragonmasters.potatoessentials.utils;

import com.mcdragonmasters.potatoessentials.PotatoEssentials;
import lombok.Getter;
import lombok.Setter;

public abstract class PotatoCommand {
    protected final String NAMESPACE = PotatoEssentials.NAMESPACE;
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

    protected boolean hasAliases() {
        return hasAliases;
    }
}
