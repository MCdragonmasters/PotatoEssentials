package com.mcdragonmasters.potatoessentials.commands;

import com.destroystokyo.paper.profile.PlayerProfile;
import com.destroystokyo.paper.profile.ProfileProperty;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.mcdragonmasters.potatoessentials.utils.Config;
import com.mcdragonmasters.potatoessentials.utils.PotatoCommand;
import com.mcdragonmasters.potatoessentials.utils.Replacer;
import dev.jorel.commandapi.CommandAPICommand;
import dev.jorel.commandapi.arguments.StringArgument;
import dev.jorel.commandapi.executors.CommandArguments;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import javax.annotation.Nullable;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.URL;

public class SkinCommand extends PotatoCommand {

    private final StringArgument skinArg = new StringArgument("skin");

    public SkinCommand() {
        super("skin");
        setPermission(NAMESPACE+".skin");
    }

    @Override
    public void register() {
        new CommandAPICommand(name)
                .withPermission(permission)
                .withArguments(skinArg)
                .executesPlayer(this::executeMain)
                .withSubcommand(
                        new CommandAPICommand("reset")
                                .executesPlayer(this::executeReset)
                )
                .register();
    }

    private void executeMain(Player player, CommandArguments args) {
        var skin = args.getByArgumentOrDefault(skinArg, "Error");
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
                executeReset(player, null);
                return;
            }
        }
        var msg = Config.replaceFormat(Config.getCmdMsg("skin", "skinSet"),
                new Replacer("skin", skinName));
        player.sendMessage(msg);
    }

    private void executeReset(Player player, @Nullable CommandArguments ca) {
        try {
            setSkin(player, player.getName());
        } catch (Exception e) {
            player.sendRichMessage("<red>Failed to reset skin!");
        }
    }

    private String setSkin(Player player, String name) throws Exception {
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
    public static void setSkin(Player player, Player newSkin) {
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
