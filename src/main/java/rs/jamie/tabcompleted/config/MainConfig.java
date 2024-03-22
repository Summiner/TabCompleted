package rs.jamie.tabcompleted.config;

import net.kyori.adventure.text.Component;
import space.arim.dazzleconf.annote.ConfComments;
import space.arim.dazzleconf.annote.ConfKey;

import static space.arim.dazzleconf.annote.ConfDefault.*;

public interface MainConfig {
    @ConfKey("header")
    @ConfComments("The top text of the tab menu")
    @DefaultString("<newline><rainbow><bold>Your Server!</rainbow><newline>")
    Component header();

    @ConfKey("footer")
    @ConfComments("The bottom text of the tab menu")
    @DefaultString("<newline><grey>Ping: <white>%ping%<newline>")
    Component footer();

    @ConfKey("player-name")
    @ConfComments("Format for the player's tab display name, works with PAPI")
    @DefaultString("%nick_rank% %player_name%")
    Component playerName();
}