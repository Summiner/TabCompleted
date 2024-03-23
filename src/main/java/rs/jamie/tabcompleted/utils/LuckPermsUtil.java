package rs.jamie.tabcompleted.utils;

import net.luckperms.api.LuckPerms;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.RegisteredServiceProvider;

import java.util.NoSuchElementException;
import java.util.OptionalInt;
import java.util.UUID;

public class LuckPermsUtil {
    
    private static boolean isSupported() {
        return Bukkit.getPluginManager().isPluginEnabled("LuckPerms");
    }

    public static int getWeight(UUID uuid, LuckPerms api) {
        try {
            return isSupported() ? api.getGroupManager().getGroup(api.getUserManager().getUser(uuid).getPrimaryGroup()).getWeight().getAsInt() : 0;
        } catch (NoSuchElementException e) {
            return 0;
        }
    }

}