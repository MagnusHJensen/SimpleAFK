package dk.magnusjensen.simpleafk;

import dk.magnusjensen.simpleafk.commands.AFKCommands;
import dk.magnusjensen.simpleafk.config.ServerConfig;
import dk.magnusjensen.simpleafk.utils.Utilities;
import fuzs.forgeconfigapiport.fabric.api.neoforge.v4.NeoForgeConfigRegistry;
import fuzs.forgeconfigapiport.fabric.api.neoforge.v4.NeoForgeModConfigEvents;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.fabricmc.fabric.api.event.player.*;
import net.fabricmc.fabric.api.message.v1.ServerMessageEvents;
import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.neoforged.fml.config.ModConfig;

public class FabricSimpleAFK implements ModInitializer {
    
    @Override
    public void onInitialize() {

        CommonClass.init();

        CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) -> dispatcher.register(AFKCommands.register()));

        // Register to all mod config events for our server config
        NeoForgeConfigRegistry.INSTANCE.register(Constants.MOD_ID, ModConfig.Type.SERVER, ServerConfig.SPEC);
        NeoForgeModConfigEvents.reloading(Constants.MOD_ID).register(config -> ServerConfig.onModConfigEvent());
        NeoForgeModConfigEvents.loading(Constants.MOD_ID).register(config -> ServerConfig.onModConfigEvent());
        NeoForgeModConfigEvents.unloading(Constants.MOD_ID).register(config -> ServerConfig.onModConfigEvent());

        // Interaction events
        ServerTickEvents.END_SERVER_TICK.register(server -> {
            server.getPlayerList().getPlayers().forEach(player -> {
                var afkPlayer = AFKManager.getInstance().getPlayer(player.getUUID());
                if (afkPlayer == null) return;
                afkPlayer.tick(player);
            });
        });

        AttackBlockCallback.EVENT.register((player, world, hand, pos, direction) -> {
            Utilities.removeAfkStatusFromPlayer(player);
            return InteractionResult.PASS;
        });

        AttackEntityCallback.EVENT.register((player, world, hand, entity, hitResult) -> {
            Utilities.removeAfkStatusFromPlayer(player);
            return InteractionResult.PASS;
        });

        UseBlockCallback.EVENT.register((player, world, hand, hitResult) -> {
            Utilities.removeAfkStatusFromPlayer(player);
            return InteractionResult.PASS;
        });

        UseItemCallback.EVENT.register((player, world, hand) -> {
            Utilities.removeAfkStatusFromPlayer(player);
            return InteractionResultHolder.pass(player.getItemInHand(hand));
        });

        UseEntityCallback.EVENT.register((player, world, hand, entity, hitResult) -> {
            Utilities.removeAfkStatusFromPlayer(player);
            return InteractionResult.PASS;
        });

        ServerPlayConnectionEvents.JOIN.register((handler, sender, server) -> {
            AFKManager.getInstance().addPlayer(handler.getPlayer());
        });

        ServerPlayConnectionEvents.DISCONNECT.register((handler, server) -> {
            AFKManager.getInstance().removePlayer(handler.getPlayer().getUUID());
        });

        ServerMessageEvents.CHAT_MESSAGE.register((message, sender, params) -> {
            Utilities.removeAfkStatusFromPlayer(sender);
        });
    }
}
