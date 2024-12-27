/*
 * Copyright (C) 2024  legenden
 * https://github.com/MagnusHJensen/simpleafk
 *
 *  This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version.
 *
 *  This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License for more details.
 */

package dk.magnusjensen.simpleafk;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.nbt.Tag;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.level.saveddata.SavedData;
import org.jetbrains.annotations.NotNull;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

public class AFKData extends SavedData {
    /**
     * List of players that are exempt from being marked as AFK
     */
    private final Set<UUID> exemptPlayers = new HashSet<>();

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


    @Override
    public @NotNull CompoundTag save(@NotNull CompoundTag compoundTag) {
        ListTag exemptPlayersTag = new ListTag();
        for (UUID player : exemptPlayers) {
            exemptPlayersTag.add(NbtUtils.createUUID(player));
        }

        compoundTag.put("exemptPlayers", exemptPlayersTag);
        return compoundTag;
    }

    public AFKData() {
    }

    public AFKData(CompoundTag tag) {
        ListTag exemptPlayersTag = tag.getList("exemptPlayers", 11);
        for (Tag value : exemptPlayersTag) {
            this.addExemptPlayer(NbtUtils.loadUUID(value));
        }
    }

    public static AFKData get(MinecraftServer server) {
        return server.overworld().getDataStorage().computeIfAbsent(AFKData::new, AFKData::new, "afk_data");
    }
}
