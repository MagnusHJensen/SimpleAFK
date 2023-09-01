/*
 * Copyright (C) 2023  legenden
 * https://github.com/MagnusHJensen/simpleafk
 *
 *  This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version.
 *
 *  This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License for more details.
 */

package dk.magnusjensen.simpleafk;

import com.mojang.logging.LogUtils;
import dk.magnusjensen.simpleafk.commands.AFKCommands;
import dk.magnusjensen.simpleafk.config.ServerConfig;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.PlayerChatMessage;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.event.ServerChatEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.slf4j.Logger;

import static dk.magnusjensen.simpleafk.utils.Utilities.formatMessageWithComponent;

@Mod(SimpleAFK.MODID)
@Mod.EventBusSubscriber(modid = SimpleAFK.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class SimpleAFK
{
    public static final String MODID = "simpleafk";
    private static final Logger LOGGER = LogUtils.getLogger();


    public SimpleAFK() {
        // Register ourselves for server and other game events we are interested in
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();
        modEventBus.register(this);
        MinecraftForge.EVENT_BUS.register(this);

        ModLoadingContext.get().registerConfig(ModConfig.Type.SERVER, ServerConfig.SPEC);
    }

    @SubscribeEvent
    public static void onPlayerTick(TickEvent.PlayerTickEvent event) {
        if (event.player.level().isClientSide()) return;
        AFKManager.getInstance().getPlayer(event.player.getUUID()).tick((ServerPlayer) event.player);
    }

    @SubscribeEvent
    public static void onPlayerLeave(PlayerEvent.PlayerLoggedOutEvent event) {
        AFKManager.getInstance().removePlayer(event.getEntity().getUUID());
    }

    @SubscribeEvent
    public static void onPlayerJoin(PlayerEvent.PlayerLoggedInEvent event) {
        AFKManager.getInstance().addPlayer((ServerPlayer) event.getEntity());
    }

    @SubscribeEvent
    public static void onPlayerMessage(ServerChatEvent event) {
        AFKManager manager = AFKManager.getInstance();
        AFKPlayer player = manager.getPlayer(event.getPlayer().getUUID());
        if (player != null && player.isAfk()) {
            player.toggleAfkStatus();
        }
    }

    @SubscribeEvent
    public static void onTabListDecorate(PlayerEvent.TabListNameFormat event) {
        AFKManager manager = AFKManager.getInstance();
        AFKPlayer player = manager.getPlayer(event.getEntity().getUUID());
        if (player != null && player.isAfk()) {
            Component name = event.getDisplayName() != null ? event.getDisplayName() : Component.literal(event.getEntity().getScoreboardName());
            event.setDisplayName(formatMessageWithComponent(ServerConfig.playerNameFormat, "player", name));
        }
    }

    @SubscribeEvent
    public static void onRegisterCommands(RegisterCommandsEvent event) {
        event.getDispatcher().register(AFKCommands.register());
    }


}
