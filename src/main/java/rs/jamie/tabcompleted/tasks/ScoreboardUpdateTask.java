package rs.jamie.tabcompleted.tasks;

import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;
import rs.jamie.tabcompleted.config.ConfigManager;
import rs.jamie.tabcompleted.utils.ScoreboardUtil;

public class ScoreboardUpdateTask {

    public ScoreboardUpdateTask(Plugin plugin, ConfigManager config) {
        Bukkit.getScheduler().runTaskTimerAsynchronously(plugin, () -> {
            Bukkit.getOnlinePlayers().forEach((player) -> {
                ScoreboardUtil.setScoreboard(player, config.getConfig().scoreboardEntries());
            });
        }, 0L, 20L);
    }

}
