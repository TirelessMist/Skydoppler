/*
package ae.skydoppler.dungeon.room_detection;

import net.minecraft.client.MinecraftClient;
import net.minecraft.item.FilledMapItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.map.MapState;

import java.awt.*;

public class MapReader {

    Point entranceRoom = null;

    public static void registerTickEndEvent() {
        MinecraftClient client = MinecraftClient.getInstance();

        // Register a tick-end event. Remember to register this only when the player is in a dungeon, otherwise unregister it.
        client.execute(() -> {
            if (client.world == null || client.player == null) {
                return;
            }

            // Check if there is a map item in the player's last hotbar slot
            ItemStack mapStack = client.player.getInventory().getStack(8);
            if (mapStack.getItem() instanceof FilledMapItem mapItem) {

                // Get the map state
                MapState mapState = FilledMapItem.getMapState(mapStack, client.world);

                if (mapState != null) {
                    // Check if the map is a dungeon map
                    if (mapState.scale == 1) {
                        // Read the map data
                        int[][] mapData = new int[128][128];
                        for (int x = 0; x < 128; x++) {
                            for (int y = 0; y < 128; y++) {
                                mapData[x][y] = mapState.colors[x + y * 128];
                            }
                        }

                        // Process the map data to locate the entrance room
                        Point entranceRoom = locateEntranceRoom(mapData);
                    }
                }

            }

        });
    }

    private static Point locateEntranceRoom(int[][] mapData) {
        for (int row = 0; row < mapData.length; row++) {
            for (int col = 0; col < mapData[row].length; col++) {
                // Assuming green tiles are represented by a specific color code, e.g., 3
                if (mapData[row][col] == 3) {
                    return new Point(row, col);
                }
            }
        }
        return null; // Return null if no green tile is found
    }

    public enum FloorType {
        TINY, // entrance, f1
        SMALL, // f2, f3, f4
        MEDIUM, // f5, f6
        LARGE // f7
    }

}


*/
