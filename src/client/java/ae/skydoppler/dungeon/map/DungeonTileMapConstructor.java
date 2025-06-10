package ae.skydoppler.dungeon.map;

import java.util.HashSet;
import java.util.Set;

public class DungeonTileMapConstructor {

    // Possible room shapes in [y][x] format (relative to top-left corner)
    private static final int[][][] ROOM_SHAPES = {
            // 1x1
            {{0, 0}},
            // 2x2
            {{0, 0}, {0, 1}, {1, 0}, {1, 1}},
            // 1x2 horizontal
            {{0, 0}, {0, 1}},
            // 1x2 vertical
            {{0, 0}, {1, 0}},
            // 1x3 horizontal
            {{0, 0}, {0, 1}, {0, 2}},
            // 1x3 vertical
            {{0, 0}, {1, 0}, {2, 0}},
            // 1x4 horizontal
            {{0, 0}, {0, 1}, {0, 2}, {0, 3}},
            // 1x4 vertical
            {{0, 0}, {1, 0}, {2, 0}, {3, 0}},
            // L-shape (normal)
            {{0, 0}, {1, 0}, {1, 1}},
            // L-shape (rotated 90° clockwise)
            {{0, 0}, {0, 1}, {1, 0}},
            // L-shape (rotated 180° clockwise)
            {{0, 0}, {0, 1}, {1, 1}},
            // L-shape (rotated 270° clockwise)
            {{1, 0}, {1, 1}, {0, 1}}
    };

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

        // Find the unit size
        int unitSize = findUnitSize(mapPixels);

        if (unitSize <= 0) {
            return new MapTile[0][0]; // Couldn't find valid rooms
        }

        // Calculate the dimensions of our tile grid based on the maximum number of tiles
        // +1 to ensure we cover the entire map
        int gridRows = 128 / (unitSize + 4) + 1;
        int gridCols = 128 / (unitSize + 4) + 2;

        // Initialize the tile grid
        MapTile[][] tileGrid = new MapTile[gridRows][gridCols];
        for (int y = 0; y < gridRows; y++) {
            for (int x = 0; x < gridCols; x++) {
                tileGrid[y][x] = new MapTile(); // Initialize with default values
            }
        }

        // First pass: Identify room types and positions
        for (int gridY = 0; gridY < gridRows; gridY++) {
            for (int gridX = 0; gridX < gridCols; gridX++) {
                // Calculate pixel coordinates for this tile position
                int pixelX = gridX * (unitSize + 4);
                int pixelY = gridY * (unitSize + 4);

                // Check if this is a valid room (top-left pixel indicates room type)
                byte roomTypeValue = getRoomTypeValue(mapPixels, pixelX, pixelY);
                if (roomTypeValue == 0) {
                    continue; // Not a valid room
                }

                // Get the room type
                RoomType roomType = RoomType.fromValue(roomTypeValue);

                // Create or update the tile
                MapTile tile = tileGrid[gridY][gridX];
                tile.setRoomType(roomType);

                // Check for checkmark in the middle 4 pixels of the room
                // We'll check these during processing of multi-unit rooms in the second pass
                if (isSingleUnitRoom(roomType)) {
                    int centerX = pixelX + unitSize / 2;
                    int centerY = pixelY + unitSize / 2;
                    CheckMarkType checkMarkType = getCheckMarkType(mapPixels, centerX, centerY, unitSize);
                    tile.setCheckMarkType(checkMarkType);
                }
            }
        }

        // Second pass: Identify multi-unit rooms and assign UUIDs
        int nextUuid = 1;
        Set<String> processedPositions = new HashSet<>();

