package dev.stelar.kits.managers;

import co.aikar.commands.PaperCommandManager;
import dev.stelar.kits.command.kit.KitCommand;
import org.bukkit.plugin.java.JavaPlugin;

public class CommandManager {
    
    private final JavaPlugin plugin;

    public CommandManager(JavaPlugin plugin) {
        this.plugin = plugin;
        register();
    }
    
    public void register() {
        PaperCommandManager commandManager = new PaperCommandManager(plugin);
        commandManager.registerCommand(new KitCommand());
    }
}
