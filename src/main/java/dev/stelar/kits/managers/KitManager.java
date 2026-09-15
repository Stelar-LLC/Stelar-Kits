package dev.stelar.kits.managers;

import dev.stelar.kits.StelarKits;
import dev.stelar.kits.kit.Kit;
import dev.stelar.kits.kit.model.KitDisplay;
import dev.stelar.kits.kit.model.KitInventory;
import dev.stelar.kits.kit.model.KitState;
import dev.stelar.kits.util.ItemUtil;
import dev.stelar.kits.util.TimeUtil;
import dev.stelar.kits.util.config.ConfigFile;
import dev.stelar.kits.util.config.Configuration;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

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
        String path = "kits." + kit.getName().toLowerCase(Locale.ROOT);

        data.set(path + ".cooldown", kit.getCooldown());
        data.set(path + ".permission", kit.getPermission());
        data.set(path + ".enabled", kit.isEnabled());

        data.set(path + ".item.material", kit.getKitDisplay().getIcon().toString());
        data.set(path + ".item.display-name", kit.getKitDisplay().getDisplayName());
        data.set(path + ".item.glow", kit.getKitDisplay().isGlow());
        data.set(path + ".item.slot", kit.getKitDisplay().getSlot());

        data.set(path + ".item.lore.available", kit.getKitDisplay().getLore().get(KitState.AVAILABLE));
        data.set(path + ".item.lore.on-cooldown", kit.getKitDisplay().getLore().get(KitState.ON_COOLDOWN));
        data.set(path + ".item.lore.no-permission", kit.getKitDisplay().getLore().get(KitState.NO_PERMISSION));

        data.set(path + ".content.armor", ItemUtil.serialize(kit.getKitInventory().getArmor()));
        data.set(path + ".content.inventory", ItemUtil.serialize(kit.getKitInventory().getContent()));
        data.set(path + ".content.offhand", ItemUtil.serialize(kit.getKitInventory().getOffhand()));

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
            List<String> availableLore = config.getStringList("kits." + key + ".item.lore.available");
            List<String> onCooldownLore = config.getStringList("kits." + key + ".item.lore.on-cooldown");
            List<String> noPermissionLore = config.getStringList("kits." + key + ".item.lore.no-permission");

            Kit kit = new Kit(key);

            kit.setCooldown(cooldown);
            kit.setPermission(permission);
            kit.setEnabled(enabled);

            KitDisplay display = new KitDisplay();

            display.setIcon(Material.matchMaterial(icon));
            display.setDisplayName(displayName);
            display.setGlow(glow);
            display.setSlot(slot);
            display.getLore().put(KitState.AVAILABLE, availableLore);
            display.getLore().put(KitState.ON_COOLDOWN, onCooldownLore);
            display.getLore().put(KitState.NO_PERMISSION, noPermissionLore);

            kit.setKitDisplay(display);

            KitInventory inventory = new KitInventory();

            inventory.setArmor(ItemUtil.deserialize(config.getString("kits." + key + ".content.armor")));
            inventory.setContent(ItemUtil.deserialize(config.getString("kits." + key + ".content.inventory")));
            inventory.setOffhand(ItemUtil.deserializeItem(config.getString("kits." + key + ".content.offhand")));

            kit.setKitInventory(inventory);

            kits.put(key.toLowerCase(Locale.ROOT), kit);

        }

    }

    /*
    TODO:
      - APLICAR COOLDOWN
     */

    public void giveKit(Player player, String name) {
        PlayerInventory inventory = player.getInventory();

        Kit kit = getKitByName(name.toLowerCase(Locale.ROOT));

        if (kit == null) {
            player.sendMessage(Configuration.KIT_NOT_FOUND
                    .replace("{kit_name}", name));
            return;
        }

        if (!player.hasPermission(kit.getPermission())) {
            player.sendMessage(Configuration.KIT_NO_PERMISSION
                    .replace("{kit_name}", name));
            return;
        }

        KitInventory kitInventory = kit.getKitInventory();

        ItemStack[] content = kitInventory.getContent();
        ItemStack[] armor = kitInventory.getArmor();
        ItemStack offHand = kitInventory.getOffhand();

        if (Configuration.CLEAR_INVENTORY_ON_KIT_APPLY) {
            inventory.clear();
            inventory.setArmorContents(new ItemStack[4]);
            inventory.setItemInOffHand(null);
        }

        if (content != null) {
            addItems(player, content);
        }

        if (armor != null) {
            for (int i = 0; i < armor.length && i < 4; i++) {
                ItemStack item = armor[i];

                if (item == null || item.getType().isAir()) {
                    continue;
                }

                if (inventory.getArmorContents()[i] == null
                        || inventory.getArmorContents()[i].getType().isAir()) {

                    ItemStack[] currentArmor = inventory.getArmorContents();
                    currentArmor[i] = item.clone();
                    inventory.setArmorContents(currentArmor);

                } else {
                    addItemOrDrop(player, item);
                }
            }
        }

        if (offHand != null && !offHand.getType().isAir()) {
            ItemStack currentOffHand = inventory.getItemInOffHand();

            if (currentOffHand.getType().isAir()) {
                inventory.setItemInOffHand(offHand.clone());
            } else {
                addItemOrDrop(player, offHand);
            }
        }

        if(StelarKits.getInstance().getCooldownManager().isOnCooldown(player.getUniqueId(), name)) {
            // mensaje de cooldown
            // format
        }

        player.sendMessage(Configuration.ON_KIT_APPLY
                .replace("{kit_name}", name));
    }

    private void addItems(Player player, ItemStack[] items) {
        for (ItemStack item : items) {
            if (item == null || item.getType().isAir()) {
                continue;
            }

            addItemOrDrop(player, item);
        }
    }

    private void addItemOrDrop(Player player, ItemStack item) {
        HashMap<Integer, ItemStack> leftovers =
                player.getInventory().addItem(item.clone());

        if (!leftovers.isEmpty()
                && Configuration.DROP_ITEMS_ON_FULL_INVENTORY) {

            dropItems(
                    player,
                    leftovers.values().toArray(new ItemStack[0])
            );
        }
    }

    private void dropItems(Player player, ItemStack[] items) {
        for (ItemStack item : items) {
            if (item == null || item.getType().isAir()) {
                continue;
            }

            player.getWorld().dropItemNaturally(
                    player.getLocation(),
                    item.clone()
            );
        }
    }


    public void createKit(String name) {
        Kit kit = new Kit(name);

        kit.setEnabled(true);
        kit.setCooldown(0);
        kit.setPermission("stelar.kit." + name);

        KitDisplay display = new KitDisplay();
        KitInventory inventory = new KitInventory();

        display.setIcon(Material.matchMaterial(Configuration.DEFAULT_KIT_ITEM));
        display.setDisplayName(name);
        display.setGlow(true);
        display.setSlot(getRandomSlot());

        display.getLore().put(KitState.AVAILABLE, Configuration.DEFAULT_KIT_LORE_AVAILABLE);
        display.getLore().put(KitState.ON_COOLDOWN, Configuration.DEFAULT_KIT_LORE_ON_COOLDOWN);
        display.getLore().put(KitState.NO_PERMISSION, Configuration.DEFAULT_KIT_LORE_NO_PERMISSION);

        inventory.setArmor(new ItemStack[]{});
        inventory.setContent(new ItemStack[]{});
        inventory.setOffhand(ItemStack.empty());

        kit.setKitDisplay(display);
        kit.setKitInventory(inventory);

        saveKit(kit);
        String key = name.toLowerCase();
        kits.put(key, kit);
    }

    public static int getRandomSlot(){
        return ThreadLocalRandom.current().nextInt(Configuration.KITS_MENU_SIZE);
    }

    public void deleteKit(String name) {
        ConfigFile data = StelarKits.getInstance().getConfigManager().getKitsData();
        name = name.toLowerCase(Locale.ROOT);

        data.set("kits." + name, null);      name = name.toLowerCase(Locale.ROOT);
        data.save();
        data.reload();

        kits.remove(name);
    }

    public void setKitContent(String kitName, ItemStack[] armor, ItemStack[] content, ItemStack offhand) {
        if(content == null) {
            content = new ItemStack[]{};
        }

        String key = kitName.toLowerCase(Locale.ROOT);

        Kit kit = getKitByName(key);
        KitInventory inventory = kit.getKitInventory();
        inventory.setArmor(armor);
        inventory.setContent(content);
        inventory.setOffhand(offhand);

        kit.setKitInventory(inventory);
        saveKit(kit);
    }

    public Kit getKitByName(String name) {
        return kits.get(name);
    }

    public List<Kit> getKits() {
        return new ArrayList<>(kits.values());
    }





}
