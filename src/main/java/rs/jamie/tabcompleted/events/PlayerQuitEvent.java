package rs.jamie.tabcompleted.events;

import com.github.retrooper.packetevents.PacketEvents;
import com.github.retrooper.packetevents.protocol.player.GameMode;
import com.github.retrooper.packetevents.protocol.player.User;
import com.github.retrooper.packetevents.protocol.player.UserProfile;
import com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerPlayerInfoUpdate;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import rs.jamie.tabcompleted.config.ConfigManager;
import rs.jamie.tabcompleted.redis.InfoType;
import rs.jamie.tabcompleted.redis.PlayerInfoObject;
import rs.jamie.tabcompleted.redis.RedisInfoObject;
import rs.jamie.tabcompleted.redis.RedisManager;
import rs.jamie.tabcompleted.utils.PapiUtil;
import rs.jamie.tabcompleted.utils.TabUtil;
import rs.jamie.tabcompleted.utils.TeamUtil;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public class PlayerQuitEvent implements Listener {

    private final ConfigManager config;
    private  final  RedisManager redisManager;

    public PlayerQuitEvent(ConfigManager config, RedisManager redisManager) {
        this.config = config;
        this.redisManager = redisManager;
    }

    @EventHandler
    public void onPlayerQuitReceive(org.bukkit.event.player.PlayerQuitEvent event) {
        Player player = event.getPlayer();
        TeamUtil.updatedPlayers.remove(player.getUniqueId());
        TeamUtil.lastTeam.remove(player.getUniqueId());
        if(redisManager.isUsable()) {
            CompletableFuture.runAsync(() -> {
                PlayerInfoObject pinfo = new PlayerInfoObject(new UserProfile(player.getUniqueId(), player.getName()), 0, GameMode.SURVIVAL, "", null);
                RedisInfoObject redisInfoObject = new RedisInfoObject(config.getConfig().multiserverName(), InfoType.REMOVE, List.of(pinfo));
                redisManager.sendPacket(redisInfoObject);
            });
        }
    }

}
