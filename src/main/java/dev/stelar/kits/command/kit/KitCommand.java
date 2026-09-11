package dev.stelar.kits.command.kit;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.*;
import dev.stelar.kits.StelarKits;
import dev.stelar.kits.util.config.Configuration;
import org.bukkit.entity.Player;

@CommandAlias("kit|kits|gkit|gkits")
public class KitCommand extends BaseCommand {

    @Description("Get a kit")
    @Syntax("<kitName>")
    public void onKitUse(Player player, String[] args) {
        String kitName = args[0];

        StelarKits.getInstance().getKitManager().giveKit(player, kitName);
    }

    @Default
    public void showKitsMenu(Player player){
        //TODO: Implement kits menu
    }

}
