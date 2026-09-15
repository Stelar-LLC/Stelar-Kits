package dev.stelar.kits.util.config;

import dev.stelar.kits.StelarKits;

import java.util.ArrayList;
import java.util.List;

public class Configuration {

    public static String DEFAULT_KIT_ITEM;
    public static String DEFAULT_KIT_COOLDOWN;

    public static String PREFIX;
    public static String KIT_NO_PERMISSION;
    public static String NO_PLAYER;

    public static String KIT_NOT_FOUND;
    public static String ON_KIT_COOLDOWN;
    public static String ON_KIT_APPLY;
    public static String KIT_ALREADY_EXISTS;
    public static String KIT_CREATED;
    public static String KIT_DELETED;

    public static boolean KITS_MENU_ENABLED;
    public static String KITS_MENU_TITLE;
    public static int KITS_MENU_SIZE;

    public static List<String> DEFAULT_KIT_LORE_AVAILABLE;
    public static List<String> DEFAULT_KIT_LORE_ON_COOLDOWN;
    public static List<String> DEFAULT_KIT_LORE_NO_PERMISSION;

    public static List<String> KIT_LIST_NO_KITS_CREATED;
    public static String KIT_LIST_KIT_FORMAT;
    public static List<String> KIT_LIST_HEADER;

    public static boolean CLEAR_INVENTORY_ON_KIT_APPLY;
    public static boolean DROP_ITEMS_ON_FULL_INVENTORY;

    public static String KIT_UPDATED_CONTENT;


    public Configuration() {
        this(StelarKits.getInstance() != null && StelarKits.getInstance().getConfigManager() != null
                ? StelarKits.getInstance().getConfigManager().getSettings()
                : null);
    }

    public Configuration(ConfigFile config) {
        if (config == null) {
            return;
        }

        DEFAULT_KIT_COOLDOWN = config.getString("kits.default-kit-cooldown");
        DEFAULT_KIT_ITEM = config.getString("kits.default-kit-item");

        PREFIX = config.getString("prefix");
        KIT_NO_PERMISSION = config.getString("kits.lang.kit-no-permission");
        NO_PLAYER = config.getString("no-player");

        KIT_NOT_FOUND = config.getString("kits.lang.kit-not-found");
        ON_KIT_COOLDOWN = config.getString("kits.lang.on-kit-cooldown");
        ON_KIT_APPLY = config.getString("kits.lang.on-kit-applied");

        KITS_MENU_ENABLED = config.getBoolean("kits-menu.enabled");
        KITS_MENU_TITLE = config.getString("kits-menu.title");
        KITS_MENU_SIZE = config.getInt("kits-menu.rows") * 9;

        DEFAULT_KIT_LORE_AVAILABLE = config.getStringList("kits.default-kit-descriptions.available");
        DEFAULT_KIT_LORE_ON_COOLDOWN = config.getStringList("kits.default-kit-descriptions.on-cooldown");
        DEFAULT_KIT_LORE_NO_PERMISSION = config.getStringList("kits.default-kit-descriptions.no-permission");

        CLEAR_INVENTORY_ON_KIT_APPLY = config.getBoolean("kits.clear-inventory-on-apply");
        DROP_ITEMS_ON_FULL_INVENTORY = config.getBoolean("kits.drop-items-when-inventory-full");

        KIT_ALREADY_EXISTS = config.getString("kits.lang.kit-already-exists");
        KIT_CREATED = config.getString("kits.lang.kit-created");
        KIT_DELETED = config.getString("kits.lang.kit-deleted");

        KIT_LIST_NO_KITS_CREATED = config.getStringList("kits.lang.kit-list.no-kits-created");
        KIT_LIST_KIT_FORMAT = config.getString("kits.lang.kit-list.kit-format");
        KIT_LIST_HEADER = config.getStringList("kits.lang.kit-list.header");

        KIT_UPDATED_CONTENT = config.getString("kits.lang.kit-updated-content");
    }
}
