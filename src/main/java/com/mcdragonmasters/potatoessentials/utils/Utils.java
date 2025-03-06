package com.mcdragonmasters.potatoessentials.utils;

import com.mcdragonmasters.potatoessentials.PotatoEssentials;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver;
import net.milkbowl.vault.chat.Chat;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.text.DecimalFormat;
import java.util.Collection;

public class Utils {
    public static Component miniMessage(String s) {
        return MiniMessage.miniMessage().deserialize(s);
    }
    public static Component miniMessage(String s, TagResolver... tagResolvers) {
        return MiniMessage.miniMessage().deserialize(s, tagResolvers);
    }
    public static String escapeTags(String s) {
        return MiniMessage.miniMessage().escapeTags(s);
    }
    public static Chat vaultChat = PotatoEssentials.getVaultChat();
    public static String formatCoords(double x, double y, double z) {
        DecimalFormat df = new DecimalFormat("#.##");
        return df.format(x) + ", " + df.format(y) + ", " + df.format(z);
    }
    public static String serialize(Component c) { return MiniMessage.miniMessage().serialize(c); }

    public static String getPrefix(CommandSender sender) {
        if (!(sender instanceof Player player)) return "";
        if (!PotatoEssentials.isVaultInstalled()) return "vaultNotInstalled";
        return vaultChat.getPlayerPrefix(player);
    }

    public static String getSuffix(CommandSender sender) {
        if (!(sender instanceof Player player)) return "";
        if (!PotatoEssentials.isVaultInstalled()) return "vaultNotInstalled";
        return vaultChat.getPlayerSuffix(player);
    }
    public static String playerNameFormat(Collection<Player> players) {
        return players.size()==1?players.toArray(new Player[0])[0].getName():players.size()+" Players";
    }
    public static String possessivePlayerNameFormat(Collection<Player> players) {
        return players.size()==1?players.toArray(new Player[0])[0].getName()+"'s":players.size()+" Players'";
    }
}
