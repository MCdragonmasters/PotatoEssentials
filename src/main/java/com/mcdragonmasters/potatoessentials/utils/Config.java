package com.mcdragonmasters.potatoessentials.utils;

import com.mcdragonmasters.potatoessentials.PotatoEssentials;
import com.tchristofferson.configupdater.ConfigUpdater;
import lombok.Getter;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.minimessage.tag.Tag;
import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;


public class Config {
    @Getter
    private static FileConfiguration config;
    private static PotatoEssentials potatoEssentials;
    @Getter
    private static final Map<String, String> emojis = new HashMap<>();

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
    public static boolean reload() {
        potatoEssentials.saveDefaultConfig();
        potatoEssentials.reloadConfig();
        config = potatoEssentials.getConfig();
        PotatoEssentials.config = config;
        if (!config.isConfigurationSection("chat.emojis")) return false;

        ConfigurationSection section = config.getConfigurationSection("chat.emojis");
        if (section==null) return false;

        for (String key : section.getKeys(false)) {
            emojis.put(key,config.getString("chat.emojis."+key));
        }
        return true;
    }
    public static Boolean chatEnabled() { return config.getBoolean("chat.enabled"); }
    public static String chatFormat() { return config.getString("chat.format"); }
    public static String messageSender() { return config.getString("commands.message.sender");}
    public static String messageReceiver() { return config.getString("commands.message.receiver");}
    public static String messageSocialSpy() { return config.getString("commands.message.social-spy");}
    public static String broadcastFormat() { return config.getString("commands.broadcast.message"); }
    public static boolean commandEnabled(String s) { return config.getBoolean("commands."+s+".enabled"); }
    public static boolean emojisEnabled() { return config.getBoolean("chat.emojis-enabled"); }
    public static String getEmojiToken() { return config.getString("chat.emoji-token"); }

    @SuppressWarnings("PatternValidation")
    public static Component replaceFormat(String format, Replacer... replacers) {
        MiniMessage miniMessage = MiniMessage.miniMessage();

        TagResolver.Builder resolverBuilder = TagResolver.builder();

        for (Replacer r : replacers) {
            String newText = r.getNewText();
            Component replacement = r.isMiniMsg()
                    ? miniMessage.deserialize(newText)
                    : Component.text(newText);
            resolverBuilder.resolver(TagResolver.resolver(r.getOldText(), Tag.inserting(replacement)));
        }
        TagResolver resolver = resolverBuilder.build();

        return Utils.miniMessage(format, resolver);
    }

}
