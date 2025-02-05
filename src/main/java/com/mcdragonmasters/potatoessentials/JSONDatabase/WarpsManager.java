package com.mcdragonmasters.potatoessentials.JSONDatabase;

import com.google.gson.*;
import com.mcdragonmasters.potatoessentials.PotatoEssentials;
import org.bukkit.Bukkit;
import org.bukkit.Location;

import java.io.*;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

public class WarpsManager {

    private static final File warpsFile = new File(PotatoEssentials.getInstance().getDataFolder(), "warps.json");
    private static final Map<String, Location> warpsMap = new HashMap<>();
    private static final Logger logger = PotatoEssentials.getInstance().getLogger();
    static {
        loadWarps();
    }

    public static void loadWarps() {
        if (!warpsFile.exists()) {
            return;
        }

        try (Reader reader = new FileReader(warpsFile)) {
            JsonElement element = JsonParser.parseReader(reader);

            if (element == null || !element.isJsonObject()) {
                return;
            }

            JsonObject jsonObject = element.getAsJsonObject();

            for (Map.Entry<String, ?> entry : jsonObject.entrySet()) {
                String warpName = entry.getKey();
                JsonObject WarpData = (JsonObject) entry.getValue();

                if (WarpData != null && WarpData.has("world")) {
                    Location location = new Location(
                            Bukkit.getWorld(WarpData.get("world").getAsString()),
                            WarpData.get("x").getAsDouble(),
                            WarpData.get("y").getAsDouble(),
                            WarpData.get("z").getAsDouble(),
                            WarpData.get("yaw").getAsFloat(),
                            WarpData.get("pitch").getAsFloat()
                    );
                    warpsMap.put(warpName, location);
                }
            }
        } catch (IOException | com.google.gson.JsonSyntaxException e) {
            logger.severe(e.getMessage());
            logger.severe("Could not load Warps from the JSON file.");
        }
    }
    public static void saveWarps() {
        JsonObject jsonObject = new JsonObject();

        for (Map.Entry<String, Location> entry : warpsMap.entrySet()) {
            String warpName = entry.getKey();
            Location location = entry.getValue();
            JsonObject warpData = new JsonObject();
            warpData.addProperty("world", location.getWorld().getName());
            warpData.addProperty("x", location.getX());
            warpData.addProperty("y", location.getY());
            warpData.addProperty("z", location.getZ());
            warpData.addProperty("yaw", location.getYaw());
            warpData.addProperty("pitch", location.getPitch());

            jsonObject.add(warpName, warpData);
        }

        try (Writer writer = new FileWriter(warpsFile)) {
            Gson gsonPretty = new GsonBuilder().setPrettyPrinting().create();
            gsonPretty.toJson(jsonObject, writer);
        } catch (IOException e) {
            logger.severe(e.getMessage());
        }
    }


    public static Location getWarp(String warpName) {
        return warpsMap.get(warpName);
    }
    public static String[] getWarps() {
        return warpsMap.keySet().toArray(new String[0]);
    }

    public static void removeWarp(String warpName) {
        warpsMap.remove(warpName);
        saveWarps();
    }

    public static void saveWarp(String warpName, Location location) {
        warpsMap.put(warpName, location);
        saveWarps();
    }
}