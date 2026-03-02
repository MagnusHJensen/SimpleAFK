/*
 * Copyright (C) 2024  legenden
 * https://github.com/MagnusHJensen/simpleafk
 *
 *  This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version.
 *
 *  This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License for more details.
 */

package dk.magnusjensen.simpleafk;

import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.UUIDUtil;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.level.saveddata.SavedData;
import net.minecraft.world.level.saveddata.SavedDataType;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

public class AFKData extends SavedData {

    public static final SavedDataType<AFKData> ID = new SavedDataType<>(
        "simpleafk",
        AFKData::new,
        RecordCodecBuilder.create(instance -> instance.group(
            UUIDUtil.CODEC_SET.fieldOf("exemptPlayers").forGetter(data -> data.exemptPlayers)
        ).apply(instance, AFKData::new)),
        null
    );

    /**
     * List of players that are exempt from being marked as AFK
     */
    private final Set<UUID> exemptPlayers;

    public AFKData() {
        this.exemptPlayers = new HashSet<>();
    }

    public AFKData(Set<UUID> exemptPlayers) {
        this.exemptPlayers = exemptPlayers;
    }

    public boolean isPlayerExempt(UUID player) {
        return exemptPlayers.contains(player);
    }

    public void addExemptPlayer(UUID player) {
        exemptPlayers.add(player);
        setDirty();
    }

    public void removeExemptPlayer(UUID player) {
        exemptPlayers.remove(player);
        setDirty();
    }

    public List<UUID> getExemptPlayers() {
        return List.copyOf(exemptPlayers);
    }


    public static AFKData get(MinecraftServer server) {
        return server.overworld().getDataStorage().computeIfAbsent(ID);
    }
}
