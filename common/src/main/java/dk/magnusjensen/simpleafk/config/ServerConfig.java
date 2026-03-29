/*
 * Copyright (C) 2023  legenden
 * https://github.com/MagnusHJensen/simpleafk
 *
 *  This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version.
 *
 *  This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License for more details.
 */

package dk.magnusjensen.simpleafk.config;


import com.electronwill.nightconfig.core.CommentedConfig;
import net.neoforged.neoforge.common.ModConfigSpec;
import org.apache.commons.lang3.tuple.Pair;


public class ServerConfig
{

    public static final ServerConfig CONFIG;
    public static final ModConfigSpec CONFIG_SPEC;

    public ModConfigSpec.ConfigValue<Integer> secondsBeforeAfk;
    public ModConfigSpec.ConfigValue<Integer> secondsBeforeKick;
    public ModConfigSpec.ConfigValue<String> isNowAfkMessage;
    public ModConfigSpec.ConfigValue<Boolean> isNowAfkMessageEnabled;
    public ModConfigSpec.ConfigValue<String> isNoLongerAfkMessage;
    public ModConfigSpec.ConfigValue<Boolean> isNoLongerAfkMessageEnabled;
    public ModConfigSpec.ConfigValue<String> playerNameFormat;
    public ModConfigSpec.ConfigValue<String> afkKickMessage;
    public ModConfigSpec.ConfigValue<Boolean> includeExemptPlayersInSleepVote;

    private ServerConfig(ModConfigSpec.Builder builder) {
        secondsBeforeAfk = builder
            .comment("How many seconds before the player is marked as AFK")
            .defineInRange("secondsBeforeAfk", 300, 5, Integer.MAX_VALUE);
        secondsBeforeKick = builder
            .comment("How many seconds before the player is kicked after being marked as AFK", "Set to 0 to disable kicking")
            .defineInRange("secondsBeforeKick", 300, 0, Integer.MAX_VALUE);
        isNowAfkMessage = builder
            .comment("How the message should be displayed when a player is marked as AFK", "Can be edited with the regular minecraft chat codes which can be found here https://minecraft.fandom.com/wiki/Formatting_codes#Color_codes", "$player will be replaced with the actual player name", "$player is required to appear in the text")
            .define("isNowAfkMessage", "§e§o$player is now AFK", (obj) -> obj instanceof String && obj.toString().contains("$player"));

        isNowAfkMessageEnabled = builder
            .comment("Whether or not the player is now AFK message should be sent to all players.", "Note: It will always be sent to the player that has gone AFK.")
            .define("isNowAfkMessageEnabled", true);
        isNoLongerAfkMessage = builder
            .comment("How the message should be displayed when a player is no longer marked as AFK", "Can be edited with the regular minecraft chat codes which can be found here https://minecraft.fandom.com/wiki/Formatting_codes#Color_codes", "$player will be replaced with the actual player name", "$player is required to appear in the text")
            .define("isNoLongerAfkMessage", "§e§o$player is no longer AFK", (obj) -> obj instanceof String && obj.toString().contains("$player"));

        isNoLongerAfkMessageEnabled = builder
            .comment("Whether or not the player is no longer AFK message should be sent to all players.", "Note: It will always be sent to the player that is no longer AFK.")
            .define("isNoLongerAfkMessageEnabled", true);
        playerNameFormat = builder
            .comment("How the player name should appear when a player is AFK (includes tab list and nametag above player)", "Can be edited with the regular minecraft chat codes which can be found here https://minecraft.fandom.com/wiki/Formatting_codes#Color_codes", "$player will be replaced with the actual player name", "$player is required to appear in the text")
            .define("playerNameFormat", "§7[AFK] §r$player", (obj) -> obj instanceof String && obj.toString().contains("$player"));
        afkKickMessage = builder
            .comment("The message that will be sent to the player after being kicked for being AFK too long.", "Can be edited with the regular minecraft chat codes which can be found here https://minecraft.fandom.com/wiki/Formatting_codes#Color_codes")
            .define("afkKickMessage", "You have been kicked for being AFK too long.", (obj) -> obj instanceof String);

        includeExemptPlayersInSleepVote = builder
            .comment("Whether exempt players should be included in the sleep vote.")
            .define("includeExemptPlayersInSleepVote", false);
    }

    //CONFIG and CONFIG_SPEC are both built from the same builder, so we use a static block to seperate the properties
    static {
        Pair<ServerConfig, ModConfigSpec> pair =
            new ModConfigSpec.Builder().configure(ServerConfig::new);

        //Store the resulting values
        CONFIG = pair.getLeft();
        CONFIG_SPEC = pair.getRight();
    }

    public void onModConfigEvent(CommentedConfig config) {
        secondsBeforeAfk.set(config.get("secondsBeforeAfk"));
        secondsBeforeKick.set(config.get("secondsBeforeKick"));
        isNowAfkMessage.set(config.get("isNowAfkMessage"));
        isNowAfkMessageEnabled.set(config.get("isNowAfkMessageEnabled"));
        isNoLongerAfkMessage.set(config.get("isNoLongerAfkMessage"));
        isNoLongerAfkMessageEnabled.set(config.get("isNoLongerAfkMessageEnabled"));
        playerNameFormat.set(config.get("playerNameFormat"));
        afkKickMessage.set(config.get("afkKickMessage"));
        includeExemptPlayersInSleepVote.set(config.get("includeExemptPlayersInSleepVote"));
    }
}
