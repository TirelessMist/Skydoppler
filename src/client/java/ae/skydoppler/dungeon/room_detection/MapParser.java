package ae.skydoppler.dungeon.room_detection;

import net.minecraft.client.MinecraftClient;
import net.minecraft.item.FilledMapItem;
import net.minecraft.item.ItemStack;
// In Yarn for 1.21.5, the map state class is usually found in this package.
import net.minecraft.item.map.MapState;

public class MapParser {

    FloorType floorType;

    /**
     * Retrieves the map from the player’s inventory slot 8, parses the pixel data into a 2D array,
     * and prints the array to the console.
     */
    public static char[][] parseMapFromSlot() {
        // Get the client instance and make sure the player is available.
        MinecraftClient client = MinecraftClient.getInstance();
        if (client.player == null) {
            System.out.println("No player available.");
            return;
        }

        // Fetch the item from slot index 8 (typically the 9th slot in the hotbar).
        ItemStack stack = client.player.getInventory().getStack(8);
        if (stack.isEmpty()) {
            System.out.println("Slot 8 is empty.");
            return;
        }

        // Verify that the item is a map (an instance of FilledMapItem).
        if (!(stack.getItem() instanceof FilledMapItem)) {
            System.out.println("The item in slot 8 is not a map.");
            return;
        }

        // Retrieve the map state. This contains the map's data and requires the world.
        MapState mapState = FilledMapItem.getMapState(stack, client.world);
        if (mapState == null) {
            System.out.println("Map state not found.");
            return;
        }

        // The map color data is held in a byte array.
        // Minecraft maps are 128 x 128 pixels, so the array should contain 16384 bytes.
        byte[] colors = mapState.colors; // In Yarn, the colors array is typically publicly accessible.
        // The map's width and height in pixels.
        final int width  = 128;
        final int height = 128;
        if (colors == null || colors.length < width * height) {
            System.out.println("Invalid map color data.");
            return;
        }

        // Create a 2D array to hold the color values (one cell per pixel).
        int[][] mapPixels = new int[height][width];
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                // Compute the flat array index corresponding to the (x, y) coordinate.
                int index = x + (y * width);
                // Since bytes in Java are signed, we use & 0xFF to convert the value to an unsigned int.
                mapPixels[y][x] = colors[index] & 0xFF;
            }
        }

        // Print the 2D array to the console, row by row.
        for (int y = 0; y < height; y++) {
            StringBuilder row = new StringBuilder();
            for (int x = 0; x < width; x++) {
                row.append(mapPixels[y][x]).append(" ");
            }
            System.out.println(row.toString());
        }
    }

    private static char[][] convertToGrid(byte[][] mapData) {

        char[][] grid;

        for (int row = 0; row < mapData[0].length; row+=2 * size) {
            for (int col = 0; col < mapData[1].length; col+=4) {

                

            }
        }

    }

    private static FloorType determineFloorType(byte[][] mapData) {

    }
}