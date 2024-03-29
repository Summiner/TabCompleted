package rs.jamie.tabcompleted.utils;

import com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerTeams;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import net.luckperms.api.LuckPerms;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import rs.jamie.tabcompleted.config.ConfigManager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public class TeamUtil {

    public static HashMap<UUID, String> lastTeam = new HashMap<>();

    public static HashMap<UUID, String> updatedPlayers = new HashMap<>();
    public static HashMap<UUID, String> lastCrossServerTeam = new HashMap<>();

    public static String getTeamName(int weight, UUID uuid) {
        return "TabListed-Weighted-" + Character.MAX_VALUE + (Character.MAX_VALUE - (char) weight)+"-"+uuid;
    }

    public static TextColor getLastColor(Component component) {
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

    public static List<WrapperPlayServerTeams> updateTeams(ConfigManager config, LuckPerms luckPerms) {
        List<WrapperPlayServerTeams> packets = new ArrayList<>();
        WrapperPlayServerTeams.CollisionRule collision = config.getConfig().miscCollision()? WrapperPlayServerTeams.CollisionRule.ALWAYS: WrapperPlayServerTeams.CollisionRule.NEVER;
        for (Player player : Bukkit.getOnlinePlayers()) {
            lastTeam.putIfAbsent(player.getUniqueId(), "");
            String lTeam = lastTeam.get(player.getUniqueId());
            int weight = LuckPermsUtil.getWeight(player.getUniqueId(), luckPerms);
            String teamName = getTeamName(weight, player.getUniqueId());
            if(!teamName.equals(lTeam)) {
                if(!teamName.isEmpty()) {
                    packets.add(new WrapperPlayServerTeams(lTeam, WrapperPlayServerTeams.TeamMode.REMOVE, (WrapperPlayServerTeams.ScoreBoardTeamInfo) null, player.getName()));
                }
                Component prefix = PapiUtil.set(LegacyComponentSerializer.legacyAmpersand(), player, config.getConfig().nametagPrefix());
                WrapperPlayServerTeams.ScoreBoardTeamInfo teamInfo = new WrapperPlayServerTeams.ScoreBoardTeamInfo(Component.text(teamName), prefix,
                        PapiUtil.set(LegacyComponentSerializer.legacyAmpersand(), player, config.getConfig().nametagSuffix()),
                        WrapperPlayServerTeams.NameTagVisibility.ALWAYS, collision, NamedTextColor.nearestTo(getLastColor(prefix)), WrapperPlayServerTeams.OptionData.NONE);
                packets.add(new WrapperPlayServerTeams(teamName, WrapperPlayServerTeams.TeamMode.CREATE, teamInfo, player.getName()));
            }
            updatedPlayers.put(player.getUniqueId(), teamName);
            lastTeam.put(player.getUniqueId(), teamName);
        }
        return packets;
    }

}
