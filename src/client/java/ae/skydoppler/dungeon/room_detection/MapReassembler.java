package ae.skydoppler.dungeon.room_detection;

import java.awt.Point;

import ae.skydoppler.structs.Size;

public class MapReassembler {

    private static MapVariantStruct mapVariantStruct; // TODO: Set this to the correct value, hardcoded depending on the current dungeon floor (just like borderWidth is set).

    public static Tile[][] reassembleMap(byte[][] mapPixels) {

        // Takes in a grid of pixel values, and returns the grid in a modular tile format.

        Tile[][] newMap;

        // set map grid size based on the current dungeon floor from scoreboard
        /*switch (SkydopplerClient.currentDungeonFloor) {
            case 0: {
                mapVariantStruct.initializeValues(22, 22, 18, new Size(4,4));
                break;
            }
            case 1: {
                mapVariantStruct.initializeValues(22,11,18,new Size(4,5));
                break;
            }
            case 2:
            case 3: {
                mapVariantStruct.initializeValues(11,11,18,new Size(5,5));
                break;
            }
            case 4:
            case 5: {
                mapVariantStruct.initializeValues(5,5,16,new Size(6,5));
                break;
            }
            case 6:
            case 7: {
                mapVariantStruct.initializeValues(5,5,16,new Size(6,6));
                break;
            }
            default: {
                mapVariantStruct = getMapVariant(mapPixels);
                break;
            }
        }*/
        mapVariantStruct = getMapVariant(mapPixels);
        if (mapVariantStruct == null) return null;
        newMap = new Tile[mapVariantStruct.gridSize.getW()][mapVariantStruct.gridSize.getH()];

        // For each tile in the new map (newMap), fill it in depending on its corresponding space in the mapPixels map data.
        for (int row = 0; row < newMap.length; row++) {
            for (int col = 0; col < newMap[0].length; col++) {

                // Point p is the top-left pixel of a room tile. The (col * 4) and (row * 4) is to account for room spacing, which is always 4 (verify).
                Point p = new Point(col * mapVariantStruct.cellSize + mapVariantStruct.borderWidth + (col * 4), row * mapVariantStruct.cellSize + mapVariantStruct.borderHeight + (row * 4));
                newMap[row][col] = new Tile(getRoomTileAtPixel(mapPixels, p), getCheckTypeForRoom(mapPixels, p), getDoorsForRoom(mapPixels, p));

                // TODO: if the room is red room, if checkmark is white, it is ready, if checkmark is green, it is completed. Add it in a map handler file.

            }
        }
        return newMap;
    }

    private static MapVariantStruct getMapVariant(byte[][] mapPixels) {

        int yDist = 0;

        int topDist = 0;
        int bottomDist = 0;

        topDist = getDistanceFromYEdge(mapPixels, 0, 1);
        if (topDist == -1) {
            bottomDist = getDistanceFromYEdge(mapPixels, 127, -1);
            if (bottomDist == -1)
                return null;
            else
                yDist = bottomDist;

        } else {
            yDist = topDist;
        }

        // TODO: add code to gather information and assemble a map variant based on the x and y distances. Remember to add checks for certain floors with rooms that stick out on the sides.

        int xDist = 0;

        int leftDist = 0;
        int rightDist = 0;

        leftDist = getDistanceFromXEdge(mapPixels, 0, 1);
        if (leftDist == -1) {
            rightDist = getDistanceFromXEdge(mapPixels, 127, -1);
            if (rightDist == -1)
                return null;
            else
                xDist = rightDist;
        } else {
            xDist = leftDist;
        }
        //please add code to check if the xDist and yDist match certain cases. please make a thing that holds the different combinations of x and y values: floor 0 -> x= 22, y = 22; floor 1 -> x=22,y=11;floor 2 and 3 -> x=11,y=11; 
        return null;
    }

    /**
     * Checks the entire row for a pixel of non-zero value,
     * and returns the row number that the first occurence
     * of a non-zero value is in. This should be called for
     * the top edge, and if return null, for the bottom
     * edge.
     *
     * @param mapPixels Takes in a byte 2D array of the map's pixels (128x128).
     * @param row       Changing variable in the recursive function. Always send in 0 or 127 as the starting row of the map.
     * @param dir       The direction the function should iterate towards (1 = down; -1 = up).
     *
     * @return a row number if a non-zero pixel is found, otherwise -1.
     */
    private static int getDistanceFromYEdge(byte[][] mapPixels, int row, int dir) {

        // Possibility case
        if ((127 - 22) > row && row > 22)
            return -1;

        // Variation case
        if (checkRowForNonZeroPixel(mapPixels, row))
            return row;

        // Recursive case
        return getDistanceFromYEdge(mapPixels, row + dir, dir);

    }

    /**
     * Function used by the method @see #getDistanceFromYEdge(byte[][], int, int)
     * Checks the given row for a non-zero pixel value, and if it finds one,
     * returns true, otherwise, returns false.
     */
    private static boolean checkRowForNonZeroPixel(byte[][] mapPixels, int row) {

        for (int col = 0; col < mapPixels[0].length; col++) {
            if (mapPixels[row][col] != 0)
                return true;
        }
        return false;
    }

    /**
     * Checks the entire row for a pixel of non-zero value,
     * and returns the row number that the first occurence
     * of a non-zero value is in. This should be called for
     * the top edge, and if return null, for the bottom
     * edge.
     *
     * @param mapPixels Takes in a byte 2D array of the map's pixels (128x128).
     * @param col       Changing variable in the recursive function. Always send in 0 or 127 as the starting column of the map.
     * @param dir       The direction the function should iterate towards (1 = right; -1 = left).
     *
     * @return a column number if a non-zero pixel is found, otherwise -1.
     */
    private static int getDistanceFromXEdge(byte[][] mapPixels, int col, int dir) {

        // Possibility case
        if ((127 - 22) > col && col > 22)
            return -1;

        // Variation case
        if (checkColForNonZeroPixel(mapPixels, col))
            return col;

        // Recursive case
        return getDistanceFromXEdge(mapPixels, col + dir, dir);

    }

