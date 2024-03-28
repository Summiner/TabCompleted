package rs.jamie.tabcompleted.tasks;

import com.github.retrooper.packetevents.PacketEvents;
import com.github.retrooper.packetevents.manager.protocol.ProtocolManager;
import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;
import rs.jamie.tabcompleted.config.ConfigManager;
import rs.jamie.tabcompleted.utils.ScoreboardUtil;

public class ScoreboardUpdateTask {

    public ScoreboardUpdateTask(Plugin plugin, ConfigManager config) {
        ProtocolManager protocolManager = PacketEvents.getAPI().getProtocolManager();
        Bukkit.getScheduler().runTaskTimerAsynchronously(plugin, () -> {
            // Reference:
            // protocolManager.getChannels().forEach((channel) -> {
            //     protocolManager.getUser(channel);
            // });
            Bukkit.getOnlinePlayers().forEach((player) -> {
                ScoreboardUtil.setScoreboard(player, config.getConfig().scoreboardEntries());
            });
        }, 0L, 20L);
    }

}
