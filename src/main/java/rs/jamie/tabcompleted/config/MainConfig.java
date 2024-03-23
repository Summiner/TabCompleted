package rs.jamie.tabcompleted.config;

import net.kyori.adventure.text.Component;
import space.arim.dazzleconf.annote.ConfComments;
import space.arim.dazzleconf.annote.ConfKey;

import static space.arim.dazzleconf.sorter.AnnotationBasedSorter.Order;
import static space.arim.dazzleconf.annote.ConfDefault.*;

public interface MainConfig {
    @ConfKey("header")
    @Order(1)
    @ConfComments("The top text of the tab menu")
    @DefaultString("<newline><rainbow>Your Server!</rainbow><newline>")
    Component header();

    @ConfKey("footer")
    @Order(2)
    @ConfComments("The bottom text of the tab menu")
    @DefaultString("<newline><gray>Ping: <white>%player_ping%<newline>")
    Component footer();

    @ConfKey("player-name")
    @Order(3)
    @ConfComments("Format for the player's tab display name, works with PAPI")
    @DefaultString("%luckperms_prefix%%player_name%")
    Component playerName();
}