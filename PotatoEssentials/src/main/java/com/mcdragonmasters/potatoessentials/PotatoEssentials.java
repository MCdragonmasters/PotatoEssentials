package com.mcdragonmasters.potatoessentials;

import com.mcdragonmasters.potatoessentials.commands.*;
import com.mcdragonmasters.potatoessentials.commands.messaging.*;
import com.mcdragonmasters.potatoessentials.commands.teleporting.*;
import com.mcdragonmasters.potatoessentials.commands.warping.DelWarpCommand;
import com.mcdragonmasters.potatoessentials.commands.warping.SetWarpCommand;
import com.mcdragonmasters.potatoessentials.commands.warping.WarpCommand;
import com.mcdragonmasters.potatoessentials.database.KitManager;
import com.mcdragonmasters.potatoessentials.listeners.PlayerChatListener;
import com.mcdragonmasters.potatoessentials.listeners.ServerListPingListener;
import com.mcdragonmasters.potatoessentials.utils.Config;
import com.mcdragonmasters.potatoessentials.utils.PotatoCommand;
import com.mcdragonmasters.potatoessentials.utils.PotatoLogger;
import com.mcdragonmasters.potatoessentials.utils.Utils;
import dev.jorel.commandapi.CommandAPI;
import dev.jorel.commandapi.CommandAPIBukkitConfig;

import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

import net.milkbowl.vault.chat.Chat;

import java.util.Arrays;

@SuppressWarnings("SameParameterValue")
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
        var cmdAPIConfig = new CommandAPIBukkitConfig(this).usePluginNamespace();
        if (!getConfig().getBoolean("reloadDatapacks")) {
            cmdAPIConfig.skipReloadDatapacks(true);
        }
        CommandAPI.onLoad(cmdAPIConfig);
    }

    @Override
    public void onEnable() {

        INSTANCE = this;

        LOGGER = new PotatoLogger(getLogger(), config);

        Config.config(this);

        CommandAPI.onEnable();

        pluginManager = Bukkit.getPluginManager();

        pluginManager.registerEvents(new PlayerChatListener(), INSTANCE);
        pluginManager.registerEvents(new ServerListPingListener(), INSTANCE);

        vaultInstalled = setupVaultChat();

        registerCommands();
        LOGGER.debug("Registered "+registeredCmds+" commands");
        LOGGER.info(vaultInstalled ? "Vault found" : "Vault not found");
        LOGGER.info("Enabled");

    }


    private static void registerCommands() {
        var kits = new KitManager(INSTANCE.getDataFolder());

        registerCommand(new MainCommand(), true);

        registerCommand(new MessageCommand());
        registerCommand(new ReplyCommand());
        registerCommand(new SocialSpyCommand());
        registerCommand(new BroadcastCommand());
        registerCommand(new MessageToggleCommand());

        registerCommand(new EnchantCommand());
        registerCommand(new GameModeCommand());
        registerCommand(new HealCommand());
        registerCommand(new HungerCommand());
        registerCommand(new InvSeeCommand());
        registerCommand(new PingCommand());
        registerCommand(new SmiteCommand());
        if (config.getBoolean("commands.tp.tphere-enabled")) new TeleportHereCommand().register();
        if (config.getBoolean("commands.tp.tpall-enabled")) new TeleportAllCommand().register();
        registerCommand(new FlySpeedCommand());
        registerCommand(new VanishCommand());
        registerCommand(new FeedCommand());
        registerCommand(new ClearInventoryCommand());

        registerCommand(new WarpCommand());
        registerCommand(new SetWarpCommand());
        registerCommand(new DelWarpCommand());

        registerCommand(new MuteChatCommand());
        registerCommand(new SudoCommand());
        registerCommand(new BackCommand());
        if (Config.commandEnabled("report")) ReportCommand.register();
        registerCommand(new UptimeCommand());
        registerCommand(new ChannelCommand());
        registerCommand(new KitCommand(kits));

        registerCommand(new ClearChatCommand());
        registerCommand(new SkinCommand());
        registerCommand(new SkullCommand());
        registerCommand(new TrollCommand());
        registerCommand(new TeleportCenterCommand());
    }

    private static int registeredCmds = 0;

    public static void registerCommand(PotatoCommand command, boolean forced, Plugin plugin) {
        String name = command.getName();
        if (Config.commandEnabled(name, plugin.getConfig()) || forced) {
            command.register();
            var msg = (plugin!=INSTANCE?"[%s] ".formatted(plugin.getName()):"")+"Registering command '%s'";
            if (command.hasAliases()) msg += " with aliases '%s'";
            LOGGER.debug(msg.formatted(name, formatList(command.getAliases())));
            if(plugin==INSTANCE) registeredCmds++;
        } else {
            LOGGER.debug("Command '%s' disabled in config, not registering".formatted(name));
        }
    }
    public static void registerCommand(PotatoCommand command, Plugin plugin) {
        registerCommand(command, false, plugin);
    }
    private static void registerCommand(PotatoCommand command, boolean forced) {
        registerCommand(command, forced, INSTANCE);
    }
    private static void registerCommand(PotatoCommand command) {
        registerCommand(command, false);
    }
    private static String formatList(String[] list) {
        String arrStr = Arrays.toString(list);
        return arrStr.substring(1, arrStr.length() - 1);
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
}
