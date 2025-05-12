package com.mcdragonmasters.potatodiscordlink.commands;

import com.mcdragonmasters.potatodiscordlink.PotatoDiscordLink;
import com.mcdragonmasters.potatoessentials.utils.Config;
import com.mcdragonmasters.potatoessentials.utils.PotatoCommand;
import com.mcdragonmasters.potatoessentials.utils.Replacer;
import com.mcdragonmasters.potatoessentials.utils.Utils;
import com.mcdragonmasters.potatoessentials.libs.commandapi.CommandAPICommand;
import com.mcdragonmasters.potatoessentials.libs.commandapi.executors.CommandArguments;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.event.ClickEvent;
import net.kyori.adventure.text.event.HoverEvent;
import org.bukkit.entity.Player;

import java.util.Objects;

public class LinkCommand extends PotatoCommand {
    public LinkCommand() {
        super("link",NAMESPACE+".discord.link");
    }
    @Override
    public void register() {
        new CommandAPICommand(name)
                .withPermission(permission)
                .executesPlayer(this::execute)
                .register();
    }
    private void execute(Player player, CommandArguments args) {
        var linkManager = PotatoDiscordLink.getLinkManager();
        String code = linkManager.generateCode(player.getUniqueId());
        if ("already linked".equals(code)) {
            player.sendMessage(
                    Utils.miniMessage(PotatoDiscordLink.config().getString("messages.minecraft.alreadyLinked"))
            );
            return;
        }
        player.sendMessage(getLinkMessage(code));
    }
    public static Component getLinkMessage(String code) {
        return Config.replaceFormat(
                        String.join("<newline>", PotatoDiscordLink.config().getStringList("messages.minecraft.linkMessage")),
                        new Replacer("code", code),
                        new Replacer("invite", PotatoDiscordLink.config().getString("discordInvite")))
                .clickEvent(ClickEvent.clickEvent(ClickEvent.Action.COPY_TO_CLIPBOARD, code))
                .hoverEvent(HoverEvent.hoverEvent(HoverEvent.Action.SHOW_TEXT,
                        Component.text(Objects.requireNonNull(PotatoDiscordLink.config().getString("messages.minecraft.codeHoverMsg")))));
    }
}
