package com.mcdragonmasters.potatoessentials.commands;

import com.mcdragonmasters.potatoessentials.PotatoEssentials;
import com.mcdragonmasters.potatoessentials.utils.PotatoCommand;
import dev.jorel.commandapi.annotations.Alias;
import dev.jorel.commandapi.annotations.Command;
import dev.jorel.commandapi.annotations.Default;
import dev.jorel.commandapi.annotations.Permission;
import dev.jorel.commandapi.annotations.arguments.AOfflinePlayerArgument;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ItemType;
import org.bukkit.inventory.meta.SkullMeta;

@SuppressWarnings("UnstableApiUsage")
@Command("skull")
@Alias("head")
@Permission(PotatoEssentials.NAMESPACE+".skull")
public class SkullCommand extends PotatoCommand {
    @Override
    public String getName() { return "skull"; }

    @Default
    public static void getSkull(Player player, @AOfflinePlayerArgument OfflinePlayer name) {
        ItemStack is = ItemType.PLAYER_HEAD.createItemStack();
        SkullMeta meta = (SkullMeta) is.getItemMeta();
        meta.setOwningPlayer(name);
    }
}
