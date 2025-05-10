package com.mcdragonmasters.potatoessentials.commands;

import com.mcdragonmasters.potatoessentials.utils.PotatoCommand;
import dev.jorel.commandapi.CommandAPICommand;
import dev.jorel.commandapi.arguments.OfflinePlayerArgument;
import dev.jorel.commandapi.executors.CommandArguments;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;

public class SkullCommand extends PotatoCommand {

    private final OfflinePlayerArgument arg = new OfflinePlayerArgument("owner");
    public SkullCommand() {
        super("skull",NAMESPACE+".skull","head");
    }
    @Override
    public void register() {
        new CommandAPICommand(name)
                .withPermission(permission)
                .withAliases(aliases)
                .withArguments(arg)
                .executesPlayer(this::getSkull)
                .register();
    }
    private void getSkull(Player player, CommandArguments args) {
        OfflinePlayer offlinePlayer = args.getByArgument(arg);
        if (offlinePlayer==null) return;
        ItemStack is = new ItemStack(Material.PLAYER_HEAD);
        SkullMeta meta = (SkullMeta) is.getItemMeta();
        var profile = offlinePlayer.getPlayerProfile();
        profile.complete(true);
        meta.setPlayerProfile(profile);
        is.setItemMeta(meta);
        player.give(is);
    }
}
