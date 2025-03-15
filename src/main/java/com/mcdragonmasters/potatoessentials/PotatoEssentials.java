package com.mcdragonmasters.potatoessentials;

import com.mcdragonmasters.potatoessentials.commands.*;
import com.mcdragonmasters.potatoessentials.commands.messaging.*;
import com.mcdragonmasters.potatoessentials.commands.teleporting.*;
import com.mcdragonmasters.potatoessentials.commands.warping.DelWarpCommand;
import com.mcdragonmasters.potatoessentials.commands.warping.SetWarpCommand;
import com.mcdragonmasters.potatoessentials.commands.warping.WarpCommand;
import com.mcdragonmasters.potatoessentials.database.KitManager;
import com.mcdragonmasters.potatoessentials.experimental.NametagManager;
import com.mcdragonmasters.potatoessentials.listeners.PlayerChatListener;
import com.mcdragonmasters.potatoessentials.listeners.ServerListPingListener;
import com.mcdragonmasters.potatoessentials.utils.Config;
import com.mcdragonmasters.potatoessentials.utils.PotatoCommand;
import com.mcdragonmasters.potatoessentials.utils.Utils;
import dev.jorel.commandapi.CommandAPI;
import dev.jorel.commandapi.CommandAPIBukkitConfig;

import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

import net.milkbowl.vault.chat.Chat;

import java.util.Arrays;
import java.util.logging.Logger;

public class PotatoEssentials extends JavaPlugin {

    public static PotatoEssentials INSTANCE;
    public final static String NAMESPACE = "potatoessentials";
    @Getter
    private static Chat vaultChat = null;
    @Getter
    private static boolean vaultInstalled = false;
    public static PluginManager pluginManager;
    public static FileConfiguration config;
    public static PotatoLogger LOGGER;

    @Override
    public void onLoad() {
        CommandAPI.onLoad(new CommandAPIBukkitConfig(this).usePluginNamespace());
    }

    @Override
    public void onEnable() {

        LOGGER = new PotatoLogger(getLogger());

        INSTANCE = this;

        Config.config(this);

        CommandAPI.onEnable();

        pluginManager = Bukkit.getPluginManager();

        pluginManager.registerEvents(new PlayerChatListener(), INSTANCE);
        pluginManager.registerEvents(new ServerListPingListener(), INSTANCE);

        vaultInstalled = setupVaultChat();

        registerCommands();

        LOGGER.info("Enabled");
        LOGGER.info(vaultInstalled ? "Vault found" : "Vault not found");

        if (config.getBoolean("experimentalFeatures")) new NametagManager();
    }


    private static void registerCommands() {
        var kits = new KitManager(INSTANCE.getDataFolder());

        registerCommand(new MainCommand(), true);

        if (Config.commandEnabled("message")) MessageCommand.register();
        registerCommand(new ReplyCommand());
        if (Config.commandEnabled("socialspy")) SocialSpyCommand.register();
        if (Config.commandEnabled("broadcast")) BroadcastCommand.register();
        if (Config.commandEnabled("messagetoggle")) MessageToggleCommand.register();

        if (Config.commandEnabled("enchant")) EnchantCommand.register();
        if (Config.commandEnabled("gamemode"))  GameModeCommand.register();
        if (Config.commandEnabled("heal")) HealCommand.register();
        if (Config.commandEnabled("hunger")) HungerCommand.register();
        if (Config.commandEnabled("invsee")) InvSeeCommand.register();
        if (Config.commandEnabled("ping")) PingCommand.register();
        registerCommand(new SmiteCommand());
        if (config.getBoolean("commands.tp.tphere-enabled")) TeleportHereCommand.register();
        if (config.getBoolean("commands.tp.tpall-enabled")) TeleportAllCommand.register();
        if (Config.commandEnabled("flyspeed")) FlySpeedCommand.register();
        registerCommand(new VanishCommand());
        if (Config.commandEnabled("feed")) FeedCommand.register();
        registerCommand(new ClearInventoryCommand());

        if (Config.commandEnabled("warp")) WarpCommand.register();
        if (Config.commandEnabled("setwarp")) SetWarpCommand.register();
        if (Config.commandEnabled("deletewarp")) DelWarpCommand.register();

        if (Config.commandEnabled("mutechat")) MuteChatCommand.register();
        if (Config.commandEnabled("sudo")) SudoCommand.register();
        if (Config.commandEnabled("back")) BackCommand.register();
        if (Config.commandEnabled("report")) ReportCommand.register();
        registerCommand(new UptimeCommand());
        if (Config.commandEnabled("channel")) ChatCommand.register();
        if (Config.commandEnabled("kit")) new KitCommand(kits).register();

        registerCommand(new ClearChatCommand());
        registerCommand(new SkinCommand());
        registerCommand(new SkullCommand());
        registerCommand(new TrollCommand());
    }
    private static void registerCommand(PotatoCommand command, boolean forced) {
        String name = command.getName();
        if (Config.commandEnabled(name) || forced) {
            command.register();
            LOGGER.debug("Registering command '%s' with aliases '%s'".formatted(name, Arrays.toString(command.getAliases())));

        } else {
            LOGGER.debug("Command '%s' disabled in config, not registering".formatted(name));
        }
    }
    private static void registerCommand(PotatoCommand command) {
        registerCommand(command, false);
    }
    private boolean setupVaultChat() {
        if (pluginManager.getPlugin("Vault") == null) {
            return false;
        }
        RegisteredServiceProvider<Chat> rsp = getServer().getServicesManager().getRegistration(Chat.class);
        if (rsp != null) {
            vaultChat = rsp.getProvider();
        }
        Utils.vaultChat = vaultChat;
        return vaultChat != null;
    }

    @Override
    public void onDisable() {
        CommandAPI.onDisable();
    }
    public static class PotatoLogger extends Logger{

        private final Logger logger;

        public PotatoLogger(Logger logger) {
            super(logger.getName(), logger.getResourceBundleName());
            this.logger = logger;
        }
        public void debug(String msg) {
            if (!Config.isDebug()) return;
            logger.info("[DEBUG] "+msg);
        }
//        public void info(String msg) {
//            logger.info(msg);
//        }
//        public void log(Level level, String msg, Throwable thrown) {
//            logger.log(level, msg, thrown);
//        }
//        public void severe(String msg) {
//            logger.severe(msg);
//        }
//        public void warning(String msg) {
//            logger.warning(msg);
//        }
    }
}
