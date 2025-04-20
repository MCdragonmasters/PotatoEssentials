package com.mcdragonmasters.potatodiscordlink.commands;

import com.mcdragonmasters.potatodiscordlink.PotatoDiscordLink;
import com.mcdragonmasters.potatoessentials.utils.Config;
import com.mcdragonmasters.potatoessentials.utils.PotatoCommand;
import com.mcdragonmasters.potatoessentials.utils.Replacer;
import com.mcdragonmasters.potatoessentials.utils.Utils;
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
            player.sendMessage(
                    Utils.miniMessage("<red>Your Minecraft account isn't currently linked with a Discord account")
            );
            return;
        }
        player.sendMessage(Utils.miniMessage("<aqua>Unlinked your Minecraft account from your Discord account"));
        linkManager.unlink(uuid);
        if (PotatoDiscordLink.config().getBoolean("linkRequiredToJoin")) {
            player.kick(Utils.miniMessage(
                    "<gray>You must link your <aqua>Discord</aqua> account to join! Rejoin to get a linking code"
            ));
        }
    }
}
