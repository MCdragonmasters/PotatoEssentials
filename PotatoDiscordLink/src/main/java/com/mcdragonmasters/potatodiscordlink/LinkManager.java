package com.mcdragonmasters.potatodiscordlink;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;
import java.util.logging.Level;

import com.mcdragonmasters.potatoessentials.utils.Config;
import com.mcdragonmasters.potatoessentials.utils.Replacer;
import lombok.Getter;
import lombok.SneakyThrows;
import net.dv8tion.jda.api.entities.User;
import org.apache.commons.collections4.bidimap.DualHashBidiMap;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

public class LinkManager {
    public LinkManager() {
        try {
            read();
            PotatoDiscordLink.LOGGER.debug("Loaded "+linkedAccounts.size()+" linked accounts");
        } catch (IOException e) {
            PotatoDiscordLink.LOGGER.log(Level.SEVERE,"Failed to load linked accounts", e);
        }
    }

    @Getter
    private final DualHashBidiMap<String, UUID> linkingCodes = new DualHashBidiMap<>();

    public String generateCode(UUID playerUuid) {
        if (linkedAccounts.containsValue(playerUuid)) {
            return "already linked";
        }
        String codeString;
        do {
            int code = ThreadLocalRandom.current().nextInt(10000);
            codeString = String.format("%04d", code);
        } while (linkingCodes.putIfAbsent(codeString, playerUuid) != null);
        return codeString;
    }

    private final DualHashBidiMap<String, UUID> linkedAccounts = new DualHashBidiMap<>();

    private void read() throws IOException {
        File file = getFile();
        if (!file.exists()) return;
        if (file.length()==0) return;

        String fileContent = FileUtils.readFileToString(file, "UTF-8");

        if (fileContent==null || StringUtils.isBlank(fileContent)) return;

        PotatoDiscordLink.LOGGER.debug("Reading linked.aof file");

        String[] split = fileContent.split("\n");
        for (String line : split) {
            String[] info = line.split(" ");
            String discordId = info[0];
            String uuid = info[1];
            linkedAccounts.put(
                    discordId,
                    UUID.fromString(uuid)
            );
        }
    }
    public String getDiscordID(UUID uuid) {
        synchronized (linkedAccounts) {
            return linkedAccounts.getKey(uuid);
        }
    }
    public UUID getUUID(String discordId) {
        synchronized (linkedAccounts) {
            return linkedAccounts.get(discordId);
        }
    }
    public void save() {
        File file = getFile();
        File tempFile = getTempFile();
        tempFile.deleteOnExit();
        file.delete();

        PotatoDiscordLink.LOGGER.debug("Saving linked.aof file");

        try {
            try (FileWriter fileWriter = new FileWriter(tempFile);
                 BufferedWriter writer = new BufferedWriter(fileWriter)) {
                for (Map.Entry<String, UUID> entry : linkedAccounts.entrySet()) {
                    String discordId = entry.getKey();
                    UUID uuid = entry.getValue();
                    writer.write(discordId+" "+uuid+"\n");
                }
            } catch (IOException e) {
                PotatoDiscordLink.LOGGER.log(Level.SEVERE, "Error saving linked.aof", e);
                return;
            }
            //noinspection ResultOfMethodCallIgnored
            file.delete();
            try {
                FileUtils.moveFile(tempFile, file);
            } catch (IOException e) {
                PotatoDiscordLink.LOGGER.log(Level.SEVERE, "Failed moving accounts.aof.tmp to accounts.aof: ", e);
            }
        } finally {
            //noinspection ResultOfMethodCallIgnored
            tempFile.delete();
        }
    }
    public String process(String linkCode, User user) {
        String discordId = user.getId();
        boolean contains;
        synchronized (linkedAccounts) {
            contains = linkedAccounts.containsKey(discordId);
        }


        if (contains) {
            UUID uuid;
            synchronized (linkedAccounts) {
                uuid = linkedAccounts.get(discordId);
            }
            OfflinePlayer offlinePlayer = PotatoDiscordLink.INSTANCE.getServer().getOfflinePlayer(uuid);
            return "You are already linked to %username% (%uuid%)"
                    .replace("%username%", offlinePlayer.getName()!=null?offlinePlayer.getName():"Unknown")
                    .replace("%uuid%", uuid.toString());
        }

        linkCode = linkCode.replaceAll("[^0-9]", "");

        if (linkingCodes.containsKey(linkCode)) {
            var linkedRole = PotatoDiscordLink.getLinkedRole();
            if (linkedRole!=null) {
                PotatoDiscordLink.getPrimaryGuild().addRoleToMember(user, linkedRole).queue();
            }
            link(discordId, linkingCodes.get(linkCode));
            linkingCodes.remove(linkCode);

            OfflinePlayer player = Bukkit.getOfflinePlayer(getUUID(discordId));
            Player onlinePlayer = player.getPlayer();
            if (player.isOnline() && onlinePlayer != null) {
                onlinePlayer.sendMessage(
                        Config.replaceFormat(
                                "<aqua>Your UUID has been linked to Discord user <username> (<id>)",
                                new Replacer("username", user.getName()),
                                new Replacer("id", user.getId()))
                );
            }

            return "Your Discord account has been linked to %s (%s)"
                    .formatted(
                            Optional.ofNullable(player.getName()).orElse("Unknown"),
                            getUUID(discordId).toString());
        }

        return "Invalid code";
    }
    public void link(String discordId, UUID uuid) {
        if (discordId.trim().isEmpty()) {
            throw new IllegalArgumentException("Empty Discord IDs are not allowed");
        }

        unlink(discordId);
        unlink(uuid);

        synchronized (linkedAccounts) {
            linkedAccounts.put(discordId, uuid);
        }
    }
    @SneakyThrows
    public void unlink(UUID uuid) {
        String discordId;
        synchronized (linkedAccounts) {
            discordId = linkedAccounts.getKey(uuid);
        }
        if (discordId == null) return;
        synchronized (linkedAccounts) {
            linkedAccounts.removeValue(uuid);
            //FileUtils.writeStringToFile(getFile(),"-"+discordId+" "+uuid+"\n", "UTF-8", true);
            var linkedRole = PotatoDiscordLink.getLinkedRole();
            var user = PotatoDiscordLink.getJda().getUserById(discordId);
            if (linkedRole!=null&&user!=null)
                PotatoDiscordLink.getPrimaryGuild().removeRoleFromMember(user, linkedRole).queue();
        }
    }
    @SneakyThrows
    public void unlink(String discordId) {
        UUID uuid;
        synchronized (linkedAccounts) {
            uuid = linkedAccounts.get(discordId);
        }
        if (uuid == null) return;
        synchronized (linkedAccounts) {
            linkedAccounts.remove(discordId);
            //FileUtils.writeStringToFile(getFile(), "-"+discordId+" "+uuid+"\n","UTF-8", true);
            var linkedRole = PotatoDiscordLink.getLinkedRole();
            var user = PotatoDiscordLink.getJda().getUserById(discordId);
            if (linkedRole!=null&&user!=null)
                PotatoDiscordLink.getPrimaryGuild().removeRoleFromMember(user, linkedRole).queue();
        }
    }
    private File getFile() {
        return new File(PotatoDiscordLink.INSTANCE.getDataFolder(), "linked.aof");
    }
    private File getTempFile() {
        return new File(PotatoDiscordLink.INSTANCE.getDataFolder(), "linked.aof.tmp");
    }
}
