package com.mcdragonmasters.potatoessentials;

import com.mcdragonmasters.potatoessentials.commands.RegisterCommands;

import dev.jorel.commandapi.CommandAPI;
import dev.jorel.commandapi.CommandAPIBukkitConfig;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

@SuppressWarnings("unused")
public class PotatoEssentials extends JavaPlugin {

    private static PotatoEssentials instance;
    public static FileConfiguration config;
    public static String prefixMini = "<gold>Potato Essentials<gray> ><reset>";

    @Override
    public void onLoad() {
        CommandAPI.onLoad(new CommandAPIBukkitConfig(this).usePluginNamespace());
    }

    @Override
    public void onEnable() {
        saveDefaultConfig();
        config = getConfig();
        instance = this;
        CommandAPI.onEnable();

        //Register Event Listeners
        //RegisterListeners.register(getServer());

        getLogger().info("Enabled");

        //Register Commands
        RegisterCommands.register();

    }

    @Override
    public void onDisable() {
        CommandAPI.onDisable();
    }
    public static Plugin getInstance() {
        return instance;
    }
}
