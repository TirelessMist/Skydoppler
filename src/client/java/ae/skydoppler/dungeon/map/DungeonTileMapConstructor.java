package ae.skydoppler.dungeon.map;

import java.util.HashMap;
import java.util.Map;

public class DungeonTileMapConstructor {

    /**
     * Constructs a 2D array of MapTile objects from the provided map pixel data.
     *
     * @param mapPixels A 2D byte array representing the map pixels.
     * @return A modular grid of MapTiles.
     */
    public static MapTile[][] constructMap(byte[][] mapPixels) {
        if (mapPixels == null || mapPixels.length == 0 || mapPixels[0].length == 0) {
            return new MapTile[0][0];
        }

        // Find the unit size and grid bounds
        int[] gridInfo = findGridInfo(mapPixels);
        int unitSize = gridInfo[0];
        int startRow = gridInfo[1];
        int startCol = gridInfo[2];
        int endRow = gridInfo[3];
        int endCol = gridInfo[4];

        if (unitSize <= 0) {
            return new MapTile[0][0]; // Couldn't find valid rooms
        }

        // Calculate the dimensions of our tile grid
        // Add 1 to ensure we include a complete grid (fixing the cutoff issue)
        int gridRows = ((endRow - startRow) / (unitSize + 4)) + 1;
        int gridCols = ((endCol - startCol) / (unitSize + 4)) + 1;

        // Initialize the tile grid
        MapTile[][] tileGrid = new MapTile[gridRows][gridCols];
        for (int y = 0; y < gridRows; y++) {
            for (int x = 0; x < gridCols; x++) {
                tileGrid[y][x] = new MapTile(); // Initialize with default values
            }
        }

        // Keep track of room UUIDs to identify multi-unit rooms
        Map<String, Integer> roomUuids = new HashMap<>();
        int nextUuid = 1; // Start with UUID 1

        // Now populate the grid with room information
        for (int gridY = 0; gridY < gridRows; gridY++) {
            for (int gridX = 0; gridX < gridCols; gridX++) {
                // Calculate pixel coordinates for this tile position
                int pixelX = startCol + gridX * (unitSize + 4);
                int pixelY = startRow + gridY * (unitSize + 4);

                // Check if this is a valid room (top-left pixel indicates room type)
                byte roomTypeValue = getRoomTypeValue(mapPixels, pixelX, pixelY);
                if (roomTypeValue == 0) {
                    continue; // Not a valid room
                }

                // Get the room type
                RoomType roomType = RoomType.fromValue(roomTypeValue);

                // Determine if this is part of a multi-unit room
                String roomKey = findAdjacentRoomWithSameType(tileGrid, gridX, gridY, roomType);
                int uuid;
                if (roomKey != null && roomUuids.containsKey(roomKey)) {
                    uuid = roomUuids.get(roomKey);
                } else {
                    uuid = nextUuid++;
                    if (roomKey != null) {
                        roomUuids.put(roomKey, uuid);
                    }
                }

                // Create or update the tile
                MapTile tile = tileGrid[gridY][gridX];
                tile.setRoomType(roomType);
                tile.setUuid(uuid);

                // Check for doors on all sides
                tile.setTopDoorType(getDoorType(mapPixels, pixelX, pixelY, pixelX + unitSize / 2, pixelY - 4, unitSize));
                tile.setRightDoorType(getDoorType(mapPixels, pixelX, pixelY, pixelX + unitSize + 4, pixelY + unitSize / 2, unitSize));
                tile.setBottomDoorType(getDoorType(mapPixels, pixelX, pixelY, pixelX + unitSize / 2, pixelY + unitSize + 4, unitSize));
                tile.setLeftDoorType(getDoorType(mapPixels, pixelX, pixelY, pixelX - 4, pixelY + unitSize / 2, unitSize));

                // Check for checkmark if applicable
                if (canHaveCheckmark(roomType)) {
                    // For check mark, look at the center of the room unit
                    int centerX = pixelX + unitSize / 2;
                    int centerY = pixelY + unitSize / 2;
                    CheckMarkType checkMarkType = getCheckMarkType(mapPixels, centerX, centerY);
                    tile.setCheckMarkType(checkMarkType);
                }
            }
        }

        return tileGrid;
    }

