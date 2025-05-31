package ae.skydoppler;

import ae.skydoppler.chat.ChatMatchHandler;
import ae.skydoppler.command.SkydopplerCommandHandler;
import ae.skydoppler.config.SkydopplerConfig;
import ae.skydoppler.config.held_item_config.HeldItemConfigScreen;
import ae.skydoppler.dungeon.DungeonClientHandler;
import ae.skydoppler.skyblock_locations.SkyblockLocationEnum;
import ae.skydoppler.structs.SkyblockPlayerDataStruct;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandRegistrationCallback;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import net.minecraft.entity.projectile.FishingBobberEntity;
import net.minecraft.util.math.Box;
import org.lwjgl.glfw.GLFW;

import java.nio.file.Path;
import java.nio.file.Paths;

public class SkydopplerClient implements ClientModInitializer {

    public static final Path CONFIG_PATH = Paths.get(MinecraftClient.getInstance() // or use a proper run directory reference
            .runDirectory.getAbsolutePath(), "config", "skydoppler.json");
    public static MinecraftClient client = MinecraftClient.getInstance();
    public static KeyBinding debugKey;
    public static Boolean debugModeEnabled = true;
    public static SkyblockPlayerDataStruct playerDataStruct;
    public static DungeonClientHandler dungeonClientHandler;
    public static SkyblockLocationEnum currentIsland = SkyblockLocationEnum.NONE;
    public static Enum<?> currentZone = SkyblockLocationEnum.NONE.getZonesForIsland()[0];
    public static Enum<?> currentRegion = null;
    public static boolean isRodCast;
    public static boolean isPlayingSkyblock = false;
    public static SkydopplerConfig CONFIG;
    private TextRenderer textRenderer;

    @Override
    public void onInitializeClient() {

        if (SkydopplerClient.debugModeEnabled)
            System.out.println("Skydoppler (Client) is initializing!");

        // Load configuration on initialization
        CONFIG = SkydopplerConfig.load(CONFIG_PATH);

        ClientCommandRegistrationCallback.EVENT.register((dispatcher, registryAccess) -> {

            SkydopplerCommandHandler.registerCommands(dispatcher);

        });

        textRenderer = new TextRenderer(client);
        textRenderer.initialize();
        ChatMatchHandler.loadJsonData();
        isRodCast = false;

        playerDataStruct = new SkyblockPlayerDataStruct();

        if (SkydopplerClient.debugModeEnabled)
            debugKey = KeyBindingHelper.registerKeyBinding(new KeyBinding("Debug Key", InputUtil.Type.KEYSYM, GLFW.GLFW_KEY_G, "Skydoppler"));
        else
            debugKey = null;

        ClientTickEvents.END_CLIENT_TICK.register(client -> {

            if (client.player == null || client.world == null) return;

            while (debugKey.wasPressed()) {
                client.setScreen(new HeldItemConfigScreen(null));
            }

            if (client.world == null || client.player == null) {
                isRodCast = false;
                return;
            }

            double r = CONFIG.hidePlayersWhileFishingRange;
            Box box = new Box(
                    client.player.getX() - r, client.player.getY() - r, client.player.getZ() - r,
                    client.player.getX() + r, client.player.getY() + r, client.player.getZ() + r
            );

            isRodCast = !client.world.getEntitiesByClass(FishingBobberEntity.class, box,
                    bobber -> bobber.getOwner() != null && bobber.getOwner().equals(client.player)
            ).isEmpty();
        });
    }
}

