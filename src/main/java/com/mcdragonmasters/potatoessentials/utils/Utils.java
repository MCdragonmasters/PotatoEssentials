package com.mcdragonmasters.potatoessentials.utils;

import com.mcdragonmasters.potatoessentials.PotatoEssentials;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver;
import net.milkbowl.vault.chat.Chat;
import org.bukkit.entity.Player;

import java.text.DecimalFormat;

public class Utils {
    public static Component miniMessage(String s) {
        return MiniMessage.miniMessage().deserialize(s);
    }
    public static Component miniMessage(String s, TagResolver... tagResolvers) {
        return MiniMessage.miniMessage().deserialize(s, tagResolvers);
    }
    public static Chat vaultChat = PotatoEssentials.getVaultChat();
    public static String formatCoords(double x, double y, double z) {
        DecimalFormat df = new DecimalFormat("#.##");
        return df.format(x) + ", " + df.format(y) + ", " + df.format(z);
    }
    public static String serialize(Component c) { return MiniMessage.miniMessage().serialize(c); }
    public static String getPrefix(Player player) { return vaultChat.getPlayerPrefix(player); }
    public static String getSuffix(Player player) { return vaultChat.getPlayerSuffix(player); }
}
