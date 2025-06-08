package ae.skydoppler.dungeon.map;

public class DungeonTileMapConstructor {

    /**
     * Constructs a 2D array of MapTile objects from the provided map pixel data.
     *
     * @param mapPixels A 2D byte array representing the map pixels.
     * @return A modular grid of MapTiles.
     */
    public static MapTile[][] constructMap(byte[][] mapPixels) {
        if (mapPixels == null || mapPixels.length == 0) {
            return new MapTile[0][0];
        }

        // First, identify room regions and calculate dimensions
        RoomRegionInfo regionInfo = identifyRoomRegions(mapPixels);

        // Calculate the dimensions of the modular grid based on room regions
        int gridHeight = regionInfo.getTileGridHeight();
        int gridWidth = regionInfo.getTileGridWidth();

        // Create the modular tile grid
        MapTile[][] tileGrid = new MapTile[gridHeight][gridWidth];

        // Initialize all grid cells with empty tiles
        for (int y = 0; y < gridHeight; y++) {
            for (int x = 0; x < gridWidth; x++) {
                tileGrid[y][x] = new MapTile();
            }
        }

        // Fill the grid with room and door information
        fillTileGrid(mapPixels, tileGrid, regionInfo);

        return tileGrid;
    }

    /**
     * Identifies all room regions in the map and calculates grid dimensions.
     */
    private static RoomRegionInfo identifyRoomRegions(byte[][] mapPixels) {
        int height = mapPixels.length;
        int width = mapPixels[0].length;

        // Find the room size (assuming square rooms) and spacing between rooms
        int roomSize = findRoomSize(mapPixels);
        int spacing = 4; // As specified, spacing between rooms is always 4 pixels

        // Calculate how many tiles we need in each dimension
        int pixelWidth = findWidthWithContent(mapPixels);
        int pixelHeight = findHeightWithContent(mapPixels);

        int tileGridWidth = calculateGridDimension(pixelWidth, roomSize, spacing);
        int tileGridHeight = calculateGridDimension(pixelHeight, roomSize, spacing);

        return new RoomRegionInfo(roomSize, spacing, tileGridWidth, tileGridHeight);
    }

    /**
     * Calculates a grid dimension based on pixel dimension, room size, and spacing.
     */
    private static int calculateGridDimension(int pixelDimension, int roomSize, int spacing) {
        // Each unit consists of a room followed by spacing
        // The formula accounts for rooms and spaces between them
        int units = 0;
        int pixelsProcessed = 0;

        while (pixelsProcessed < pixelDimension) {
            // Add a room tile
            units++;
            pixelsProcessed += roomSize;

            // If there's enough space for another room, add a space tile
            if (pixelsProcessed + spacing < pixelDimension) {
                units++;
                pixelsProcessed += spacing;
            }
        }

        return units;
    }

    /**
     * Finds the width of the map that contains actual content (non-zero pixels).
     */
    private static int findWidthWithContent(byte[][] mapPixels) {
        int maxX = 0;
        for (byte[] row : mapPixels) {
            for (int x = row.length - 1; x >= 0; x--) {
                if (row[x] != 0) {
                    maxX = Math.max(maxX, x);
                    break;
                }
            }
        }
        return maxX + 1;
    }

    /**
     * Finds the height of the map that contains actual content (non-zero pixels).
     */
    private static int findHeightWithContent(byte[][] mapPixels) {
        int maxY = 0;
        for (int y = mapPixels.length - 1; y >= 0; y--) {
            for (byte pixelValue : mapPixels[y]) {
                if (pixelValue != 0) {
                    maxY = Math.max(maxY, y);
                    break;
                }
            }
        }
        return maxY + 1;
    }

