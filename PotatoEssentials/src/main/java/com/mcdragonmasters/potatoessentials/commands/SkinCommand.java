package com.mcdragonmasters.potatoessentials.commands;

import com.destroystokyo.paper.profile.PlayerProfile;
import com.destroystokyo.paper.profile.ProfileProperty;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.mcdragonmasters.potatoessentials.utils.Config;
import com.mcdragonmasters.potatoessentials.utils.PotatoCommand;
import com.mcdragonmasters.potatoessentials.utils.Replacer;
import dev.jorel.commandapi.CommandAPICommand;
import dev.jorel.commandapi.arguments.EntitySelectorArgument.ManyPlayers;
import dev.jorel.commandapi.arguments.StringArgument;
import dev.jorel.commandapi.executors.CommandArguments;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import javax.annotation.Nullable;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.URL;
import java.util.Collection;
import java.util.List;

@SuppressWarnings("unchecked")
public class SkinCommand extends PotatoCommand {

    private final StringArgument skinArg = new StringArgument("skin");

    private final ManyPlayers playerArg = new ManyPlayers("targets", false);

    public SkinCommand() {
        super("skin",NAMESPACE+".skin");
    }

    @Override
    public void register() {
        new CommandAPICommand(name)
                .withPermission(permission)
                .withArguments(skinArg)
                .withOptionalArguments(playerArg)
                .executesPlayer(this::executeMain)
                .withSubcommand(
                        new CommandAPICommand("reset")
                                .executesPlayer(this::executeReset)
//                                .executesProxy((proxy, args) -> {
//                                    if (!(proxy.getCallee() instanceof Player p)) return;
//                                    executeReset(p, args);
//                                })
                )
//                .executesProxy((proxy, args) -> {
//                    if (!(proxy.getCallee() instanceof Player p)) return;
//                    executeMain(p, args);
//                })
                .register();
    }

    private void executeMain(Player sender, CommandArguments args) {
        String skin = args.getByArgumentOrDefault(skinArg, "Error");
        Collection<Player> players = args.getByArgumentOrDefault(playerArg, List.of(sender));
        var newSkinFromPlayer = Bukkit.getPlayer(skin);
        String skinName = "skinName not found";
        if (newSkinFromPlayer!=null) {
            for (Player player : players)
                setSkin(player, newSkinFromPlayer);
        } else {
            try {
                skinName = setSkin(players, skin);
            } catch (Exception e) {
                var msg = Config.replaceFormat(Config.getCmdMsg("skin", "skinNotFound"));
                sender.sendMessage(msg);
                for (Player player : players)
                    executeReset(player, null);
                return;
            }
        }
        var msg = Config.replaceFormat(Config.getCmdMsg("skin", "skinSet"),
                new Replacer("skin", skinName));
        for (Player player : players)
            player.sendMessage(msg);
    }

    private void executeReset(Player player, @Nullable CommandArguments args) {
        try {
            if (args!=null)
                setSkin(args.getByArgumentOrDefault(playerArg, List.of(player)), player.getName());
            else setSkin(player, player.getName());
        } catch (Exception e) {
            player.sendRichMessage("<red>Failed to reset skin!");
        }
    }
    private void setSkin(Player player, String name) throws Exception {
        setSkin(List.of(player), name);
    }
    private String setSkin(Collection<Player> players, String name) throws Exception {
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

        for (Player player : players) {
            PlayerProfile playerProfile = player.getPlayerProfile();
            playerProfile.setProperty(new ProfileProperty("textures", value, signature));
            player.setPlayerProfile(playerProfile);
        }
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
