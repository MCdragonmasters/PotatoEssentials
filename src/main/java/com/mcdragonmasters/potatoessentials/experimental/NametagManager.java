package com.mcdragonmasters.potatoessentials.experimental;

import com.mcdragonmasters.potatoessentials.PotatoEssentials;
import com.mcdragonmasters.potatoessentials.utils.Utils;
import io.netty.buffer.Unpooled;
import lombok.SneakyThrows;
import net.kyori.adventure.text.Component;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.protocol.game.*;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.decoration.ArmorStand;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.phys.Vec3;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.craftbukkit.entity.CraftArmorStand;
import org.bukkit.craftbukkit.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.lang.reflect.Constructor;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class NametagManager implements Listener {
    private final Map<UUID, Map<UUID, Integer>> nametags = new HashMap<>();
    private static final Map<Player, Component> nametagComponents = new HashMap<>();

    public NametagManager() {
        Bukkit.getPluginManager().registerEvents(this, PotatoEssentials.INSTANCE);
    }

    public static void setNametag(Player player, Component nametag) {
        nametagComponents.put(player, nametag);
    }
    public void spawnNametag(Player player, Player viewer) {
        UUID playerUUID = player.getUniqueId();
        UUID viewerUUID = viewer.getUniqueId();

        // Avoid redundant nametag spawning
        if (nametags.getOrDefault(playerUUID, Collections.emptyMap()).containsKey(viewerUUID)) {
            return;
        }

        Location location = player.getLocation().add(0, 1, 0); // Position offset for nametag
        org.bukkit.entity.ArmorStand armorStand = player.getWorld().spawn(location, org.bukkit.entity.ArmorStand.class);
        armorStand.remove();
        armorStand.customName(nametagComponents.getOrDefault(player, player.name()));
        armorStand.setCustomNameVisible(true);
        armorStand.setInvisible(true);
        armorStand.setMarker(true);
        armorStand.setGravity(false);

        ServerPlayer nmsViewer = ((CraftPlayer) viewer).getHandle();
        ServerPlayer nmsPlayer = ((CraftPlayer) player).getHandle();

        int entityId = nmsPlayer.getId() + 1000; // Unique entity ID
        Vec3 pos = nmsPlayer.position().add(0, 2, 0); // Position offset

        // Create a fake ArmorStand
        ArmorStand stand = ((CraftArmorStand) armorStand).getHandle();

        // Packets
        ClientboundAddEntityPacket spawnPacket = new ClientboundAddEntityPacket(
                entityId, UUID.randomUUID(), pos.x, pos.y, pos.z, 0, 0,
                EntityType.ARMOR_STAND, 0, Vec3.ZERO, 0
        );

        ClientboundSetEntityDataPacket metadataPacket = new ClientboundSetEntityDataPacket(
                entityId,
                stand.getEntityData().getNonDefaultValues() != null
                        ? stand.getEntityData().getNonDefaultValues()
                        : Collections.emptyList() // Provide an empty list if it's null
        );

        // Attach the ArmorStand to the player as a passenger
        var passengerPacket = getPassengerPacket(player, entityId);
        // Send packets
        nmsViewer.connection.send(spawnPacket);
        nmsViewer.connection.send(metadataPacket);
        nmsViewer.connection.send(passengerPacket);

        // Store tracking info
        nametags.computeIfAbsent(playerUUID, k -> new HashMap<>()).put(viewerUUID, entityId);
    }
    public void removeNametag(Player player, Player viewer) {
        UUID playerUUID = player.getUniqueId();
        UUID viewerUUID = viewer.getUniqueId();

        Map<UUID, Integer> viewers = nametags.get(playerUUID);
        if (viewers == null) return; // No nametags for this player

        Integer entityId = viewers.remove(viewerUUID);
        if (entityId == null) return; // Not being tracked

        if (viewers.isEmpty()) {
            nametags.remove(playerUUID); // Clean up memory if no viewers remain
        }

        ServerPlayer nmsViewer = ((CraftPlayer) viewer).getHandle();
        nmsViewer.connection.send(new ClientboundRemoveEntitiesPacket(entityId));
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        setNametag(player, Utils.miniMessage(
                "<gradient:#fc0703:#fc7f03><bold>Owner </bold>"+player.getName()));
        for (Player viewer : Bukkit.getOnlinePlayers()) {
            if (viewer != player) {
                // Spawn nametag for other players who are already online
                spawnNametag(player, viewer);
            }
        }
        // Also spawn the nametag for the player themselves
        for (Player viewer : Bukkit.getOnlinePlayers()) {
            spawnNametag(viewer, player);
        }
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        for (Player viewer : Bukkit.getOnlinePlayers()) {
            removeNametag(player, viewer);
        }
    }

    @EventHandler
    public void onEntityDeath(EntityDeathEvent event) {
        if (event.getEntity() instanceof Player player) {
            for (Player viewer : Bukkit.getOnlinePlayers()) {
                // Remove nametag when the player dies
                removeNametag(player, viewer);
            }
        }
    }
    @SneakyThrows
    private ClientboundSetPassengersPacket getPassengerPacket(Player player, int nameId) {
        var buf = new FriendlyByteBuf(Unpooled.buffer());
        buf.writeVarInt(player.getEntityId());
        int[] intArray = {nameId};
        buf.writeVarIntArray(intArray);
        Class<ClientboundSetPassengersPacket> clazz = ClientboundSetPassengersPacket.class;

        Constructor<ClientboundSetPassengersPacket> declaredConstructor = clazz.getDeclaredConstructor(FriendlyByteBuf.class);
        declaredConstructor.setAccessible(true);
        return declaredConstructor.newInstance(buf);
    }
}
