package com.armeniumstudios.plugintemplate.locale;

import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.logging.Level;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import com.armeniumstudios.plugintemplate.PluginUtilities;
import com.armeniumstudios.plugintemplate.SpigotPluginTemplate;

public class LocaleManager {
    private static SpigotPluginTemplate plugin;

    private static Set<String> languageFiles = new HashSet<>();

    private static HashMap<String, YamlConfiguration> languages = new HashMap<>();

    private static YamlConfiguration playerLanguages;
    private static File playerLanguagesFile;

    public static void init() {
        plugin = SpigotPluginTemplate.getInstance();

        // Generate the configuration files
        generateFiles();
    }

    private static void generateFiles() {
        // Generate localization files
        getLanguageFiles(); // Initialize language manager
        for (String languageFile : languageFiles) {
            languages.put(languageFile, getConfig(languageFile));
        }

        getPlayerLanguagesFile();
    }

    private static void getLanguageFiles() {
        // Get the user-created "messages" folder
        File messagesFolder = new File(plugin.getDataFolder(), "messages");

        // Create if it doesn't exist
        if (!messagesFolder.exists()) {
            messagesFolder.mkdir();
        }

        // Get every language file added by the user and add it to the list of available
        // languages
        for (File file : messagesFolder.listFiles()) {
            String fileName = FilenameUtils.removeExtension(file.getName());
            if (file.getName().endsWith("yml") && (fileName.length() == 2 || fileName.length() == 5)) {
                languageFiles.add(fileName);
            } else {
                plugin.getLogger().log(Level.WARNING,
                        String.format("Invalid file \"%s\" in messages folder, ignoring", file.getName()));
            }
        }

        if (languageFiles.size() == 0) {
            languageFiles.add(PluginUtilities.getDefaultLanguage());
        }
    }

    private static void getPlayerLanguagesFile() {
        try {
            playerLanguagesFile = new File(plugin.getDataFolder(), "player-languages.yml");
            if (!playerLanguagesFile.exists()) {
                playerLanguagesFile.createNewFile();
            }

            playerLanguages = YamlConfiguration.loadConfiguration(playerLanguagesFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static YamlConfiguration getConfig(String fileName) {
        //Get default properties for this configuration file
        String defaultFileName = "messages/" + PluginUtilities.getDefaultLanguage() + ".yml";
        Reader defaultsReader = new InputStreamReader(plugin.getResource(defaultFileName), StandardCharsets.UTF_8);
        YamlConfiguration defaultConfig = YamlConfiguration.loadConfiguration(defaultsReader);

        //Get the file in the plugin's data folder
        File file = new File(plugin.getDataFolder() + "/" + defaultFileName);
        YamlConfiguration config = YamlConfiguration.loadConfiguration(file);

        if (!file.exists()) {
            //If file doesn't exist, set defaults and create it
            try {
                config.setDefaults(defaultConfig);
                config.save(file);
                FileUtils.copyInputStreamToFile(plugin.getResource(defaultFileName), file);
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            //Check if any properties are missing by comparing it with the default file
            boolean changed = false;
            for (Map.Entry<String, Object> entry : defaultConfig.getValues(true).entrySet()) {
                if (!config.contains(entry.getKey())) {
                    config.set(entry.getKey(), entry.getValue());
                    changed = true;
                }
            }
            if (changed) {
                try {
                    config.save(file);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        plugin.getLogger().log(Level.INFO, String.format("Language \"%s\" successfully loaded!", fileName));

        return config;
    }

    public static boolean languageExists(String language) {
        return languageFiles.contains(language);
    }

    public static String getPlayerLanguage(UUID uuid) {
        String lang = playerLanguages.getString(uuid.toString());
        if (lang != null) {
            return lang;
        } else {
            return PluginUtilities.getDefaultLanguage();
        }
    }

    public static String getPlayerLanguage(Player player) {
        return getPlayerLanguage(player.getUniqueId());
    }

    public static void setPlayerLanguage(Player player, String language) {
        playerLanguages.set(player.getUniqueId().toString(), language);
        try {
            playerLanguages.save(playerLanguagesFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static List<String> getLanguageList() {
        return new ArrayList<String>(languageFiles);
    }

    public static String getMessage(String language, String key, Object... args) {
        String message = languages.get(language).getString(key);
        if (message == null) {
            message = key;
        }
        if (args != null && args.length > 0) {
            return String.format(message, args);
        } else {
            return message;
        }
    }

    public static String getMessage(String key, Object... args) {
        return getMessage(PluginUtilities.getDefaultLanguage(), key, args);
    }

    public static String getMessage(CommandSender sender, String key, Object... args) {
        if (sender != null && sender instanceof Player) {
            return getMessage(getPlayerLanguage(((Player) sender).getUniqueId()), key, args);
        }
        return getMessage(key, args);
    }

    public static String getMessage(CommandSender sender, String key, List<Object> args) {
        return getMessage(sender, key, args.toArray());
    }

    public static String getMessage(UUID uuid, String key, Object... args) {
        if (uuid != null) {
            return getMessage(getPlayerLanguage(uuid), key, args);
        }
        return getMessage(key, args);
    }
}
