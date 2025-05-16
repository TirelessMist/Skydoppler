package ae.skydoppler.dungeon.room_detection;

import ae.skydoppler.structs.Size;
import net.minecraft.client.MinecraftClient;

import java.awt.*;

public class MapReader {

    public static void registerTickEndEvent() {
        MinecraftClient client = MinecraftClient.getInstance();

        // Register a tick-end event. Remember to register this only when the player is in a dungeon, otherwise unregister it.
        client.execute(() -> {
            if (client.world == null || client.player == null) {
                return;
            }


        });
    }

    private static Point locateEntranceRoom(byte[][] mapData) {
        for (int row = 0; row < mapData.length; row++) {
            for (int col = 0; col < mapData[row].length; col++) {
                // Assuming green tiles are represented by a specific color code, e.g., 3
                if (mapData[row][col] == 30) {

                    /*switch (getRoomDimensions(mapData, new Point(row, col))) {
                        case TINY:
                            System.out.println("Found a tiny room at: " + row + ", " + col);
                            break;
                        case SMALL:
                            System.out.println("Found a small room at: " + row + ", " + col);
                            break;
                        case MEDIUM:
                            System.out.println("Found a medium room at: " + row + ", " + col);
                            break;
                        case LARGE:
                            System.out.println("Found a large room at: " + row + ", " + col);
                            break;
                    }*/

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


