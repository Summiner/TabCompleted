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

    @ConfKey("multi-server.enabled")
    @Order(9)
    @ConfComments("Should we enable multi-server support, this means that every connected server will update tab users between each other. all config options are still independent excluding the player's prefix and suffix?")
    @ConfDefault.DefaultBoolean(false)
    Boolean multiserverEnabled();

    @ConfKey("multi-server.name")
    @Order(10)
    @ConfComments("Unique identifier for this node on your mutli-server network")
    @DefaultString("TabCompleted-Server-1")
    String multiserverName();

    @ConfKey("multi-server.channel")
    @Order(11)
    @ConfComments("What channel should we listen on? This MUST be the same for all servers you want to be linked, you can use different channels on the same network but players will be seperated by channels")
    @DefaultString("TabCompleted-Channel-1")
    String multiserverChannel();

    @ConfKey("multi-server.ip")
    @Order(12)
    @ConfComments("Ip used to connect to the redis server")
    @DefaultString("192.168.0.233")
    String multiserverIP();

    @ConfKey("multi-server.port")
    @Order(13)
    @ConfComments("Port used to connect to the redis server")
    @DefaultInteger(20050)
    Integer multiserverPort();

    @ConfKey("multi-server.password")
    @Order(14)
    @ConfComments("Password used to connect to the redis server")
    @DefaultString("abc123")
    String multiserverPassword();

    @ConfKey("misc.collision")
    @Order(15)
    @ConfComments("Should player collision be enabled?")
    @ConfDefault.DefaultBoolean(true)
    Boolean miscCollision();
}