package ae.skydoppler;

import ae.skydoppler.dungeon.map.MapTile;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.rendering.v1.HudLayerRegistrationCallback;
import net.fabricmc.fabric.api.client.rendering.v1.IdentifiedLayer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.render.RenderTickCounter;
import net.minecraft.util.Identifier;

public class HudRenderingEntrypoint implements ClientModInitializer {
    private static final Identifier MAP_LAYER = Identifier.of(Skydoppler.MOD_ID, "dungeon_map_layer");

    public static MapTile[][] dungeonMapTiles = new MapTile[0][0];

    private static void render(DrawContext context, RenderTickCounter tickCounter) {
        if (dungeonMapTiles == null || dungeonMapTiles.length == 0 || dungeonMapTiles[0].length == 0) {
            // No tiles to render, return early
            return;
        }
        for (int y = 0; y < dungeonMapTiles.length; y++) {
            for (int x = 0; x < dungeonMapTiles[y].length; x++) {
                context.fill(
                        x * 10, // x position
                        y * 10, // y position
                        (x + 1) * 10, // x2 position
                        (y + 1) * 10, // y2 position
                        0xFF000000 | (dungeonMapTiles[y][x].getRoomType().getValue() & 0xFF) // convert byte to ARGB color (opaque)
                );
            }
        }
    }

    @Override
    public void onInitializeClient() {
        // Attach our rendering code to before the chat hud layer. Our layer will render right before the chat. The API will take care of z spacing and automatically add 200 after every layer.
        HudLayerRegistrationCallback.EVENT.register(layeredDrawer -> layeredDrawer.attachLayerBefore(IdentifiedLayer.CHAT, MAP_LAYER, HudRenderingEntrypoint::render));
    }
}