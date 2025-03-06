package com.mcdragonmasters.potatoessentials.database;

import com.mcdragonmasters.potatoessentials.PotatoEssentials;
import lombok.Getter;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.logging.Level;

public class KitManager {
    private final File kitFile;
    private final FileConfiguration kitConfig;

    private final Map<String, Map<Integer, ItemStack>> kitCache = new HashMap<>();

    @Getter
    private final Set<String> kitNames = new HashSet<>();

    public KitManager(File dataFolder) {
        this.kitFile = new File(dataFolder, "kits.yml");
        this.kitConfig = YamlConfiguration.loadConfiguration(kitFile);
        loadKits();
    }

    private void loadKits() {
        var section1 = kitConfig.getConfigurationSection("kits");
        if (section1==null) return;

        for (String kitName : section1.getKeys(false)) {
            Map<Integer, ItemStack> kitContents = new HashMap<>();
            var section = kitConfig.getConfigurationSection("kits." + kitName);
            if (section != null) {
                for (String key : section.getKeys(false)) {
                    int slot = Integer.parseInt(key);
                    kitContents.put(slot, kitConfig.getItemStack("kits."+kitName+"."+key));
                }
            }
            kitCache.put(kitName, kitContents);
            kitNames.add(kitName);
        }
    }

    public void saveKit(String kitName, Player player) {
        if (kitCache.containsKey(kitName)) {
            PotatoEssentials.LOGGER.warning("Kit '" + kitName + "' already exists. Not saving.");
            return ;
        }

        Map<Integer, ItemStack> kitContents = new HashMap<>();
        for (int slot = 0; slot < player.getInventory().getSize(); slot++) {
            ItemStack item = player.getInventory().getItem(slot);
            if (item != null) {
                kitContents.put(slot, item);
            }
        }

        kitCache.put(kitName, kitContents);
        kitNames.add(kitName);

        saveKitToFile(kitName, kitContents);
    }

    private void saveKitToFile(String kitName, Map<Integer, ItemStack> kitContents) {
        kitConfig.set("kits." + kitName, null);
        for (Map.Entry<Integer, ItemStack> entry : kitContents.entrySet()) {
            kitConfig.set("kits." + kitName + "." + entry.getKey(), entry.getValue());
        }

        try {
            kitConfig.save(kitFile);
        } catch (IOException e) {
            PotatoEssentials.LOGGER.log(Level.SEVERE, "Error saving kit file", e);
        }
    }

    public void deleteKit(String kitName) {
        if (!kitCache.containsKey(kitName)) {
            PotatoEssentials.LOGGER.warning("Kit '" + kitName + "' does not exist.");
            return;
        }

        kitCache.remove(kitName);
        kitNames.remove(kitName);
        kitConfig.set("kits." + kitName, null);

        try {
            kitConfig.save(kitFile);
        } catch (IOException e) {
            PotatoEssentials.LOGGER.log(Level.SEVERE, "Error saving kits.yml while deleting kit '"+kitName+"'", e);
        }
    }

    public void giveKit(String kitName, Player playerTo) {
        if (!kitCache.containsKey(kitName)) {
            PotatoEssentials.LOGGER.warning("Kit '"+kitName+"' not found.");
            return;
        }

        Map<Integer, ItemStack> kitContents = kitCache.get(kitName);
        for (Map.Entry<Integer, ItemStack> entry : kitContents.entrySet()) {
            playerTo.getInventory().setItem(entry.getKey(), entry.getValue());
        }
    }
}
