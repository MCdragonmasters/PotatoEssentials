package com.mcdragonmasters.potatoessentials;

import com.mcdragonmasters.potatoessentials.commands.*;
import com.mcdragonmasters.potatoessentials.commands.messaging.*;
import com.mcdragonmasters.potatoessentials.commands.teleporting.TeleportAllCommand;
import com.mcdragonmasters.potatoessentials.commands.teleporting.TeleportHereCommand;
import com.mcdragonmasters.potatoessentials.commands.warping.DelWarpCommand;
import com.mcdragonmasters.potatoessentials.commands.warping.SetWarpCommand;
import com.mcdragonmasters.potatoessentials.commands.warping.WarpCommand;
import com.mcdragonmasters.potatoessentials.database.KitManager;
import com.mcdragonmasters.potatoessentials.listeners.PlayerChatListener;
import com.mcdragonmasters.potatoessentials.listeners.ServerListPingListener;
import com.mcdragonmasters.potatoessentials.utils.Config;
import com.mcdragonmasters.potatoessentials.utils.PotatoCommandRegistrar;
import com.mcdragonmasters.potatoessentials.utils.PotatoLogger;
import com.mcdragonmasters.potatoessentials.utils.Utils;
import dev.jorel.commandapi.CommandAPI;
import dev.jorel.commandapi.CommandAPIBukkitConfig;
import lombok.Getter;
import net.milkbowl.vault.chat.Chat;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.RegisteredServiceProvider;

@SuppressWarnings("SameParameterValue")
public class PotatoEssentials extends PotatoPlugin {

    public static PotatoEssentials INSTANCE;
    public final static String NAMESPACE = "potatoessentials";
    @Getter
    private static Chat vaultChat = null;
    @Getter
    private static boolean vaultInstalled = false;
    public static PluginManager pluginManager;
    public FileConfiguration config;
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

        config = getConfig();

        LOGGER = getLogger();

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
        PotatoCommandRegistrar registrar = this.getCommandRegistrar();
        registrar.register(new MainCommand(), true);
        registrar.register(new MessageCommand(),
                new ReplyCommand(),
                new SocialSpyCommand(),
                new BroadcastCommand(),
                new MessageToggleCommand(),
                new EnchantCommand(),
                new GameModeCommand(),
                new HealCommand(),
                new HungerCommand(),
                new InvSeeCommand(),
                new PingCommand(),
                new SmiteCommand(),
                new FlySpeedCommand(),
                new VanishCommand(),
                new FeedCommand(),
                new ClearInventoryCommand(),
                new WarpCommand(),
                new SetWarpCommand(),
                new DelWarpCommand(),
                new MuteChatCommand(),
                new SudoCommand(),
                new BackCommand(),
                new UptimeCommand(),
                new ChannelCommand(),
                new KitCommand(kits),
                new ClearChatCommand(),
                new SkinCommand(),
                new SkullCommand(),
                new TrollCommand(),
                new TeleportCenterCommand(),
                new ReportCommand()
        );

        if (config.getBoolean("commands.tp.tphere-enabled")) new TeleportHereCommand().register();
        if (config.getBoolean("commands.tp.tpall-enabled")) new TeleportAllCommand().register();

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
