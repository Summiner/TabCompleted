package rs.jamie.tabcompleted;

import net.luckperms.api.LuckPerms;
import org.bukkit.Bukkit;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scoreboard.Scoreboard;
import redis.clients.jedis.DefaultJedisClientConfig;
import redis.clients.jedis.Jedis;
import rs.jamie.tabcompleted.config.ConfigManager;
import rs.jamie.tabcompleted.events.PlayerLoginEvent;
import rs.jamie.tabcompleted.events.PlayerQuitEvent;
import rs.jamie.tabcompleted.packets.PacketManager;
import rs.jamie.tabcompleted.redis.RedisManager;
import rs.jamie.tabcompleted.tasks.ScoreboardUpdateTask;
import rs.jamie.tabcompleted.tasks.TabUpdateTask;
import rs.jamie.tabcompleted.tasks.TeamUpdateTask;

public final class TabCompletedPlugin extends JavaPlugin {

    private PacketManager packetManager;
    private RedisManager redisManager;
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
        if(configManager.getConfig().multiserverEnabled()) {
            redisManager = new RedisManager(this, configManager);
        }
        new TabUpdateTask(this, configManager, redisManager);
        new TeamUpdateTask(this, configManager, luckperms);
        new ScoreboardUpdateTask(this, configManager);
        Bukkit.getPluginManager().registerEvents(new PlayerLoginEvent(configManager, scoreboard), this);
        Bukkit.getPluginManager().registerEvents(new PlayerQuitEvent(configManager, redisManager), this);
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
