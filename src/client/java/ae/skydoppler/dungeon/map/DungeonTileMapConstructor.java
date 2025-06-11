package ae.skydoppler.dungeon.map;

import java.awt.*;

public class DungeonTileMapConstructor {

    /**
     * Constructs a 2D array of MapTile objects from the provided map pixel data.
     *
     * @param mapPixels A 2D byte array representing the map pixels.
     * @return A modular grid of MapTiles.
     */
    public static MapTile[][] constructMap(byte[][] mapPixels) {
        if (mapPixels.length == 0 || mapPixels[0].length == 0) {
            throw new IllegalArgumentException("Map pixels cannot be null or empty.");
        }

        MapTile[][] tileGrid;

        if (DungeonMapHandler.mapTileSize == 0) {
            DungeonMapHandler.mapTileSize = determineTileSize(mapPixels);
        }

        // Calculate the amount of pixels used when the maximum amount of mapTileSize with ROOM_GAP_SIZE between each mapTileSize can fit in the square map size of 128 pixels.
        int tilesInRowCount = (DungeonMapHandler.SQUARE_MAP_SIZE / (DungeonMapHandler.mapTileSize + DungeonMapHandler.ROOM_GAP_SIZE)) - 1; // -1 because the last tile does not have a gap after it
        int tilesInColCount = 0; // for calculations for rows and cols, add 1 pixel to the left and right to make sure door checks are not off the map.

        int usedMapPixels = tilesInRowCount * DungeonMapHandler.mapTileSize + (tilesInRowCount - 1) * DungeonMapHandler.ROOM_GAP_SIZE;

        // Calculate the number of rows and columns in the square map based on the amount of tiles and gaps between those tiles that can fit in the square map size.
        int tileGridRowsCols = tilesInRowCount;
        int tileGridRows = 0;
        int tileGridCols = 0;
        tileGrid = new MapTile[tileGridRows][tileGridCols];

        if (DungeonMapHandler.mapBorderSize == 0) {
            // Calculate the border size based on the square map size and the number of tiles and gaps between those tiles that can fit in the square map size.
            DungeonMapHandler.mapBorderSize = (128 - usedMapPixels) / 2;
        }

        for (int y = 0; y < tileGridRowsCols; y++) {
            for (int x = 0; x < tileGridRowsCols; x++) {

                // Top-left pixel of the room
                int roomPosX = (DungeonMapHandler.mapTileSize + DungeonMapHandler.ROOM_GAP_SIZE) * x + (DungeonMapHandler.mapBorderSize / 2);
                int roomPosY = (DungeonMapHandler.mapTileSize + DungeonMapHandler.ROOM_GAP_SIZE) * y + (DungeonMapHandler.mapBorderSize / 2);

                int tileSize = DungeonMapHandler.mapTileSize;

                byte pixelValue = mapPixels[roomPosY][roomPosX];

                int halfTile = tileSize / 2;

                MapTile tile = new MapTile();

                tile.setRoomType(getRoomTypeFromPixel(mapPixels[roomPosY][roomPosX]));

                tile.setTopDoorType(getDoorTypeFromPixel(mapPixels[roomPosY - 1][roomPosX + halfTile]));
                tile.setRightDoorType(getDoorTypeFromPixel(mapPixels[roomPosY + halfTile][roomPosX + tileSize]));
                tile.setBottomDoorType(getDoorTypeFromPixel(mapPixels[roomPosY + tileSize][roomPosX + halfTile]));
                tile.setLeftDoorType(getDoorTypeFromPixel(mapPixels[roomPosY + halfTile][roomPosX - 1]));

                tile.setRoomMarkType(determineRoomMarkType(mapPixels, roomPosX, roomPosY, pixelValue));

                tileGrid[y][x] = tile;

            }
        }

        return tileGrid;

    }

    private static int determineTileSize(byte[][] mapPixels) {
        Point entrancePos = locateRoom(mapPixels, RoomType.ENTRANCE);
        if (entrancePos != null) {
            return determineRoomLength(mapPixels, entrancePos);
        } else {
            throw new IllegalArgumentException("Entrance room not found in the map pixels.");
        }
    }

    private static Point locateRoom(byte[][] mapPixels, RoomType roomType) {
        // Locate the entrance room by finding the first pixel that matches the entrance room type
        for (int y = 0; y < mapPixels.length; y++) {
            for (int x = 0; x < mapPixels[0].length; x++) {
                byte roomByteValue = roomType.getValue();
                if (mapPixels[y][x] == roomByteValue && mapPixels[y][x + 4] == roomByteValue) {
                    return new Point(x, y);
                }
            }
        }
        return null; // Return null if no entrance room is found
    }

    private static int determineRoomLength(byte[][] mapPixels, Point entrancePos) {
        // Determine the room length by checking the pixels in the row of the entrance room
        int roomLength = 0;
        byte roomByteValue = mapPixels[entrancePos.y][entrancePos.x];

        for (int x = entrancePos.x; x < mapPixels[0].length; x++) {
            if (mapPixels[entrancePos.y][x] == roomByteValue) {
                roomLength++;
            } else {
                break; // Stop counting when the non-room pixel is encountered
            }
        }
        return roomLength;
    }

    private static RoomMarkType determineRoomMarkType(byte[][] mapPixels, int roomPosX, int roomPosY, byte roomColor) {
        // Determine the room mark type based on the pixel data at the area of pixels in the halfTile of the room
        int center = DungeonMapHandler.mapTileSize / 2;
        for (int y = roomPosY + center - 2; y < roomPosY + center + 2; y++) {
            for (int x = roomPosX + center - 2; x < roomPosX + center + 2; x++) {
                if (mapPixels[y][x] != roomColor) { // If any pixel in the halfTile area does not match the room color, return the mark type
                    return getRoomMarkTypeFromPixel(mapPixels[y][x]);
                }
            }
        }
        return RoomMarkType.NONE; // If all pixels match the room color, return NONE
    }

    //region Pixel Conversion Methods
    private static RoomType getRoomTypeFromPixel(byte pixelValue) {
        return RoomType.fromValue(pixelValue);
    }

    private static DoorType getDoorTypeFromPixel(byte pixelValue) {
        return DoorType.fromValue(pixelValue);
    }

    private static RoomMarkType getRoomMarkTypeFromPixel(byte pixelValue) {
        return RoomMarkType.fromValue(pixelValue);
    }
    //endregion
}