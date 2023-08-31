package dk.magnusjensen.simpleafk.utils;


import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.server.ServerLifecycleHooks;
import net.minecraftforge.server.permission.PermissionAPI;
import net.minecraftforge.server.permission.nodes.PermissionNode;

public class Utilities {
    public static Component formatMessageWithPlayerName(String message, String playerName) {
        return Component.literal(message.replace("$player", playerName));
    }

    public static Component formatMessageWithComponent(String message, String replace, Component component) {
        return Component.literal(message.replace("$" + replace, component.getString()));
    }

    public static void broadcastSystemMessage(Component message) {
        ServerLifecycleHooks.getCurrentServer().getPlayerList().broadcastSystemMessage(message, false);
    }

    public static boolean hasPermission(ServerPlayer player, PermissionNode<Boolean> node) {
        return PermissionAPI.getPermission(player, node);
    }
}
