package rs.jamie.tabcompleted.tasks;

import com.github.retrooper.packetevents.PacketEvents;
import com.github.retrooper.packetevents.protocol.player.GameMode;
import com.github.retrooper.packetevents.protocol.player.User;
import com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerPlayerInfo;
import com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerPlayerInfoUpdate;
import com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerPlayerInfoUpdate.PlayerInfo;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import rs.jamie.tabcompleted.config.ConfigManager;
import rs.jamie.tabcompleted.utils.PapiUtil;
import rs.jamie.tabcompleted.utils.TabUtil;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;

import static com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerPlayerInfoUpdate.Action.*;

public class TabUpdateTask {

    public TabUpdateTask(Plugin plugin, ConfigManager config) {
        Bukkit.getScheduler().runTaskTimerAsynchronously(plugin, () -> {
            List<PlayerInfo> info = new ArrayList<>();
            //this looks so ugly but it works for now!
            for (Player onlinePlayer : Bukkit.getOnlinePlayers()) {
                User user = PacketEvents.getAPI().getPlayerManager().getUser(onlinePlayer);
                info.add(new WrapperPlayServerPlayerInfoUpdate.PlayerInfo(user.getProfile(), true, onlinePlayer.getPing(), GameMode.getById(onlinePlayer.getGameMode().getValue()), Component.text(onlinePlayer.getName()), null));
            }
            WrapperPlayServerPlayerInfoUpdate test = new WrapperPlayServerPlayerInfoUpdate(EnumSet.of(ADD_PLAYER, INITIALIZE_CHAT, UPDATE_GAME_MODE, UPDATE_LISTED, UPDATE_LATENCY, UPDATE_DISPLAY_NAME), info);
            Bukkit.getOnlinePlayers().forEach((player) -> {
                TabUtil.updateTab(player, PapiUtil.set(LegacyComponentSerializer.legacyAmpersand(), player, config.getConfig().header()), PapiUtil.set(LegacyComponentSerializer.legacyAmpersand(), player, config.getConfig().footer()), test);
            });
        }, 0L, 20L);
    }

}
