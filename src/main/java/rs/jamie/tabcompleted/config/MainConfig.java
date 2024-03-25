package rs.jamie.tabcompleted.config;

import net.kyori.adventure.text.Component;
import space.arim.dazzleconf.annote.ConfComments;
import space.arim.dazzleconf.annote.ConfDefault;
import space.arim.dazzleconf.annote.ConfKey;
import space.arim.dazzleconf.sorter.AnnotationBasedSorter;

import static space.arim.dazzleconf.sorter.AnnotationBasedSorter.Order;
import static space.arim.dazzleconf.annote.ConfDefault.*;

public interface MainConfig {
    @ConfKey("tablist.header")
    @Order(1)
    @ConfComments("The top text of the tab menu")
    @DefaultString("<newline><rainbow>Your Server!</rainbow><newline>")
    Component tablistHeader();

    @ConfKey("tablist.footer")
    @Order(2)
    @ConfComments("The bottom text of the tab menu")
    @DefaultString("<newline><gray>Ping: <white>%player_ping%<newline>")
    Component tablistFooter();

    @ConfKey("tablist.player-name")
    @Order(3)
    @ConfComments("Format for the player's tab display name, works with PAPI")
    @DefaultString("%luckperms_prefix%%player_name%")
    Component tablistPlayerName();

    @ConfKey("nametag.prefix")
    @Order(4)
    @ConfComments("Prefix that is added to the player's nametag")
    @DefaultString("%luckperms_prefix%")
    Component nametagPrefix();

    @ConfKey("nametag.suffix")
    @Order(5)
    @ConfComments("Suffix that is added to the player's nametag")
    @DefaultString("%luckperms_suffix%")
    Component nametagSuffix();

    @ConfKey("scoreboard.enabled")
    @Order(6)
    @ConfComments("Should the scoreboard feature of this plugin be enabled?")
    @ConfDefault.DefaultBoolean(true)
    Boolean scoreboardEnabled();

    @ConfKey("scoreboard.name")
    @Order(7)
    @ConfComments("Scoreboard name that is displayed for the player")
    @DefaultString("<rainbow>Your Server!</rainbow>")
    Component scoreboardName();

    @ConfKey("scoreboard.entries")
    @Order(8)
    @ConfComments("Scoreboard entries to be displayed for the player")
    @ConfDefault.DefaultStrings({"<gray>Ping: <white>%player_ping%", "<red>Test"})
    Component[] scoreboardEntries();

    @ConfKey("misc.collision")
    @Order(9)
    @ConfComments("Should player collision be enabled?")
    @ConfDefault.DefaultBoolean(true)
    Boolean miscCollision();
}