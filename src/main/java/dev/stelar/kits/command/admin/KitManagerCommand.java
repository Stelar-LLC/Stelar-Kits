package dev.stelar.kits.command.admin;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.*;
import dev.stelar.kits.StelarKits;
import dev.stelar.kits.kit.Kit;
import dev.stelar.kits.util.config.Configuration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.Locale;

@CommandAlias("kitmanager|kitm|gkitmanager|gkitm")
public class KitManagerCommand extends BaseCommand {

    @Subcommand("create")
    @CommandPermission("stelarkits.admin.create")
    @Description("Create a kit")
    @Syntax("<kitName>")
    public void onKitCreate(Player player, @NotNull String kitName) {
        String key = kitName.toLowerCase(Locale.ROOT);

        if(StelarKits.getInstance().getKitManager().getKitByName(key) != null) {
            player.sendMessage(Configuration.KIT_ALREADY_EXISTS
                    .replace("{kit_name}", kitName)
            );
            return;
        }

        try {

            StelarKits.getInstance().getKitManager().createKit(kitName);
            player.sendMessage(Configuration.KIT_CREATED.replace("{kit_name}", kitName));

        } catch (NullPointerException | IllegalArgumentException e) {

            player.sendMessage("Error trying to create kit. Check console for more info.");
            StelarKits.getInstance().getLogger().severe("Error creating kit: " + e.getMessage());
        }

    }

    @Subcommand("delete")
    @CommandPermission("stelarkits.admin.delete")
    @Description("Delete a kit")
    @Syntax("<kitName>")
    public void onKitDelete(Player player, @NotNull String kitName) {
        String key = kitName.toLowerCase(Locale.ROOT);

        if(StelarKits.getInstance().getKitManager().getKitByName(key) == null) {
            player.sendMessage(Configuration.KIT_NOT_FOUND
                    .replace("{kit_name}", kitName)
            );
            return;
        }

        try {
            StelarKits.getInstance().getKitManager().deleteKit(kitName);
            player.sendMessage(Configuration.KIT_DELETED.replace("{kit_name}", kitName));

        } catch (NullPointerException | IllegalArgumentException e) {
            player.sendMessage("Error trying to delete kit. Check console for more info.");
            StelarKits.getInstance().getLogger().severe("Error deleting kit: " + e.getMessage());
        }
    }

    @Subcommand("list")
    @CommandPermission("stelarkits.admin.list")
    @Description("Show the list of all kits")
    @Syntax("<kitName>")
    public void showKitList(Player player) {

        if(StelarKits.getInstance().getKitManager().getKits().isEmpty()) {
            for(String line : Configuration.KIT_LIST_NO_KITS_CREATED) {
                player.sendMessage(line);
            }
            return;
        }

        for(String line : Configuration.KIT_LIST_HEADER) {
            player.sendMessage(line);
        }

        for(String kit : StelarKits.getInstance().getKitManager().getKits().stream().map(Kit::getName).toList()) {
            String key = kit.toLowerCase(Locale.ROOT);
            player.sendMessage(Configuration.KIT_LIST_KIT_FORMAT
                    .replace("{kit_name}", kit)
                    .replace("{kit_display}", StelarKits.getInstance().getKitManager().getKitByName(key).getKitDisplay().getDisplayName())
            );
        }
    }

    @Subcommand("setcontent")
    @CommandPermission("stelarkits.admin.setcontent")
    @Description("Set the content of a kit")
    @Syntax("<kitName>")
    public void setContentKit(Player player, @NotNull String kitName) {
        ItemStack[] content = player.getInventory().getStorageContents();
        ItemStack[] armor = player.getInventory().getArmorContents();
        ItemStack offhand = player.getInventory().getItemInOffHand();

        String key = kitName.toLowerCase(Locale.ROOT);
        Kit kit = StelarKits.getInstance().getKitManager().getKitByName(key);

        if(kit == null) {
            player.sendMessage(Configuration.KIT_NOT_FOUND.replace("{kit_name}", kitName));
            return;
        }

        StelarKits.getInstance().getKitManager().setKitContent(kitName, armor, content, offhand);
        player.sendMessage(Configuration.KIT_UPDATED_CONTENT);
    }

}
