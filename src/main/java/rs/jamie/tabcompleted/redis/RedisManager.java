package rs.jamie.tabcompleted.redis;

import com.github.retrooper.packetevents.PacketEvents;
import com.github.retrooper.packetevents.manager.player.PlayerManager;
import com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerPlayerInfoRemove;
import com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerPlayerInfoUpdate;
import com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerTeams;
import com.google.common.reflect.TypeToken;
import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;
import redis.clients.jedis.DefaultJedisClientConfig;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPubSub;
import rs.jamie.tabcompleted.config.ConfigManager;
import rs.jamie.tabcompleted.tasks.TeamUpdateTask;
import rs.jamie.tabcompleted.utils.LuckPermsUtil;
import rs.jamie.tabcompleted.utils.PapiUtil;
import rs.jamie.tabcompleted.utils.TabUtil;
import rs.jamie.tabcompleted.utils.TeamUtil;

import java.lang.reflect.Type;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.logging.Logger;

import static com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerPlayerInfoUpdate.Action.*;
import static com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerPlayerInfoUpdate.Action.UPDATE_DISPLAY_NAME;
import static rs.jamie.tabcompleted.utils.TeamUtil.lastCrossServerTeam;

public class RedisManager {


    private final ConfigManager config;
    private final PlayerManager playerManager;
    private final Gson gson;
    private Jedis publisher;
    private Jedis subscriber;

    public RedisManager(Plugin plugin, ConfigManager config) {
        Logger logger = plugin.getLogger();
        this.config = config;
        playerManager = PacketEvents.getAPI().getPlayerManager();
        gson = new Gson();
        CompletableFuture.runAsync(() -> {
            try {
                DefaultJedisClientConfig clientConfig = DefaultJedisClientConfig.builder().password(config.getConfig().multiserverPassword()).build();
                publisher = new Jedis(config.getConfig().multiserverIP(), config.getConfig().multiserverPort(), clientConfig);
                subscriber = new Jedis(config.getConfig().multiserverIP(), config.getConfig().multiserverPort(), clientConfig);
            } catch (Exception e) {
                e.printStackTrace();
            }
            load(logger);
        });
    }

    public boolean isUsable() {
        return publisher!=null&&subscriber!=null;
    }

    public void sendPacket(RedisInfoObject packet) {
        publisher.publish(config.getConfig().multiserverChannel(), serialize(packet));
    }

    public void load(Logger logger) {
        subscriber.subscribe(new JedisPubSub() {
            @Override
            public void onMessage(String channel, String message) {
                try {
                    RedisInfoObject packetObject = deserialize(message);
                    // TODO: add method to detect if a user provided by another server already exists and ignore sending their information
                    if (!Objects.equals(packetObject.getServer(), config.getConfig().multiserverName())) {
                        if(packetObject.getType()==InfoType.REMOVE) {
                            PlayerInfoObject info = packetObject.getObjects().get(0);
                            UUID uuid = info.gameProfile().getUUID();
                            WrapperPlayServerPlayerInfoRemove wrapper = new WrapperPlayServerPlayerInfoRemove(uuid);
                            lastCrossServerTeam.remove(uuid);
                            Bukkit.getOnlinePlayers().forEach((player) -> {
                                playerManager.sendPacket(player, wrapper);
                            });
                        }
                        if(packetObject.getType()==InfoType.UPDATE) {
                            List<WrapperPlayServerPlayerInfoUpdate.PlayerInfo> playerInfo = new ArrayList<>();
                            List<WrapperPlayServerTeams> packets = new ArrayList<>();
                            WrapperPlayServerTeams.CollisionRule collision = config.getConfig().miscCollision()? WrapperPlayServerTeams.CollisionRule.ALWAYS: WrapperPlayServerTeams.CollisionRule.NEVER;
                            for (PlayerInfoObject info : packetObject.getObjects()) {
                                String team = info.team();
                                playerInfo.add(new WrapperPlayServerPlayerInfoUpdate.PlayerInfo(info.gameProfile(), true, info.ping(), info.gameMode(), MiniMessage.miniMessage().deserialize(info.displayName()), null));
                                if(team!=null) {
                                    String lastTeam = lastCrossServerTeam.get(info.gameProfile().getUUID());
                                    if(lastTeam!=null) {
                                        packets.add(new WrapperPlayServerTeams(lastTeam, WrapperPlayServerTeams.TeamMode.REMOVE, (WrapperPlayServerTeams.ScoreBoardTeamInfo) null, info.gameProfile().getName()));
                                    }
                                    WrapperPlayServerTeams.ScoreBoardTeamInfo teamInfo = new WrapperPlayServerTeams.ScoreBoardTeamInfo(Component.text(""), Component.text(""), Component.text(""),
                                            WrapperPlayServerTeams.NameTagVisibility.NEVER, collision, NamedTextColor.WHITE, WrapperPlayServerTeams.OptionData.NONE);
                                    packets.add(new WrapperPlayServerTeams(team, WrapperPlayServerTeams.TeamMode.CREATE, teamInfo, info.gameProfile().getName()));
                                    lastCrossServerTeam.put(info.gameProfile().getUUID(), team);
                                }
                            }
                            Bukkit.getOnlinePlayers().forEach((player) -> {
                                packets.forEach((packet) -> {
                                    playerManager.sendPacketSilently(player, packet);
                                });
                                WrapperPlayServerPlayerInfoUpdate wrapper = new WrapperPlayServerPlayerInfoUpdate(EnumSet.of(ADD_PLAYER, INITIALIZE_CHAT, UPDATE_GAME_MODE, UPDATE_LISTED, UPDATE_LATENCY, UPDATE_DISPLAY_NAME), playerInfo);
                                TabUtil.updateTab(player, PapiUtil.set(LegacyComponentSerializer.legacyAmpersand(), player, config.getConfig().tablistHeader()), PapiUtil.set(LegacyComponentSerializer.legacyAmpersand(), player, config.getConfig().tablistFooter()), wrapper);
                            });
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }, config.getConfig().multiserverChannel());
        System.out.println("Something fucking broke lmao");
    }

    private String serialize(RedisInfoObject object) {
        return gson.toJson(object);
    }

    private RedisInfoObject deserialize(String json) {
        Type type = new TypeToken<RedisInfoObject>(){}.getType();
        return new Gson().fromJson(json, type);
    }
}
