package rs.jamie.tabcompleted.utils;

import com.github.retrooper.packetevents.PacketEvents;
import com.github.retrooper.packetevents.protocol.player.User;
import com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerPlayerListHeaderAndFooter;
import net.kyori.adventure.text.Component;
import org.bukkit.entity.Player;

public class TabUtil {

    public static void updateTab(Player player, Component header, Component footer) {
        WrapperPlayServerPlayerListHeaderAndFooter wrapper = new WrapperPlayServerPlayerListHeaderAndFooter(header, footer);
        User user = PacketEvents.getAPI().getPlayerManager().getUser(player);
        if(user!=null) {
            user.sendPacket(wrapper);
        }
    }

}
