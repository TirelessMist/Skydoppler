package ae.skydoppler.dungeon.room_detection;

import ae.skydoppler.structs.Size;
import net.minecraft.client.MinecraftClient;
import net.minecraft.item.FilledMapItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.map.MapState;

import java.awt.*;

public class MapParser {

    MapReader.FloorType floorType;

    /**
     * Retrieves the map from the player’s inventory slot 8 (9th/last Hotbar Slot), parses the pixel data into a 2D array.
     */
    public static byte[][] parseMapFromSlot() {
        // Get the client instance and make sure the player is available.
        MinecraftClient client = MinecraftClient.getInstance();
        if (client.player == null) {
            System.out.println("No player available.");
            return null;
        }

        // Fetch the item from slot index 8 (typically the 9th slot in the hotbar).
        ItemStack stack = client.player.getInventory().getStack(8);
        if (stack.isEmpty()) {
            System.out.println("Slot 8 is empty.");
            return null;
        }

        // Verify that the item is a map (an instance of FilledMapItem).
        if (!(stack.getItem() instanceof FilledMapItem)) {
            System.out.println("The item in slot 8 is not a map.");
            return null;
        }

        // Retrieve the map state. This contains the map's data and requires the world.
        MapState mapState = FilledMapItem.getMapState(stack, client.world);
        if (mapState == null) {
            System.out.println("Map state not found.");
            return null;
        }

        // The map color data is held in a byte array.
        // Minecraft maps are 128 col 128 pixels, so the array should contain 16384 bytes.
        byte[] colors = mapState.colors; // In Yarn, the colors array is typically publicly accessible.
        // The map's width and height in pixels.
        final int width = 128;
        final int height = 128;
        if (colors == null || colors.length < width * height) {
            System.out.println("Invalid map color data.");
            return null;
        }

        // Create a 2D array to hold the color values (one cell per pixel).
        byte[][] mapPixels = new byte[height][width];
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                // Compute the flat array index corresponding to the (col, row) coordinate.
                int index = x + (y * width);
                // Since bytes in Java are signed, we use & 0xFF to convert the value to an unsigned int.
                mapPixels[y][x] = colors[index];
            }
        }

        return mapPixels;
    }

}




















