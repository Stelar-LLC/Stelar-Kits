package dev.stelar.kits;

import org.bukkit.plugin.java.JavaPlugin;

public final class StelarKits extends JavaPlugin {

    @Override
    public void onEnable() {
        getServer().getConsoleSender().sendMessage(" ");
        getServer().getConsoleSender().sendMessage(" ");
        getServer().getConsoleSender().sendMessage("StelarKits enabled!");
    }

    @Override
    public void onDisable() {
        getServer().getConsoleSender().sendMessage("StelarKits disabled!");
    }
}
