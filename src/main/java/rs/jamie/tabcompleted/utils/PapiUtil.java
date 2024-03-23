package rs.jamie.tabcompleted.utils;

import me.clip.placeholderapi.PlaceholderAPI;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class PapiUtil {
    private static boolean isSupported() {
        return Bukkit.getPluginManager().isPluginEnabled("PlaceholderAPI");
    }

    public static String set(String str) {
        return isSupported() ? set(null, str) : str;
    }

    public static String set(Player player, String str) {
        return isSupported() ? PlaceholderAPI.setPlaceholders(player, str) : str;
    }

    public static Component set(LegacyComponentSerializer serializer, Player player, Component component) {
        if (!isSupported()) return component;
        return serializer.deserialize(set(player, serializer.serialize(component)));
    }

    public static Component[] set(LegacyComponentSerializer serializer, Player player, Component[] components) {
        if (!isSupported()) return components;
        List<Component> list = new ArrayList<>();
        for (Component component : components) {
            list.add(set(serializer, player, component));
        }
        return list.toArray(new Component[0]);
    }

}