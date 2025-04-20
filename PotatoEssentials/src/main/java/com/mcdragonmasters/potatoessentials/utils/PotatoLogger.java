package com.mcdragonmasters.potatoessentials.utils;

import com.mcdragonmasters.potatoessentials.PotatoEssentials;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.logging.Logger;

public class PotatoLogger extends Logger {
    private FileConfiguration config;
    private final Logger logger;

    public PotatoLogger(Logger logger, FileConfiguration config) {
        super(logger.getName(), logger.getResourceBundleName());
        this.setParent(logger);
        this.config = config;
        this.logger = logger;
    }

    public void setDefaultConfig() {
        this.config = PotatoEssentials.config;
    }

    public void debug(String msg) {
        if (!this.config.getBoolean("debug")) return;
        logger.info("[DEBUG] " + msg);
    }
}
