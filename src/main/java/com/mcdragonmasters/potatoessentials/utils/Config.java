package com.mcdragonmasters.potatoessentials.utils;

import com.mcdragonmasters.potatoessentials.PotatoEssentials;
import com.tchristofferson.configupdater.ConfigUpdater;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.milkbowl.vault.chat.Chat;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.io.IOException;

public class Config {
    private static FileConfiguration config;
    private static PotatoEssentials potatoEssentials;

    public static void config(PotatoEssentials plugin) {
        plugin.saveDefaultConfig();
        potatoEssentials = plugin;
        try {
            ConfigUpdater.update(plugin,"config.yml",
                    new File(plugin.getDataFolder(),"config.yml"));
        } catch (IOException e) {
            plugin.getLogger().severe(e.getMessage());
        }

        reload();

    }
    public static void reload() {
        potatoEssentials.saveDefaultConfig();
        potatoEssentials.reloadConfig();
        config = potatoEssentials.getConfig();
    }
    public static Boolean chatEnabled() { return config.getBoolean("chat.enabled"); }
    public static String chatFormat() { return config.getString("chat.format"); }
    public static String messageSender(){return config.getString("messages.commands.message.sender");}
    public static String messageReceiver(){return config.getString("messages.commands.message.receiver");}
    public static String messageSocialSpy(){return config.getString("messages.commands.message.social-spy");}
    public static String broadcastFormat() { return config.getString("messages.broadcast"); }

    public static Component replaceFormat(String format,Component message,@Nullable CommandSender sender, @Nullable CommandSender secondary) {
        Chat vaultChat = PotatoEssentials.getVaultChat();
        if (sender!=null) {
            format = format.replace("%name%", sender.getName());
            if (secondary!=null) format = format.replace("%name-2%", secondary.getName());
            if (PotatoEssentials.hasVault()&&sender instanceof Player p) {
                format = format
                        .replace("%prefix%", vaultChat.getPlayerPrefix(p))
                        .replace("%suffix%", vaultChat.getPlayerSuffix(p));
            }
        }
        Component formattedComponent = MiniMessage.miniMessage().deserialize(format);
        return formattedComponent.replaceText(builder ->
                builder.match("%message%").replacement(message)
        );
    }
    public static Component replaceFormatMiniMessage(String format,Component message,@Nullable CommandSender sender, @Nullable CommandSender secondary) {
        return replaceFormat(format,Utils.miniMessage(MiniMessage.miniMessage().serialize(message)),sender, secondary);
    }
    public static Component replaceFormatMiniMessage(String format,String message,@Nullable CommandSender sender, @Nullable CommandSender secondary) {
        return replaceFormat(format,Utils.miniMessage(message),sender, secondary);
    }
    public static Component replaceFormat(String format,String message,@Nullable CommandSender sender, @Nullable CommandSender secondary) {
        return replaceFormat(format, Component.text(message), sender, secondary);
    }

}
