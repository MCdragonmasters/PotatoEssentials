package com.mcdragonmasters.potatoessentials.objects;

import com.mcdragonmasters.potatoessentials.PotatoEssentials;
import com.mcdragonmasters.potatoessentials.api.event.ChatCooldownEvent;
import com.mcdragonmasters.potatoessentials.api.event.CustomChannelChatEvent;
import com.mcdragonmasters.potatoessentials.utils.Config;
import com.mcdragonmasters.potatoessentials.utils.Utils;
import dev.jorel.commandapi.CommandAPIBukkit;
import dev.jorel.commandapi.CommandAPICommand;
import dev.jorel.commandapi.arguments.GreedyStringArgument;
import lombok.Getter;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import javax.annotation.Nullable;
import java.text.DecimalFormat;
import java.util.*;


@Getter
public class CustomChat {
    @Getter
    private static final Set<CustomChat> customChats = new HashSet<>();
    @Getter
    private static final Map<String, CustomChat> chatMap = new HashMap<>();
    @Getter
    private static final Map<Player, CustomChat> playerChat = new HashMap<>();
    @Getter
    private static final Map<Player, Set<CustomChat>> ignoredChannels = new HashMap<>();
    @Getter
    private static final Set<String> chatCommandList = new HashSet<>();
    private final String key;
    private final String name;
    private final @Nullable String permission;
    private final @Nullable String command;
    private final @Nullable Integer cooldown;
    @Getter
    private final HashMap<CommandSender, Long> lastChatTimes = new HashMap<>();

    public CustomChat(String key, String name, @Nullable String permission, @Nullable String command, @Nullable Integer cooldown) {
        this.key = key;
        this.name = name;
        this.permission = permission;
        this.command = command;
        this.cooldown = cooldown;
        chatMap.put(key, this);
        customChats.add(this);
        if (command==null) return;
        if (PotatoEssentials.INSTANCE.getCommandRegistrar().getRegisteredCommandNames().contains(command)) {
            PotatoEssentials.LOGGER.warning(
                    ("Command '%s' for custom chat '%s' was already registered, not registering").formatted(command, key));
            return;
        }
        if (chatCommandList.contains(command)) {
            PotatoEssentials.LOGGER.warning(
                    ("Command '%s' for custom chat '%s' already has a custom chat assigned to it, not registering").formatted(command, key));
            return;
        }

        CommandAPIBukkit.unregister(command, false, true);
        CommandAPIBukkit.unregister(command, false, false);

        chatCommandList.add(command);
        var messageArg = new GreedyStringArgument("message");
        var cmd = new CommandAPICommand(command)
                .withArguments(messageArg)
                .executes((sender, args) -> {
                    if (sender instanceof Player p && getPlayerIgnoredChannels(p).contains(this)) {
                        sender.sendMessage(Config.replaceFormat(
                                Config.getString("chats.chatChannelIgnored"),
                                new Replacer("channel", this.getName())
                        ));
                        return;
                    }
                    String message = args.getByArgument(messageArg);
                    this.sendMessage(sender,message);
                });
        if (permission!=null) cmd.withPermission(permission);
        cmd.register("potatoessentialschannel");
    }
    public void sendMessage(CommandSender sender, String message) {
        if (this.getCooldown()!=null) {
            boolean canBypassCooldown = sender.hasPermission(PotatoEssentials.NAMESPACE+".chat.bypass-cooldown");
            if (this.getLastChatTimes().containsKey(sender) && !canBypassCooldown) {
                long lastTime = this.getLastChatTimes().get(sender);
                long currentTime = System.currentTimeMillis();
                long cooldownMillis = this.getCooldown() * 1000L;
                long timeElapsed = currentTime - lastTime;
                long cooldownRemainderMillis = (cooldownMillis - timeElapsed);

                if (timeElapsed < cooldownMillis) {
                    DecimalFormat df = new DecimalFormat("0.0");
                    double cooldownRemainder = cooldownRemainderMillis / 1000D;
                    var msg = Config.replaceFormat(Config.getString("chat.cooldownMessage"),
                            new Replacer("cooldown", df.format(cooldownRemainder)));
                    var event = new ChatCooldownEvent((Player) sender, msg, cooldownRemainder, this.getKey());
                    Bukkit.getPluginManager().callEvent(event);
                    sender.sendMessage(msg);
                    return;
                }
            }
            this.getLastChatTimes().put(sender, System.currentTimeMillis());
        }
        boolean miniMsg = sender.hasPermission(PotatoEssentials.NAMESPACE+".chat.minimessage");
        if (!miniMsg) message = Utils.escapeTags(message);
        message = Utils.formatChatMessage(sender, message);
        Component msg = Config.replaceFormat(Config.customChatFormat(),
                new Replacer("chat-name", this.getName()),
                new Replacer("prefix", Utils.getPrefix(sender)),
                new Replacer("name", sender.getName()),
                new Replacer("message", message, false));
        var event = new CustomChannelChatEvent(sender, msg);
        Bukkit.getPluginManager().callEvent(event);
        if (event.isCancelled()) return;
        var recipients = Bukkit.getOnlinePlayers().stream()
                .filter(p -> {
                    boolean ignoredByPlayer = getPlayerIgnoredChannels(p).contains(this);
                    return checkPerm(p) && !ignoredByPlayer;
                }).toList();
        recipients.forEach(p -> p.sendMessage(msg));
        Bukkit.getConsoleSender().sendMessage(msg);
    }
    public boolean checkPerm(CommandSender sender) {
        return this.getPermission() == null || sender.hasPermission(this.getPermission());
    }
    public static Set<CustomChat> getPlayerIgnoredChannels(Player p) {
        ignoredChannels.computeIfAbsent(p, p1 -> new HashSet<>());
        return ignoredChannels.get(p);
    }
    public static void clearAll() {
        customChats.clear();
        chatMap.clear();
        playerChat.clear();
        chatCommandList.clear();
        ignoredChannels.clear();
    }
}
