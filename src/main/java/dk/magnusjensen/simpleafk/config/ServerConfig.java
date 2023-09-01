/*
 * Copyright (C) 2023  legenden
 * https://github.com/MagnusHJensen/simpleafk
 *
 *  This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version.
 *
 *  This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License for more details.
 */

package dk.magnusjensen.simpleafk.config;

import dk.magnusjensen.simpleafk.SimpleAFK;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.config.ModConfigEvent;

@Mod.EventBusSubscriber(modid = SimpleAFK.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ServerConfig
{
    private static final ForgeConfigSpec.Builder BUILDER = new ForgeConfigSpec.Builder();

    private static final ForgeConfigSpec.IntValue SECONDS_BEFORE_AFK = BUILDER
        .comment("How many seconds before the player is marked as AFK")
        .defineInRange("secondsBeforeAfk", 300, 5, Integer.MAX_VALUE);
    private static final ForgeConfigSpec.IntValue SECONDS_BEFORE_KICK = BUILDER
        .comment("How many seconds before the player is kicked after being marked as AFK", "Set to 0 to disable kicking")
        .defineInRange("secondsBeforeKick", 300, 0, Integer.MAX_VALUE);
    private static final ForgeConfigSpec.ConfigValue<String> IS_NOW_AFK_MESSAGE = BUILDER
        .comment("How the message should be displayed when a player is marked as AFK", "Can be edited with the regular minecraft chat codes which can be found here https://minecraft.fandom.com/wiki/Formatting_codes#Color_codes", "$player will be replaced with the actual player name", "$player is required to appear in the text")
        .define("isNowAfkMessage", "§e§o$player is now AFK", (obj) -> obj instanceof String && obj.toString().contains("$player"));
    private static final ForgeConfigSpec.ConfigValue<String> IS_NO_LONGER_AFK_MESSAGE = BUILDER
        .comment("How the message should be displayed when a player is no longer marked as AFK", "Can be edited with the regular minecraft chat codes which can be found here https://minecraft.fandom.com/wiki/Formatting_codes#Color_codes", "$player will be replaced with the actual player name", "$player is required to appear in the text")
        .define("isNoLongerAfkMessage", "§e§o$player is no longer AFK", (obj) -> obj instanceof String && obj.toString().contains("$player"));
    private static final ForgeConfigSpec.ConfigValue<String> PLAYER_NAME_FORMAT = BUILDER
        .comment("How the player name should appear when a player is AFK (includes tab list and nametag above player)", "Can be edited with the regular minecraft chat codes which can be found here https://minecraft.fandom.com/wiki/Formatting_codes#Color_codes", "$player will be replaced with the actual player name", "$player is required to appear in the text")
        .define("playerNameFormat", "§7[AFK] §r$player", (obj) -> obj instanceof String && obj.toString().contains("$player"));
    private static final ForgeConfigSpec.ConfigValue<String> AFK_KICK_MESSAGE = BUILDER
        .comment("The message that will be sent to the player after being kicked for being AFK too long.", "Can be edited with the regular minecraft chat codes which can be found here https://minecraft.fandom.com/wiki/Formatting_codes#Color_codes")
        .define("afkKickMessage", "§1You have been kicked for being AFK too long.", (obj) -> obj instanceof String);
    public static final ForgeConfigSpec SPEC = BUILDER.build();

    public static int secondsBeforeAfk;
    public static int secondsBeforeKick;
    public static String isNowAfkMessage;
    public static String isNoLongerAfkMessage;
    public static String playerNameFormat;
    public static String afkKickMessage;


    @SubscribeEvent
    static void onLoad(final ModConfigEvent event)
    {
        secondsBeforeAfk = SECONDS_BEFORE_AFK.get();
        secondsBeforeKick = SECONDS_BEFORE_KICK.get();
        isNowAfkMessage = IS_NOW_AFK_MESSAGE.get();
        isNoLongerAfkMessage = IS_NO_LONGER_AFK_MESSAGE.get();
        playerNameFormat = PLAYER_NAME_FORMAT.get();
        afkKickMessage = AFK_KICK_MESSAGE.get();
    }
}
