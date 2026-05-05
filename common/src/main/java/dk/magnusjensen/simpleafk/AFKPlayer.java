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
import dk.magnusjensen.simpleafk.platform.Services;
import dk.magnusjensen.simpleafk.utils.Permissions;
import dk.magnusjensen.simpleafk.utils.Utilities;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.phys.Vec3;

public class AFKPlayer {
    private final ServerPlayer player;
    private boolean isAfk;
    private long timestampSinceAfk;
    private long timestampSinceLastMove;
    private long timestampSinceLastLook;
    private BlockPos lastPosition;
    private Vec3 lastLookAngle;


    public AFKPlayer(ServerPlayer player) {
        this.player = player;
        this.isAfk = false;
        this.timestampSinceAfk = System.currentTimeMillis() / 1000;
        this.timestampSinceLastMove = System.currentTimeMillis() / 1000;
        this.timestampSinceLastLook = System.currentTimeMillis() / 1000;
        this.lastPosition = null;
    }

    /**
     * Checks for movement every 5 ticks (0,25 seconds).
     * Checks for no movement every 20 ticks (1 second).
     *
     * @param player
     */
    public void tick(ServerPlayer player) {
        if (Utilities.hasPermission(player, Permissions.BYPASS_AFK) || player.isSleeping()) return; // SKip if the player has the bypass permission.

        if ((hasPlayerLookedAround(player) || hasPlayerMoved(player)) && isAfk) {
            removeAfkStatus();
        }

        move(player);
        lookAround(player);

        if (player.level().getGameTime() % 20 == 0) {
            long timestampInSeconds = System.currentTimeMillis() / 1000;
            if (checkIfShouldBeAfk(timestampInSeconds)) {
                setAfkStatus();
            } else if (checkIfShouldBeKicked(timestampInSeconds)) {
                kickPlayer();
            }
        }
    }

    /**
     * Check if the player should be AFK, based on movement and current AFK status.
     * 
     * @param timestampInSeconds Current timestamp in seconds
     * @return True if the player should be AFK, false otherwise
     */
    public boolean checkIfShouldBeAfk(long timestampInSeconds) {
        // Check if the player is not marked as AFK, and if the player has not moved for the amount of seconds specified in the config.
        boolean isMoveAfkFactor = timestampInSeconds - timestampSinceLastMove >= ServerConfig.secondsBeforeAfk;
        boolean isLookAfkFactor = timestampInSeconds - timestampSinceLastLook >= ServerConfig.secondsBeforeAfk;

        boolean afkFactors = isMoveAfkFactor && isLookAfkFactor;

        return !isAfk && afkFactors;
    }

    /**
     * Check if the player should be kicked, based on movement and current AFK status.
     * 
     * @param timestampInSeconds Current timestamp in seconds
     * @return True if the player should be kicked, false otherwise
     */
    public boolean checkIfShouldBeKicked(long timestampInSeconds) {
        boolean isAfkKickFactor = timestampInSeconds - timestampSinceAfk >= ServerConfig.secondsBeforeKick;

        return ServerConfig.secondsBeforeKick > 0 && isAfk && isAfkKickFactor;
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
        lookAround(player);
        move(player);
        Services.PLATFORM.refreshTabListName(this.player);

        if (ServerConfig.isNowAfkMessageEnabled) {
            Utilities.broadcastSystemMessage(Utilities.formatMessageWithPlayerName(ServerConfig.isNowAfkMessage, player.getDisplayName().getString()), player.getServer());
        } else {
            player.sendSystemMessage(Utilities.formatMessageWithPlayerName(ServerConfig.isNowAfkMessage, player.getDisplayName().getString()), false);
        }
    }

    public void removeAfkStatus() {
        if (!isAfk) return;

        this.isAfk = false;
        this.timestampSinceAfk = System.currentTimeMillis() / 1000;
        resetMovement();
        lookAround(player);
        move(player);
        Services.PLATFORM.refreshTabListName(this.player);

        if (ServerConfig.isNoLongerAfkMessageEnabled) {
            Utilities.broadcastSystemMessage(Utilities.formatMessageWithPlayerName(ServerConfig.isNoLongerAfkMessage, player.getDisplayName().getString()), player.getServer());
        } else {
            player.sendSystemMessage(Utilities.formatMessageWithPlayerName(ServerConfig.isNoLongerAfkMessage, player.getDisplayName().getString()), false);
        }
    }

    public void resetMovement() {
        long currentTimestamp = System.currentTimeMillis() / 1000;
        this.timestampSinceLastMove = currentTimestamp;
        this.timestampSinceLastLook = currentTimestamp;
    }

    public void kickPlayer() {
        player.connection.disconnect(Component.literal(ServerConfig.afkKickMessage));
    }

    private boolean hasPlayerMoved(ServerPlayer player) {
        return !player.blockPosition().equals(getLastPosition());
    }

    private boolean hasPlayerLookedAround(ServerPlayer player) {
        return !player.getLookAngle().equals(this.lastLookAngle);
    }

    private void lookAround(ServerPlayer player) {
        if (hasPlayerLookedAround(player) && !isAfk) {
            this.timestampSinceLastLook = System.currentTimeMillis() / 1000;
            this.lastLookAngle = player.getLookAngle();
        }
    }

    private void move(ServerPlayer player) {
        if (hasPlayerMoved(player) && !isAfk) {
            this.timestampSinceLastMove = System.currentTimeMillis() / 1000;
            this.lastPosition = player.blockPosition();
        }
    }

    public ServerPlayer getPlayer() {
        return player;
    }

    public boolean isAfk() {
        return isAfk;
    }

    public boolean bypassesSleep() {
        return isAfk() || Utilities.hasPermission(player, Permissions.BYPASS_SLEEP) || AFKData.get(player.server).isPlayerExempt(player.getUUID());
    }

    public BlockPos getLastPosition() {
        return lastPosition;
    }
}
