/*
 * Copyright (C) 2023  legenden
 * https://github.com/MagnusHJensen/simpleafk
 *
 *  This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version.
 *
 *  This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License for more details.
 */

package dk.magnusjensen.simpleafk;

import net.minecraft.server.level.ServerPlayer;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class AFKManager {
    private static AFKManager INSTANCE;
    private final Map<UUID, AFKPlayer> players;

    public AFKManager() {
        this.players = new HashMap<>();
    }

    public AFKPlayer getPlayer(UUID uuid) {
        return players.get(uuid);
    }

    public void addPlayer(ServerPlayer player) {
        if (!players.containsKey(player.getUUID())) {
            players.put(player.getUUID(), new AFKPlayer(player));
        }
    }

    public void removePlayer(UUID player) {
        players.remove(player);
    }


    public static AFKManager getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new AFKManager();
        }

        return INSTANCE;
    }
}