package com.mcdragonmasters.potatoessentials.listeners;

import com.mcdragonmasters.potatoessentials.commands.HungerCommand;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.FoodLevelChangeEvent;

public class HungerChangeListener implements Listener {
    @EventHandler
    public void onHungerChange(FoodLevelChangeEvent e) {
        if (!HungerCommand.hungerEnabled) e.setCancelled(true);
    }
}
