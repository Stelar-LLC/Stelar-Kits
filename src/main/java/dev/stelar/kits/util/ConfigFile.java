package dev.stelar.kits.util;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.Set;

public class ConfigFile {

    private final String path;
    private final JavaPlugin plugin;
    private File file;
    private FileConfiguration fileConfiguration;

    public ConfigFile(JavaPlugin plugin, String path) {
        this.path = path;
        this.plugin = plugin;
        init();
    }

    private void init() {
        this.file = new File(plugin.getDataFolder(), path);
        File parent = file.getParentFile();

        if(parent != null && !parent.exists()) {
            throw new IllegalStateException("Error trying to create folder for " + path);
        }

        if(!file.exists()) {
            try (InputStream resource = plugin.getResource(path)) {
                if (resource != null) {
                    Files.copy(resource, file.toPath(), StandardCopyOption.REPLACE_EXISTING);
                } else if (!file.createNewFile()) {
                    throw new IllegalStateException("Error trying to create file for " + path);
                }

            }catch (IOException e){
                throw new IllegalStateException("Error trying to initialize file for " + path, e);
            }
        }
        
        this.fileConfiguration = YamlConfiguration.loadConfiguration(file);
        applyDefaults();
    }

    public void reload() {
        init();
    }

    public void save() {
        if (this.fileConfiguration == null || this.file == null) {
            throw new IllegalStateException("Config not initialize: " + path);
        }

        try {
            this.fileConfiguration.save(this.file);
        } catch (IOException ex) {
            throw new IllegalStateException("Error trying to save config: " + path, ex);
        }
    }

    public FileConfiguration getConfig() {
        if (this.fileConfiguration == null) {
            init();
        }
        return this.fileConfiguration;
    }

    public Object get(String path) {
        return getConfig().get(path);
    }

    public Object get(String path, Object def) {
        return getConfig().get(path, def);
    }

    public void set(String path, Object value) {
        getConfig().set(path, value);
    }

    public boolean contains(String path) {
        return getConfig().contains(path);
    }

    public String getString(String path) {
        return getConfig().getString(path);
    }

    public String getString(String path, String def) {
        return getConfig().getString(path, def);
    }

    public int getInt(String path) {
        return getConfig().getInt(path);
    }


    public long getLong(String path) {
        return getConfig().getLong(path);
    }


    public double getDouble(String path) {
        return getConfig().getDouble(path);
    }


    public boolean getBoolean(String path) {
        return getConfig().getBoolean(path);
    }


    public List<?> getList(String path) {
        return getConfig().getList(path);
    }

    public List<String> getStringList(String path) {
        return getConfig().getStringList(path);
    }

    public List<Integer> getIntegerList(String path) {
        return getConfig().getIntegerList(path);
    }

    public boolean isConfigurationSection(String path) {
        return getConfig().isConfigurationSection(path);
    }

    public ConfigurationSection getConfigurationSection(String path) {
        return getConfig().getConfigurationSection(path);
    }

    public ConfigurationSection createSection(String path) {
        return getConfig().createSection(path);
    }

    public Set<String> getKeys(boolean deep) {
        return getConfig().getKeys(deep);
    }

    public void addDefault(String path, Object value) {
        getConfig().addDefault(path, value);
    }



    private void applyDefaults() {
        InputStream resource = plugin.getResource(path);
        if (resource == null) return;

        try (InputStream defaultStream = resource;
             InputStreamReader reader = new InputStreamReader(defaultStream, StandardCharsets.UTF_8)) {

            YamlConfiguration defaultConfig = YamlConfiguration.loadConfiguration(reader);
            this.fileConfiguration.setDefaults(defaultConfig);
            this.fileConfiguration.options().copyDefaults(true);

        } catch (IOException e){
            throw new IllegalStateException("Error trying to apply defaults for " + path, e);
        }
    }

    private String normalizePath(String path){
        String normalized = path.replace('\\', '/');
        while (normalized.startsWith("/")) {
            normalized = normalized.substring(1);
        }

        return normalized;
    }


}