        for (int gridY = 0; gridY < gridRows; gridY++) {
            for (int gridX = 0; gridX < gridCols; gridX++) {
                String posKey = gridY + "," + gridX;
                if (processedPositions.contains(posKey)) {
                    continue; // Skip already processed positions
                }

                MapTile tile = tileGrid[gridY][gridX];
                if (tile.getRoomType() == RoomType.NONE) {
                    continue; // Skip empty tiles
                }

                // Special case rooms that should always be single units
                if (isSingleUnitRoom(tile.getRoomType())) {
                    tile.setUuid(nextUuid++);
                    processedPositions.add(posKey);
                    continue;
                }

                // Try to find a matching room shape
                int[][] matchedShape = findMatchingRoomShape(tileGrid, gridY, gridX, gridRows, gridCols);
                if (matchedShape != null && matchedShape.length > 1) {
                    // Found a multi-unit room shape
                    int shapeUuid = nextUuid++;

                    // The top-left-most unit is the current gridY, gridX
                    // Check for checkmark in the top-left unit of the multi-unit room
                    int pixelX = gridX * (unitSize + 4);
                    int pixelY = gridY * (unitSize + 4);
                    int centerX = pixelX + unitSize / 2;
                    int centerY = pixelY + unitSize / 2;
                    CheckMarkType checkMarkType = getCheckMarkType(mapPixels, centerX, centerY, unitSize);
                    tile.setCheckMarkType(checkMarkType);

                    // Set UUID and mark all tiles in this shape as processed
                    tile.setUuid(shapeUuid);
                    processedPositions.add(posKey);

                    for (int i = 1; i < matchedShape.length; i++) {
                        int[] pos = matchedShape[i];
                        int y = gridY + pos[0];
                        int x = gridX + pos[1];
                        String key = y + "," + x;

                        if (y < gridRows && x < gridCols && !processedPositions.contains(key)) {
                            tileGrid[y][x].setUuid(shapeUuid);
                            processedPositions.add(key);
                        }
                    }
                } else {
                    // Single unit room
                    tile.setUuid(nextUuid++);
                    processedPositions.add(posKey);
                }
            }
        }

        // Third pass: Detect doors between rooms
        for (int gridY = 0; gridY < gridRows; gridY++) {
            for (int gridX = 0; gridX < gridCols; gridX++) {
                MapTile tile = tileGrid[gridY][gridX];
                if (tile.getRoomType() == RoomType.NONE) {
                    continue;
                }

                // Calculate pixel coordinates for this tile position
                int pixelX = gridX * (unitSize + 4);
                int pixelY = gridY * (unitSize + 4);

                // Detect doors in all four directions
                detectDoors(mapPixels, tileGrid, gridY, gridX, gridRows, gridCols, pixelX, pixelY, unitSize);
            }
        }

