package rs.jamie.tabcompleted.events;

import com.github.retrooper.packetevents.PacketEvents;
import com.github.retrooper.packetevents.protocol.entity.type.EntityType;
import com.github.retrooper.packetevents.protocol.world.Location;
import com.github.retrooper.packetevents.util.Vector3d;
import com.github.retrooper.packetevents.wrapper.PacketWrapper;
import com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerSetPassengers;
import com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerSpawnEntity;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import org.apache.commons.lang3.math.NumberUtils;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;
import org.bukkit.util.StringUtil;
import rs.jamie.tabcompleted.TabCompletedPlugin;
import rs.jamie.tabcompleted.config.ConfigManager;
import rs.jamie.tabcompleted.utils.PapiUtil;
import rs.jamie.tabcompleted.utils.ScoreboardUtil;
import rs.jamie.tabcompleted.utils.TabUtil;

import java.util.Optional;
import java.util.UUID;

import static com.github.retrooper.packetevents.protocol.entity.type.EntityTypes.ARMOR_STAND;

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
        for (Team team : scoreboard.getTeams()) {
            team.removeEntry(player.getName());
        }

        //CHANGE
        player.setScoreboard(scoreboard);

        TabUtil.updateTab(player, PapiUtil.set(LegacyComponentSerializer.legacyAmpersand(), player, config.getConfig().tablistHeader()), PapiUtil.set(LegacyComponentSerializer.legacyAmpersand(), player, config.getConfig().tablistFooter()));
        ScoreboardUtil.setupScoreboard(player, config.getConfig().scoreboardName(), config.getConfig().scoreboardEntries());
    }

}
