package dev.stelar.kits.command.kit;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.*;
import dev.stelar.kits.StelarKits;
import dev.stelar.kits.kit.Kit;
import dev.stelar.kits.util.config.Configuration;
import org.bukkit.entity.Player;

@CommandAlias("kit|kits|gkit|gkits")
public class KitCommand extends BaseCommand {

    @Subcommand("use")
    @Description("Get a kit")
    @Syntax("<kitName>")
    public void onKitUse(Player player, String kitName) {

        Kit kit = StelarKits.getInstance().getKitManager().getKitByName(kitName);
        if(kit == null) {
            player.sendMessage(Configuration.KIT_NOT_FOUND.replace("{kit_name}", kitName));
            return;
        }

        StelarKits.getInstance().getKitManager().giveKit(player, kitName);
    }
}
