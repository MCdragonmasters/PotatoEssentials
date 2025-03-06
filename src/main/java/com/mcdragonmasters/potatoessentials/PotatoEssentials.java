package com.mcdragonmasters.potatoessentials;

import com.mcdragonmasters.potatoessentials.commands.*;
import com.mcdragonmasters.potatoessentials.commands.messaging.*;
import com.mcdragonmasters.potatoessentials.commands.teleporting.*;
import com.mcdragonmasters.potatoessentials.commands.warping.DelWarpCommand;
import com.mcdragonmasters.potatoessentials.commands.warping.SetWarpCommand;
import com.mcdragonmasters.potatoessentials.commands.warping.WarpCommand;
import com.mcdragonmasters.potatoessentials.database.KitManager;
import com.mcdragonmasters.potatoessentials.listeners.PlayerChatListener;
import com.mcdragonmasters.potatoessentials.utils.Config;
import com.mcdragonmasters.potatoessentials.utils.PotatoCommand;
import dev.jorel.commandapi.CommandAPI;
import dev.jorel.commandapi.CommandAPIBukkitConfig;

import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

import net.milkbowl.vault.chat.Chat;

import java.util.logging.Level;
import java.util.logging.Logger;

@SuppressWarnings("SameParameterValue")
public class PotatoEssentials extends JavaPlugin {

    public static PotatoEssentials INSTANCE;
    public final static String NAMESPACE = "potatoessentials";
    @Getter
    private static Chat vaultChat = null;
    @Getter
    private static boolean vaultInstalled = false;
    public static Logger LOGGER;
    public static PluginManager pluginManager;
    public static FileConfiguration config;

    @Override
    public void onLoad() {
        CommandAPI.onLoad(new CommandAPIBukkitConfig(this).usePluginNamespace());
    }

    @Override
    public void onEnable() {
        Config.config(this);
        INSTANCE = this;
        CommandAPI.onEnable();

        LOGGER = getLogger();

        pluginManager = Bukkit.getPluginManager();

        pluginManager.registerEvents(new PlayerChatListener(), INSTANCE);

        vaultInstalled = setupVaultChat();

        registerCommands();

        LOGGER.info("Enabled");
        LOGGER.info(vaultInstalled ? "Vault found" : "Vault not found");
    }


    private static void registerCommands() {
        CommandAPI.registerCommand(MainCommand.class);

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
        registerCommand(SmiteCommand.class);
        if (config.getBoolean("commands.tp.tphere-enabled")) TeleportHereCommand.register();
        if (config.getBoolean("commands.tp.tpall-enabled")) TeleportAllCommand.register();
        if (Config.commandEnabled("flyspeed")) FlySpeedCommand.register();
        if (Config.commandEnabled("vanish")) VanishCommand.register();
        if (Config.commandEnabled("feed")) FeedCommand.register();
        if (Config.commandEnabled("clearinventory")) ClearInventoryCommand.register();

        if (Config.commandEnabled("warp")) WarpCommand.register();
        if (Config.commandEnabled("setwarp")) SetWarpCommand.register();
        if (Config.commandEnabled("deletewarp")) DelWarpCommand.register();

        if (Config.commandEnabled("mutechat")) MuteChatCommand.register();
        if (Config.commandEnabled("sudo")) SudoCommand.register();
        if (Config.commandEnabled("back")) BackCommand.register();
        if (Config.commandEnabled("report")) ReportCommand.register();
        registerCommand(UptimeCommand.class);
        if (Config.commandEnabled("channel")) ChatCommand.register();
        if (Config.commandEnabled("kit")) {
            new KitCommand(new KitManager(INSTANCE.getDataFolder())).register();
        }
        registerCommand(ClearChatCommand.class);
        registerCommand(SkinCommand.class);
    }
    private static void registerCommand(Class<? extends PotatoCommand> clazz) {
        registerCommand(clazz, () -> {});
    }
    private static void registerCommand(Class<? extends PotatoCommand> clazz, Runnable runnable) {
        try {
            PotatoCommand commandInstance = clazz.getDeclaredConstructor().newInstance();
            String name = commandInstance.getName();
            if (Config.commandEnabled(name)) {
                CommandAPI.registerCommand(clazz);
                LOGGER.info("'"+name+"' enabled in config");

            } else {
                LOGGER.info("'"+name+"' disabled in config");
            }
            runnable.run();
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error registering command", e);
        }
    }
    private boolean setupVaultChat() {
        if (pluginManager.getPlugin("Vault") == null) {
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
}
