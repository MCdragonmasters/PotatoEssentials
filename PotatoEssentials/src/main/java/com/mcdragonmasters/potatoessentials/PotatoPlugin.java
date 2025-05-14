package com.mcdragonmasters.potatoessentials;

import com.mcdragonmasters.potatoessentials.utils.PotatoCommandRegistrar;
import com.mcdragonmasters.potatoessentials.utils.PotatoLogger;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

public abstract class PotatoPlugin extends JavaPlugin {
    private PotatoLogger logger = null;
    private PotatoCommandRegistrar commandRegistrar = null;
    public PotatoCommandRegistrar getCommandRegistrar() {
        if (this.commandRegistrar == null) {
            this.commandRegistrar = new PotatoCommandRegistrar(this);
        }
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
