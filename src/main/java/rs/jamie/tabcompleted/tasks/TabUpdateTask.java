package rs.jamie.tabcompleted.tasks;

import com.github.retrooper.packetevents.PacketEvents;
import com.github.retrooper.packetevents.protocol.player.GameMode;
import com.github.retrooper.packetevents.protocol.player.User;
import com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerPlayerInfo;
import com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerPlayerInfoUpdate;
import com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerPlayerInfoUpdate.PlayerInfo;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import net.luckperms.api.LuckPerms;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;
import rs.jamie.tabcompleted.config.ConfigManager;
import rs.jamie.tabcompleted.utils.LuckPermsUtil;
import rs.jamie.tabcompleted.utils.PapiUtil;
import rs.jamie.tabcompleted.utils.ScoreboardUtil;
import rs.jamie.tabcompleted.utils.TabUtil;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.List;

import static com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerPlayerInfoUpdate.Action.*;

public class TabUpdateTask {

    HashMap<Player, String> lastTeam = new HashMap<>();

    private String getTeamName(int weight) {
        return "TabListed-Weighted-" + Character.MAX_VALUE + (Character.MAX_VALUE - (char) weight);
    }

    public TabUpdateTask(Plugin plugin, ConfigManager config, LuckPerms luckPerms, Scoreboard scoreboard) {
        Bukkit.getScheduler().runTaskTimerAsynchronously(plugin, () -> {
            List<PlayerInfo> info = new ArrayList<>();
            //this looks so ugly but it works for now!
            for (Player onlinePlayer : Bukkit.getOnlinePlayers()) {
                lastTeam.putIfAbsent(onlinePlayer, "");
                String last = lastTeam.get(onlinePlayer);
                int weight = LuckPermsUtil.getWeight(onlinePlayer.getUniqueId(), luckPerms);
                String teamName = getTeamName(weight);
                if(!teamName.equals(last)) {
                    if(!teamName.isEmpty()) {
                        try {
                            scoreboard.getTeam(last).removeEntry(onlinePlayer.getName());
                        } catch (IllegalArgumentException | NullPointerException ignored) {
                        }
                    }
                    try {
                        scoreboard.getTeam(teamName).addEntry(onlinePlayer.getName());
                    } catch (IllegalArgumentException | NullPointerException ignored) {
                        scoreboard.registerNewTeam(teamName).addEntry(onlinePlayer.getName());
                    }
                    User user = PacketEvents.getAPI().getPlayerManager().getUser(onlinePlayer);
                    info.add(new WrapperPlayServerPlayerInfoUpdate.PlayerInfo(user.getProfile(), true, onlinePlayer.getPing(), GameMode.getById(onlinePlayer.getGameMode().getValue()), Component.text(onlinePlayer.getName()), null));
                    lastTeam.put(onlinePlayer, teamName);
                }
            }
            WrapperPlayServerPlayerInfoUpdate test = new WrapperPlayServerPlayerInfoUpdate(EnumSet.of(ADD_PLAYER, INITIALIZE_CHAT, UPDATE_GAME_MODE, UPDATE_LISTED, UPDATE_LATENCY, UPDATE_DISPLAY_NAME), info);
            Bukkit.getOnlinePlayers().forEach((player) -> {
                TabUtil.updateTab(player, PapiUtil.set(LegacyComponentSerializer.legacyAmpersand(), player, config.getConfig().tablistHeader()), PapiUtil.set(LegacyComponentSerializer.legacyAmpersand(), player, config.getConfig().tablistFooter()), test);
            });
        }, 0L, 20L);
    }

}
