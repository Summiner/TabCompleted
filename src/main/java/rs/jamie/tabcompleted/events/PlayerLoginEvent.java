package rs.jamie.tabcompleted.events;

import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.scoreboard.Scoreboard;
import rs.jamie.tabcompleted.config.ConfigManager;
import rs.jamie.tabcompleted.utils.PapiUtil;
import rs.jamie.tabcompleted.utils.ScoreboardUtil;
import rs.jamie.tabcompleted.utils.TabUtil;

public class PlayerLoginEvent implements Listener {

    private final ConfigManager config;
    private final Scoreboard scoreboard;

    public PlayerLoginEvent(ConfigManager config, Scoreboard scoreboard) {
        this.scoreboard = scoreboard;
        this.config = config;
    }

    @EventHandler
    public void onPlayerLoginReceive(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        player.setScoreboard(scoreboard);
        TabUtil.updateTab(player, PapiUtil.set(LegacyComponentSerializer.legacyAmpersand(), player, config.getConfig().tablistHeader()), PapiUtil.set(LegacyComponentSerializer.legacyAmpersand(), player, config.getConfig().tablistFooter()));
        ScoreboardUtil.setupScoreboard(player, config.getConfig().scoreboardName(), config.getConfig().scoreboardEntries());
    }

}
