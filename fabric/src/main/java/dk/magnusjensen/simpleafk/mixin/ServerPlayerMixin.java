package dk.magnusjensen.simpleafk.mixin;

import com.mojang.authlib.GameProfile;
import dk.magnusjensen.simpleafk.AFKManager;
import dk.magnusjensen.simpleafk.AFKPlayer;
import dk.magnusjensen.simpleafk.config.ServerConfig;
import dk.magnusjensen.simpleafk.utils.Utilities;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ServerPlayer.class)
public abstract class ServerPlayerMixin extends Player {


    public ServerPlayerMixin(Level level, GameProfile gameProfile) {
        super(level, gameProfile);
    }

    @Inject(at = @org.spongepowered.asm.mixin.injection.At("HEAD"), method = "getTabListDisplayName", cancellable = true)
    public void getTabListDisplayName(CallbackInfoReturnable<Component> cir) {
        AFKManager manager = AFKManager.getInstance();
        AFKPlayer afkPlayer = manager.getPlayer(this.getUUID());
        if (afkPlayer != null && afkPlayer.isAfk()) {
            Component name = this.getDisplayName();
            cir.setReturnValue(Utilities.formatMessageWithComponent(ServerConfig.CONFIG.playerNameFormat.get(), "player", name));
        }
    }
}
