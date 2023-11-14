/*
 * Copyright (C) 2023  legenden
 * https://github.com/MagnusHJensen/simpleafk
 *
 *  This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version.
 *
 *  This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License for more details.
 */

package dk.magnusjensen.simpleafk.utils;

import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.server.ServerLifecycleHooks;
import net.minecraftforge.server.permission.PermissionAPI;
import net.minecraftforge.server.permission.nodes.PermissionNode;

public class Utilities {
    public static Component formatMessageWithPlayerName(String message, String playerName) {
        return Component.literal(message.replace("$player", playerName));
    }

    public static Component formatMessageWithComponent(String message, String replace, Component component) {
        return Component.literal(message.replace("$" + replace, component.getString()));
    }

    public static void broadcastSystemMessage(Component message) {
        ServerLifecycleHooks.getCurrentServer().getPlayerList().broadcastSystemMessage(message, false);
    }

    public static boolean hasPermission(ServerPlayer player, PermissionNode<Boolean> node) {
        return PermissionAPI.getPermission(player, node);
    }
}
