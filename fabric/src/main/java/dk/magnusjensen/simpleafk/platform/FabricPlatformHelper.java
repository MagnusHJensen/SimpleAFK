package dk.magnusjensen.simpleafk.platform;

import dk.magnusjensen.simpleafk.platform.services.IPlatformHelper;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.network.protocol.game.ClientboundPlayerInfoUpdatePacket;
import net.minecraft.server.level.ServerPlayer;

import static net.minecraft.network.protocol.game.ClientboundPlayerInfoUpdatePacket.Action;

public class FabricPlatformHelper implements IPlatformHelper {

    @Override
    public String getPlatformName() {
        return "Fabric";
    }

    @Override
    public boolean isModLoaded(String modId) {

        return FabricLoader.getInstance().isModLoaded(modId);
    }

    @Override
    public boolean isDevelopmentEnvironment() {

        return FabricLoader.getInstance().isDevelopmentEnvironment();
    }

    @Override
    public void refreshTabListName(ServerPlayer player) {
        player.getServer().getPlayerList().broadcastAll(new ClientboundPlayerInfoUpdatePacket(Action.UPDATE_DISPLAY_NAME, player));
    }
}
