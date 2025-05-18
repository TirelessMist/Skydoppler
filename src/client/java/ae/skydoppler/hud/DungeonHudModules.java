package ae.skydoppler.hud;

import ae.skydoppler.dungeon.map.DungeonMapHandler;
import ae.skydoppler.dungeon.room_detection.MapParser;
import ae.skydoppler.dungeon.room_detection.MapReassembler;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.render.RenderTickCounter;
import net.minecraft.item.FilledMapItem;
import net.minecraft.item.ItemStack;

import java.awt.*;

public class DungeonHudModules {

    private static MinecraftClient client = MinecraftClient.getInstance();

    public static class SecretCountModule {
        public static int x = 0;
        public static int y = 0;
        public static int scale = 1;
        public static boolean textShadow = true;
        public static int textColor = new Color(240, 240, 40).getRGB();

        public static void render(DrawContext drawContext, RenderTickCounter renderTickCounter, int secretCount) {
            String secretCountText = Integer.toString(secretCount);
            drawContext.drawText(client.textRenderer, secretCountText, x, y, textColor, textShadow);
        }
    }

    public static class CurrentRoomNameModule {
        public static int x = 0;
        public static int y = 0;
        public static int scale = 1;
        public static boolean textShadow = true;
        public static int textColor = new Color(240, 240, 40).getRGB();

        public static void render(DrawContext drawContext, RenderTickCounter renderTickCounter, String roomName) {
            drawContext.drawText(client.textRenderer, roomName, x, y, textColor, textShadow);
        }
    }

    public static class DungeonMapModule {

        public static int x = 0;
        public static int y = 0;
        public static final int tileSize = 50; // Default tile size
        public static int scale = 1; // Default scale

        public static int NORMAL_COLOR = new Color(139, 69, 19).getRGB(); // Brown color
        public static int ENTRANCE_COLOR = new Color(0, 255, 0).getRGB(); // Green color
        public static int FAIRY_COLOR = new Color(255, 34, 174).getRGB(); // Pink color
        public static int PUZZLE_COLOR = new Color(213, 25, 227).getRGB(); // Purple color
        public static int MINIBOSS_COLOR = new Color(231, 155, 42).getRGB(); // Yellow color
        public static int TRAP_COLOR = new Color(254, 97, 0).getRGB(); // Orange color
        public static int BLOOD_COLOR = new Color(255, 0, 0).getRGB(); // Red color


        public static void render(DrawContext drawContext, RenderTickCounter renderTickCounter) {
    
            MinecraftClient client = MinecraftClient.getInstance();

            if (DungeonMapHandler.useRawMap) {

                ItemStack mapStack = client.player.getInventory().getStack(8); // Get the map from the 9th hotbar slot
                if (mapStack.getItem() instanceof FilledMapItem) {

                    byte[][] mapPixels = MapParser.parseMap(FilledMapItem.getMapState(mapStack, client.world));

                    if (mapPixels == null) {
                        return; // No map data available
                    }

                    for (int row = 0; row < mapPixels.length; row++) {
                        for (int col = 0; col < mapPixels[row].length; col++) {
                            byte pixel = mapPixels[row][col];
                            if (pixel != 0) { // Do not render empty pixels
                                int color = new Color(pixel).getRGB();
                                drawContext.fill(x + col * scale, y + row * scale, x + (col + 1) * scale, y + (row + 1) * scale, color);
                            }
                        }

                    }

                } else {
                    MapReassembler.Tile[][] map = DungeonMapHandler.map;

                    if (map == null) {
                        return; // No map data available
                    }

                    // Render the map overlay
                    for (int row = 0; row < map.length; row++) {
                        for (int col = 0; col < map[row].length; col++) {
                            MapReassembler.Tile tile = map[row][col];
                            if (tile.getTileType() != MapReassembler.TileType.EMPTY) { // Do not render EMPTY (WHITESPACE) tiles

                                switch (tile.getTileType()) {

                                    case ENTRANCE_ROOM:
                                        renderTile(drawContext, x + col * tileSize, y + row * tileSize, ENTRANCE_COLOR);
                                        break;
                                    case NORMAL_ROOM:
                                        renderTile(drawContext, x + col * tileSize, y + row * tileSize, NORMAL_COLOR);
                                        break;
                                    case FAIRY_ROOM:
                                        renderTile(drawContext, x + col * tileSize, y + row * tileSize, FAIRY_COLOR);
                                        break;
                                    case PUZZLE_ROOM:
                                        renderTile(drawContext, x + col * tileSize, y + row * tileSize, PUZZLE_COLOR);
                                        break;
                                    case MINIBOSS_ROOM:
                                        renderTile(drawContext, x + col * tileSize, y + row * tileSize, MINIBOSS_COLOR);
                                        break;
                                    case TRAP_ROOM:
                                        renderTile(drawContext, x + col * tileSize, y + row * tileSize, TRAP_COLOR);
                                        break;
                                    case BLOOD_ROOM:
                                        renderTile(drawContext, x + col * tileSize, y + row * tileSize, BLOOD_COLOR);
                                        break;

                                }

                            }
                        }
                    }
                }
            }
        }

        private static void renderTile(DrawContext drawContext, int x, int y, int color) {

            if (MinecraftClient.getInstance().player == null || MinecraftClient.getInstance().world == null) return;

            drawContext.fill(x, y, x + scale, y + scale, color);

        }

    }
}
