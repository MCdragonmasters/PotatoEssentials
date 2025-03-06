package com.mcdragonmasters.potatoessentials.commands;

import com.destroystokyo.paper.profile.PlayerProfile;
import com.destroystokyo.paper.profile.ProfileProperty;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.mcdragonmasters.potatoessentials.PotatoEssentials;
import com.mcdragonmasters.potatoessentials.utils.Config;
import com.mcdragonmasters.potatoessentials.utils.PotatoCommand;
import com.mcdragonmasters.potatoessentials.utils.Replacer;
import dev.jorel.commandapi.annotations.Command;
import dev.jorel.commandapi.annotations.Default;
import dev.jorel.commandapi.annotations.Permission;
import dev.jorel.commandapi.annotations.Subcommand;
import dev.jorel.commandapi.annotations.arguments.AStringArgument;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.io.InputStreamReader;
import java.net.URI;
import java.net.URL;

@Command("skin")
public class SkinCommand extends PotatoCommand {
    @Override
    public String getName() { return "skin"; }

    @Default
    @Permission(PotatoEssentials.NAMESPACE+".skin")
    public static void setSkinCmd(Player player, @AStringArgument String skin) {
        var newSkinFromPlayer = Bukkit.getPlayer(skin);
        String skinName = "skinName not found";
        if (newSkinFromPlayer!=null) {
            setSkin(player, newSkinFromPlayer);
        } else {
            try {
                skinName = setSkin(player, skin);
            } catch (Exception e) {
                var msg = Config.replaceFormat(Config.getCmdMsg("skin", "skinNotFound"));
                player.sendMessage(msg);
                resetSkin(player);
                return;
            }
        }
        var msg = Config.replaceFormat(Config.getCmdMsg("skin", "setSkin"),
                new Replacer("skin", skinName));
        player.sendMessage(msg);
    }
    @Subcommand("reset")
    @Permission(PotatoEssentials.NAMESPACE+".skin")
    public static void resetSkin(Player player) {
        try {
            setSkin(player, player.getName());
        } catch (Exception e) {
            player.sendRichMessage("<red>Failed to reset skin!");
        }
    }

    private static String setSkin(Player player, String name) throws Exception {
        PlayerProfile playerProfile = player.getPlayerProfile();
        var uuidUrl = new URI("https://api.mojang.com/users/profiles/minecraft/"+name).toURL();

        var uuidReader = new InputStreamReader(uuidUrl.openStream());

        String uuid = JsonParser.parseReader(uuidReader).getAsJsonObject().get("id").getAsString();

        URL texturesUrl = new URI("https://sessionserver.mojang.com/session/minecraft/profile/"+uuid+"?unsigned=false").toURL();
        InputStreamReader texturesReader = new InputStreamReader(texturesUrl.openStream());
        JsonObject responseJson = JsonParser.parseReader(texturesReader).getAsJsonObject();

        String realName = responseJson.get("name").getAsString();

        JsonObject properties = responseJson.get("properties").getAsJsonArray().get(0).getAsJsonObject();

        String value = properties.get("value").getAsString();
        String signature = properties.get("signature").getAsString();

        playerProfile.setProperty(new ProfileProperty("textures", value, signature));
        player.setPlayerProfile(playerProfile);

        return realName;
    }
    private static void setSkin(Player player, Player newSkin) {
        var profile = player.getPlayerProfile();
        var newProfile = newSkin.getPlayerProfile();

        var properties = newProfile.getProperties();
        properties.stream()
                .filter(p -> p.getName().equals("textures"))
                .findFirst().ifPresent(newTextures ->
                        profile.setProperty(new ProfileProperty(
                                "textures", newTextures.getValue(), newTextures.getSignature())));

        player.setPlayerProfile(profile);
    }

}
