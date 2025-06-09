package ae.skydoppler;

import ae.skydoppler.dungeon.map.MapTile;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.rendering.v1.HudLayerRegistrationCallback;
import net.fabricmc.fabric.api.client.rendering.v1.IdentifiedLayer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.render.RenderTickCounter;
import net.minecraft.util.Identifier;

import java.awt.*;

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
                Color color = switch (dungeonMapTiles[y][x].getRoomType()) {
                    case NONE -> new Color(0, 0, 0);
                    case NORMAL -> new Color(127, 90, 19);
                    case ENTRANCE -> new Color(0, 255, 0);
                    case UNKNOWN -> new Color(71, 71, 71);
                    case PUZZLE -> new Color(182, 36, 205);
                    case TRAP -> new Color(216, 113, 31);
                    case MINIBOSS -> new Color(244, 212, 25);
                    case BLOOD -> new Color(214, 20, 20);
                    case FAIRY -> new Color(228, 90, 209);
                    default -> new Color(0, 0, 255);
                };
                context.fill(
                        x * 15 + 30,
                        y * 15 + 30,
                        (x + 1) * 15 + 30,
                        (y + 1) * 15 + 30,
                        color.getRGB()
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