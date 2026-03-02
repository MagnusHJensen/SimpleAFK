package dk.magnusjensen.simpleafk.platform;

import dk.magnusjensen.simpleafk.NeoforgePermissions;
import dk.magnusjensen.simpleafk.platform.services.IPermissionHelper;
import net.minecraft.resources.Identifier;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.neoforge.server.permission.PermissionAPI;

public class NeoforgePermissionHelper implements IPermissionHelper {
    @Override
    public boolean hasPermission(ServerPlayer player, Identifier permission) {
        var node = NeoforgePermissions.PERMISSION_NODES.get(permission);
        if (node == null) {
            return false;
        }

        return PermissionAPI.getPermission(player, node);
    }
}
