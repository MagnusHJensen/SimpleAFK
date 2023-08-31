package dk.magnusjensen.simpleafk.commands;

import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import dk.magnusjensen.simpleafk.AFKManager;
import dk.magnusjensen.simpleafk.AFKPlayer;
import dk.magnusjensen.simpleafk.utils.Permissions;
import dk.magnusjensen.simpleafk.utils.Utilities;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.server.permission.PermissionAPI;
import net.minecraftforge.server.permission.nodes.PermissionNode;

import static dk.magnusjensen.simpleafk.utils.Utilities.hasPermission;

public class AFKCommands {
    public static LiteralArgumentBuilder<CommandSourceStack> register() {
        return Commands.literal("afk")
            .then(Commands.literal("toggle")
                .requires(ctx -> hasPermission(ctx, Permissions.TOGGLE))
                .then(Commands.argument("player", EntityArgument.player())
                    .requires(ctx -> hasPermission(ctx, Permissions.TOGGLE_OTHER))
                    .executes(ctx -> toggleAfkStatus(ctx.getSource().getPlayerOrException(), EntityArgument.getPlayer(ctx, "player")))
                )
                .executes(ctx -> toggleAfkStatus(ctx.getSource().getPlayerOrException()))

            )
            .executes(ctx -> toggleAfkStatus(ctx.getSource().getPlayerOrException())
        );
    }

    private static int toggleAfkStatus(ServerPlayer self) {
        AFKManager.getInstance().getPlayer(self.getUUID()).toggleAfkStatus();
        return 1;
    }

    private static int toggleAfkStatus(ServerPlayer executor, ServerPlayer target) {
        AFKPlayer player = AFKManager.getInstance().getPlayer(target.getUUID());
        player.toggleAfkStatus();
        executor.sendSystemMessage(Component.literal("Toggled AFK status for " + target.getScoreboardName()), false);
        return 1;
    }

    public static boolean hasPermission(CommandSourceStack ctx, PermissionNode<Boolean> node) {
        try {
            ServerPlayer player = ctx.getPlayerOrException();
            return Utilities.hasPermission(player, node);
        } catch (CommandSyntaxException e) {
            return false;
        }
    }

}
