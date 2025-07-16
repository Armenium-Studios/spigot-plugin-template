package com.armeniumstudios.plugintemplate;

import java.util.logging.Level;

import org.bukkit.plugin.java.JavaPlugin;

import com.armeniumstudios.plugintemplate.command.CommandManager;
import com.armeniumstudios.plugintemplate.listeners.ListenerManager;
import com.armeniumstudios.plugintemplate.locale.LocaleManager;

public class SpigotPluginTemplate extends JavaPlugin {

    private static SpigotPluginTemplate INSTANCE;

    public static SpigotPluginTemplate getInstance() {
        return INSTANCE;
    }

    @Override
    public void onEnable() {
        SpigotPluginTemplate.INSTANCE = this;
        this.saveDefaultConfig();
        PluginUtilities.init();
        ListenerManager.init();
        LocaleManager.init();
        CommandManager.init();

        this.getLogger().log(Level.INFO, "Plugin initialized!");
    }

    @Override
    public void onDisable() {
        //On disable
    }
}

