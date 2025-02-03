package com.mcdragonmasters.potatoessentials;

import com.mcdragonmasters.potatoessentials.commands.*;
import com.mcdragonmasters.potatoessentials.commands.messaging.*;
import com.mcdragonmasters.potatoessentials.commands.teleporting.TeleportAllCommand;
import com.mcdragonmasters.potatoessentials.listeners.PlayerChatListener;
import com.mcdragonmasters.potatoessentials.commands.teleporting.TeleportHereCommand;
import com.mcdragonmasters.potatoessentials.utils.Config;
import dev.jorel.commandapi.CommandAPI;
import dev.jorel.commandapi.CommandAPIBukkitConfig;

import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

import net.milkbowl.vault.chat.Chat;

@SuppressWarnings("unused")
public class PotatoEssentials extends JavaPlugin {

    @Getter
    private static PotatoEssentials instance;
    @Getter
    private final static String nameSpace = "potatoessentials";
    @Getter
    private static Chat vaultChat = null;
    @Getter
    private static boolean hasVault = false;
    public static PluginManager pluginManager;
    public static FileConfiguration config;

    @Override
    public void onLoad() {
        CommandAPI.onLoad(new CommandAPIBukkitConfig(this).usePluginNamespace());
    }

    @Override
    public void onEnable() {
        Config.config(this);
        instance = this;
        CommandAPI.onEnable();

        hasVault = setupChat();

        //Register Event Listeners
        pluginManager = Bukkit.getPluginManager();
        pluginManager.registerEvents(new PlayerChatListener(), instance);

        //Register Commands
        registerCommands();

        getLogger().info(hasVault ? "Vault found" : "Vault not found");

        getLogger().info("Enabled");
    }


    private static void registerCommands() {
        MainCommand.register();

        if (config.getBoolean("commands.message.enabled")) MessageCommand.register();
        if (config.getBoolean("commands.reply.enabled")) ReplyCommand.register();
        if (config.getBoolean("commands.socialspy.enabled")) SocialSpyCommand.register();
        if (config.getBoolean("commands.broadcast.enabled")) BroadcastCommand.register();
        if (config.getBoolean("commands.messagetoggle.enabled")) MessageToggleCommand.register();

        if (config.getBoolean("commands.enchant.enabled")) EnchantCommand.register();
        if (config.getBoolean("commands.gamemode.enabled"))  GameModeCommand.register();
        if (config.getBoolean("commands.heal.enabled")) HealCommand.register();
        if (config.getBoolean("commands.hunger.enabled")) HungerCommand.register();
        if (config.getBoolean("commands.invsee.enabled")) InvSeeCommand.register();
        if (config.getBoolean("commands.ping.enabled")) PingCommand.register();
        if (config.getBoolean("commands.smite.enabled")) SmiteCommand.register();
        if (config.getBoolean("commands.tphere.enabled")) TeleportHereCommand.register();
        if (config.getBoolean("commands.tpall.enabled")) TeleportAllCommand.register();
        if (config.getBoolean("commands.flyspeed.enabled")) FlySpeedCommand.register();
        if (config.getBoolean("commands.vanish.enabled")) VanishCommand.register();
        if (config.getBoolean("commands.feed.enabled")) FeedCommand.register();

    }
    private boolean setupChat() {
        if (getServer().getPluginManager().getPlugin("Vault") == null) {
            return false;
        }
        RegisteredServiceProvider<Chat> rsp = getServer().getServicesManager().getRegistration(Chat.class);
        if (rsp != null) {
            vaultChat = rsp.getProvider();
        }
        return vaultChat != null;
    }

    @Override
    public void onDisable() {
        CommandAPI.onDisable();
    }
    public static boolean hasVault() {
        return hasVault;
    }
}
