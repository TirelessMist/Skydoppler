package ae.skydoppler;

import ae.skydoppler.chat.ChatMatchHandler;
import ae.skydoppler.config.SkydopplerConfig;
import ae.skydoppler.dungeon.map.DungeonMapHandler;
import ae.skydoppler.skyblock_locations.SkyblockIslandEnum;
import ae.skydoppler.structs.SkyblockPlayerDataStruct;
import net.fabricmc.api.ClientModInitializer;
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

    public static MinecraftClient client = MinecraftClient.getInstance();

    public static KeyBinding debugKey;
    public static SkyblockPlayerDataStruct playerDataStruct;
    public static SkyblockIslandEnum currentIsland = SkyblockIslandEnum.NONE;
    public static Enum<?> currentZone = SkyblockIslandEnum.NONE.getZonesForIsland()[0];
    public static Enum<?> currentRegion = null;
    public static boolean isRodCast;
    public static boolean isPlayingSkyblock = false;
    public static boolean hideExplosionParticle = true;
    public static DungeonMapHandler dungeonMapHandler;
    public static int currentDungeonFloor;
    private TextRenderer textRenderer;

    public static SkydopplerConfig CONFIG;
    public static final Path CONFIG_PATH = Paths.get(MinecraftClient.getInstance() // or use a proper run directory reference
            .runDirectory.getAbsolutePath(), "config", "skydoppler.json");

    @Override
    public void onInitializeClient() {

        System.out.println("Skydoppler (Client) is initializing!");

        // Load configuration on initialization
        CONFIG = SkydopplerConfig.load(CONFIG_PATH);


        textRenderer = new TextRenderer(client);
        textRenderer.initialize();
        ChatMatchHandler.loadJsonData();
        isRodCast = false;

        dungeonMapHandler = new DungeonMapHandler();

        playerDataStruct = new SkyblockPlayerDataStruct();

        debugKey = KeyBindingHelper.registerKeyBinding(new KeyBinding("Debug Key", InputUtil.Type.KEYSYM, GLFW.GLFW_KEY_G, "Skydoppler"));

        ClientTickEvents.END_CLIENT_TICK.register(client -> {

            if (client.player == null || client.world == null) return;

            while (debugKey.wasPressed()) {
                // add debug key stuff here if you need
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