    /**
     * Determines the size of rooms by finding the smallest continuous block of non-zero pixels.
     */
    private static int findRoomSize(byte[][] mapPixels) {
        int height = mapPixels.length;
        int width = mapPixels[0].length;

        // Find first non-zero pixel
        int startX = -1;
        int startY = -1;
        outerLoop:
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                if (mapPixels[y][x] != 0 && isRoomType(mapPixels[y][x])) {
                    startX = x;
                    startY = y;
                    break outerLoop;
                }
            }
        }

        if (startX == -1 || startY == -1) {
            return 7; // Default size if no rooms found
        }

        // Measure room width
        int roomWidth = 0;
        for (int x = startX; x < width; x++) {
            if (x >= width || mapPixels[startY][x] == 0 || !isRoomType(mapPixels[startY][x])) {
                break;
            }
            roomWidth++;
        }

        // Measure room height
        int roomHeight = 0;
        for (int y = startY; y < height; y++) {
            if (y >= height || mapPixels[y][startX] == 0 || !isRoomType(mapPixels[y][startX])) {
                break;
            }
            roomHeight++;
        }

        // Return the minimum of width and height to ensure we have a valid room size
        return Math.min(roomWidth, roomHeight);
    }

    /**
     * Checks if a pixel value represents a room type.
     */
    private static boolean isRoomType(byte pixelValue) {
        return getRoomType(pixelValue) != RoomType.NONE;
    }

    /**
     * Fills the tile grid with room and door information based on pixel data.
     */
    private static void fillTileGrid(byte[][] mapPixels, MapTile[][] tileGrid, RoomRegionInfo regionInfo) {
        int roomSize = regionInfo.getRoomSize();
        int spacing = regionInfo.getSpacing();

        int gridY = 0;
        int pixelY = 0;

        while (gridY < tileGrid.length) {
            int gridX = 0;
            int pixelX = 0;

            while (gridX < tileGrid[0].length) {
                if (isValidPixelCoordinate(pixelY, pixelX, mapPixels)) {
                    // Check if this is a room position
                    if (isRoomPosition(gridX, gridY)) {
                        processRoomTile(mapPixels, tileGrid, pixelX, pixelY, gridX, gridY, roomSize);
                        pixelX += roomSize;
                    }
                    // Check if this is a horizontal corridor position
                    else if (isHorizontalCorridorPosition(gridX, gridY)) {
                        processHorizontalCorridor(mapPixels, tileGrid, pixelX, pixelY, gridX, gridY, spacing, roomSize);
                        pixelX += spacing;
                    }
                    // Check if this is a vertical corridor position
                    else if (isVerticalCorridorPosition(gridX, gridY)) {
                        processVerticalCorridor(mapPixels, tileGrid, pixelX, pixelY, gridX, gridY, spacing, roomSize);
                        pixelX += roomSize;
                    }
                    // This is an intersection position
                    else {
                        pixelX += spacing;
                    }
                }

                gridX++;
            }

            // Move to the next row in the grid
            if (isRoomPosition(0, gridY)) {
                pixelY += roomSize;
            } else {
                pixelY += spacing;
            }

            gridY++;
        }
    }

    /**
     * Processes a room tile in the map and fills in the corresponding grid cell.
     */
    private static void processRoomTile(byte[][] mapPixels, MapTile[][] tileGrid, int pixelX, int pixelY,
                                        int gridX, int gridY, int roomSize) {
        if (!isValidPixelCoordinate(pixelY, pixelX, mapPixels)) {
            return;
        }

        // Get room type from the center of the room
        int centerX = pixelX + roomSize / 2;
        int centerY = pixelY + roomSize / 2;

        if (isValidPixelCoordinate(centerY, centerX, mapPixels)) {
            RoomType roomType = getRoomType(mapPixels[centerY][centerX]);
            tileGrid[gridY][gridX].setRoomType(roomType);

            // Check for checkmark in the top-left of the room
            if (isValidPixelCoordinate(pixelY, pixelX, mapPixels)) {
                CheckMarkType checkMarkType = getCheckMarkType(mapPixels[pixelY][pixelX]);
                tileGrid[gridY][gridX].setCheckMarkType(checkMarkType);
            }
        }
    }

    /**
     * Processes a horizontal corridor between rooms.
     */
    private static void processHorizontalCorridor(byte[][] mapPixels, MapTile[][] tileGrid, int pixelX, int pixelY,
                                                  int gridX, int gridY, int spacing, int roomSize) {
        // Check the middle of the corridor for a door
        int middleX = pixelX + spacing / 2;
        int middleY = pixelY + roomSize / 2;

        if (isValidPixelCoordinate(middleY, middleX, mapPixels)) {
            // Check if there's a horizontal door
            DoorType leftDoorType = getDoorType(mapPixels[middleY][middleX]);
            DoorType rightDoorType = getDoorType(mapPixels[middleY][middleX]);

            // Set door types for adjacent cells
            if (gridX > 0) {
                tileGrid[gridY][gridX - 1].setRightDoorType(leftDoorType);
            }
            if (gridX < tileGrid[0].length - 1) {
                tileGrid[gridY][gridX + 1].setLeftDoorType(rightDoorType);
            }
        }
    }

    /**
     * Processes a vertical corridor between rooms.
     */
    private static void processVerticalCorridor(byte[][] mapPixels, MapTile[][] tileGrid, int pixelX, int pixelY,
                                                int gridX, int gridY, int spacing, int roomSize) {
        // Check the middle of the corridor for a door
        int middleX = pixelX + roomSize / 2;
        int middleY = pixelY + spacing / 2;

        if (isValidPixelCoordinate(middleY, middleX, mapPixels)) {
            // Check if there's a vertical door
            DoorType topDoorType = getDoorType(mapPixels[middleY][middleX]);
            DoorType bottomDoorType = getDoorType(mapPixels[middleY][middleX]);

            // Set door types for adjacent cells
            if (gridY > 0) {
                tileGrid[gridY - 1][gridX].setBottomDoorType(topDoorType);
            }
            if (gridY < tileGrid.length - 1) {
                tileGrid[gridY + 1][gridX].setTopDoorType(bottomDoorType);
            }
        }
    }

    /**
     * Checks if a grid position corresponds to a room position.
     */
    private static boolean isRoomPosition(int gridX, int gridY) {
        return gridX % 2 == 0 && gridY % 2 == 0;
    }

    /**
     * Checks if a grid position corresponds to a horizontal corridor.
     */
    private static boolean isHorizontalCorridorPosition(int gridX, int gridY) {
        return gridX % 2 == 1 && gridY % 2 == 0;
    }

    /**
     * Checks if a grid position corresponds to a vertical corridor.
     */
    private static boolean isVerticalCorridorPosition(int gridX, int gridY) {
        return gridX % 2 == 0 && gridY % 2 == 1;
    }

    /**
     * Checks if the pixel coordinates are valid within the map.
     */
    private static boolean isValidPixelCoordinate(int y, int x, byte[][] mapPixels) {
        return y >= 0 && y < mapPixels.length && x >= 0 && x < mapPixels[0].length;
    }

    //region Type Conversion Methods
    private static RoomType getRoomType(byte pixelValue) {
        return RoomType.fromValue(pixelValue);
    }

    private static DoorType getDoorType(byte pixelValue) {
        return DoorType.fromValue(pixelValue);
    }

    private static CheckMarkType getCheckMarkType(byte pixelValue) {
        return CheckMarkType.fromValue(pixelValue);
    }
    //endregion

    /**
     * Helper class to store information about room regions.
     */
    private static class RoomRegionInfo {
        private final int roomSize;
        private final int spacing;
        private final int tileGridWidth;
        private final int tileGridHeight;

        public RoomRegionInfo(int roomSize, int spacing, int tileGridWidth, int tileGridHeight) {
            this.roomSize = roomSize;
            this.spacing = spacing;
            this.tileGridWidth = tileGridWidth;
            this.tileGridHeight = tileGridHeight;
        }

        public int getRoomSize() {
            return roomSize;
        }

        public int getSpacing() {
            return spacing;
        }

        public int getTileGridWidth() {
            return tileGridWidth;
        }

        public int getTileGridHeight() {
            return tileGridHeight;
        }
    }
}
