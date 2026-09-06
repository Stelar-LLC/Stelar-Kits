package dev.stelar.kits.kit.model;

import lombok.Getter;
import lombok.Setter;
import org.bukkit.Material;

import java.util.EnumMap;
import java.util.List;
import java.util.Map;

@Getter
@Setter
public class KitDisplay {

    private String displayName;
    private Material icon;
    private boolean glow;
    private int slot;

    private Map<KitState, List<String>> lore = new EnumMap<>(KitState.class);

    public List<String> get(KitState state){
        return lore.get(state);
    }
}
