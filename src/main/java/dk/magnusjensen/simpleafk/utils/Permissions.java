/*
 * Copyright (C) 2023  legenden
 * https://github.com/MagnusHJensen/simpleafk
 *
 *  This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version.
 *
 *  This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License for more details.
 */

package dk.magnusjensen.simpleafk.utils;

import dk.magnusjensen.simpleafk.SimpleAFK;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.server.ServerLifecycleHooks;
import net.minecraftforge.server.permission.events.PermissionGatherEvent;
import net.minecraftforge.server.permission.nodes.PermissionNode;
import net.minecraftforge.server.permission.nodes.PermissionTypes;

@Mod.EventBusSubscriber(modid = SimpleAFK.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class Permissions {
    public static final PermissionNode<Boolean> TOGGLE = new PermissionNode<>(SimpleAFK.MOD_ID, "toggle", PermissionTypes.BOOLEAN, (player, playerUUID, context) -> true);
    public static final PermissionNode<Boolean> TOGGLE_OTHER = new PermissionNode<>(SimpleAFK.MOD_ID, "toggle.target", PermissionTypes.BOOLEAN, (player, playerUUID, context) -> isOp(player));
    public static final PermissionNode<Boolean> BYPASS_AFK = new PermissionNode<>(SimpleAFK.MOD_ID, "bypass", PermissionTypes.BOOLEAN, (player, playerUUID, context) -> isOp(player));


    private static boolean isOp(ServerPlayer player) {
        return ServerLifecycleHooks.getCurrentServer().getPlayerList().isOp(player.getGameProfile());
    }

    @SubscribeEvent
    public static void onPermissionGather(PermissionGatherEvent.Nodes event) {
        event.addNodes(TOGGLE, TOGGLE_OTHER, BYPASS_AFK);
    }
}
