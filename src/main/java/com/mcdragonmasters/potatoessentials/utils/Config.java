package com.mcdragonmasters.potatoessentials.utils;

import com.mcdragonmasters.potatoessentials.PotatoEssentials;
import com.tchristofferson.configupdater.ConfigUpdater;

import lombok.Getter;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.tag.Tag;
import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver;

import org.bukkit.Bukkit;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.logging.Level;

import static com.mcdragonmasters.potatoessentials.PotatoEssentials.LOGGER;

public class Config {
    @Getter
    private static FileConfiguration config;
    private static PotatoEssentials potatoEssentials;
    @Getter
    private static final Map<String, String> emojis = new HashMap<>();

    public static void config(PotatoEssentials plugin) {
        potatoEssentials = plugin;
        plugin.saveDefaultConfig();
        plugin.reloadConfig();
        config = plugin.getConfig();
        try {
            List<String> ignoredList = new ArrayList<>();
            if (config.isConfigurationSection("chat.emojis")) ignoredList.add("chat.emojis");
            if (config.isConfigurationSection("chat.filteredWords")) ignoredList.add("chat.filteredWords");
            if (config.isConfigurationSection("chats.customChats")) ignoredList.add("chats.customChats");
            ConfigUpdater.update(plugin,"config.yml",
                    new File(plugin.getDataFolder(), "config.yml"), ignoredList.isEmpty()?null:ignoredList);
        } catch (IOException e) {
            PotatoEssentials.LOGGER.log(Level.SEVERE, "Error updating config", e);
            return;
        }
        String reload = reload();
        if (!"passed".equals(reload)) LOGGER.severe("Error loading config\n"+reload);

    }
    public static String reload() {
        for (Player p : Bukkit.getOnlinePlayers()) {
            p.removeCustomChatCompletions(Config.getEmojis().keySet());
        }
        potatoEssentials.saveDefaultConfig();
        potatoEssentials.reloadConfig();
        config = potatoEssentials.getConfig();
        PotatoEssentials.config = config;

        emojiConfig();

        ConfigurationSection chatsSection = config.getConfigurationSection("chats.customChats");
        CustomChat.getCustomChats().clear();
        if (customChatsEnabled()&&chatsSection!=null) {
            for (String key : chatsSection.getKeys(false)) {
                if ("global".equals(key) || "all".equals(key)) {
                    return "keys 'global' and 'all' are reserved for global chat.";
                }
                String chatConfPrefix = "chats.customChats."+key+".";
                CustomChat.addChat(
                        new CustomChat(key,
                                getString(chatConfPrefix+"name"),
                                getString(chatConfPrefix+"permission"),
                                getString(chatConfPrefix+"command"))
                );
            }
        }
        return "passed";
    }
    private static void emojiConfig() {
        ConfigurationSection emojiSection = config.getConfigurationSection("chat.emojis");
        emojis.clear();
        if (emojisEnabled()&&emojiSection!=null) {
            for (String key : emojiSection.getKeys(false)) {
                emojis.put(key,config.getString("chat.emojis."+key));
            }
        }
        for (Player p : Bukkit.getOnlinePlayers()) {
            p.addCustomChatCompletions(Config.getEmojis().keySet());
        }
    }
    public static Boolean chatEnabled() {
        return config.getBoolean("chat.enabled");
    }
    public static Boolean formatURLs() {
        return config.getBoolean("chat.format-urls");
    }
    public static String chatFormat() {
        return config.getString("chat.format");
    }
    public static String messageSender() {
        return config.getString("commands.message.sender");
    }
    public static String messageReceiver() {
        return config.getString("commands.message.receiver");
    }
    public static String messageSocialSpy() {
        return config.getString("commands.socialspy.message");
    }
    public static String broadcastFormat() {
        return getString("commands.broadcast.message");
    }
    public static boolean commandEnabled(String s) {
        return getBoolean("commands."+s+".enabled");
    }
    public static boolean emojisEnabled() {
        return getBoolean("chat.emojis-enabled");
    }

    public static String teleportedMsg() {
        return getString("commands.tp.teleported-message");
    }
    public static String teleporterMsg() {
        return getString("commands.tp.teleporter-message");
    }
    public static String muteChatMutedMsg() {
        return getString("commands.mutechat.mute");
    }
    public static String muteChatUnmutedMsg() {
        return getString("commands.mutechat.unmute");
    }
    public static String sudoSayMsg() {
        return getString("commands.sudo.say-message");
    }
    public static String sudoCmdMsg() {
        return getString("commands.sudo.command-message");
    }
    public static String reportLogFormat() {
        return getString("commands.report.report-log-format");
    }
    public static String reportFormat() {
        return getString("commands.report.report-format");
    }
    public static String joinMessageFormat() {
        return getString("connectionMessages.joinMessage");
    }
    public static String firstJoinMessageFormat() {
        return getString("connectionMessages.firstJoinMessage");
    }
    public static String quitMessageFormat() {
        return getString("connectionMessages.quitMessage");
    }
    @SuppressWarnings("BooleanMethodIsAlwaysInverted")
    public static boolean modifyConnectionMessages() {
        return config.getBoolean("connectionMessages.modifyConnectionMessages");
    }
    public static String uptimeMessageFormat() {
        return config.getString("commands.uptime.message");
    }

    public static boolean chatFilterWords() {
        return config.getBoolean("chat.filterWords");
    }
    public static Collection<String> chatFilteredWords() {
        return config.getStringList("chat.filteredWords");
    }

    public static String enchantmentFormat() {
        return config.getString("commands.enchant.message");
    }
    public static String customChatFormat() {
        return getString("chats.format");
    }
    public static boolean customChatsEnabled() {
        return getBoolean("chats.enabled");
    }
    public static String chatChannelChangeFormat() {
        return getString("commands.channel.message");
    }
    public static boolean addEmojisToCustomChatCompletions() {
        return getBoolean("chat.addEmojisToCustomChatCompletions");
    }

    public static String getString(String s) {
        return config.getString(s);
    }
    public static boolean getBoolean(String s) {
        return config.getBoolean(s);
    }
    public static String getCmdMsg(String cmdName, String field) {
        return getString("commands."+cmdName+"."+field);
    }


    @SuppressWarnings("PatternValidation")
    public static Component replaceFormat(String format, Replacer... replacers) {
        TagResolver.Builder resolverBuilder = TagResolver.builder();
        for (Replacer r : replacers) {
            String newText = r.getNewText();
            String oldText = r.getOldText();
            Component replacement;
            if (r.replaceRaw()) {
                format = format.replace("<"+oldText+">", newText);
                continue;
            }
            if (r.usesMiniMsg()) {
                replacement = Utils.miniMessage(newText);
            } else replacement = Component.text(newText);
            resolverBuilder.resolver(TagResolver.resolver(oldText, Tag.inserting(replacement)));
        }

        TagResolver resolver = resolverBuilder.build();
        return Utils.miniMessage(format, resolver);
    }
}
