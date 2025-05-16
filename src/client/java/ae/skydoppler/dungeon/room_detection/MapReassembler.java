package ae.skydoppler.dungeon.room_detection;

import ae.skydoppler.SkydopplerClient;

import java.awt.*;

public class MapReassembler {

    private static int borderWidth;
    private static int borderHeight; // TODO: Set this to the correct value, hardcoded depending on the current dungeon floor (just like borderWidth is set).

    private static int tileLength;

    public static Tile[][] reassembleMap(byte[][] mapPixels) {

        Tile[][] newMap;

        // set map grid size based on the current dungeon floor from scoreboard
        switch (SkydopplerClient.currentDungeonFloor) {
            case 0: {
                newMap = new Tile[7][7];
                borderWidth = 22;
                tileLength = 18;
                break;
            }
            case 1: {
                newMap = new Tile[7][9];
                borderWidth = 22;
                borderHeight = 11;
                tileLength = 18;
                break;
            }
            case 2:
            case 3: {
                newMap = new Tile[9][9];
                borderWidth = 11;
                borderHeight = 11;
                tileLength = 18;
                break;
            }
            case 4:
            case 5: {
                newMap = new Tile[11][9];
                borderWidth = 5;
                borderHeight = 5;
                tileLength = 16;
                break;
            }
            case 6:
            case 7: {
                newMap = new Tile[11][11];
                borderWidth = 5;
                tileLength = 16;
                break;
            }
            default: {
                newMap = new Tile[0][0]; // Later, add actual calculation for the map size, as a fallback in case scoreboard dungeon floor detection doesn't work.
                borderWidth = 0;
                borderHeight = 0;
                tileLength = 0;
                break;
            }
        }

        // For each tile in the new map (newMap), fill it in depending on its corresponding space in the mapPixels map data.
        for (int row = 0; row < newMap.length; row++) {
            for (int col = 0; col < newMap[0].length; col++) {

                // Point p is the top-left pixel of a room tile.
                Point p = new Point(col * tileLength + borderWidth, row * tileLength + borderHeight);
                newMap[row][col] = new Tile(getRoomTileAtPixel(mapPixels, p), getCheckTypeForRoom(mapPixels, p), getDoorsForRoom(mapPixels, p));

                // TODO: if the room is red room, if checkmark is white, it is ready, if checkmark is green, it is completed.

            }
        }
        return newMap;
    }

    private static TileType getRoomTileAtPixel(byte[][] mapPixels, Point pos) {

        switch (mapPixels[row][col]) {

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
            case default:
                return null;

        }

    }

    private static DoorType[] getDoorsForRoom(byte[][] mapPixels, Point pos) {

        // TODO: Add checks for if the given room is a special kind that can only have one door to reduce unnessecary checks if it finds a door before it finishes checking all 4 sides.

        DoorType[] doors = new DoorType[4];
        
        doors[0] = checkForDoor(mapPixels, new Point(pos.X + (tileLength / 2), pos.Y - 1)); // top
        doors[1] = checkForDoor(mapPixels, new Point(pos.X + tileLength + 1, pos.Y + (tileLength / 2))); // right
        doors[2] = checkForDoor(mapPixels, new Point(pos.X + (tileLength / 2), pos.Y + tileLength + 1)) // bottom
        doors[3] = checkForDoor(mapPixels, new Point(pos.X - 1, pos.Y + (tileLength / 2))); // left

        return doors;

    }

    private static CheckType getCheckTypeForRoom(byte[][] mapPixels, Point pos) {

        // In the case that the given room is the Entrance (same color as green checkmark), return none, as the Entrance room cannot have any checkmarks.
        if (mapPixels[pos.X][pos.Y] == 30) return CheckType.NONE;

        // TODO: change the checkmark offset values to the actual offsets, based on the tile size
        int checkmarkOffsetX = 0;
        int checkmarkOffsetY = 0;

        byte checkmark = mapPixels[pos.X + checkmarkOffsetX][pos.Y + checkmarkOffsetY];

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
        
        switch (mapPixels[pos.X][pos.Y]) {

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
