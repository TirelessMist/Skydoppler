package ae.skydoppler.dungeon.map;

import ae.skydoppler.SkydopplerClient;

import java.awt.*;

public class DungeonTileMapConstructor {

    /**
     * Constructs a 2D array of MapTile objects from the provided map pixel data.
     *
     * @param mapPixels A 2D byte array representing the map pixels.
     * @return A modular grid of MapTiles.
     */
    public static MapTile[][] constructMap(byte[][] mapPixels) {

        if (SkydopplerClient.dungeonClientHandler.getCurrentDungeonFloor() == -1) {
            throw new IllegalStateException("Dungeon floor is not set. Please set the current dungeon floor before constructing the map.");
        }

        if (mapPixels.length == 0 || mapPixels[0].length == 0) {
            throw new IllegalArgumentException("Map pixels cannot be null or empty.");
        }

        MapTile[][] tileGrid;

        if (DungeonMapHandler.entranceRoomPosition == new Point(0, 0)) {
            DungeonMapHandler.entranceRoomPosition = locateRoom(mapPixels, RoomType.ENTRANCE);
        }

        if (DungeonMapHandler.mapTileSize == 0) {
            DungeonMapHandler.mapTileSize = determineTileSize(mapPixels);
        }

        // Calculate the amount of pixels used when the maximum amount of mapTileSize with ROOM_GAP_SIZE between each mapTileSize can fit in the square map size of 128 pixels.
        int gridRows = (mapPixels.length / (DungeonMapHandler.mapTileSize + DungeonMapHandler.ROOM_GAP_SIZE)); // -1 because the last tile does not have a gap after it
        int gridCols = (mapPixels[0].length / (DungeonMapHandler.mapTileSize + DungeonMapHandler.ROOM_GAP_SIZE)); // for calculations for rows and cols, add 1 pixel to the left and right to make sure door checks are not off the map.

        int leftBorderPixels = 0;
        int topBorderPixels = 0;

        switch (SkydopplerClient.dungeonClientHandler.getCurrentDungeonFloor()) {
            case 0: // entrance floor
                DungeonMapHandler.mapTileSize = 18;
                gridRows = 4;
                gridCols = 4;
                leftBorderPixels = 22;
                topBorderPixels = 22;
                break;
            case 1:
                DungeonMapHandler.mapTileSize = 18;
                gridRows = 5;
                gridCols = 4;
                leftBorderPixels = 22;
                topBorderPixels = 11;
                break;
            case 2, 3:
                DungeonMapHandler.mapTileSize = 18;
                gridRows = 5;
                gridCols = 5;
                leftBorderPixels = 11;
                topBorderPixels = 11;
                break;
            case 4:
                DungeonMapHandler.mapTileSize = 16;
                gridRows = 5;
                gridCols = 6;
                leftBorderPixels = 5;
                topBorderPixels = 16;
                break;
            case 5:
                DungeonMapHandler.mapTileSize = 16;
                gridRows = 6;
                gridCols = 6;
                leftBorderPixels = 5;
                topBorderPixels = 5;
                break;
            case 6, 7:
                DungeonMapHandler.mapTileSize = 16;
                gridRows = 6;
                gridCols = 6;
                leftBorderPixels = 5;
                topBorderPixels = 5;
                break;
            default:
                throw new IllegalArgumentException("Invalid map tile size: " + DungeonMapHandler.mapTileSize);
        }

        // Calculate the number of rows and columns in the square map based on the amount of tiles and gaps between those tiles that can fit in the square map size.
        tileGrid = new MapTile[gridRows][gridCols];

        for (int y = 0; y < gridRows; y++) {
            for (int x = 0; x < gridCols; x++) {

                // Top-left pixel of the room
                int roomPosX = (DungeonMapHandler.mapTileSize + DungeonMapHandler.ROOM_GAP_SIZE) * x + leftBorderPixels;
                int roomPosY = (DungeonMapHandler.mapTileSize + DungeonMapHandler.ROOM_GAP_SIZE) * y + topBorderPixels;

                int tileSize = DungeonMapHandler.mapTileSize;

                byte pixelValue = mapPixels[roomPosY][roomPosX];

                int halfTile = tileSize / 2;

                MapTile tile = new MapTile();

                tile.setRoomType(getRoomTypeFromPixel(mapPixels[roomPosY][roomPosX]));

                tile.setTopDoorType(
                        (roomPosY - 1 >= 0 && roomPosX + halfTile < mapPixels[0].length)
                                ? getDoorTypeFromPixel(mapPixels[roomPosY - 1][roomPosX + halfTile])
                                : DoorType.NONE
                );
                tile.setRightDoorType(
                        (roomPosY + halfTile < mapPixels.length && roomPosX + tileSize < mapPixels[0].length)
                                ? getDoorTypeFromPixel(mapPixels[roomPosY + halfTile][roomPosX + tileSize])
                                : DoorType.NONE
                );
                tile.setBottomDoorType(
                        (roomPosY + tileSize < mapPixels.length && roomPosX + halfTile < mapPixels[0].length)
                                ? getDoorTypeFromPixel(mapPixels[roomPosY + tileSize][roomPosX + halfTile])
                                : DoorType.NONE
                );
                tile.setLeftDoorType(
                        (roomPosY + halfTile < mapPixels.length && roomPosX - 1 >= 0)
                                ? getDoorTypeFromPixel(mapPixels[roomPosY + halfTile][roomPosX - 1])
                                : DoorType.NONE
                );

                tile.setRoomMarkType(determineRoomMarkType(mapPixels, roomPosX, roomPosY, pixelValue));

                tileGrid[y][x] = tile;

            }
        }

        return tileGrid;

    }

