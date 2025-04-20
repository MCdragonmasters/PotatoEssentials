package com.mcdragonmasters.potatodiscordlink.listeners;

import com.mcdragonmasters.potatodiscordlink.PotatoDiscordLink;
import com.mcdragonmasters.potatoessentials.utils.Utils;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.bukkit.Bukkit;

import java.util.Objects;

public class DcSlashCommandListener extends ListenerAdapter {
    @Override
    public void onSlashCommandInteraction(SlashCommandInteractionEvent e) {
        switch (e.getName()) {
            case "link":
                String code = Objects.requireNonNull(e.getOption("code")).getAsString();
                e.reply(PotatoDiscordLink.getLinkManager().process(code, e.getUser())).queue();
                break;
            case "unlink":
                var linkManager = PotatoDiscordLink.getLinkManager();
                var id = e.getUser().getId();
                var uuid = linkManager.getUUID(id);
                if (uuid == null) {
                    e.reply("You're not currently linked!").queue();
                    return;
                }
                var player = Bukkit.getOfflinePlayer(uuid);
                e.reply("Unlinked from %s (%s)".formatted(player.getName(), uuid)).queue();
                linkManager.unlink(id);
                var onlinePlayer = player.getPlayer();
                if (PotatoDiscordLink.config().getBoolean("linkRequiredToJoin") && onlinePlayer!=null) {
                    onlinePlayer.kick(Utils.miniMessage(
                            "<gray>You must link your <aqua>Discord</aqua> account to join! Rejoin to get a linking code"
                    ));
                }
                break;
        }
    }
}
