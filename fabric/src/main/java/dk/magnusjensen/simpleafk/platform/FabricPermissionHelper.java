package dk.magnusjensen.simpleafk.platform;

import dk.magnusjensen.simpleafk.AFKData;
import dk.magnusjensen.simpleafk.platform.services.IPermissionHelper;
import dk.magnusjensen.simpleafk.utils.Permissions;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;

public class FabricPermissionHelper implements IPermissionHelper {
    @Override
    public boolean hasPermission(ServerPlayer player, ResourceLocation permission) {
        if (permission.equals(Permissions.TOGGLE)) {
            return true;
        } else if (permission.equals(Permissions.TOGGLE_OTHER)) {
            return isOp(player);
        } else if (permission.equals(Permissions.BYPASS_AFK)) {
            return isOnBypassList(player) || isOp(player);
        } else if (permission.equals(Permissions.BYPASS_SLEEP)) {
            return isOp(player);
        } else if (permission.equals(Permissions.MODIFY_BYPASS)) {
            return isOp(player);
        }
        return false;
    }


    private static boolean isOp(ServerPlayer player) {
        return player.getServer().getPlayerList().isOp(player.getGameProfile());
    }

    private static boolean isOnBypassList(ServerPlayer player) {
        return AFKData.get(player.server).isPlayerExempt(player.getUUID());
    }
}
