package dev.stelar.kits.managers;

import dev.stelar.kits.util.ConfigFile;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashMap;
import java.util.Map;

public class ConfigManager {

    private Map<String, ConfigFile> files = new HashMap<>();
    private final JavaPlugin plugin;

    public ConfigManager(JavaPlugin plugin) {
        this.plugin = plugin;
        load();
    }

    public void load() {
        files.clear();
        register("settings", this.plugin, "settings.yml");
        register("kits", this.plugin, "data/kits.yml");
    }

    private void register(String key, JavaPlugin plugin, String path){
        files.put(key, new ConfigFile(plugin, path));
    }

    public ConfigFile getSettings() {
        return files.get("settings");
    }


    public ConfigFile getKitsData() {
        return files.get("kits");
    }
}
