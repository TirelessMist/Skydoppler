package ae.skydoppler;

import ae.skydoppler.chat.ChatMatchHandler;
import ae.skydoppler.fishing.FishingHideState;
import ae.skydoppler.structs.SkyblockPlayerDataStruct;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.fabricmc.fabric.api.client.screen.v1.ScreenEvents;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import net.minecraft.entity.projectile.FishingBobberEntity;
import net.minecraft.scoreboard.Scoreboard;
import net.minecraft.scoreboard.Team;
import net.minecraft.text.Text;
import net.minecraft.util.math.Box;
import org.lwjgl.glfw.GLFW;import net.fabricmc.fabric.api.client.screen.v1.ScreenEvents;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.ingame.InventoryScreen;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import static ae.skydoppler.Skydoppler.LOGGER;

public class SkydopplerClient implements ClientModInitializer {

    public static MinecraftClient client = MinecraftClient.getInstance();

    public static KeyBinding debugKey;
    public static SkyblockPlayerDataStruct playerDataStruct;
    public static boolean RareSeaCreatureNotifications;
    public static boolean ShowRareSeaCreatureNotificationsChatMessage;
    public static boolean PlayRareSeaCreatureNotificationsSound;
    private TextRenderer textRenderer;

    public static boolean isRodCast;

    @Override
    public void onInitializeClient() {

        System.out.println("Skydoppler (Client) is initializing!");
        textRenderer = new TextRenderer(client);
        textRenderer.initialize();
        ChatMatchHandler.loadJsonData();
        isRodCast = false;

        RareSeaCreatureNotifications = true;
        ShowRareSeaCreatureNotificationsChatMessage = true;
        PlayRareSeaCreatureNotificationsSound = true;

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