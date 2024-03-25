package rs.jamie.tabcompleted.packets;

import com.github.retrooper.packetevents.PacketEvents;
import com.github.retrooper.packetevents.event.PacketListenerCommon;
import io.github.retrooper.packetevents.factory.spigot.SpigotPacketEventsBuilder;
import org.bukkit.plugin.Plugin;
import rs.jamie.tabcompleted.config.ConfigManager;
import rs.jamie.tabcompleted.packets.listeners.PlayerInfoListener;

import java.util.HashMap;
import java.util.concurrent.CompletableFuture;

public class PacketManager {

    private final ConfigManager config;
    private final HashMap<String, PacketListenerCommon> listeners = new HashMap<>();

    public PacketManager(Plugin plugin, ConfigManager config) {
        this.config = config;
        PacketEvents.setAPI(SpigotPacketEventsBuilder.build(plugin));
        PacketEvents.getAPI().getSettings()
                .reEncodeByDefault(false)
                .checkForUpdates(true)
                .bStats(true);
        PacketEvents.getAPI().load();
        reload();
    }

    public void unload() {
        listeners.forEach((key,value) -> PacketEvents.getAPI().getEventManager().unregisterListener(value));
        listeners.clear();
    }

    public CompletableFuture<Void> reload() {
        return CompletableFuture.runAsync(() -> {
            unload();
            listeners.put("PlayerInfo", new PlayerInfoListener(config));
            listeners.forEach((key,value) -> PacketEvents.getAPI().getEventManager().registerListener(value));
        });
    }

}
