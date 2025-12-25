package dk.magnusjensen.simpleafk.platform.services;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;

public interface IPermissionHelper {
    boolean hasPermission(ServerPlayer player, ResourceLocation permission);
}
