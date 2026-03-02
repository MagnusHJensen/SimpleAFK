/*
 * Copyright (C) 2023  legenden
 * https://github.com/MagnusHJensen/simpleafk
 *
 *  This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version.
 *
 *  This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License for more details.
 */

package dk.magnusjensen.simpleafk;

import dk.magnusjensen.simpleafk.utils.Permissions;
import net.minecraft.resources.Identifier;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.server.ServerLifecycleHooks;
import net.neoforged.neoforge.server.permission.events.PermissionGatherEvent;
import net.neoforged.neoforge.server.permission.nodes.PermissionDynamicContextKey;
import net.neoforged.neoforge.server.permission.nodes.PermissionNode;
import net.neoforged.neoforge.server.permission.nodes.PermissionTypes;

import java.util.HashMap;
import java.util.Map;

@EventBusSubscriber(modid = Constants.MOD_ID)
public class NeoforgePermissions {
    public static final PermissionNode<Boolean> TOGGLE = registerPermissionNode(Permissions.TOGGLE, (player, playerUUID, context) -> true);
    public static final PermissionNode<Boolean> TOGGLE_OTHER = registerPermissionNode(Permissions.TOGGLE_OTHER, (player, playerUUID, context) -> isOp(player));
    public static final PermissionNode<Boolean> BYPASS_AFK = registerPermissionNode(Permissions.BYPASS_AFK, (player, playerUUID, context) -> isOp(player) || isOnBypassList(player));
    public static final PermissionNode<Boolean> BYPASS_SLEEP = registerPermissionNode(Permissions.BYPASS_SLEEP, (player, playerUUID, context) -> isOp(player));
    public static final PermissionNode<Boolean> MODIFY_BYPASS = registerPermissionNode(Permissions.MODIFY_BYPASS, (player, playerUUID, context) -> isOp(player));

    public static Map<Identifier, PermissionNode<Boolean>> PERMISSION_NODES;

    private static PermissionNode<Boolean> registerPermissionNode(Identifier id, PermissionNode.PermissionResolver<Boolean> defaultResolver, PermissionDynamicContextKey... dynamics) {
        var node = new PermissionNode<>(
            id,
            PermissionTypes.BOOLEAN,
            defaultResolver,
            dynamics
        );

        if (PERMISSION_NODES == null) {
            PERMISSION_NODES = new HashMap<>();
        }
        PERMISSION_NODES.put(id, node);
        return node;
    }


    private static boolean isOp(ServerPlayer player) {
        return ServerLifecycleHooks.getCurrentServer().getPlayerList().isOp(player.nameAndId());
    }

    private static boolean isOnBypassList(ServerPlayer player) {
        return AFKData.get(player.level().getServer()).isPlayerExempt(player.getUUID());
    }

    @SubscribeEvent
    public static void onPermissionGather(PermissionGatherEvent.Nodes event) {
        event.addNodes(TOGGLE, TOGGLE_OTHER, BYPASS_AFK, BYPASS_SLEEP, MODIFY_BYPASS);
    }
}
