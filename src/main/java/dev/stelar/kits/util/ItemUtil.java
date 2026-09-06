package dev.stelar.kits.util;

import lombok.experimental.UtilityClass;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.io.BukkitObjectInputStream;
import org.bukkit.util.io.BukkitObjectOutputStream;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Base64;

@UtilityClass
@SuppressWarnings("unused")
public class ItemUtil {

    public String serialize(ItemStack[] items) {
        if (items == null) {
            return null;
        }

        try (ByteArrayOutputStream output = new ByteArrayOutputStream();
             BukkitObjectOutputStream data = new BukkitObjectOutputStream(output)) {

            data.writeObject(items);

            return Base64.getEncoder().encodeToString(output.toByteArray());

        } catch (IOException exception) {
            throw new RuntimeException("Failed to serialize ItemStack array", exception);
        }
    }

    public ItemStack[] deserialize(String data) {
        if (data == null || data.isBlank()) {
            return null;
        }

        try (ByteArrayInputStream input =
                     new ByteArrayInputStream(Base64.getDecoder().decode(data));
             BukkitObjectInputStream dataInput =
                     new BukkitObjectInputStream(input)) {

            return (ItemStack[]) dataInput.readObject();

        } catch (IOException | ClassNotFoundException | IllegalArgumentException exception) {
            throw new RuntimeException("Failed to deserialize ItemStack array", exception);
        }
    }
}
