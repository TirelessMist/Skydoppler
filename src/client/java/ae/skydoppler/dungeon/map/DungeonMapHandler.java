package ae.skydoppler.dungeon.map;

import java.awt.*;

public class DungeonMapHandler {
    /**
     * Size of the map in pixels (square, 128x128)
     */

    /**
     * Gap size between rooms in pixels
     */
    public static final int ROOM_GAP_SIZE = 4;

    public static int mapTileSize = 0; // Size of each tile in pixels
    public static Point entranceRoomPosition = new Point(0, 0); // Position of the entrance room in the map
}
