package ae.skydoppler;

import ae.skydoppler.chat.ChatMatchHandler;
import ae.skydoppler.chat.command.SkydopplerCommand;
import ae.skydoppler.fishing.FishingHideState;
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

public class SkydopplerClient implements ClientModInitializer {

    public static MinecraftClient client = MinecraftClient.getInstance();

    public static KeyBinding debugKey;
    public static SkyblockPlayerDataStruct playerDataStruct;
    public static SkyblockIslandEnum currentIsland = SkyblockIslandEnum.NONE;
    public static Enum<?> currentZone = SkyblockIslandEnum.NONE.getZonesForIsland()[0]; // Sets currentZone to the first enum for the island of type "NONE", which is also "NONE" (the only value for the island of type "NONE").
    public static Enum<?> currentRegion = null;
    public static boolean isRodCast;
    public static boolean isPlayingSkyblock = false;
    private TextRenderer textRenderer;

    public static boolean hideExplosionParticle = true;

    @Override
    public void onInitializeClient() {

        System.out.println("Skydoppler (Client) is initializing!");
        textRenderer = new TextRenderer(client);
        textRenderer.initialize();
        ChatMatchHandler.loadJsonData();
        isRodCast = false;

        SkydopplerCommand.register();

        playerDataStruct = new SkyblockPlayerDataStruct();

        debugKey = KeyBindingHelper.registerKeyBinding(new KeyBinding("Debug Key", InputUtil.Type.KEYSYM, GLFW.GLFW_KEY_G, "Skydoppler"));

        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            while (debugKey.wasPressed()) {
                // add debug key stuff here if you need
            }

            // Check every tick if the local player's fishing bobber is present within hideRange
            if (client.world == null || client.player == null) {
                FishingHideState.rodCastActive = false;
                return;
            }
            // Create a bounding box centered around the player
            double r = FishingHideState.hideRange;
            Box box = new Box(
                    client.player.getX() - r, client.player.getY() - r, client.player.getZ() - r,
                    client.player.getX() + r, client.player.getY() + r, client.player.getZ() + r
            );

            // Check if any FishingBobberEntity in the world (within the box) belongs to the local player
            boolean found = client.world.getEntitiesByClass(FishingBobberEntity.class, box,
                    bobber -> bobber.getOwner() != null && bobber.getOwner().equals(client.player)
            ).size() > 0;

            FishingHideState.rodCastActive = found;
        });

    }


}