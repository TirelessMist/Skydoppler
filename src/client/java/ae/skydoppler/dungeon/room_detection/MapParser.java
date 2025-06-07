package ae.skydoppler.dungeon.room_detection;

import ae.skydoppler.SkydopplerClient;
import net.minecraft.client.MinecraftClient;
import net.minecraft.item.map.MapState;

public class MapParser {

    MapReader.FloorType floorType;

    /**
     * Retrieves the map from the player’s inventory slot index 8 (9th Hotbar Slot), parses the pixel data into a 2D array.
     */
    public static byte[][] parseMap(MapState mapState) {
        // Get the client instance and make sure the player is available.
        MinecraftClient client = MinecraftClient.getInstance();
        if (client.player == null) {
            if (SkydopplerClient.debugModeEnabled)
                System.out.println("No player available.");
            return null;
        }

        if (mapState == null) {
            if (SkydopplerClient.debugModeEnabled)
                System.out.println("Map state not found.");
            return null;
        }

        // The map color data is held in a byte array.
        // Minecraft maps are 128 col 128 pixels, so the array should contain 16384 bytes.
        byte[] colors = mapState.colors;
        // The map's width and height in pixels.
        final int width = 128;
        final int height = 128;
        if (colors == null || colors.length < width * height) {
            if (SkydopplerClient.debugModeEnabled)
                System.out.println("Invalid map color data.");
            return null;
        }

        // Create a 2D array to hold the color values (one cell per pixel).
        byte[][] mapPixels = new byte[height][width];
        for (int x = 0; x < height; x++) {
            for (int y = 0; y < width; y++) {
                // Compute the flat array index corresponding to the (col, row) coordinate.
                int index = x + (y * width);
                mapPixels[y][x] = colors[index];
            }
        }

        return mapPixels;
    }

}




















