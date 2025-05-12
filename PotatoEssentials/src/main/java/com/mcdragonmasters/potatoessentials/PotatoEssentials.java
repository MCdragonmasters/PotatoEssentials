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
import com.mcdragonmasters.potatoessentials.utils.*;
import dev.jorel.commandapi.CommandAPI;
import dev.jorel.commandapi.CommandAPIBukkitConfig;

import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

import net.milkbowl.vault.chat.Chat;

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
    @Getter
    private CommandAPIBukkitConfig cmdAPIConfig;

    @Override
    public void onLoad() {
        cmdAPIConfig = new CommandAPIBukkitConfig(this);
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
        LOGGER.info(vaultInstalled ? "Vault found" : "Vault not found");
        LOGGER.info("Enabled");

    }


    private void registerCommands() {
        var kits = new KitManager(INSTANCE.getDataFolder());
        var registrar = new PotatoCommandRegistrar(INSTANCE);
        registrar.register(new MainCommand(), true);

        registrar.register(new MessageCommand());
        registrar.register(new ReplyCommand());
        registrar.register(new SocialSpyCommand());
        registrar.register(new BroadcastCommand());
        registrar.register(new MessageToggleCommand());

        registrar.register(new EnchantCommand());
        registrar.register(new GameModeCommand());
        registrar.register(new HealCommand());
        registrar.register(new HungerCommand());
        registrar.register(new InvSeeCommand());
        registrar.register(new PingCommand());
        registrar.register(new SmiteCommand());
        if (config.getBoolean("commands.tp.tphere-enabled")) new TeleportHereCommand().register();
        if (config.getBoolean("commands.tp.tpall-enabled")) new TeleportAllCommand().register();
        registrar.register(new FlySpeedCommand());
        registrar.register(new VanishCommand());
        registrar.register(new FeedCommand());
        registrar.register(new ClearInventoryCommand());

        registrar.register(new WarpCommand());
        registrar.register(new SetWarpCommand());
        registrar.register(new DelWarpCommand());

        registrar.register(new MuteChatCommand());
        registrar.register(new SudoCommand());
        registrar.register(new BackCommand());
        if (Config.commandEnabled("report")) ReportCommand.register();
        registrar.register(new UptimeCommand());
        registrar.register(new ChannelCommand());
        registrar.register(new KitCommand(kits));

        registrar.register(new ClearChatCommand());
        registrar.register(new SkinCommand());
        registrar.register(new SkullCommand());
        registrar.register(new TrollCommand());
        registrar.register(new TeleportCenterCommand());
        LOGGER.debug("Registered "+registrar.getRegisteredCmds()+" commands");
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
