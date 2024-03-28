package rs.jamie.tabcompleted.redis;

import com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerPlayerInfoUpdate;
import com.google.common.reflect.TypeToken;
import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;
import redis.clients.jedis.DefaultJedisClientConfig;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPubSub;
import rs.jamie.tabcompleted.config.ConfigManager;
import rs.jamie.tabcompleted.utils.PapiUtil;
import rs.jamie.tabcompleted.utils.TabUtil;

import java.lang.reflect.Type;
import java.util.EnumSet;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;
import java.util.logging.Logger;

import static com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerPlayerInfoUpdate.Action.*;
import static com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerPlayerInfoUpdate.Action.UPDATE_DISPLAY_NAME;

public class RedisManager {


    private final ConfigManager config;
    private final Gson gson;
    private Jedis publisher;
    private Jedis subscriber;

    public RedisManager(Plugin plugin, ConfigManager config) {
        Logger logger = plugin.getLogger();
        this.config = config;
        gson = new Gson();
        CompletableFuture.supplyAsync(() -> {
            try {
                DefaultJedisClientConfig clientConfig = DefaultJedisClientConfig.builder().password(config.getConfig().multiserverPassword()).build();
                publisher = new Jedis(config.getConfig().multiserverIP(), config.getConfig().multiserverPort(), clientConfig);
                subscriber = new Jedis(config.getConfig().multiserverIP(), config.getConfig().multiserverPort(), clientConfig);
            } catch (Exception e) {
                e.printStackTrace();
            }
            load(logger);
            return null;
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
                    if (!Objects.equals(packetObject.getServer(), config.getConfig().multiserverName())) {
                        if(packetObject.getType()==InfoType.UPDATE) {
                            Bukkit.getOnlinePlayers().forEach((player) -> {
                                WrapperPlayServerPlayerInfoUpdate wrapper = new WrapperPlayServerPlayerInfoUpdate(EnumSet.of(ADD_PLAYER, INITIALIZE_CHAT, UPDATE_GAME_MODE, UPDATE_LISTED, UPDATE_LATENCY, UPDATE_DISPLAY_NAME), RedisInfoObject.deserialize(packetObject.getObjects()));
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
