package com.mcdragonmasters.potatodiscordlink.commands;

import com.mcdragonmasters.potatodiscordlink.PotatoDiscordLink;
import com.mcdragonmasters.potatoessentials.utils.Config;
import com.mcdragonmasters.potatoessentials.utils.PotatoCommand;
import dev.jorel.commandapi.CommandAPICommand;
import dev.jorel.commandapi.executors.CommandArguments;
import org.bukkit.entity.Player;

public class UnlinkCommand extends PotatoCommand {
    public UnlinkCommand() {
        super("unlink");
        setPermission(NAMESPACE+".discord.unlink");
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
        var uuid = player.getUniqueId();
        if (linkManager.getDiscordID(uuid) == null) {
            player.sendMessage(Config.replaceFormat(
                    PotatoDiscordLink.config().getString("messages.minecraft.notLinked"))
            );
            return;
        }
        player.sendMessage(Config.replaceFormat(PotatoDiscordLink.config().getString("messages.minecraft.unlinked")));
        linkManager.unlink(uuid);
        if (PotatoDiscordLink.config().getBoolean("linkRequiredToJoin")) {
            player.kick(Config.replaceFormat(
                    PotatoDiscordLink.config().getString("messages.minecraft.kickUnlinked")
            ));
        }
    }
}
