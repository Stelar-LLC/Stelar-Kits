package dev.stelar.kits;

import dev.stelar.kits.managers.CommandManager;
import dev.stelar.kits.managers.ConfigManager;
import dev.stelar.kits.managers.KitManager;
import dev.stelar.kits.util.config.Configuration;
import lombok.Getter;
import org.bukkit.plugin.java.JavaPlugin;

public final class StelarKits extends JavaPlugin {

    @Getter private static StelarKits instance;
    @Getter private ConfigManager configManager;
    @Getter private KitManager kitManager;

    @Override
    public void onEnable() {
        instance = this;

        this.configManager = new ConfigManager(this);
        this.kitManager = new KitManager();
        new CommandManager(this);
    }


    @Override
    public void onDisable() {

        getConfigManager().getKitsData().save();
        getConfigManager().getSettings().save();
    }


}
