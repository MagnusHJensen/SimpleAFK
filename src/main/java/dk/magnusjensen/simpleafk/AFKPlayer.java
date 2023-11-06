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
import dk.magnusjensen.simpleafk.utils.Permissions;
import dk.magnusjensen.simpleafk.utils.Utilities;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.server.level.ServerPlayer;

public class AFKPlayer {
    private final ServerPlayer player;
    private boolean isAfk;
    private long timestampSinceAfk;
    private long timesstampSinceLastMove;
    private BlockPos lastPosition;


    public AFKPlayer(ServerPlayer player) {
        this.player = player;
        this.isAfk = false;
        this.timestampSinceAfk = System.currentTimeMillis() / 1000;
        this.timesstampSinceLastMove = System.currentTimeMillis() / 1000;
        this.lastPosition = null;
    }

    /**
     * Checks for movement every 5 ticks (0,25 seconds).
     * Checks for no movement every 20 ticks (1 second).
     *
     * @param player
     */
    public void tick(ServerPlayer player) {
        if (Utilities.hasPermission(player, Permissions.BYPASS_AFK)) return; // SKip if the player has the bypass permission.

        if (hasPlayerMoved(player.blockPosition())) {
            if (isAfk) {
                removeAfkStatus();
            } else {
                move(player.blockPosition());
            }
        } else if (player.getLevel().getGameTime() % 20 == 0) {
            long timestampInSeconds = System.currentTimeMillis() / 1000;
            // Check if the player is not marked as AFK, and if the player has not moved for the amount of seconds specified in the config.
            if (!isAfk && timestampInSeconds - timesstampSinceLastMove >= ServerConfig.secondsBeforeAfk) {
                setAfkStatus();
            } else if (
                ServerConfig.secondsBeforeKick > 0 &&
                    isAfk &&
                    timestampInSeconds - timestampSinceAfk >= ServerConfig.secondsBeforeKick
            ) {
                player.connection.disconnect(new TextComponent(ServerConfig.afkKickMessage));
            }
        }

    }

    public void toggleAfkStatus() {
        if (isAfk) {
            removeAfkStatus();
        } else {
            setAfkStatus();
        }
    }

    private void setAfkStatus() {
        this.isAfk = true;
        this.timestampSinceAfk = System.currentTimeMillis() / 1000;
        move(player.blockPosition());
        this.player.refreshDisplayName();
        this.player.refreshTabListName();
        dk.magnusjensen.simpleafk.utils.Utilities.broadcastSystemMessage(Utilities.formatMessageWithPlayerName(ServerConfig.isNowAfkMessage, player.getDisplayName().getString()));
    }

    private void removeAfkStatus() {
        this.isAfk = false;
        this.timestampSinceAfk = System.currentTimeMillis() / 1000;
        this.timesstampSinceLastMove = System.currentTimeMillis() / 1000;
        move(player.blockPosition());
        this.player.refreshDisplayName();
        this.player.refreshTabListName();
        Utilities.broadcastSystemMessage(Utilities.formatMessageWithPlayerName(ServerConfig.isNoLongerAfkMessage, player.getDisplayName().getString()));
    }

    private boolean hasPlayerMoved(BlockPos currentPos) {
        return !currentPos.equals(lastPosition);
    }

    private void move(BlockPos pos) {
        this.lastPosition = pos;
        this.timesstampSinceLastMove = System.currentTimeMillis() / 1000;
    }

    public ServerPlayer getPlayer() {
        return player;
    }

    /**
     * This does not mean that the player is AFK, to ensure the player is AFK check the isAfk.
     */
    public long getSecondsSinceAfk() {
        return (System.currentTimeMillis() / 1000) - timestampSinceAfk;
    }

    public long getSecondsSinceLastMove() {
        return (System.currentTimeMillis() / 1000) - timesstampSinceLastMove;
    }

    public boolean isAfk() {
        return isAfk;
    }

    public BlockPos getLastPosition() {
        return lastPosition;
    }
}
