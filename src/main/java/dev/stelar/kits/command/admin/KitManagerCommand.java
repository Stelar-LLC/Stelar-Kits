package dev.stelar.kits.command.admin;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.*;
import dev.stelar.kits.StelarKits;
import dev.stelar.kits.kit.Kit;
import dev.stelar.kits.util.config.Configuration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

@CommandAlias("kitmanager|kitm|gkitmanager|gkitm")
public class KitManagerCommand extends BaseCommand {

    @Subcommand("create")
    @CommandPermission("stelarkits.admin.create")
    @Description("Create a kit")
    @Syntax("<kitName>")
    public void onKitCreate(Player player, String kitName) {

        if(StelarKits.getInstance().getKitManager().getKitByName(kitName) != null) {
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
    public void onKitDelete(Player player, String kitName) {

        if(StelarKits.getInstance().getKitManager().getKitByName(kitName) == null) {
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

        for(String kit : StelarKits.getInstance().getKitManager().getKits().stream().map(kit -> kit.getName()).toList()) {
            player.sendMessage(Configuration.KIT_LIST_KIT_FORMAT
                    .replace("{kit_name}", kit)
                    .replace("{kit_display}", StelarKits.getInstance().getKitManager().getKitByName(kit).getKitDisplay().getDisplayName())
            );
        }
    }

    @Subcommand("setcontent")
    @CommandPermission("stelarkits.admin.setcontent")
    @Description("Set the content of3 a kit")
    @Syntax("<kitName>")
    public void setContentKit(Player player, String kitName) {
        ItemStack[] content = player.getInventory().getContents();
        Kit kit = StelarKits.getInstance().getKitManager().getKitByName(kitName);

        if(kit == null) {
            player.sendMessage(Configuration.KIT_NOT_FOUND.replace("{kit_name}", kitName));
            return;
        }

        StelarKits.getInstance().getKitManager().setKitContent(kitName, content);
        player.sendMessage("Content updated!");
    }

}
