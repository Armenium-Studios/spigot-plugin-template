package com.armeniumstudios.plugintemplate.command;

import org.bukkit.command.PluginCommand;

import com.armeniumstudios.plugintemplate.SpigotPluginTemplate;

public class CommandManager {
    public static void init() {
        PluginCommand languageCommand = SpigotPluginTemplate.getInstance().getCommand("language");
        languageCommand.setExecutor(new LanguageCommand());
        languageCommand.setTabCompleter(new LanguageCompleter());
    }
}
