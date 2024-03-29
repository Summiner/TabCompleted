package rs.jamie.tabcompleted.redis;

import com.github.retrooper.packetevents.protocol.player.GameMode;
import com.github.retrooper.packetevents.protocol.player.UserProfile;

import javax.annotation.Nullable;

public record PlayerInfoObject(UserProfile gameProfile, int ping, GameMode gameMode, String displayName, @Nullable String team) {

}
