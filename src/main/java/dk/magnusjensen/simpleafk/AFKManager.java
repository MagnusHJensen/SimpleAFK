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
