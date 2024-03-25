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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerTeams.TeamMode;
import static com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerTeams.CollisionRule;
import static com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerTeams.NameTagVisibility;
import static com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerTeams.OptionData;
import static com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerTeams.ScoreBoardTeamInfo;


public class TeamUpdateTask {

    HashMap<Player, String> lastTeams = new HashMap<>();
    private final PlayerManager playerManager;

    private String getTeamName(int weight, int entityID) {
        return "TabListed-Weighted-" + Character.MAX_VALUE + (Character.MAX_VALUE - (char) weight)+"-"+entityID;
    }

    private TextColor getLastColor(Component component) {
        if (component instanceof TextComponent) {
            TextColor color = component.color();
            if (color != null) return color;
        } else {
            List<Component> children = component.children();
            for(int i=1;i<=children.size();i++) {
                Component child = children.get(children.size()-i);
                if(child instanceof TextComponent) {
                    TextColor color = child.color();
                    if(color!=null) return color;
                }
            }
        }
        return NamedTextColor.WHITE;
    }

    public TeamUpdateTask(Plugin plugin, ConfigManager config, LuckPerms luckPerms) {
        playerManager = PacketEvents.getAPI().getPlayerManager();
        Bukkit.getScheduler().runTaskTimerAsynchronously(plugin, () -> {
            List<WrapperPlayServerTeams> packets = new ArrayList<>();
            CollisionRule collision = config.getConfig().miscCollision()?CollisionRule.ALWAYS:CollisionRule.NEVER;
            for (Player player : Bukkit.getOnlinePlayers()) {
                lastTeams.putIfAbsent(player, "");
                String lastTeam = lastTeams.get(player);
                int weight = LuckPermsUtil.getWeight(player.getUniqueId(), luckPerms);
                String teamName = getTeamName(weight, player.getEntityId());
                if(!teamName.equals(lastTeam)) {
                    if(!teamName.isEmpty()) {
                        packets.add(new WrapperPlayServerTeams(lastTeam, TeamMode.REMOVE, (ScoreBoardTeamInfo) null, player.getName()));
                    }
                    Component prefix = PapiUtil.set(LegacyComponentSerializer.legacyAmpersand(), player, config.getConfig().nametagPrefix());
                    ScoreBoardTeamInfo teamInfo = new ScoreBoardTeamInfo(Component.text(teamName), prefix,
                            PapiUtil.set(LegacyComponentSerializer.legacyAmpersand(), player, config.getConfig().nametagSuffix()),
                            NameTagVisibility.ALWAYS, collision, NamedTextColor.nearestTo(getLastColor(prefix)), OptionData.NONE);
                    packets.add(new WrapperPlayServerTeams(teamName, TeamMode.CREATE, teamInfo, player.getName()));
                }
                lastTeams.put(player, teamName);
            }
            Bukkit.getOnlinePlayers().forEach((player) -> {
                packets.forEach((packet) -> {
                    playerManager.sendPacketSilently(player, packet);
                });
            });
        }, 0L, 20L);
    }
}
