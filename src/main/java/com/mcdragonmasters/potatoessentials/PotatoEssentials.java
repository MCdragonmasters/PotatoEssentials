package com.mcdragonmasters.potatoessentials;

import com.mcdragonmasters.potatoessentials.commands.*;
import com.mcdragonmasters.potatoessentials.commands.messaging.*;
import com.mcdragonmasters.potatoessentials.commands.teleporting.*;
import com.mcdragonmasters.potatoessentials.commands.warping.DelWarpCommand;
import com.mcdragonmasters.potatoessentials.commands.warping.SetWarpCommand;
import com.mcdragonmasters.potatoessentials.commands.warping.WarpCommand;
import com.mcdragonmasters.potatoessentials.listeners.PlayerChatListener;
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

        if (Config.commandEnabled("message")) MessageCommand.register();
        if (Config.commandEnabled("reply")) ReplyCommand.register();
        if (Config.commandEnabled("socialspy")) SocialSpyCommand.register();
        if (Config.commandEnabled("broadcast")) BroadcastCommand.register();
        if (Config.commandEnabled("messagetoggle")) MessageToggleCommand.register();

        if (Config.commandEnabled("enchant")) EnchantCommand.register();
        if (Config.commandEnabled("gamemode"))  GameModeCommand.register();
        if (Config.commandEnabled("heal")) HealCommand.register();
        if (Config.commandEnabled("hunger")) HungerCommand.register();
        if (Config.commandEnabled("invsee")) InvSeeCommand.register();
        if (Config.commandEnabled("ping")) PingCommand.register();
        if (Config.commandEnabled("smite")) SmiteCommand.register();
        if (config.getBoolean("commands.tpcommands.tphere-enabled")) TeleportHereCommand.register();
        if (config.getBoolean("commands.tpcommands.tpall-enabled")) TeleportAllCommand.register();
        if (Config.commandEnabled("flyspeed")) FlySpeedCommand.register();
        if (Config.commandEnabled("vanish")) VanishCommand.register();
        if (Config.commandEnabled("feed")) FeedCommand.register();
        if (Config.commandEnabled("clearinventory")) ClearInventoryCommand.register();

        // ---- Warps ----
        if (Config.commandEnabled("warp")) WarpCommand.register();
        if (Config.commandEnabled("setwarp")) SetWarpCommand.register();
        if (Config.commandEnabled("deletewarp")) DelWarpCommand.register();

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