    /**
     * Finds the unit size and grid boundaries in the pixel map.
     * Returns an array with [unitSize, startRow, startCol, endRow, endCol]
     */
    private static int[] findGridInfo(byte[][] mapPixels) {
        int rows = mapPixels.length;
        int cols = mapPixels[0].length;

        // Find the first non-zero pixel (start of a room)
        int firstNonZeroRow = -1;
        int firstNonZeroCol = -1;

        // Find first and last non-zero pixels for accurate grid boundaries
        int minRow = Integer.MAX_VALUE;
        int minCol = Integer.MAX_VALUE;
        int maxRow = Integer.MIN_VALUE;
        int maxCol = Integer.MIN_VALUE;

        // First pass - find ALL non-zero pixels to get the true boundaries
        for (int y = 0; y < rows; y++) {
            for (int x = 0; x < cols; x++) {
                if (mapPixels[y][x] != 0) {
                    if (firstNonZeroRow == -1) {
                        firstNonZeroRow = y;
                        firstNonZeroCol = x;
                    }

                    minRow = Math.min(minRow, y);
                    minCol = Math.min(minCol, x);
                    maxRow = Math.max(maxRow, y);
                    maxCol = Math.max(maxCol, x);
                }
            }
        }

        // If no room found
        if (firstNonZeroRow == -1) {
            return new int[]{0, 0, 0, 0, 0};
        }

        // Find the unit size by locating where the first room ends horizontally
        int unitSize = 0;
        byte firstRoomValue = mapPixels[firstNonZeroRow][firstNonZeroCol];
        for (int x = firstNonZeroCol; x < cols; x++) {
            if (x == cols - 1 || mapPixels[firstNonZeroRow][x] != firstRoomValue) {
                unitSize = x - firstNonZeroCol;
                if (unitSize == 0) unitSize = 1; // Failsafe for single-pixel units
                break;
            }
        }

        // If couldn't determine unit size, try vertical scanning instead
        if (unitSize <= 0) {
            for (int y = firstNonZeroRow; y < rows; y++) {
                if (y == rows - 1 || mapPixels[y][firstNonZeroCol] != firstRoomValue) {
                    unitSize = y - firstNonZeroRow;
                    if (unitSize == 0) unitSize = 1; // Failsafe for single-pixel units
                    break;
                }
            }
        }

        // If still couldn't determine unit size, use a default size
        if (unitSize <= 0) {
            unitSize = 16; // Default unit size as fallback
        }

        // Calculate grid boundaries, aligning to the unit+gap structure
        // This fixes the cutoff issue by ensuring we start at a proper grid boundary
        int startRow = (minRow / (unitSize + 4)) * (unitSize + 4);
        int startCol = (minCol / (unitSize + 4)) * (unitSize + 4);

        // Make sure we include the entire map area
        int endRow = maxRow + unitSize;
        int endCol = maxCol + unitSize;

        return new int[]{unitSize, startRow, startCol, endRow, endCol};
    }

    /**
     * Gets the room type value from the top-left pixel of a unit.
     */
    private static byte getRoomTypeValue(byte[][] mapPixels, int x, int y) {
        if (isValidCoordinate(mapPixels, x, y)) {
            return mapPixels[y][x];
        }
        return 0;
    }

    /**
     * Detects if a door exists between two room units.
     */
    private static DoorType getDoorType(byte[][] mapPixels, int roomX, int roomY, int doorX, int doorY, int unitSize) {
        // Check if the door coordinate is valid
        if (!isValidCoordinate(mapPixels, doorX, doorY)) {
            return DoorType.NONE;
        }

        // Get the door pixel value
        byte doorValue = mapPixels[doorY][doorX];

        // If it's 0, there's no door
        if (doorValue == 0) {
            return DoorType.NONE;
        }

        // Return the appropriate door type
        return DoorType.fromValue(doorValue);
    }

    /**
     * Checks if the given room type can have a checkmark.
     */
    private static boolean canHaveCheckmark(RoomType roomType) {
        return roomType != RoomType.ENTRANCE && roomType != RoomType.UNKNOWN && roomType != RoomType.NONE;
    }

    /**
     * Gets the check mark type at the specified coordinates.
     */
    private static CheckMarkType getCheckMarkType(byte[][] mapPixels, int x, int y) {
        if (!isValidCoordinate(mapPixels, x, y)) {
            return CheckMarkType.NONE;
        }

        byte value = mapPixels[y][x];

        // Check if it's a recognized check mark value
        for (CheckMarkType type : CheckMarkType.values()) {
            if (type.getValue() == value) {
                return type;
            }
        }

        return CheckMarkType.NONE;
    }

    /**
     * Checks if the coordinates are within the bounds of the map.
     */
    private static boolean isValidCoordinate(byte[][] mapPixels, int x, int y) {
        return y >= 0 && y < mapPixels.length && x >= 0 && x < mapPixels[0].length;
    }

    /**
     * Finds if this room is adjacent to another room with the same type
     * Returns a key for the multi-unit room, or null if this is a standalone room.
     */
    private static String findAdjacentRoomWithSameType(MapTile[][] tileGrid, int x, int y, RoomType roomType) {
        // Check adjacent tiles (only top and left since we're iterating from top-left)
        MapTile topTile = (y > 0) ? tileGrid[y - 1][x] : null;
        MapTile leftTile = (x > 0) ? tileGrid[y][x - 1] : null;

        // Special case for rooms that can only have one door
        if (roomType == RoomType.PUZZLE || roomType == RoomType.TRAP ||
                roomType == RoomType.MINIBOSS || roomType == RoomType.BLOOD ||
                roomType == RoomType.ENTRANCE) {
            return null; // These are always standalone rooms with one door
        }

        // Check left tile first
        if (leftTile != null && leftTile.getRoomType() == roomType) {
            return "room-" + leftTile.getUuid();
        }

        // Then check top tile
        if (topTile != null && topTile.getRoomType() == roomType) {
            return "room-" + topTile.getUuid();
        }

        // No adjacent tiles with matching type
        return null;
    }
}
