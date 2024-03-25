package rs.jamie.tabcompleted.utils;

import com.github.retrooper.packetevents.PacketEvents;
import com.github.retrooper.packetevents.protocol.player.User;
import com.github.retrooper.packetevents.protocol.score.ScoreFormat;
import com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerDisplayScoreboard;
import com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerResetScore;
import com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerScoreboardObjective;
import com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerUpdateScore;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import org.bukkit.entity.Player;
import java.util.concurrent.CompletableFuture;

public class ScoreboardUtil {

    public static void setupScoreboard(Player player, Component displayName, Component[] defaultScores) {
        CompletableFuture.supplyAsync(() -> {
            User user = PacketEvents.getAPI().getPlayerManager().getUser(player);
            user.sendPacket(new WrapperPlayServerResetScore(user.getName(), null));
            user.sendPacket(new WrapperPlayServerScoreboardObjective("TabListed-Scoreboard", WrapperPlayServerScoreboardObjective.ObjectiveMode.CREATE, PapiUtil.set(LegacyComponentSerializer.legacyAmpersand(), player, displayName), WrapperPlayServerScoreboardObjective.RenderType.INTEGER));
            user.sendPacket(new WrapperPlayServerDisplayScoreboard(1, "TabListed-Scoreboard"));
            setScoreboard(player, defaultScores);
            return null;
        });
    }

    public static void setScoreboard(Player player, Component[] defaultScores) {
        CompletableFuture.supplyAsync(() -> {
            User user = PacketEvents.getAPI().getPlayerManager().getUser(player);
            Component[] scores = PapiUtil.set(LegacyComponentSerializer.legacyAmpersand(), player, defaultScores);
            user.sendPacket(new WrapperPlayServerResetScore(user.getName(), null));
            for (int i=0;i<scores.length;i++) {
                user.sendPacket(new WrapperPlayServerUpdateScore("TabListed-Scoreboard-Entry-"+i, WrapperPlayServerUpdateScore.Action.CREATE_OR_UPDATE_ITEM, "TabListed-Scoreboard", scores.length-i, scores[i], ScoreFormat.blankScore()));
            }
            return null;
        });
    }

}
