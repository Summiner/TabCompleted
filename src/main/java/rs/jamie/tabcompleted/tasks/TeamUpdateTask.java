package rs.jamie.tabcompleted.tasks;

import com.github.retrooper.packetevents.PacketEvents;
import com.github.retrooper.packetevents.manager.player.PlayerManager;
import com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerTeams;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import net.luckperms.api.LuckPerms;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import rs.jamie.tabcompleted.config.ConfigManager;
import rs.jamie.tabcompleted.utils.LuckPermsUtil;
import rs.jamie.tabcompleted.utils.PapiUtil;
import rs.jamie.tabcompleted.utils.TeamUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import static com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerTeams.TeamMode;
import static com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerTeams.CollisionRule;
import static com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerTeams.NameTagVisibility;
import static com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerTeams.OptionData;
import static com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerTeams.ScoreBoardTeamInfo;


public class TeamUpdateTask {


    private final PlayerManager playerManager;



    public TeamUpdateTask(Plugin plugin, ConfigManager config, LuckPerms luckPerms) {
        playerManager = PacketEvents.getAPI().getPlayerManager();
        Bukkit.getScheduler().runTaskTimerAsynchronously(plugin, () -> {
            List<WrapperPlayServerTeams> packets = TeamUtil.updateTeams(config, luckPerms);
            Bukkit.getOnlinePlayers().forEach((player) -> {
                packets.forEach((packet) -> {
                    playerManager.sendPacketSilently(player, packet);
                });
            });
        }, 0L, 20L);
    }
}
