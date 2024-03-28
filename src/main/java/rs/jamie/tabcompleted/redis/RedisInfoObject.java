package rs.jamie.tabcompleted.redis;

import com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerPlayerInfoUpdate;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import rs.jamie.tabcompleted.config.ComponentSerializer;

import java.util.ArrayList;
import java.util.List;

public class RedisInfoObject {

    private final String server;
    private final InfoType type;
    private final List<PlayerInfoObject> objects;

    public RedisInfoObject(String server, InfoType type, List<PlayerInfoObject> objects) {
        this.server = server;
        this.type = type;
        this.objects = objects;
    }

    public String getServer() {
        return server;
    }

    public InfoType getType() {
        return type;
    }

    public List<PlayerInfoObject> getObjects() {
        return objects;
    }

    public static List<PlayerInfoObject> serialize(List<WrapperPlayServerPlayerInfoUpdate.PlayerInfo> playerInfo) {
        List<PlayerInfoObject> list = new ArrayList<>();
        for (WrapperPlayServerPlayerInfoUpdate.PlayerInfo info : playerInfo) {
            list.add(new PlayerInfoObject(info.getGameProfile(), info.getLatency(), info.getGameMode(), MiniMessage.miniMessage().serialize(info.getDisplayName()!=null?info.getDisplayName():Component.text(""))));
        }
        return list;
    }

    public static List<WrapperPlayServerPlayerInfoUpdate.PlayerInfo> deserialize(List<PlayerInfoObject> playerInfo) {
        List<WrapperPlayServerPlayerInfoUpdate.PlayerInfo> list = new ArrayList<>();
        for (PlayerInfoObject info : playerInfo) {
            list.add(new WrapperPlayServerPlayerInfoUpdate.PlayerInfo(info.gameProfile(), true, info.ping(), info.gameMode(), MiniMessage.miniMessage().deserialize(info.displayName()), null));
        }
        return list;
    }
}
