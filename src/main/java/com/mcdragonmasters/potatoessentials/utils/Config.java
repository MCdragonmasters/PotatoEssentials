package com.mcdragonmasters.potatoessentials.utils;

import com.mcdragonmasters.potatoessentials.PotatoEssentials;
import com.tchristofferson.configupdater.ConfigUpdater;
import lombok.Getter;
import net.kyori.adventure.text.Component;
import org.bukkit.configuration.file.FileConfiguration;

import java.io.File;
import java.io.IOException;

public class Config {
    @Getter
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
        PotatoEssentials.config = config;
    }
    public static Boolean chatEnabled() { return config.getBoolean("chat.enabled"); }
    public static String chatFormat() { return config.getString("chat.format"); }
    public static String messageSender(){return config.getString("commands.message.sender");}
    public static String messageReceiver(){return config.getString("commands.message.receiver");}
    public static String messageSocialSpy(){return config.getString("commands.message.social-spy");}
    public static String broadcastFormat() { return config.getString("commands.broadcast.message"); }
    public static boolean commandEnabled(String s) { return config.getBoolean("commands."+s+".enabled"); }

    public static Component replaceFormat(String format, boolean miniMessage, Replacer... replacers) {
        Component formatComponent = Utils.miniMessage(format);
        for (Replacer replacer : replacers) {
            formatComponent =  formatComponent.replaceText(builder ->
               builder.match("%"+replacer.getOldText()+"%")
                 .replacement(miniMessage?Utils.miniMessage(replacer.getNewText())
                    :Component.text(replacer.getNewText())));
        }

        return formatComponent;
    }
    public static Component replaceFormat(String format, Replacer... replacers) {
        return replaceFormat(format, false, replacers);
    }

}
