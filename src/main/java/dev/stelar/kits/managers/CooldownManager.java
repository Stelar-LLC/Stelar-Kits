package dev.stelar.kits.managers;

import dev.stelar.kits.StelarKits;
import dev.stelar.kits.util.config.ConfigFile;
import org.bukkit.configuration.ConfigurationSection;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class CooldownManager {

    private final Map<UUID, Map<String, Long>> cooldowns;

    public CooldownManager() {
        this.cooldowns = new HashMap<>();
        loadCooldowns();
    }

    public void setCooldown(UUID uuid, String kitName, long cooldown) {
        Map<String, Long> playerCooldowns = this.cooldowns.computeIfAbsent(uuid, k -> new HashMap<>());
        playerCooldowns.put(kitName, System.currentTimeMillis() + cooldown);
        saveCooldowns();
    }

    public boolean isOnCooldown(UUID uuid, String kitName) {
        Map<String, Long> playerCooldowns = cooldowns.get(uuid);

        if (playerCooldowns == null) {
            return false;
        }

        Long expiresAt = playerCooldowns.get(kitName);

        return expiresAt != null && expiresAt > System.currentTimeMillis();
    }

    public long getCooldown(UUID uuid, String kitName) {
        Map<String, Long> playerCooldowns = cooldowns.get(uuid);

        if (playerCooldowns == null) {
            return 0;
        }

        Long expiresAt = playerCooldowns.get(kitName);

        if (expiresAt == null) {
            return 0;
        }

        return Math.max(0, expiresAt - System.currentTimeMillis());
    }

    public void removeCooldown(UUID uuid, String kitName) {
        Map<String, Long> playerCooldowns = this.cooldowns.get(uuid);

        if(playerCooldowns == null) return;

        playerCooldowns.remove(kitName);

        if(playerCooldowns.isEmpty()) {
            cooldowns.remove(uuid);
        }

        saveCooldowns();
    }

    private void saveCooldowns() {
        ConfigFile data = StelarKits.getInstance()
                .getConfigManager()
                .getData();

        data.set("player_data", null);

        ConfigurationSection playerData = data.createSection("player_data");

        for (Map.Entry<UUID, Map<String, Long>> entry : cooldowns.entrySet()) {
            ConfigurationSection playerSection =
                    playerData.createSection(entry.getKey().toString());

            for (Map.Entry<String, Long> cooldownEntry : entry.getValue().entrySet()) {
                playerSection.set(
                        cooldownEntry.getKey(),
                        cooldownEntry.getValue()
                );
            }
        }

        data.save();
    }

    private void loadCooldowns(){
        ConfigFile data = StelarKits.getInstance().getConfigManager().getData();

        if(data.getConfigurationSection("player_data") == null) return;

        cooldowns.clear();

        for(String uuidString : data.getConfigurationSection("player_data").getKeys(false)) {
            UUID uuid = UUID.fromString(uuidString);
            ConfigurationSection cooldownsSection = data.getConfigurationSection("player_data").getConfigurationSection(uuidString);

            if(cooldownsSection == null) continue;

            Map<String, Long> cooldownsMap = new HashMap<>();
            for(String kitName : cooldownsSection.getKeys(false)) {
                cooldownsMap.put(kitName, cooldownsSection.getLong(kitName));
            }

            cooldowns.put(uuid, cooldownsMap);
        }
    }


}
