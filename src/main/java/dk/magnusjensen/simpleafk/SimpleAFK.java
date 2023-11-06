/*
 * Copyright (C) 2023  legenden
 * https://github.com/MagnusHJensen/simpleafk
 *
 *  This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version.
 *
 *  This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License for more details.
 */

package dk.magnusjensen.simpleafk;

import dk.magnusjensen.simpleafk.config.ServerConfig;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.event.ServerChatEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.InterModComms;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.event.lifecycle.InterModEnqueueEvent;
import net.minecraftforge.fml.event.lifecycle.InterModProcessEvent;
import net.minecraftforge.event.server.ServerStartingEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.stream.Collectors;

@Mod(SimpleAFK.MOD_ID)
@Mod.EventBusSubscriber(modid = SimpleAFK.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class SimpleAFK
{
    // Directly reference a slf4j logger
    private static final Logger LOGGER = LogManager.getLogger();
    public static final String MOD_ID = "simpleafk";

    public SimpleAFK() {
        // Register ourselves for server and other game events we are interested in
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();
        modEventBus.register(this);
        MinecraftForge.EVENT_BUS.register(this);

        ModLoadingContext.get().registerConfig(ModConfig.Type.SERVER, ServerConfig.SPEC);
    }

    @SubscribeEvent
    public static void onPlayerTick(TickEvent.PlayerTickEvent event) {
        if (event.player.getLevel().isClientSide()) return;
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
