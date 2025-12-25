package dk.magnusjensen.simpleafk.platform;

import dk.magnusjensen.simpleafk.ForgePermissions;
import dk.magnusjensen.simpleafk.platform.services.IPermissionHelper;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.server.permission.PermissionAPI;

public class ForgePermissionHelper implements IPermissionHelper {
    @Override
    public boolean hasPermission(ServerPlayer player, ResourceLocation permission) {
        var node = ForgePermissions.PERMISSION_NODES.get(permission);
        if (node == null) {
            return false;
        }

        return PermissionAPI.getPermission(player, node);
    }
}
