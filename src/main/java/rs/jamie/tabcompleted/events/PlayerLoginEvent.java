package rs.jamie.tabcompleted.events;

import com.github.retrooper.packetevents.PacketEvents;
import com.github.retrooper.packetevents.protocol.player.GameMode;
import com.github.retrooper.packetevents.protocol.player.User;
import com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerPlayerInfoUpdate;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import rs.jamie.tabcompleted.config.ConfigManager;
import rs.jamie.tabcompleted.utils.PapiUtil;
import rs.jamie.tabcompleted.utils.TabUtil;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;

import static com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerPlayerInfoUpdate.Action.UPDATE_DISPLAY_NAME;

public class PlayerLoginEvent implements Listener {

    private final ConfigManager config;

    public PlayerLoginEvent(ConfigManager config) {
        this.config = config;
    }

    @EventHandler
    public void onPlayerLoginReceive(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        TabUtil.updateTab(player, PapiUtil.set(LegacyComponentSerializer.legacyAmpersand(), player, config.getConfig().header()), PapiUtil.set(LegacyComponentSerializer.legacyAmpersand(), player, config.getConfig().footer()));
    }

}
