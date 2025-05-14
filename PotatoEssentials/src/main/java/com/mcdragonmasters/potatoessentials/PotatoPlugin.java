package com.mcdragonmasters.potatoessentials;

import com.mcdragonmasters.potatoessentials.utils.PotatoCommandRegistrar;
import com.mcdragonmasters.potatoessentials.utils.PotatoLogger;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

public abstract class PotatoPlugin extends JavaPlugin {
    private PotatoLogger logger = null;
    private final PotatoCommandRegistrar commandRegistrar = new PotatoCommandRegistrar(this);
    public PotatoCommandRegistrar getCommandRegistrar() {
        return this.commandRegistrar;
    }
    @Override
    public @NotNull PotatoLogger getLogger() {
        if (this.logger==null) {
            this.logger = new PotatoLogger(super.getLogger(), this.getConfig());
        }
        return this.logger;
    }
}
