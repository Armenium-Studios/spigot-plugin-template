package com.armeniumstudios.plugintemplate.listeners;

import org.bukkit.event.Listener;

import com.armeniumstudios.plugintemplate.SpigotPluginTemplate;

public class ListenerManager {
    private static Listener[] listeners = new Listener[] {
            // Add listeners here
    };

    public static void init() {
        SpigotPluginTemplate plugin = SpigotPluginTemplate.getInstance();

        for (Listener listener : listeners) {
            plugin.getServer().getPluginManager().registerEvents(listener, plugin);
        }
    }
}
