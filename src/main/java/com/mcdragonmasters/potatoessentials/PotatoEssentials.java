package com.mcdragonmasters.potatoessentials;

import com.mcdragonmasters.potatoessentials.commands.*;
import com.mcdragonmasters.potatoessentials.commands.message.BroadcastCommand;
import com.mcdragonmasters.potatoessentials.commands.message.MessageCommand;
import com.mcdragonmasters.potatoessentials.commands.message.ReplyCommand;
import com.mcdragonmasters.potatoessentials.commands.message.SocialSpyCommand;
import com.mcdragonmasters.potatoessentials.listeners.PlayerChatListener;
import com.mcdragonmasters.potatoessentials.utils.Config;
import dev.jorel.commandapi.CommandAPI;
import dev.jorel.commandapi.CommandAPIBukkitConfig;

import lombok.Getter;
import org.bukkit.Bukkit;
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
        if (Config.chatFormatEnabled()) pluginManager.registerEvents(new PlayerChatListener(), instance);

        //Register Commands
        registerCommands();

        getLogger().info(hasVault ? "Vault found" : "Vault not found");

        getLogger().info("Enabled");
    }


    private static void registerCommands() {
        EnchantCommand.register();
        GameModeCommand.register();
        PingCommand.register();
        SmiteCommand.register();
        MessageCommand.register();
        ReplyCommand.register();
        SocialSpyCommand.register();
        TeleportHereCommand.register();
        HungerCommand.register();
        BroadcastCommand.register();
        InvSeeCommand.register();
        //VisibilityCommand.register();

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
