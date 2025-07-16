package com.armeniumstudios.plugintemplate.command;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.armeniumstudios.plugintemplate.SpigotPluginTemplate;
import com.armeniumstudios.plugintemplate.locale.LocaleManager;

public class LanguageCommand implements CommandExecutor {

    SpigotPluginTemplate plugin;

    public LanguageCommand() {
        plugin = SpigotPluginTemplate.getInstance();
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(ChatColor.RED + LocaleManager.getMessage(sender, "general.players_only"));
        } else if (args.length == 1) {
            String language = args[0];
            if (!LocaleManager.languageExists(language)) {
                sender.sendMessage(ChatColor.RED +
                        LocaleManager.getMessage(sender, "language.not_found", args[0]) + "\n"
                        + LocaleManager.getMessage(sender, "language.supported_list",
                                String.join(", ", LocaleManager.getLanguageList())));
            } else {
                LocaleManager.setPlayerLanguage((Player) sender, language);
                sender.sendMessage(ChatColor.GREEN + LocaleManager.getMessage(sender, "language.set", language));
            }
        } else if (args.length == 0) {
            String currentLanguage = LocaleManager.getPlayerLanguage((Player) sender);
            sender.sendMessage(
                    LocaleManager.getMessage(sender, "language.current", currentLanguage) + "\n"
                            + LocaleManager.getMessage(sender, "language.supported_list",
                                    String.join(", ", LocaleManager.getLanguageList())));
        } else {
            return false;
        }
        return true;
    }
}
