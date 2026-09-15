package dev.stelar.kits.kit;

import dev.stelar.kits.kit.model.KitDisplay;
import dev.stelar.kits.kit.model.KitInventory;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.util.List;

@SuppressWarnings("unused")
@Getter
@Setter
public class Kit {

    private final String name;

    private long cooldown;
    private String permission;
    private boolean enabled;

    private KitDisplay kitDisplay;
    private KitInventory kitInventory;

    public Kit(String name) {
        this.name = name;
    }

}
