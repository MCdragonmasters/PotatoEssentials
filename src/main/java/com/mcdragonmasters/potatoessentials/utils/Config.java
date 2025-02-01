package com.mcdragonmasters.potatoessentials.utils;

import com.mcdragonmasters.potatoessentials.PotatoEssentials;
import com.tchristofferson.configupdater.ConfigUpdater;
import org.bukkit.configuration.file.FileConfiguration;

import java.io.File;
import java.io.IOException;

public class Config {
    private static FileConfiguration config;

    public static void config(PotatoEssentials plugin) {
        plugin.saveDefaultConfig();

        try {
            ConfigUpdater.update(plugin,"config.yml",new File(plugin.getDataFolder(),"config.yml"));
        } catch (IOException e) {
            plugin.getLogger().severe(e.getMessage());
        }
        plugin.reloadConfig();

        config = plugin.getConfig();

    }
    public static Boolean chatFormatEnabled() { return config.getBoolean("chat.enabled"); }
    public static String chatFormat() {
        return config.getString("chat.format");
    }
    public static String broadcastFormat() { return config.getString("messages.broadcast"); }
}