    private static boolean canFitTile(int inSize) {
        return inSize < DungeonMapHandler.mapTileSize;
    }

    private static int determineTileSize(byte[][] mapPixels) {
        return determineRoomLength(mapPixels, DungeonMapHandler.entranceRoomPosition);
    }

    private static int determineEdgeGapSize(byte[][] mapPixels, Point direction) {
        if (direction.equals(new Point(0, -1))) {
            // Count whitespace rows from the top
            int topCount = 0;
            for (int y = 0; y < mapPixels.length; y++) {
                if (isRowWhitespace(mapPixels, y)) {
                    topCount++;
                } else {
                    break;
                }
            }
            return topCount;
        } else if (direction.equals(new Point(1, 0))) {
            // Count whitespace columns from the right
            int rightCount = 0;
            for (int x = mapPixels[0].length - 1; x >= 0; x--) {
                if (isColWhitespace(mapPixels, x)) {
                    rightCount++;
                } else {
                    break;
                }
            }
            return rightCount;
        } else if (direction.equals(new Point(0, 1))) {
            // Count whitespace rows from the bottom
            int bottomCount = 0;
            for (int y = mapPixels.length - 1; y >= 0; y--) {
                if (isRowWhitespace(mapPixels, y)) {
                    bottomCount++;
                } else {
                    break;
                }
            }
            return bottomCount;
        } else if (direction.equals(new Point(-1, 0))) {
            // Count whitespace columns from the left
            int leftCount = 0;
            for (int x = 0; x < mapPixels[0].length; x++) {
                if (isColWhitespace(mapPixels, x)) {
                    leftCount++;
                } else {
                    break;
                }
            }
            return leftCount;
        } else {
            return 0;
        }
    }

    // Helper: checks if a row is all whitespace (0)
    private static boolean isRowWhitespace(byte[][] arr, int row) {
        for (int x = 0; x < arr[row].length; x++) {
            if (arr[row][x] != 0) return false;
        }
        return true;
    }

    // Helper: checks if a column is all whitespace (0)
    private static boolean isColWhitespace(byte[][] arr, int col) {
        for (int y = 0; y < arr.length; y++) {
            if (arr[y][col] != 0) return false;
        }
        return true;
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