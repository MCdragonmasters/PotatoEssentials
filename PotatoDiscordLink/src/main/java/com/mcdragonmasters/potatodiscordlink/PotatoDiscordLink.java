package com.mcdragonmasters.potatodiscordlink;

import com.google.common.util.concurrent.ThreadFactoryBuilder;
import com.mcdragonmasters.potatodiscordlink.commands.LinkCommand;
import com.mcdragonmasters.potatodiscordlink.commands.UnlinkCommand;
import com.mcdragonmasters.potatodiscordlink.listeners.DcSlashCommandListener;
import com.mcdragonmasters.potatodiscordlink.listeners.MCPreJoinListener;
import com.mcdragonmasters.potatoessentials.PotatoEssentials;
import com.mcdragonmasters.potatoessentials.utils.PotatoLogger;
import dev.jorel.commandapi.CommandAPI;
import dev.jorel.commandapi.CommandAPIBukkitConfig;
import lombok.Getter;
import lombok.SneakyThrows;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.dv8tion.jda.api.utils.MemberCachePolicy;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.concurrent.*;

public class PotatoDiscordLink extends JavaPlugin {
    private static FileConfiguration config;
    public static PotatoDiscordLink INSTANCE;
    public static PotatoLogger LOGGER;
    @Getter
    private static JDA jda;
    @Getter
    private static LinkManager linkManager;
    @Override
    public void onLoad() {
        var cmdAPIConfig = new CommandAPIBukkitConfig(this).usePluginNamespace();
        if (!getConfig().getBoolean("reloadDatapacks")) {
            cmdAPIConfig.skipReloadDatapacks(true);
        }
        CommandAPI.onLoad(cmdAPIConfig);
    }
    @Override
    @SneakyThrows
    public void onEnable() {
        saveDefaultConfig();
        config = getConfig();
        INSTANCE = this;
        LOGGER = new PotatoLogger(this.getLogger(), config);
        String token = config.getString("botToken");
        if (token == null || token.equals("TOKEN_HERE") || token.isEmpty()) {
            disablePlugin("Disabling due to token being not set");
            return;
        }

        ForkJoinPool callbackThreadPool = new ForkJoinPool(Runtime.getRuntime().availableProcessors(), pool -> {
            final ForkJoinWorkerThread worker = ForkJoinPool.defaultForkJoinWorkerThreadFactory.newThread(pool);
            worker.setName("DiscordSRV - JDA Callback " + worker.getPoolIndex());
            return worker;
        }, null, true);

        ThreadFactory gatewayThreadFactory = new ThreadFactoryBuilder().setNameFormat("DiscordSRV - JDA Gateway").build();
        ScheduledExecutorService gatewayThreadPool = Executors.newSingleThreadScheduledExecutor(gatewayThreadFactory);

        jda = JDABuilder.createLight(token)
                .setCallbackPool(callbackThreadPool, false)
                .setGatewayPool(gatewayThreadPool, true)
                .setMemberCachePolicy(MemberCachePolicy.ALL)
                .enableIntents(GatewayIntent.GUILD_MEMBERS)
                .addEventListeners(new DcSlashCommandListener())
                .build();

        jda.awaitReady();
        var linkedRoleId = config.getString("linkedRole");

        if (linkedRoleId!=null && !linkedRoleId.isEmpty()) {
            linkedRole = jda.getRoleById(linkedRoleId);
        }
        primaryGuild = jda.getGuilds().getFirst();


        var commands = jda.updateCommands();

        commands.addCommands(
                Commands.slash("link", "Links your Discord to your Minecraft")
                        .addOption(OptionType.STRING, "code",
                                "The code you got by running /link in-game", true),
                Commands.slash("unlink", "Unlinks your Discord from your Minecraft")
        );
        commands.queue();

        linkManager = new LinkManager();
        Bukkit.getPluginManager().registerEvents(new MCPreJoinListener(), INSTANCE);
        PotatoEssentials.registerCommand(new LinkCommand(), INSTANCE);
        PotatoEssentials.registerCommand(new UnlinkCommand(), INSTANCE);
    }
    @Override
    public void onDisable() {
        if (linkManager == null) return;
        linkManager.save();
    }
    public void disablePlugin(String msg) {
        Bukkit.getScheduler().runTask(
                this,
                () -> {
                    LOGGER.severe(msg);
                    Bukkit.getPluginManager().disablePlugin(this);
                }
        );
    }
    public static FileConfiguration config() {
        return config;
    }
    private static Role linkedRole;
    public static Role getLinkedRole() {
        return linkedRole;
    }
    private static Guild primaryGuild;
    public static Guild getPrimaryGuild() {
        return primaryGuild;
    }

}
