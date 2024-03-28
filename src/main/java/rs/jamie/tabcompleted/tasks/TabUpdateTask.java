package rs.jamie.tabcompleted.tasks;

import com.github.retrooper.packetevents.PacketEvents;
import com.github.retrooper.packetevents.protocol.player.GameMode;
import com.github.retrooper.packetevents.protocol.player.User;
import com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerPlayerInfoUpdate;
import com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerPlayerInfoUpdate.PlayerInfo;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import rs.jamie.tabcompleted.config.ConfigManager;
import rs.jamie.tabcompleted.redis.InfoType;
import rs.jamie.tabcompleted.redis.RedisInfoObject;
import rs.jamie.tabcompleted.redis.RedisManager;
import rs.jamie.tabcompleted.utils.PapiUtil;
import rs.jamie.tabcompleted.utils.TabUtil;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;

import static com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerPlayerInfoUpdate.Action.*;

public class TabUpdateTask {

    public TabUpdateTask(Plugin plugin, ConfigManager config, RedisManager redisManager) {
        Bukkit.getScheduler().runTaskTimerAsynchronously(plugin, () -> {
            List<PlayerInfo> info = new ArrayList<>();
            for (Player player : Bukkit.getOnlinePlayers()) {
                User user = PacketEvents.getAPI().getPlayerManager().getUser(player);
                if(user==null) continue;
                info.add(new WrapperPlayServerPlayerInfoUpdate.PlayerInfo(user.getProfile(), true, player.getPing(), GameMode.getById(player.getGameMode().getValue()), PapiUtil.set(LegacyComponentSerializer.legacyAmpersand(), player, config.getConfig().tablistPlayerName()), null));
            }
            WrapperPlayServerPlayerInfoUpdate wrapper = new WrapperPlayServerPlayerInfoUpdate(EnumSet.of(ADD_PLAYER, INITIALIZE_CHAT, UPDATE_GAME_MODE, UPDATE_LISTED, UPDATE_LATENCY, UPDATE_DISPLAY_NAME), info);
            if(redisManager.isUsable()) {
                redisManager.sendPacket(new RedisInfoObject(config.getConfig().multiserverName(), InfoType.UPDATE, RedisInfoObject.serialize(info)));
            }
            Bukkit.getOnlinePlayers().forEach((player) -> {
                TabUtil.updateTab(player, PapiUtil.set(LegacyComponentSerializer.legacyAmpersand(), player, config.getConfig().tablistHeader()), PapiUtil.set(LegacyComponentSerializer.legacyAmpersand(), player, config.getConfig().tablistFooter()), wrapper);
            });
        }, 0L, 20L);
    }

}
