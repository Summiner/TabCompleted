package rs.jamie.tabcompleted.utils;

import com.github.retrooper.packetevents.PacketEvents;
import com.github.retrooper.packetevents.protocol.player.User;
import com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerPlayerInfoRemove;
import com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerPlayerInfoUpdate;
import com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerPlayerListHeaderAndFooter;
import net.kyori.adventure.text.Component;
import org.bukkit.entity.Player;

public class TabUtil {

    public static void updateTab(Player player, Component header, Component footer, WrapperPlayServerPlayerInfoUpdate playerInfo) {
        WrapperPlayServerPlayerListHeaderAndFooter wrapper = new WrapperPlayServerPlayerListHeaderAndFooter(header, footer);
        User user = PacketEvents.getAPI().getPlayerManager().getUser(player);
        if(user!=null) {
            user.sendPacket(wrapper);
            user.sendPacketSilently(playerInfo);
        }
    }

    public static void updateTab(Player player, Component header, Component footer, WrapperPlayServerPlayerInfoRemove playerInfo) {
        WrapperPlayServerPlayerListHeaderAndFooter wrapper = new WrapperPlayServerPlayerListHeaderAndFooter(header, footer);
        User user = PacketEvents.getAPI().getPlayerManager().getUser(player);
        if(user!=null) {
            user.sendPacket(wrapper);
            user.sendPacketSilently(playerInfo);
        }
    }

    public static void updateTab(Player player, Component header, Component footer) {
        WrapperPlayServerPlayerListHeaderAndFooter wrapper = new WrapperPlayServerPlayerListHeaderAndFooter(header, footer);
        User user = PacketEvents.getAPI().getPlayerManager().getUser(player);
        if(user!=null) {
            user.sendPacket(wrapper);
        }
    }

}
