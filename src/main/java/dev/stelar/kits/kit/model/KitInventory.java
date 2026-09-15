package dev.stelar.kits.kit.model;

import lombok.Getter;
import lombok.Setter;
import org.bukkit.inventory.ItemStack;

@Getter
@Setter
public class KitInventory {

    public ItemStack[] content;
    public ItemStack[] armor;
    public ItemStack offhand;
}
