package dev.stelar.kits.util.config;

import dev.stelar.kits.StelarKits;

import java.util.ArrayList;
import java.util.List;

public class Configuration {

    public static String DEFAULT_KIT_ITEM;
    public static String DEFAULT_KIT_COOLDOWN;

    public static String PREFIX;
    public static String NO_PERMISSION;
    public static String NO_PLAYER;

    public static String KIT_NOT_FOUND;
    public static String ON_KIT_COOLDOWN;
    public static String ON_KIT_APPLY;

    public static boolean KITS_MENU_ENABLED;
    public static String KITS_MENU_TITLE;
    public static int KITS_MENU_SIZE;

    public static List<String> DEFAULT_KIT_LORE_AVAILABLE;
    public static List<String> DEFAULT_KIT_LORE_ON_COOLDOWN;
    public static List<String> DEFAULT_KIT_LORE_NO_PERMISSION;

    public static boolean CLEAR_INVENTORY_ON_KIT_APPLY;

    public Configuration() {
        ConfigFile config = StelarKits.getInstance().getConfigManager().getSettings();

        DEFAULT_KIT_COOLDOWN = config.getString("kits.default-kit-cooldown");
        DEFAULT_KIT_ITEM = config.getString("kits.default-kit-item");

        PREFIX = config.getString("prefix");
        NO_PERMISSION = config.getString("no-permission");
        NO_PLAYER = config.getString("no-player");

        KIT_NOT_FOUND = config.getString("kits.lang.kit-not-found");
        ON_KIT_COOLDOWN = config.getString("kits.lang.on-kit-cooldown");
        ON_KIT_APPLY = config.getString("kits.lang.on-kit-apply");

        KITS_MENU_ENABLED = config.getBoolean("kits-menu.enabled");
        KITS_MENU_TITLE = config.getString("kits-menu.title");
        KITS_MENU_SIZE = config.getInt("kits-menu.rows") * 9;

        DEFAULT_KIT_LORE_AVAILABLE = config.getStringList("kits.default-kit-descriptions.available");
        DEFAULT_KIT_LORE_ON_COOLDOWN = config.getStringList("kits.default-kit-descriptions.on-cooldown");
        DEFAULT_KIT_LORE_NO_PERMISSION = config.getStringList("kits.default-kit-descriptions.no-permission");

        CLEAR_INVENTORY_ON_KIT_APPLY = config.getBoolean("kits.clear-inventory-on-apply");
    }
}
