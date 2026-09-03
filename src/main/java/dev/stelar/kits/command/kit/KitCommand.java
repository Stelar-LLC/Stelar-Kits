package dev.stelar.kits.command.kit;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.Subcommand;
import org.bukkit.entity.Player;

@CommandAlias("kit|kits|gkit|gkits")
public class KitCommand extends BaseCommand {

    @Subcommand("list")
    public void onList(Player sender) {
        sender.sendMessage("Kits...");
    }

}