        return tileGrid;
    }

    /**
     * Find the unit size by looking at the first room in the map
     */
    private static int findUnitSize(byte[][] mapPixels) {
        int rows = mapPixels.length;
        int cols = mapPixels[0].length;

        // Find the first non-zero pixel (start of a room)
        int firstNonZeroRow = -1;
        int firstNonZeroCol = -1;

        for (int y = 0; y < rows; y++) {
            for (int x = 0; x < cols; x++) {
                if (mapPixels[y][x] != 0) {
                    firstNonZeroRow = y;
                    firstNonZeroCol = x;
                    break;
                }
            }
            if (firstNonZeroRow != -1) break;
        }

        // If no room found
        if (firstNonZeroRow == -1) {
            return 0;
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

        return unitSize;
    }

    /**
     * Finds a matching room shape for a multi-unit room.
     * Returns the coordinates of all units in the room relative to the starting position.
     */
    private static int[][] findMatchingRoomShape(MapTile[][] tileGrid, int startY, int startX, int maxRows, int maxCols) {
        MapTile startTile = tileGrid[startY][startX];
        RoomType roomType = startTile.getRoomType();

        // For each possible room shape
        for (int[][] shape : ROOM_SHAPES) {
            boolean matches = true;

            // Check if all tiles in this shape match the room type
            for (int[] pos : shape) {
                int y = startY + pos[0];
                int x = startX + pos[1];

                if (y >= maxRows || x >= maxCols || tileGrid[y][x].getRoomType() != roomType) {
                    matches = false;
                    break;
                }
            }

            if (matches) {
                return shape;
            }
        }

        // No matching shape, return just the current tile
        return new int[][]{{0, 0}};
    }

    /**
     * Detects doors in all four directions for a tile.
     */
    private static void detectDoors(byte[][] mapPixels, MapTile[][] tileGrid, int gridY, int gridX,
                                    int gridRows, int gridCols, int pixelX, int pixelY, int unitSize) {
        MapTile tile = tileGrid[gridY][gridX];

        // Skip if this is not a room
        if (tile.getRoomType() == RoomType.NONE) {
            return;
        }

        // Debug info for this tile
        System.out.println("Checking doors for tile at [" + gridX + "," + gridY + "] - Room type: " + tile.getRoomType());

        // Check top door
        if (gridY > 0) {
            MapTile aboveTile = tileGrid[gridY - 1][gridX];
            // Only check for a door if the tile above has a different UUID (not part of the same room)
            // or if it's not a room at all (RoomType.NONE)
            if (aboveTile.getRoomType() == RoomType.NONE || aboveTile.getUuid() != tile.getUuid()) {
                // Check the middle pixel of the top edge
                int doorX = pixelX + unitSize / 2;
                int doorY = pixelY - 1; // Middle of the gap between rooms

                // Debug pixel value at door location
                byte pixelValue = isValidCoordinate(mapPixels, doorX, doorY) ? mapPixels[doorY][doorX] : 0;
                System.out.println("  Top door pixel [" + doorX + "," + doorY + "] value: " + pixelValue);

                DoorType topDoor = getDoorType(mapPixels, doorX, doorY);
                tile.setTopDoorType(topDoor);

                if (topDoor != DoorType.NONE) {
                    System.out.println("  ✓ Top door detected: " + topDoor);
                }
            } else {
                System.out.println("  ✗ No top door check: Same room or empty");
                // Ensure no door is set between units of the same room
                tile.setTopDoorType(DoorType.NONE);
            }
        }

        // Check right door
        if (gridX < gridCols - 1) {
            MapTile rightTile = tileGrid[gridY][gridX + 1];
            // Only check for a door if the tile to the right has a different UUID or is not a room
            if (rightTile.getRoomType() == RoomType.NONE || rightTile.getUuid() != tile.getUuid()) {
                // Check the middle pixel of the right edge
                int doorX = pixelX + unitSize + 1; // Middle of the gap between rooms
                int doorY = pixelY + unitSize / 2;

                // Debug pixel value at door location
                byte pixelValue = isValidCoordinate(mapPixels, doorX, doorY) ? mapPixels[doorY][doorX] : 0;
                System.out.println("  Right door pixel [" + doorX + "," + doorY + "] value: " + pixelValue);

                DoorType rightDoor = getDoorType(mapPixels, doorX, doorY);
                tile.setRightDoorType(rightDoor);

                if (rightDoor != DoorType.NONE) {
                    System.out.println("  ✓ Right door detected: " + rightDoor);
                }
            } else {
                System.out.println("  ✗ No right door check: Same room or empty");
                // Ensure no door is set between units of the same room
                tile.setRightDoorType(DoorType.NONE);
            }
        }

        // Check bottom door
        if (gridY < gridRows - 1) {
            MapTile belowTile = tileGrid[gridY + 1][gridX];
            // Only check for a door if the tile below has a different UUID or is not a room
            if (belowTile.getRoomType() == RoomType.NONE || belowTile.getUuid() != tile.getUuid()) {
                // Check the middle pixel of the bottom edge
                int doorX = pixelX + unitSize / 2;
                int doorY = pixelY + unitSize + 1; // Middle of the gap between rooms

                // Debug pixel value at door location
                byte pixelValue = isValidCoordinate(mapPixels, doorX, doorY) ? mapPixels[doorY][doorX] : 0;
                System.out.println("  Bottom door pixel [" + doorX + "," + doorY + "] value: " + pixelValue);

                DoorType bottomDoor = getDoorType(mapPixels, doorX, doorY);
                tile.setBottomDoorType(bottomDoor);

                if (bottomDoor != DoorType.NONE) {
                    System.out.println("  ✓ Bottom door detected: " + bottomDoor);
                }
            } else {
                System.out.println("  ✗ No bottom door check: Same room or empty");
                // Ensure no door is set between units of the same room
                tile.setBottomDoorType(DoorType.NONE);
            }
        }

        // Check left door
        if (gridX > 0) {
            MapTile leftTile = tileGrid[gridY][gridX - 1];
            // Only check for a door if the tile to the left has a different UUID or is not a room
            if (leftTile.getRoomType() == RoomType.NONE || leftTile.getUuid() != tile.getUuid()) {
                // Check the middle pixel of the left edge
                int doorX = pixelX - 1; // Middle of the gap between rooms
                int doorY = pixelY + unitSize / 2;

                // Debug pixel value at door location
                byte pixelValue = isValidCoordinate(mapPixels, doorX, doorY) ? mapPixels[doorY][doorX] : 0;
                System.out.println("  Left door pixel [" + doorX + "," + doorY + "] value: " + pixelValue);

                DoorType leftDoor = getDoorType(mapPixels, doorX, doorY);
                tile.setLeftDoorType(leftDoor);

                if (leftDoor != DoorType.NONE) {
                    System.out.println("  ✓ Left door detected: " + leftDoor);
                }
            } else {
                System.out.println("  ✗ No left door check: Same room or empty");
                // Ensure no door is set between units of the same room
                tile.setLeftDoorType(DoorType.NONE);
            }
        }

        // Print summary of detected doors
        System.out.println("  Door summary: Top=" + tile.getTopDoorType() +
                ", Right=" + tile.getRightDoorType() +
                ", Bottom=" + tile.getBottomDoorType() +
                ", Left=" + tile.getLeftDoorType());
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
     * Gets a door type at the specified coordinates.
     */
    private static DoorType getDoorType(byte[][] mapPixels, int x, int y) {
        if (!isValidCoordinate(mapPixels, x, y)) {
            return DoorType.NONE;
        }

        byte doorValue = mapPixels[y][x];
        if (doorValue == 0) {
            return DoorType.NONE;
        }

        return DoorType.fromValue(doorValue);
    }

    /**
     * Checks if the given room type can only be a single unit.
     */
    private static boolean isSingleUnitRoom(RoomType roomType) {
        return roomType == RoomType.PUZZLE ||
                roomType == RoomType.TRAP ||
                roomType == RoomType.MINIBOSS ||
                roomType == RoomType.BLOOD ||
                roomType == RoomType.ENTRANCE;
    }

    /**
     * Gets the check mark type at the specified coordinates by checking the middle 4 pixels.
     * Checks a 2x2 area in the center of the room unit.
     */
    private static CheckMarkType getCheckMarkType(byte[][] mapPixels, int centerX, int centerY, int unitSize) {
        // Check a 2x2 area in the middle of the room
        // Calculate the top-left corner of our 2x2 checking area
        int x1 = centerX - 1;
        int y1 = centerY - 1;

        // If any of the 4 pixels has a checkmark, return that type
        // Priority: GREEN > WHITE
        boolean foundWhite = false;

        // Check the 4 center pixels (2x2 area)
        for (int y = y1; y <= y1 + 1; y++) {
            for (int x = x1; x <= x1 + 1; x++) {
                if (!isValidCoordinate(mapPixels, x, y)) {
                    continue;
                }

                byte value = mapPixels[y][x];

                // Check if it's a GREEN checkmark (higher priority)
                if (value == CheckMarkType.GREEN.getValue()) {
                    return CheckMarkType.GREEN;
                }

                // Check if it's a WHITE checkmark
                if (value == CheckMarkType.WHITE.getValue()) {
                    return CheckMarkType.WHITE;
                }
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
}
