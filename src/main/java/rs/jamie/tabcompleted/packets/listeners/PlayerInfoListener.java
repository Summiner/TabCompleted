package rs.jamie.tabcompleted.packets.listeners;

import com.github.retrooper.packetevents.PacketEvents;
import com.github.retrooper.packetevents.event.PacketListenerPriority;
import com.github.retrooper.packetevents.event.SimplePacketListenerAbstract;
import com.github.retrooper.packetevents.event.simple.*;
import com.github.retrooper.packetevents.protocol.packettype.PacketType;
import com.github.retrooper.packetevents.protocol.player.GameMode;
import com.github.retrooper.packetevents.protocol.player.User;
import com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerPlayerInfo;
import com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerPlayerInfoUpdate;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import rs.jamie.tabcompleted.config.ConfigManager;
import rs.jamie.tabcompleted.utils.PapiUtil;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

import static com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerPlayerInfoUpdate.Action.UPDATE_DISPLAY_NAME;

public class PlayerInfoListener extends SimplePacketListenerAbstract {

    private final ConfigManager config;

    public PlayerInfoListener(ConfigManager config) {
        super(PacketListenerPriority.NORMAL);
        this.config = config;
    }

    @Override
    public void onPacketPlaySend(PacketPlaySendEvent event) {
        if (event.getPacketType() != PacketType.Play.Server.PLAYER_INFO_UPDATE) return;
        WrapperPlayServerPlayerInfoUpdate wrapper = new WrapperPlayServerPlayerInfoUpdate(event);
        WrapperPlayServerPlayerInfoUpdate wrapper2 = new WrapperPlayServerPlayerInfoUpdate(wrapper.getActions(), wrapper.getEntries());
        List<WrapperPlayServerPlayerInfoUpdate.PlayerInfo> playerInfo = new ArrayList<>();
        wrapper2.getEntries().forEach((playerData) -> {
            Player player = Bukkit.getPlayer(playerData.getGameProfile().getUUID());
            if(player==null) return;
            playerData.setDisplayName(PapiUtil.set(LegacyComponentSerializer.legacyAmpersand(), player, config.getConfig().playerName()));
            playerInfo.add(playerData);
        });
        wrapper2.setEntries(playerInfo);
        event.getUser().sendPacketSilently(wrapper2);
        event.setCancelled(true);
    }

}
