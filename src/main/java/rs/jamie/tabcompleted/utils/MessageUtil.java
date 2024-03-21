package rs.jamie.tabcompleted.utils;

import net.kyori.adventure.text.Component;
import org.bukkit.entity.Player;

public class MessageUtil {

    public static void sendMessage(Player player, Component message) {
        player.sendMessage(message);
    }

}
