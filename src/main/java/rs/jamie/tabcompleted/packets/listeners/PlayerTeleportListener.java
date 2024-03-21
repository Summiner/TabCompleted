package rs.jamie.tabcompleted.packets.listeners;

import com.github.retrooper.packetevents.event.PacketListenerPriority;
import com.github.retrooper.packetevents.event.SimplePacketListenerAbstract;
import com.github.retrooper.packetevents.event.simple.*;
import com.github.retrooper.packetevents.protocol.packettype.PacketType;
import com.github.retrooper.packetevents.protocol.player.User;
import rs.jamie.tabcompleted.config.ConfigManager;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

public class PlayerTeleportListener extends SimplePacketListenerAbstract {

    private final ConfigManager config;

    public PlayerTeleportListener(ConfigManager config) {
        super(PacketListenerPriority.NORMAL);
        this.config = config;
    }

    @Override
    public void onPacketConfigReceive(PacketConfigReceiveEvent event) {
        if (event.getPacketType() != PacketType.Configuration.Client.CONFIGURATION_END_ACK) return;
        sendTab(event.getUser());
    }

    public CompletableFuture<Void> sendTab(User user) {
        return CompletableFuture.runAsync(() -> {

        }, CompletableFuture.delayedExecutor(1L, TimeUnit.SECONDS));
    }
}
