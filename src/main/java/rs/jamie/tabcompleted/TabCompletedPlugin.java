package rs.jamie.tabcompleted;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;
import rs.jamie.tabcompleted.config.ConfigManager;
import rs.jamie.tabcompleted.events.PlayerLoginEvent;
import rs.jamie.tabcompleted.packets.PacketManager;
import rs.jamie.tabcompleted.tasks.TabUpdateTask;

public final class TabCompletedPlugin extends JavaPlugin {

    private PacketManager packetManager;

    @Override
    public void onEnable() {
        getDataFolder().mkdirs();
        ConfigManager configManager = new ConfigManager(getDataFolder());
        packetManager = new PacketManager(this, configManager);
        new TabUpdateTask(this, configManager);
        Bukkit.getPluginManager().registerEvents(new PlayerLoginEvent(configManager), this);
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
