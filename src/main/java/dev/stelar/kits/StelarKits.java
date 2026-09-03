package dev.stelar.kits;

import dev.stelar.kits.managers.CommandManager;
import dev.stelar.kits.managers.ConfigManager;
import lombok.Getter;
import org.bukkit.plugin.java.JavaPlugin;

public final class StelarKits extends JavaPlugin {

    @Getter private ConfigManager configManager;
    private CommandManager commandManager;

    @Override
    public void onEnable() {
        configManager = new ConfigManager(this);
        commandManager = new CommandManager(this);
    }

    @Override
    public void onDisable() {
    }


}
