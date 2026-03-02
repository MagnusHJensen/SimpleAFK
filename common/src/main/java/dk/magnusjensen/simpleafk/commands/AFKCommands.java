/*
 * Copyright (C) 2023  legenden
 * https://github.com/MagnusHJensen/simpleafk
 *
 *  This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version.
 *
 *  This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License for more details.
 */

package dk.magnusjensen.simpleafk.commands;

import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import dk.magnusjensen.simpleafk.AFKData;
import dk.magnusjensen.simpleafk.AFKManager;
import dk.magnusjensen.simpleafk.AFKPlayer;
import dk.magnusjensen.simpleafk.utils.Permissions;
import dk.magnusjensen.simpleafk.utils.Utilities;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.players.PlayerList;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class AFKCommands {
    public static LiteralArgumentBuilder<CommandSourceStack> register() {
        return Commands.literal("afk")
            .then(Commands.literal("toggle")
                .requires(ctx -> hasPermission(ctx, Permissions.TOGGLE))
                .then(Commands.argument("player", EntityArgument.player())
                    .requires(ctx -> hasPermission(ctx, Permissions.TOGGLE_OTHER))
                    .executes(ctx -> toggleAfkStatus(ctx.getSource().getPlayerOrException(), EntityArgument.getPlayer(ctx, "player")))
                )
                .executes(ctx -> toggleAfkStatus(ctx.getSource().getPlayerOrException()))
            )
            .then(Commands.literal("bypass-list")
                .requires(ctx -> hasPermission(ctx, Permissions.MODIFY_BYPASS))
                .then(Commands.literal("add")
                    .then(Commands.argument("player", EntityArgument.player())
                        .executes(ctx -> addExemptPlayer(ctx.getSource(), EntityArgument.getPlayer(ctx, "player")))
                    )
                )
                .then(Commands.literal("remove")
                    .then(Commands.argument("player", EntityArgument.player())
                        .executes(ctx -> removeExemptPlayer(ctx.getSource(), EntityArgument.getPlayer(ctx, "player")))
                    )
                )
                .executes(ctx -> {
                    PlayerList list = ctx.getSource().getServer().getPlayerList();
                    List<UUID> uuids = AFKData.get(ctx.getSource().getServer()).getExemptPlayers();

                    List<String> players = new ArrayList<>();
                    for (UUID uuid : uuids) {
                        ServerPlayer player = list.getPlayer(uuid);
                        if (player == null) {
                            continue;
                        }
                        players.add(player.getName().getString());
                    }

                    ctx.getSource().sendSuccess(() -> Component.literal("AFK Bypass List: " + players), false);
                    return 1;
                })
            )
            .executes(ctx -> toggleAfkStatus(ctx.getSource().getPlayerOrException())
            );
    }

    private static int toggleAfkStatus(ServerPlayer self) {
        AFKManager.getInstance().getPlayer(self.getUUID()).toggleAfkStatus();
        return 1;
    }

    private static int toggleAfkStatus(ServerPlayer executor, ServerPlayer target) {
        AFKPlayer player = AFKManager.getInstance().getPlayer(target.getUUID());
        player.toggleAfkStatus();
        executor.sendSystemMessage(Component.literal("Toggled AFK status for " + target.getScoreboardName()), false);
        return 1;
    }

    private static int addExemptPlayer(CommandSourceStack ctx, ServerPlayer player) {
        AFKData.get(ctx.getServer()).addExemptPlayer(player.getUUID());
        ctx.sendSuccess(() -> Component.literal("Added " + player.getScoreboardName() + " to the AFK Bypass List"), false);
        return 1;
    }

    private static int removeExemptPlayer(CommandSourceStack ctx, ServerPlayer player) {
        AFKData data = AFKData.get(ctx.getServer());

        if (data.isPlayerExempt(player.getUUID())) {
            data.removeExemptPlayer(player.getUUID());
            ctx.sendSuccess(() -> Component.literal("Removed " + player.getScoreboardName() + " from the AFK Bypass List"), false);
            return 1;
        }

        ctx.sendSystemMessage(Component.literal(player.getScoreboardName() + " is not on the AFK Bypass List"));
        return 0;
    }

    public static boolean hasPermission(CommandSourceStack ctx, Identifier node) {
        try {
            ServerPlayer player = ctx.getPlayerOrException();
            return Utilities.hasPermission(player, node);
        } catch (CommandSyntaxException e) {
            return false;
        }
    }

}
