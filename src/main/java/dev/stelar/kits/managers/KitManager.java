package dev.stelar.kits.managers;

import dev.stelar.kits.StelarKits;
import dev.stelar.kits.kit.Kit;
import dev.stelar.kits.kit.model.KitState;
import dev.stelar.kits.util.ItemUtil;
import dev.stelar.kits.util.TimeUtil;
import dev.stelar.kits.util.config.ConfigFile;
import dev.stelar.kits.util.config.Configuration;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

public class KitManager {

    private final Map<String, Kit> kits;

    public KitManager() {
        this.kits = new HashMap<>();
        loadKits();
    }

    public void saveKit(Kit kit) {
        ConfigFile data = StelarKits.getInstance().getConfigManager().getKitsData();
        String path = "kits." + kit.getName();

        data.set(path + ".cooldown", kit.getCooldown());
        data.set(path + ".permission", kit.getPermission());
        data.set(path + ".enabled", kit.isEnabled());

        data.set(path + ".item.material", kit.getKitDisplay().getIcon());
        data.set(path + ".item.display-name", kit.getKitDisplay().getDisplayName());
        data.set(path + ".item.glow", kit.getKitDisplay().isGlow());
        data.set(path + ".item.slot", kit.getKitDisplay().getSlot());

        data.set(path + ".item.lore.available", kit.getKitDisplay().getLore().get(KitState.AVAILABLE));
        data.set(path + ".item.lore.on-cooldown", kit.getKitDisplay().getLore().get(KitState.ON_COOLDOWN));
        data.set(path + ".item.lore.no-permission", kit.getKitDisplay().getLore().get(KitState.NO_PERMISSION));

        data.set(path + ".content", ItemUtil.serialize(kit.getContent()));

        data.save();
        data.reload();
    }

    public void loadKits() {
        ConfigFile config = StelarKits.getInstance().getConfigManager().getKitsData();
        if(config.getConfigurationSection("kits") == null) return;

        kits.clear();

        for(String key : config.getConfigurationSection("kits").getKeys(false)) {

            long cooldown = config.getLong("kits." + key + ".cooldown");
            String permission = config.getString("kits." + key + ".permission");
            boolean enabled = config.getBoolean("kits." + key + ".enabled");
            String icon = config.getString("kits." + key + ".item.material");
            String displayName = config.getString("kits." + key + ".item.display-name");
            boolean glow = config.getBoolean("kits." + key + ".item.glow");
            int slot = config.getInt("kits." + key + ".item.slot");
            List<String> availableLore = config.getStringList("kits." + key + ".item.available");
            List<String> onCooldownLore = config.getStringList("kits." + key + ".item.on-cooldown");
            List<String> noPermissionLore = config.getStringList("kits." + key + ".item.no-permission");

            String content = config.getString("kits." + key + ".content");

            Kit kit = new Kit(key);

            kit.setCooldown(cooldown);
            kit.setPermission(permission);
            kit.setEnabled(enabled);

            kit.getKitDisplay().setIcon(Material.matchMaterial(icon));
            kit.getKitDisplay().setDisplayName(displayName);
            kit.getKitDisplay().setGlow(glow);
            kit.getKitDisplay().setSlot(slot);
            kit.getKitDisplay().getLore().put(KitState.AVAILABLE, availableLore);
            kit.getKitDisplay().getLore().put(KitState.ON_COOLDOWN, onCooldownLore);
            kit.getKitDisplay().getLore().put(KitState.NO_PERMISSION, noPermissionLore);

            kit.setContent(ItemUtil.deserialize(content));

            saveKit(kit);
            kits.put(key, kit);

        }

    }

    /*
    TODO
    - APLICAR COOLDOWN
    - VERIFICAR PERMISO
    - VERIFICAR SI EL KIT EXISTE
    - MEJORA DEL METODO EN GENERAL

    METODO NO TERMINADO
     */

    public void giveKit(Player player, String name) {
        Inventory inventory = player.getInventory();
        ItemStack[] stack = getKitByName(name).getContent();

        if(Configuration.CLEAR_INVENTORY_ON_KIT_APPLY){
            inventory.clear();
        }

        inventory.addItem(stack);
    }

    public void createKit(String name) {
        Kit kit = new Kit(name);

        kit.setEnabled(true);
        kit.setCooldown(0);
        kit.setPermission("stelar.kit." + name);

        kit.getKitDisplay().setIcon(Material.matchMaterial(Configuration.DEFAULT_KIT_ITEM));
        kit.getKitDisplay().setDisplayName(name);
        kit.getKitDisplay().setGlow(true);
        kit.getKitDisplay().setSlot(getRandomSlot());

        kit.getKitDisplay().getLore().put(KitState.AVAILABLE, Configuration.DEFAULT_KIT_LORE_AVAILABLE);
        kit.getKitDisplay().getLore().put(KitState.ON_COOLDOWN, Configuration.DEFAULT_KIT_LORE_ON_COOLDOWN);
        kit.getKitDisplay().getLore().put(KitState.NO_PERMISSION, Configuration.DEFAULT_KIT_LORE_NO_PERMISSION);

        kit.setContent(new ItemStack[]{});

        saveKit(kit);
        kits.put(name, kit);
    }

    public static int getRandomSlot(){
        return ThreadLocalRandom.current().nextInt(Configuration.KITS_MENU_SIZE);
    }

    public void deleteKit(String name) {
        ConfigFile data = StelarKits.getInstance().getConfigManager().getKitsData();

        data.set("kits." + name, null);
        data.save();
        data.reload();

        kits.remove(name);
    }

    public Kit getKitByName(String name) {
        return kits.get(name);
    }

    public List<Kit> getKits() {
        return new ArrayList<>(kits.values());
    }

    public List<String> getKitNames() {
        return new ArrayList<>(kits.keySet());
    }




}
