package com.mcdragonmasters.potatodiscordlink.commands;

import com.mcdragonmasters.potatodiscordlink.PotatoDiscordLink;
import com.mcdragonmasters.potatoessentials.utils.Config;
import com.mcdragonmasters.potatoessentials.utils.PotatoCommand;
import com.mcdragonmasters.potatoessentials.utils.Replacer;
import com.mcdragonmasters.potatoessentials.utils.Utils;
import dev.jorel.commandapi.CommandAPICommand;
import dev.jorel.commandapi.executors.CommandArguments;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.event.ClickEvent;
import net.kyori.adventure.text.event.HoverEvent;
import org.bukkit.entity.Player;

public class LinkCommand extends PotatoCommand {
    public LinkCommand() {
        super("link");
        setPermission(NAMESPACE+".discord.link");
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
                    Utils.miniMessage("<red>You're already linked!")
            );
            return;
        }
        player.sendMessage(getLinkMessage(code));
    }
    public static Component getLinkMessage(String code) {
        return Config.replaceFormat(
                "<gray>Your link<aqua><b> code is <code></b></aqua>.<newline>"+
                        "Use /link in the <aqua>Discord server</aqua> to link your account<newline><newline>"+
                        "<gray>Discord Invite » <aqua><invite>",
                        new Replacer("code", code),
                        new Replacer("invite", PotatoDiscordLink.config().getString("discordInvite")))
                .clickEvent(ClickEvent.clickEvent(ClickEvent.Action.COPY_TO_CLIPBOARD, code))
                .hoverEvent(HoverEvent.hoverEvent(HoverEvent.Action.SHOW_TEXT, Component.text("Click to copy code")));
    }
}
