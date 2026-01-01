package dk.magnusjensen.simpleafk;

import dk.magnusjensen.simpleafk.commands.AFKCommands;
import dk.magnusjensen.simpleafk.config.ServerConfig;
import dk.magnusjensen.simpleafk.utils.Utilities;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.event.ServerChatEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.player.AttackEntityEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.config.ModConfigEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@Mod(Constants.MOD_ID)
@Mod.EventBusSubscriber(modid = Constants.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class ForgeSimpleAFK {
    
    public ForgeSimpleAFK() {

        CommonClass.init();
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();
        modEventBus.addListener(this::onConfigUpdates);

        ModLoadingContext.get().registerConfig(ModConfig.Type.SERVER, ServerConfig.SPEC);
    }

    private void onConfigUpdates(final ModConfigEvent event) {
        ServerConfig.onModConfigEvent(event.getConfig());
    }


    @SubscribeEvent
    public static void onPlayerTick(TickEvent.PlayerTickEvent event) {
        if (event.player.level().isClientSide()) return;
        var player = AFKManager.getInstance().getPlayer(event.player.getUUID());
        if (player == null) return;
        player.tick((ServerPlayer) event.player);
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