    /**
     * Function used by the method @see #getDistanceFromXEdge(byte[][], int, int)
     * Checks the given column for a non-zero pixel value, and if it finds one,
     * returns true, otherwise, returns false.
     */
    private static boolean checkColForNonZeroPixel(byte[][] mapPixels, int col) {

        for (int row = 0; row < mapPixels.length; row++) {
            if (mapPixels[row][col] != 0)
                return true;
        }
        return false;
    }

    private static TileType getRoomTileAtPixel(byte[][] mapPixels, Point pos) {

        switch (mapPixels[pos.x][pos.y]) {

            case 0:
                return TileType.EMPTY;
            case 30:
                return TileType.ENTRANCE_ROOM;
            case 63:
                return TileType.NORMAL_ROOM;
            case 85:
                return TileType.UNEXPLORED_ROOM;
            case 66:
                return TileType.PUZZLE_ROOM;
            case 74:
                return TileType.MINIBOSS_ROOM;
            case 62:
                return TileType.TRAP_ROOM;
            case 82:
                return TileType.FAIRY_ROOM;
            case 18:
                return TileType.BLOOD_ROOM;

        }
        return null;
    }

    private static DoorType[] getDoorsForRoom(byte[][] mapPixels, Point pos) {

        // TODO: Add checks for if the given room is a special kind that can only have one door to reduce unnessecary checks if it finds a door before it finishes checking all 4 sides.

        DoorType[] doors = new DoorType[4];

        doors[0] = getDoorAtPos(mapPixels, new Point(pos.x + (mapVariantStruct.cellSize / 2), pos.y - 1)); // top
        doors[1] = getDoorAtPos(mapPixels, new Point(pos.x + mapVariantStruct.cellSize + 1, pos.y + (mapVariantStruct.cellSize / 2))); // right
        doors[2] = getDoorAtPos(mapPixels, new Point(pos.x + (mapVariantStruct.cellSize / 2), pos.y + mapVariantStruct.cellSize + 1)); // bottom
        doors[3] = getDoorAtPos(mapPixels, new Point(pos.x - 1, pos.y + (mapVariantStruct.cellSize / 2))); // left

        return doors;

    }

    private static CheckType getCheckTypeForRoom(byte[][] mapPixels, Point pos) {

        // In the case that the given room is the Entrance (same color as green checkmark), return none, as the Entrance room cannot have any checkmarks.
        if (mapPixels[pos.x][pos.y] == 30 || mapPixels[pos.x][pos.y] == 82) return CheckType.NONE;

        // the checkmark positions are always the same
        int chX = 9;
        int chY = 9;

        byte checkmark = mapPixels[pos.x + chX][pos.y + chY];

        switch (checkmark) {

            case 30:
                return CheckType.GREEN;
            case 34:
                return CheckType.WHITE;
            default:
                return CheckType.NONE;

        }

    }

    private static DoorType getDoorAtPos(byte[][] mapPixels, Point pos) {

        switch (mapPixels[pos.x][pos.y]) {

            case 0:
                return DoorType.NONE;
            case 63:
                return DoorType.NORMAL_DOOR;
            case 119:
                return DoorType.WITHER_DOOR;
            case 18:
                return DoorType.BLOOD_DOOR;
            default:
                return DoorType.NONE;

        }

    }

    public enum TileType {
        EMPTY,
        ENTRANCE_ROOM,
        NORMAL_ROOM,
        FAIRY_ROOM,
        UNEXPLORED_ROOM,
        MINIBOSS_ROOM,
        PUZZLE_ROOM,
        TRAP_ROOM,
        BLOOD_ROOM
    }

    public enum CheckType {
        NONE,
        WHITE,
        GREEN
    }

    public enum DoorType {
        NONE,
        NORMAL_DOOR,
        WITHER_DOOR,
        BLOOD_DOOR
    }

    public static class MapVariantStruct {

        public int borderWidth;
        public int borderHeight;
        public int cellSize;
        public Size gridSize;

        public void initializeValues(int borderWidth, int borderHeight, int cellSize, Size gridSize) {
            this.borderWidth = borderWidth;
            this.borderHeight = borderHeight;
            this.cellSize = cellSize;
            this.gridSize = gridSize;
        }

    }

    public static class Tile {

        private TileType tileType;
        private DoorType[] doors; // Index reference: 0 = up; 1 = right; 2 = down; 3 = left.

        private CheckType checkType; // This value should never be null; instead, it is set to NONE.

        public Tile() {
            this.tileType = TileType.EMPTY;
            this.checkType = CheckType.NONE;
            this.doors = null;
        }

        public Tile(TileType tileType, DoorType[] doors) {
            this.tileType = tileType;
            this.checkType = CheckType.NONE;
            this.doors = doors;
        }

        public Tile(TileType tileType, CheckType checkType, DoorType[] doors) {
            this.tileType = tileType;
            this.checkType = checkType;
            this.doors = doors;
        }

        public TileType getTileType() {
            return tileType;
        }

        public void setTileType(TileType tileType) {
            this.tileType = tileType;
        }

        public DoorType[] getDoors() {
            return doors;
        }

        public void setDoors(DoorType[] doors) {
            this.doors = doors;
        }

        public CheckType getCheckType() {
            return checkType;
        }

        public void setCheckType(CheckType checkType) {
            this.checkType = checkType;
        }
    }

}
