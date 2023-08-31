package dk.magnusjensen.simpleafk.utils;

import dk.magnusjensen.simpleafk.SimpleAFK;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.server.ServerLifecycleHooks;
import net.minecraftforge.server.permission.events.PermissionGatherEvent;
import net.minecraftforge.server.permission.nodes.PermissionNode;
import net.minecraftforge.server.permission.nodes.PermissionTypes;

@Mod.EventBusSubscriber(modid = SimpleAFK.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class Permissions {
    public static final PermissionNode<Boolean> TOGGLE = new PermissionNode<>(SimpleAFK.MODID, "toggle", PermissionTypes.BOOLEAN, (player, playerUUID, context) -> true);
    public static final PermissionNode<Boolean> TOGGLE_OTHER = new PermissionNode<>(SimpleAFK.MODID, "toggle.target", PermissionTypes.BOOLEAN, (player, playerUUID, context) -> isOp(player));
    public static final PermissionNode<Boolean> BYPASS_AFK = new PermissionNode<>(SimpleAFK.MODID, "bypass", PermissionTypes.BOOLEAN, (player, playerUUID, context) -> isOp(player));


    private static boolean isOp(ServerPlayer player) {
        return ServerLifecycleHooks.getCurrentServer().getPlayerList().isOp(player.getGameProfile());
    }

    @SubscribeEvent
    public static void onPermissionGather(PermissionGatherEvent.Nodes event) {
        event.addNodes(TOGGLE, TOGGLE_OTHER, BYPASS_AFK);
    }
}
