package rs.jamie.tabcompleted;

import net.luckperms.api.LuckPerms;
import org.bukkit.Bukkit;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scoreboard.Scoreboard;
import rs.jamie.tabcompleted.config.ConfigManager;
import rs.jamie.tabcompleted.events.PlayerLoginEvent;
import rs.jamie.tabcompleted.packets.PacketManager;
import rs.jamie.tabcompleted.tasks.TabUpdateTask;

public final class TabCompletedPlugin extends JavaPlugin {

    private PacketManager packetManager;
    private Scoreboard scoreboard;

    @Override
    public void onEnable() {
        this.saveDefaultConfig();
        ConfigManager configManager = new ConfigManager(getDataFolder());
        RegisteredServiceProvider<LuckPerms> provider = Bukkit.getServicesManager().getRegistration(LuckPerms.class);
        LuckPerms luckperms = null;
        if (provider != null) luckperms = provider.getProvider();
        packetManager = new PacketManager(this, configManager);
        scoreboard = Bukkit.getScoreboardManager().getMainScoreboard();
        new TabUpdateTask(this, configManager, luckperms, scoreboard);
        Bukkit.getPluginManager().registerEvents(new PlayerLoginEvent(configManager, scoreboard), this);
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
