package com.mcdragonmasters.potatoessentials.objects;

import com.mcdragonmasters.potatoessentials.utils.PotatoCommandRegistrar;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

public abstract class PotatoPlugin extends JavaPlugin {
    private final PotatoLogger logger = new PotatoLogger(super.getLogger(), this.getConfig());
    private final PotatoCommandRegistrar commandRegistrar = new PotatoCommandRegistrar(this);
    public PotatoCommandRegistrar getCommandRegistrar() {
        return this.commandRegistrar;
    }
    @Override
    public @NotNull PotatoLogger getLogger() {
        return this.logger;
    }
}
