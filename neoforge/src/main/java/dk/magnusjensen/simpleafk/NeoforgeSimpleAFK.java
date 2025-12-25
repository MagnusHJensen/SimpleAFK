package dk.magnusjensen.simpleafk;

import dk.magnusjensen.simpleafk.commands.AFKCommands;
import dk.magnusjensen.simpleafk.config.ServerConfig;
import dk.magnusjensen.simpleafk.utils.Utilities;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.fml.event.config.ModConfigEvent;
import net.neoforged.neoforge.event.RegisterCommandsEvent;
import net.neoforged.neoforge.event.ServerChatEvent;
import net.neoforged.neoforge.event.entity.player.AttackEntityEvent;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;
import net.neoforged.neoforge.event.entity.player.PlayerInteractEvent;
import net.neoforged.neoforge.event.tick.PlayerTickEvent;

@Mod(Constants.MOD_ID)
@EventBusSubscriber(modid = Constants.MOD_ID, bus = EventBusSubscriber.Bus.GAME)
public class NeoforgeSimpleAFK {
    
    public NeoforgeSimpleAFK(IEventBus modEventBus, ModContainer modContainer) {

        CommonClass.init();
        modEventBus.addListener(this::onConfigUpdates);

        modContainer.registerConfig(ModConfig.Type.SERVER, ServerConfig.SPEC);
    }

    private void onConfigUpdates(final ModConfigEvent event) {
        ServerConfig.onModConfigEvent();
    }


    @SubscribeEvent
    public static void onPlayerTick(PlayerTickEvent.Post event) {
        if (event.getEntity().level().isClientSide()) return;
        AFKManager.getInstance().getPlayer(event.getEntity().getUUID()).tick((ServerPlayer) event.getEntity());
    }

    @SubscribeEvent
    public static void onPlayerLeftClickBlock(PlayerInteractEvent.LeftClickBlock event) {
        Utilities.removeAfkStatusFromPlayer(event.getEntity());
    }

    @SubscribeEvent
    public static void onPlayerLeftClickEmpty(PlayerInteractEvent.LeftClickEmpty event) {
        Utilities.removeAfkStatusFromPlayer(event.getEntity());
    }

    @SubscribeEvent
    public static void onPlayerRightClickEmpty(PlayerInteractEvent.RightClickEmpty event) {
        Utilities.removeAfkStatusFromPlayer(event.getEntity());
    }

    @SubscribeEvent
    public static void onPlayerRightClickBlock(PlayerInteractEvent.RightClickBlock event) {
        Utilities.removeAfkStatusFromPlayer(event.getEntity());

    }

    @SubscribeEvent
    public static void onPlayerRightClickItem(PlayerInteractEvent.RightClickItem event) {
        Utilities.removeAfkStatusFromPlayer(event.getEntity());
    }

    @SubscribeEvent
    public static void onPlayerInteractEntity(PlayerInteractEvent.EntityInteract event) {
        Utilities.removeAfkStatusFromPlayer(event.getEntity());
    }

    @SubscribeEvent
    public static void onPlayerAttackEntity(AttackEntityEvent event) {
        Utilities.removeAfkStatusFromPlayer(event.getEntity());
    }

    @SubscribeEvent
    public static void onPlayerLeave(PlayerEvent.PlayerLoggedOutEvent event) {
        AFKManager.getInstance().removePlayer(event.getEntity().getUUID());
    }

    @SubscribeEvent
    public static void onPlayerJoin(PlayerEvent.PlayerLoggedInEvent event) {
        AFKManager.getInstance().addPlayer((ServerPlayer) event.getEntity());
    }

    @SubscribeEvent
    public static void onPlayerMessage(ServerChatEvent event) {
        Utilities.removeAfkStatusFromPlayer(event.getPlayer());
    }

    @SubscribeEvent
    public static void onTabListDecorate(PlayerEvent.TabListNameFormat event) {
        AFKManager manager = AFKManager.getInstance();
        AFKPlayer player = manager.getPlayer(event.getEntity().getUUID());
        if (player != null && player.isAfk()) {
            Component name = event.getDisplayName() != null ? event.getDisplayName() : Component.literal(event.getEntity().getScoreboardName());
            event.setDisplayName(Utilities.formatMessageWithComponent(ServerConfig.playerNameFormat, "player", name));
        }
    }

    @SubscribeEvent
    public static void onRegisterCommands(RegisterCommandsEvent event) {
        event.getDispatcher().register(AFKCommands.register());
    }
}