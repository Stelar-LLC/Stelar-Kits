package dev.stelar.kits;

import dev.stelar.kits.managers.CommandManager;
import dev.stelar.kits.managers.ConfigManager;
import dev.stelar.kits.managers.KitManager;
import lombok.Getter;
import org.bukkit.plugin.java.JavaPlugin;

public final class StelarKits extends JavaPlugin {

    @Getter private static StelarKits instance;
    @Getter private ConfigManager configManager;
    @Getter private KitManager kitManager;

    @Override
    public void onEnable() {
        instance = this;
        new CommandManager(this);

        this.configManager = new ConfigManager(this);
        this.kitManager = new KitManager();
    }


    @Override
    public void onDisable() {

        getConfigManager().getKitsData().save();
        getConfigManager().getSettings().save();
    }


}
