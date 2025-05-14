package com.mcdragonmasters.potatodiscordlink;

import com.google.common.util.concurrent.ThreadFactoryBuilder;
import com.mcdragonmasters.potatodiscordlink.commands.LinkCommand;
import com.mcdragonmasters.potatodiscordlink.commands.LinkedCommand;
import com.mcdragonmasters.potatodiscordlink.commands.UnlinkCommand;
import com.mcdragonmasters.potatodiscordlink.listeners.DcSlashCommandListener;
import com.mcdragonmasters.potatodiscordlink.listeners.MCPreJoinListener;
import com.mcdragonmasters.potatoessentials.PotatoPlugin;
import com.mcdragonmasters.potatoessentials.libs.configupdater.ConfigUpdater;
import com.mcdragonmasters.potatoessentials.utils.PotatoLogger;
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

import java.io.File;
import java.io.IOException;
import java.util.concurrent.*;
import java.util.logging.Level;

public class PotatoDiscordLink extends PotatoPlugin {
    private static FileConfiguration config;
    public static PotatoDiscordLink INSTANCE;
    public static PotatoLogger LOGGER;
    @Getter
    private static JDA jda;
    @Getter
    private static LinkManager linkManager;
    @Override
    @SneakyThrows
    public void onEnable() {
        saveDefaultConfig();
        INSTANCE = this;
        try {
            ConfigUpdater.update(INSTANCE, "config.yml",
                    new File(INSTANCE.getDataFolder(), "config.yml"));
        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, "Error updating config", e);
        }
        config = getConfig();
        LOGGER = getLogger();
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
        jda = JDABuilder.create(token, GatewayIntent.GUILD_MEMBERS, GatewayIntent.GUILD_PRESENCES,
                        GatewayIntent.GUILD_VOICE_STATES, GatewayIntent.GUILD_EXPRESSIONS, GatewayIntent.SCHEDULED_EVENTS)
                .setCallbackPool(callbackThreadPool, false)
                .setGatewayPool(gatewayThreadPool, true)
                .setMemberCachePolicy(MemberCachePolicy.ALL)
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
        var registrar = this.getCommandRegistrar();
        registrar.register(new LinkCommand(),
                new UnlinkCommand(),
                new LinkedCommand()
        );
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
